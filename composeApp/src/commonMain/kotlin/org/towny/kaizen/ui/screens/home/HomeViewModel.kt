package org.towny.kaizen.ui.screens.home

import androidx.lifecycle.ViewModel
import org.towny.kaizen.domain.entities.Challenger

class HomeViewModel: ViewModel() {
    val mockedChallengers = Challenger.getMockedChallengers()
    val mockedUser = Challenger.getMockedUser()
}