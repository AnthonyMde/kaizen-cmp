package org.towny.kaizen.ui.screens.account

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import org.towny.kaizen.domain.repository.AuthRepository
import org.towny.kaizen.domain.repository.UsersRepository

class AccountViewModel(
    private val authRepository: AuthRepository,
    usersRepository: UsersRepository
) : ViewModel() {
    private val _accountScreenState = MutableStateFlow(AccountScreenState())
    val accountScreenState = _accountScreenState.asStateFlow()
        .onStart {
            _accountScreenState.update {
                it.copy(user = usersRepository.getCurrentUser())
            }
        }

    suspend fun onAction(action: AccountAction) {
        when (action) {
            AccountAction.OnLogout -> {
                _accountScreenState.update { it.copy(isLogoutLoading = true) }
                authRepository.logout()
                _accountScreenState.update { it.copy(isLogoutLoading = true) }
            }

            else -> {}
        }
    }
}