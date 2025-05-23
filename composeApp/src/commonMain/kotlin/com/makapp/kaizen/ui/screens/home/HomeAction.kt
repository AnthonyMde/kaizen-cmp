package com.makapp.kaizen.ui.screens.home

import com.makapp.kaizen.ui.screens.challenge_details.ChallengeDetailsNavArgs

sealed class HomeAction {
    data class OnToggleChallenge(
        val userId: String,
        val challengeId: String,
        val isChecked: Boolean
    ) : HomeAction()

    data object OnAccountClicked : HomeAction()
    data object OnCreateFirstChallengeClicked : HomeAction()
    data object OnSwipeToRefreshFriendList : HomeAction()
    data object OnFriendEmptyViewClicked : HomeAction()
    data object OnRefreshFriendsOnResume : HomeAction()
    data class OnChallengeClicked(val navArgs: ChallengeDetailsNavArgs) : HomeAction()
}
