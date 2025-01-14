import { getFirestore } from "firebase-admin/firestore";
//import * as logger from "firebase-functions/logger";
import { HttpsError, onCall } from "firebase-functions/v2/https";
import { Collection } from "./collection";
import { FriendPreview } from "./dto/friend_preview";
import { FriendRequest, FriendRequestProfile, FriendRequestStatus } from "./dto/friend_request";

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

export const getFriendRequests = onCall(async (request) => {
    if (request.auth == null || request.auth.uid == null) {
        throw new HttpsError("unauthenticated", "You must be authenticated.")
    }

    const userId = request.auth.uid

    const friendRequestSnapshots = await getFirestore()
        .collection(Collection.USERS)
        .doc(userId)
        .collection(Collection.FRIEND_REQUESTS)
        .limit(10)
        .get()

    return friendRequestSnapshots.docs.map((doc) => doc.data() as FriendRequest);
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

    if (userId === friendId) {
        throw new HttpsError("invalid-argument", "You cannot create a friend request for yourself.")
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

    const uniqueId = generateUniqueId(userId, friendId)

    const friendRequest = {
        id: uniqueId,
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

    batch.set(userRef.collection(Collection.FRIEND_REQUESTS).doc(uniqueId), friendRequest)
    batch.set(friendRef.collection(Collection.FRIEND_REQUESTS).doc(uniqueId), friendRequest)

    await batch.commit()

    return friendRequest;
});

export const updateFriendRequest = onCall(async (request) => {
    
});

const generateUniqueId = (idA: string, idB: string): string => {
    const sorted = [idA, idB].sort()
    return sorted[0]+sorted[1]
}