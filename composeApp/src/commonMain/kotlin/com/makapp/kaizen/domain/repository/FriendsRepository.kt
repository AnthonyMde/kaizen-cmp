package com.makapp.kaizen.domain.repository

import com.makapp.kaizen.domain.models.Friend
import com.makapp.kaizen.domain.models.Resource

interface FriendsRepository {
    suspend fun getFriends(): Resource<List<Friend>>
}
