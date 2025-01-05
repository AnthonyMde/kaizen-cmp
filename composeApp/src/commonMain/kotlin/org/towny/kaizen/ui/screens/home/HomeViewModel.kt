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
import org.towny.kaizen.domain.models.Resource
import org.towny.kaizen.domain.models.User
import org.towny.kaizen.domain.repository.AuthRepository
import org.towny.kaizen.domain.repository.UsersRepository
import org.towny.kaizen.domain.services.ChallengesService
import org.towny.kaizen.domain.usecases.GetReloadedUserSessionUseCase
import org.towny.kaizen.domain.usecases.GetUserSessionUseCase

class HomeViewModel(
    private val usersRepository: UsersRepository,
    private val authRepository: AuthRepository,
    private val challengesService: ChallengesService,
    private val getUserSessionUseCase: GetUserSessionUseCase,
    private val getReloadedUserSessionUseCase: GetReloadedUserSessionUseCase
) : ViewModel() {
    private val _homeScreenState = MutableStateFlow(HomeScreenState())
    val homeScreenState = _homeScreenState.asStateFlow()
        .onStart {
            watchUsers()
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

    private fun watchUsers() {
        viewModelScope.launch {
            usersRepository.watchAll
                .collectLatest { result ->
                    when (result) {
                        is Resource.Error -> {
                            _homeScreenState.update {
                                it.copy(error = result.throwable?.message, isLoading = false)
                            }
                        }

                        is Resource.Loading -> {
                            _homeScreenState.update {
                                it.copy(error = null, isLoading = true)
                            }
                        }

                        is Resource.Success -> {
                            val userId = result.data!!.first().id
                            _homeScreenState.update {
                                it.copy(
                                    currentChallenger = filterCurrentChallenger(
                                        userId,
                                        result.data ?: emptyList()
                                    ),
                                    otherChallengers = filterOtherChallengers(
                                        userId,
                                        result.data ?: emptyList()
                                    ),
                                    error = null,
                                    isLoading = false
                                )
                            }
                        }
                    }
                }
        }
    }

    private fun filterCurrentChallenger(userId: String, users: List<User>): User? {
        return users.firstOrNull { it.id == userId }
    }

    private fun filterOtherChallengers(userId: String, users: List<User>): List<User> {
        return users.filter { it.id != userId }
    }
}
