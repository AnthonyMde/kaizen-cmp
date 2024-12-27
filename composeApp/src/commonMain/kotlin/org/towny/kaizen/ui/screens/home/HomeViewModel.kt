package org.towny.kaizen.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.towny.kaizen.domain.models.Resource
import org.towny.kaizen.domain.models.User
import org.towny.kaizen.domain.repository.ChallengesRepository
import org.towny.kaizen.domain.repository.UsersRepository
import org.towny.kaizen.domain.services.GetUserSessionUseCase

class HomeViewModel(
    private val usersRepository: UsersRepository,
    private val challengesRepository: ChallengesRepository,
    private val getUserSessionUseCase: GetUserSessionUseCase
) : ViewModel() {
    private var userSession: String? = null
    private val _homeScreenState = MutableStateFlow(HomeScreenState())
    val homeScreenState = _homeScreenState.asStateFlow()
        .onStart {
            userSession = getUserSessionUseCase()
            watchUsers()
        }

    fun onAction(action: HomeAction) {
        when (action) {
            is HomeAction.OnToggleChallenge -> {
                viewModelScope.launch {
                    challengesRepository.toggleChallenge(
                        action.userId,
                        action.challengeId,
                        action.isChecked
                    )
                }
            }
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
                            val username = userSession ?: result.data!!.first().name
                            _homeScreenState.update {
                                it.copy(
                                    currentChallenger = filterCurrentChallenger(
                                        username,
                                        result.data ?: emptyList()
                                    ),
                                    otherChallengers = filterOtherChallengers(
                                        username,
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

    private fun filterCurrentChallenger(username: String, users: List<User>): User? {
        return users.firstOrNull { it.name == username }
    }

    private fun filterOtherChallengers(username: String, users: List<User>): List<User> {
        return users.filter { it.name != username }
    }
}