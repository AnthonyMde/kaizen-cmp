package org.towny.kaizen.ui.screens.home

import androidx.lifecycle.ViewModel
import org.towny.kaizen.domain.entities.User
import org.towny.kaizen.domain.repository.UserRepository

class HomeViewModel(
    userRepository: UserRepository
): ViewModel() {
    val mockedUsers = User.getMockedChallengers()
    val mockedUser = User.getMockedUser()

    val users = userRepository.getUsers()
}