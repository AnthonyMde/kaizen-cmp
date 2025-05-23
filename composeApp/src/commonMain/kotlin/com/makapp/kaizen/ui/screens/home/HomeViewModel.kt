package com.makapp.kaizen.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.makapp.kaizen.domain.exceptions.DomainException
import com.makapp.kaizen.domain.models.Resource
import com.makapp.kaizen.domain.models.challenge.Challenge
import com.makapp.kaizen.domain.models.friend.Friend
import com.makapp.kaizen.domain.repository.AuthRepository
import com.makapp.kaizen.domain.services.ChallengesService
import com.makapp.kaizen.domain.services.FriendsService
import com.makapp.kaizen.domain.services.UsersService
import com.makapp.kaizen.domain.usecases.user.GetReloadedUserSessionUseCase
import kaizen.composeapp.generated.resources.Res
import kaizen.composeapp.generated.resources.unknown_error
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val usersService: UsersService,
    private val authRepository: AuthRepository,
    private val challengesService: ChallengesService,
    private val friendsService: FriendsService,
    private val getReloadedUserSessionUseCase: GetReloadedUserSessionUseCase,
) : ViewModel() {
    private val _homeScreenState = MutableStateFlow(HomeScreenState())
    val homeScreenState = _homeScreenState.asStateFlow()
        .onStart {
            watchMe()
            watchFriends()
            watchFriendsLoading()
            _homeScreenState.update {
                it.copy(
                    userSession = authRepository.getUserSession(),
                    currentChallenger = usersService.getUser()
                )
            }
        }
    private val _events = Channel<HomeNavigationEvent>()
    val events = _events.receiveAsFlow()

    init {
        watchUserSession()
    }

    private fun watchUserSession() {
        viewModelScope.launch {
            authRepository.watchUserSession.collectLatest { userSession ->
                if (userSession == null) {
                    _events.send(HomeNavigationEvent.PopToLogin)
                }
            }
        }
    }

    fun onAction(action: HomeAction) {
        when (action) {
            is HomeAction.OnToggleChallenge -> viewModelScope.launch {
                challengesService.toggleChallenge(
                    action.userId,
                    action.challengeId,
                    action.isChecked
                )
            }

            is HomeAction.OnSwipeToRefreshFriendList -> viewModelScope.launch {
                _homeScreenState.update { it.copy(isSwipeToRefreshing = true) }
                friendsService.refreshFriends()
                _homeScreenState.update { it.copy(isSwipeToRefreshing = false) }
            }

            is HomeAction.OnRefreshFriendsOnResume -> viewModelScope.launch {
                friendsService.refreshFriends()
            }

            else -> {}
        }
    }

    private fun watchMe() = viewModelScope.launch {
        usersService.watchMe().collectLatest { result ->
            when (result) {
                is Resource.Error -> {
                    if (result.throwable is DomainException.User.NoUserAccountFound) {
                        _events.send(HomeNavigationEvent.GoToUserAccountCreation)
                    } else {
                        _homeScreenState.update {
                            it.copy(
                                currentChallengerError = Res.string.unknown_error,
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
                            currentChallenger = result.data?.copy(
                                challenges = result.data.challenges.filter { challenge ->
                                    !challenge.isFailed() && !challenge.isAbandoned()
                                }
                            ),
                            currentChallengerError = null,
                            isCurrentChallengerLoading = false
                        )
                    }
                }
            }
        }
    }

    private fun watchFriends() = viewModelScope.launch {
        friendsService.watchFavoriteFriends().collectLatest { result ->
            when (result) {
                is Resource.Error -> {
                    _homeScreenState.update {
                        it.copy(friendsError = Res.string.unknown_error)
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
                            friends = orderFriends(result.data).map { friend ->
                                friend.copy(challenges = orderChallenges(friend.challenges))
                            },
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
            val done =
                friend.challenges.count { it.isDoneForToday || it.isPaused() || it.isFailed() }
                    .toDouble()
            done / friend.challenges.size
        }
    }

    private fun orderChallenges(challenges: List<Challenge>): List<Challenge> =
        challenges.sortedBy {
            it.isDoneForToday
        }
}
