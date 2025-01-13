/**
 * Import function triggers from their respective submodules:
 *
 * import {onCall} from "firebase-functions/v2/https";
 * import {onDocumentWritten} from "firebase-functions/v2/firestore";
 *
 * See a full list of supported triggers at https://firebase.google.com/docs/functions
 */

import { initializeApp } from "firebase-admin/app";
import { getFirestore } from "firebase-admin/firestore";
//import * as logger from "firebase-functions/logger";
import { HttpsError, onCall } from "firebase-functions/v2/https";
import { Collection } from "./collection";
import { FriendPreview } from "./dto/friend_preview";
import { FriendRequest, FriendRequestProfile, FriendRequestStatus } from "./dto/friend_request";

// Start writing functions
// https://firebase.google.com/docs/functions/typescript

initializeApp();

export const getFriendPreviewById = onCall(async (request) => {
    if (request.auth == null || request.auth.uid == null) {
        throw new HttpsError("unauthenticated", "You must be authenticated.")
    }

    let friendUsername: string
    try {
        friendUsername = request.data.username as string
    } catch (e) {
        throw new HttpsError("invalid-argument", "The function must be called with friend username.")
    }
    const snapshot = await getFirestore()
        .collection(Collection.USERS)
        .where("name", "==", friendUsername)
        .limit(1)
        .get()
    const docs = snapshot.docs

    if (docs.length == 0) {
        throw new HttpsError("not-found", "Friend was not found")
    }

    const user = docs[0].data() as User

    return {
        id: user.id,
        name: user.name,
        profilePictureIndex: user.profilePictureIndex
    } as FriendPreview
});

export const createFriendRequest = onCall(async (request) => {
    if (request.auth == null || request.auth.uid == null) {
        throw new HttpsError("unauthenticated", "You must be authenticated.")
    }

    const userId = request.auth.uid
    let friendId: string
    try {
        friendId = request.data.friendId as string
    } catch (e) {
        throw new HttpsError("invalid-argument", "The function must be called with friend id.")
    }

    const firestore = getFirestore()
    const batch = firestore.batch()

    const userRef = firestore.collection(Collection.USERS).doc(userId)
    const friendRef = firestore.collection(Collection.USERS).doc(friendId)

    let user: User
    try {
        const userDoc = await userRef.get()
        user = userDoc.data() as User 
    } catch (e) {
        throw new HttpsError("internal", `Cannot retrieve User with id ${userId}`)
    }

    let friend: User
    try {
        const friendDoc = await friendRef.get()
        friend = friendDoc.data() as User
    } catch (e) {
        throw new HttpsError("internal", `Cannot retrieve friend (User) with id ${userId}`)
    }

    const friendRequest = {
        sender: {
            id: user.id,
            username: user.name,
            profilePictureIndex: user.profilePictureIndex
        } as FriendRequestProfile,
        receiver: {
            id: friend.id,
            username: friend.name,
            profilePictureIndex: friend.profilePictureIndex
        },
        status: FriendRequestStatus[FriendRequestStatus.PENDING]
    } as FriendRequest

    batch.set(userRef.collection(Collection.FRIEND_REQUESTS).doc(), friendRequest)
    batch.set(friendRef.collection(Collection.FRIEND_REQUESTS).doc(), friendRequest)

    await batch.commit()
});
