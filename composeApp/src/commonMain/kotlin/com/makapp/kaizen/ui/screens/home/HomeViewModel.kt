package com.makapp.kaizen.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.makapp.kaizen.domain.exceptions.DomainException
import com.makapp.kaizen.domain.models.Friend
import com.makapp.kaizen.domain.models.Resource
import com.makapp.kaizen.domain.repository.AuthRepository
import com.makapp.kaizen.domain.services.ChallengesService
import com.makapp.kaizen.domain.services.FriendsService
import com.makapp.kaizen.domain.services.UsersService
import com.makapp.kaizen.domain.usecases.GetReloadedUserSessionUseCase
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val usersService: UsersService,
    private val authRepository: AuthRepository,
    private val challengesService: ChallengesService,
    private val friendsService: FriendsService,
    private val getReloadedUserSessionUseCase: GetReloadedUserSessionUseCase
) : ViewModel() {
    private val _homeScreenState = MutableStateFlow(HomeScreenState())
    val homeScreenState = _homeScreenState.asStateFlow()
        .onStart {
            watchMe()
            watchFriends()
            watchFriendsLoading()
            viewModelScope.launch { friendsService.refreshFriends() }
            _homeScreenState.update {
                it.copy(
                    userSession = authRepository.getUserSession(),
                    currentChallenger = usersService.getUser()
                )
            }
        }
    private val _navigationEvents = MutableSharedFlow<HomeNavigationEvent>(
        replay = 0,
        onBufferOverflow = BufferOverflow.DROP_LATEST,
        extraBufferCapacity = 1
    )
    val navigationEvents = _navigationEvents.asSharedFlow()

    init {
        watchUserSession()
    }

    private fun watchUserSession() {
        viewModelScope.launch {
            authRepository.watchUserSession.collectLatest { userSession ->
                if (userSession == null) {
                    _navigationEvents.tryEmit(HomeNavigationEvent.PopToLogin)
                }
            }
        }
    }

    fun onAction(action: HomeAction) {
        when (action) {
            is HomeAction.OnToggleChallenge -> {
                viewModelScope.launch {
                    challengesService.toggleChallenge(
                        action.userId,
                        action.challengeId,
                        action.isChecked
                    )
                }
            }

            is HomeAction.OnEmailVerified -> {
                viewModelScope.launch {
                    _homeScreenState.update {
                        val reloadedUser = getReloadedUserSessionUseCase()
                        it.copy(userSession = reloadedUser)
                    }
                }
            }

            is HomeAction.OnSwipeToRefreshFriendList -> {
                viewModelScope.launch {
                    _homeScreenState.update { it.copy(
                        isSwipeToRefreshing = true
                    ) }
                    friendsService.refreshFriends()
                    _homeScreenState.update { it.copy(
                        isSwipeToRefreshing = false
                    ) }
                }
            }

            else -> {}
        }
    }

    private fun watchMe() = viewModelScope.launch {
        usersService.watchMe().collectLatest { result ->
            when (result) {
                is Resource.Error -> {
                    if (result.throwable is DomainException.User.NoUserAccountFound) {
                        _navigationEvents.tryEmit(HomeNavigationEvent.GoToUserAccountCreation)
                    } else {
                        _homeScreenState.update {
                            it.copy(
                                currentChallengerError = result.throwable?.message,
                                isCurrentChallengerLoading = false
                            )
                        }
                    }
                }

                is Resource.Loading -> {
                    _homeScreenState.update {
                        it.copy(
                            isCurrentChallengerLoading = true,
                            currentChallengerError = null,
                        )
                    }
                }

                is Resource.Success -> {
                    _homeScreenState.update {
                        it.copy(
                            currentChallenger = result.data,
                            currentChallengerError = null,
                            isCurrentChallengerLoading = false
                        )
                    }
                }
            }
        }
    }

    private fun watchFriends() = viewModelScope.launch {
        friendsService.watchFriends().collectLatest { result ->
            when (result) {
                is Resource.Error -> {
                    _homeScreenState.update {
                        it.copy(friendsError = result.throwable?.message)
                    }
                }

                is Resource.Loading -> {
                    _homeScreenState.update {
                        it.copy(friendsError = null)
                    }
                }

                is Resource.Success -> {
                    _homeScreenState.update {
                        it.copy(
                            friends = orderFriends(result.data),
                            friendsError = null,
                        )
                    }
                }
            }
        }
    }

    private fun watchFriendsLoading() = viewModelScope.launch {
        friendsService.isRefreshFriendsLoading.collectLatest { isRefreshing ->
            _homeScreenState.update {
                it.copy(
                    isFriendsLoading = isRefreshing
                )
            }
        }
    }

    private fun orderFriends(friends: List<Friend>?): List<Friend> {
        if (friends == null) return emptyList()

        return friends.sortedBy { friend ->
            friend.challenges.count { it.isDoneForToday }
        }
    }
}
