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
import org.towny.kaizen.domain.repository.UsersRepository

class HomeViewModel(
    private val userRepository: UsersRepository
) : ViewModel() {
    private val _homeScreenState = MutableStateFlow(HomeScreenState())
    val homeScreenState = _homeScreenState.asStateFlow()
        .onStart {
            watchUsers()
        }

    private fun watchUsers() {
        viewModelScope.launch {
            userRepository.watchAll
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
                                    users = result.data ?: emptyList(),
                                    error = null,
                                    isLoading = false
                                )
                            }
                        }
                    }
                }
        }
    }
}