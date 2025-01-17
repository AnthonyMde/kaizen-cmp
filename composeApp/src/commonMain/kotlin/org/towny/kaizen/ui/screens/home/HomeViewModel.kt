package org.towny.kaizen.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.towny.kaizen.domain.exceptions.DomainException
import org.towny.kaizen.domain.models.Resource
import org.towny.kaizen.domain.repository.AuthRepository
import org.towny.kaizen.domain.services.ChallengesService
import org.towny.kaizen.domain.services.FriendsService
import org.towny.kaizen.domain.services.UsersService
import org.towny.kaizen.domain.usecases.GetReloadedUserSessionUseCase

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
            _homeScreenState.update {
                it.copy(userSession = authRepository.getUserSession())
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
                            it.copy(currentChallengerError = result.throwable?.message)
                        }
                    }
                }

                is Resource.Loading -> {
                    _homeScreenState.update {
                        it.copy(
                            currentChallengerError = null,
                        )
                    }
                }

                is Resource.Success -> {
                    _homeScreenState.update {
                        it.copy(
                            currentChallenger = result.data,
                            currentChallengerError = null,
                        )
                    }
                }
            }
        }
    }

    private fun watchFriends() = viewModelScope.launch {
        friendsService.getFriends().collectLatest { result ->
            when (result) {
                is Resource.Error -> {
                    _homeScreenState.update {
                        it.copy(
                            friendsError = result.throwable?.message,
                            isFriendsLoading = false
                        )
                    }
                }

                is Resource.Loading -> {
                    _homeScreenState.update {
                        it.copy(
                            friendsError = null,
                            isFriendsLoading = true
                        )
                    }
                }

                is Resource.Success -> {
                    _homeScreenState.update {
                        it.copy(
                            friends = result.data ?: emptyList(),
                            friendsError = null,
                            isFriendsLoading = false
                        )
                    }
                }
            }
        }
    }
}
