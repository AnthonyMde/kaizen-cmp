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

class HomeViewModel(
    private val usersRepository: UsersRepository,
    private val challengesRepository: ChallengesRepository
) : ViewModel() {
    private val _homeScreenState = MutableStateFlow(HomeScreenState())
    val homeScreenState = _homeScreenState.asStateFlow()
        .onStart {
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
                                it.copy(error = result.message, isLoading = false)
                            }
                        }

                        is Resource.Loading -> {
                            _homeScreenState.update {
                                it.copy(error = null, isLoading = true)
                            }
                        }

                        is Resource.Success -> {
                            _homeScreenState.update {
                                it.copy(
                                    currentChallenger = getCurrentChallenger(
                                        "Towny",
                                        result.data ?: emptyList()
                                    ),
                                    otherChallengers = getOtherChallengers(
                                        "Towny",
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

    private fun getCurrentChallenger(userName: String, users: List<User>): User? {
        return users.firstOrNull { it.name == userName }
    }

    private fun getOtherChallengers(userName: String, users: List<User>): List<User> {
        return users.filter { it.name != userName }
    }
}