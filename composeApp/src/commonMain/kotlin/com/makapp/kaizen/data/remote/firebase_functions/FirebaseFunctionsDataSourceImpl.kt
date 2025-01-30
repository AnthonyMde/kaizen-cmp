package com.makapp.kaizen.data.remote.firebase_functions

import com.makapp.kaizen.data.remote.dto.CreateUserRequest
import com.makapp.kaizen.data.remote.dto.FriendDTO
import com.makapp.kaizen.data.remote.dto.IsUsernameAvailableDTO
import com.makapp.kaizen.data.repository.entities.CreateChallengeRequest
import com.makapp.kaizen.data.repository.sources.FirebaseFunctionsDataSource
import com.makapp.kaizen.domain.models.FriendRequest
import com.makapp.kaizen.domain.models.FriendSearchPreview
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.functions.functions

class FirebaseFunctionsDataSourceImpl : FirebaseFunctionsDataSource {
    private val functions = Firebase.functions

    override suspend fun isUsernameAvailable(username: String): IsUsernameAvailableDTO {
        val body = mapOf(
            "username" to username
        )

        return functions
            .httpsCallable("isUsernameAvailable")
            .invoke(body)
            .data<IsUsernameAvailableDTO>()
    }

    override suspend fun getFriendSearchPreview(username: String): FriendSearchPreview {
        val body = mapOf("username" to username)

        return functions
            .httpsCallable("getFriendSearchPreview")
            .invoke(body)
            .data<FriendSearchPreview>()
    }

    override suspend fun getFriendRequests(): List<FriendRequest> {
        return functions
            .httpsCallable("getFriendRequests")
            .invoke()
            .data<List<FriendRequest>>()
    }

    override suspend fun createFriendRequest(friendId: String) {
        val body = mapOf("friendId" to friendId)

        functions
            .httpsCallable("createFriendRequest")
            .invoke(body)
    }

    override suspend fun updateFriendRequest(requestId: String, status: FriendRequest.Status) {
        val body = mapOf(
            "friendRequestId" to requestId,
            "status" to status
        )

        functions
            .httpsCallable("updateFriendRequest")
            .invoke(body)
    }

    override suspend fun getFriends(includeChallenges: Boolean): List<FriendDTO> {
        val body = mapOf(
            "includeChallenges" to includeChallenges
        )
        val result = functions
            .httpsCallable("getFriends")
            .invoke(body)
            .data<List<FriendDTO>>()

        return result
    }

    override suspend fun createUserAccount(request: CreateUserRequest) {
        functions
            .httpsCallable("createUserAccount")
            .invoke(request)
    }

    override suspend fun deleteUserAccount() {
        functions
            .httpsCallable("deleteUserAccount")
            .invoke()
    }

    override suspend fun toggleFriendAsFavorite(friendId: String) {
        val body = mapOf("friendId" to friendId)
        functions
            .httpsCallable("toggleFriendAsFavorite")
            .invoke(body)
    }

    override suspend fun createChallenge(request: CreateChallengeRequest) {
        functions
            .httpsCallable("createChallenge")
            .invoke(request)
    }
}
