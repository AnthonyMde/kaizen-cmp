package org.towny.kaizen.ui.screens.account

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
import org.towny.kaizen.domain.repository.AuthRepository
import org.towny.kaizen.domain.services.UsersService

class AccountViewModel(
    private val authRepository: AuthRepository,
    private val usersService: UsersService
) : ViewModel() {
    private val _accountScreenState = MutableStateFlow(AccountScreenState())
    val accountScreenState = _accountScreenState.asStateFlow()
        .onStart {
            _accountScreenState.update {
                it.copy(user = usersService.getMe())
            }
        }

    private val _accountEvents = MutableSharedFlow<AccountEvent>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_LATEST
    )
    val accountEvents = _accountEvents.asSharedFlow()

    fun onAction(action: AccountAction) {
        when (action) {
            AccountAction.OnLogoutClicked -> {
                _accountScreenState.update { it.copy(showLogoutConfirmationModal = true) }
            }

            AccountAction.OnLogoutConfirmed -> viewModelScope.launch {
                _accountScreenState.update { it.copy(isLogoutLoading = true) }
                authRepository.logout()
                _accountScreenState.update {
                    it.copy(
                        isLogoutLoading = false,
                        showLogoutConfirmationModal = false
                    )
                }
                _accountEvents.tryEmit(AccountEvent.PopToLogin)
            }

            AccountAction.OnLogoutDismissed -> {
                _accountScreenState.update {
                    it.copy(
                        isLogoutLoading = false,
                        showLogoutConfirmationModal = false
                    )
                }
            }

            AccountAction.OnDeleteAccountClicked -> {
                deleteAccount()
            }

            else -> {}
        }
    }

    private fun deleteAccount() = viewModelScope.launch {
        usersService.deleteUserAccount().collectLatest { result ->
            when (result) {
                is Resource.Error -> {
                    // TODO: display error
                }
                is Resource.Success -> {
                    _accountEvents.tryEmit(AccountEvent.PopToLogin)
                }
                is Resource.Loading -> {
                    // TODO: load
                }
            }
        }
    }
}