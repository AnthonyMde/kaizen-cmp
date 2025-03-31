package com.makapp.kaizen.di

import com.makapp.kaizen.data.local.room.AppDatabase
import com.makapp.kaizen.data.local.room.app.AppDao
import com.makapp.kaizen.data.local.room.challenges.ChallengesDao
import com.makapp.kaizen.data.local.room.friendPreviews.FriendPreviewsDao
import com.makapp.kaizen.data.local.room.friendRequests.FriendRequestsDao
import com.makapp.kaizen.data.local.room.friends.FriendsDao
import com.makapp.kaizen.data.local.room.user.UserDao
import org.koin.dsl.module

val daoModule = module {
    single<FriendRequestsDao> { getFriendRequestsDao(get()) }
    single<FriendPreviewsDao> { getFriendPreviewsDao(get()) }
    single<FriendsDao> { getFriendsDao(get()) }
    single<UserDao> { getUserDao(get()) }
    single<AppDao> { getAppDao(get()) }
    single<ChallengesDao> { getChallengesDao(get()) }
}

private fun getFriendRequestsDao(db: AppDatabase): FriendRequestsDao = db.getFriendRequestsDao()
private fun getFriendPreviewsDao(db: AppDatabase): FriendPreviewsDao = db.getFriendPreviewsDao()
private fun getFriendsDao(db: AppDatabase): FriendsDao = db.getFriendsDao()
private fun getUserDao(db: AppDatabase): UserDao = db.getUserDao()
private fun getAppDao(db: AppDatabase): AppDao = db.getAppDao()
private fun getChallengesDao(db: AppDatabase): ChallengesDao = db.getChallengesDao()