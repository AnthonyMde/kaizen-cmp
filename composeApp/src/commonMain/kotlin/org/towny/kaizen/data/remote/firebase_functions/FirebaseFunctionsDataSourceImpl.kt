package org.towny.kaizen.data.remote.firebase_functions

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.functions.functions
import org.towny.kaizen.data.repository.sources.FirebaseFunctionsDataSource
import org.towny.kaizen.domain.models.FriendPreview

class FirebaseFunctionsDataSourceImpl: FirebaseFunctionsDataSource {
    override suspend fun getFriendPreviewByName(username: String): FriendPreview {
        val data = mapOf("username" to username)
        return Firebase.functions
            .httpsCallable("getFriendPreviewById")
            .invoke(data)
            .data<FriendPreview>()
    }
}
