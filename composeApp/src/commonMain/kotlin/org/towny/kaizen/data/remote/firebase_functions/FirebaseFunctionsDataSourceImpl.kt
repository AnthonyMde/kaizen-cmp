package org.towny.kaizen.data.remote.firebase_functions

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.functions.functions
import org.towny.kaizen.data.repository.sources.FirebaseFunctionsDataSource
import org.towny.kaizen.domain.models.FriendPreview
import org.towny.kaizen.domain.models.FriendRequest

class FirebaseFunctionsDataSourceImpl: FirebaseFunctionsDataSource {
    private val functions = Firebase.functions

    override suspend fun getFriendPreviewByName(username: String): FriendPreview {
        val body = mapOf("username" to username)

        return functions
            .httpsCallable("getFriendPreviewById")
            .invoke(body)
            .data<FriendPreview>()
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
}
