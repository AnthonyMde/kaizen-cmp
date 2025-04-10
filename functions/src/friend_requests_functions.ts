import { FieldValue, getFirestore } from "firebase-admin/firestore";
//import * as logger from "firebase-functions/logger";
import { HttpsError, onCall } from "firebase-functions/v2/https";
import { Collection } from "./collection";
import { FriendRequest, FriendRequestProfile, FriendRequestStatus } from "./dto/friend_request";

export const getFriendRequests = onCall(async (request) => {
    if (request.auth == null || request.auth.uid == null) {
        throw new HttpsError("unauthenticated", "You must be authenticated.")
    }

    const userId = request.auth.uid

    const friendRequests = await getFirestore()
        .collection(Collection.USERS)
        .doc(userId)
        .collection(Collection.FRIEND_REQUESTS)
        .limit(10)
        .get()
        .then((snapshot) => snapshot.docs.map((doc) => doc.data() as FriendRequest))

    return friendRequests
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
        throw new HttpsError("invalid-argument", "Function must be called with friend id.")
    }

    if (userId === friendId) {
        throw new HttpsError("invalid-argument", "You cannot create a friend request for yourself.")
    }

    const firestore = getFirestore()
    const batch = firestore.batch()

    const userRef = firestore.collection(Collection.USERS).doc(userId)
    const friendRef = firestore.collection(Collection.USERS).doc(friendId)

    let user: User
    let friend: User
    try {
        user = await userRef.get().then((doc) => doc.data() as User)
    } catch (e) {
        throw new HttpsError("internal", `Cannot retrieve User with id ${userId}`)
    }
    try {
        friend = await friendRef.get().then((doc) => doc.data() as User)
    } catch (e) {
        throw new HttpsError("internal", `Cannot retrieve friend (User) with id ${userId}`)
    }

    const uniqueId = generateUniqueId(userId, friendId)

    const friendRequest = {
        id: uniqueId,
        sender: {
            id: user.id,
            username: user.name,
            displayName: user.displayName,
            profilePictureIndex: user.profilePictureIndex
        } as FriendRequestProfile,
        receiver: {
            id: friend.id,
            username: friend.name,
            displayName: friend.displayName,
            profilePictureIndex: friend.profilePictureIndex
        } as FriendRequestProfile,
        status: FriendRequestStatus[FriendRequestStatus.PENDING]
    } as FriendRequest

    batch.set(userRef.collection(Collection.FRIEND_REQUESTS).doc(uniqueId), friendRequest)
    batch.set(friendRef.collection(Collection.FRIEND_REQUESTS).doc(uniqueId), friendRequest)

    await batch.commit()

    return friendRequest;
});

export const updateFriendRequest = onCall(async (request) => {
    if (request.auth == null || request.auth.uid == null) {
        throw new HttpsError("unauthenticated", "You must be authenticated.")
    }

    let friendRequestId: string
    let newStatus: string
    try {
        friendRequestId = request.data.friendRequestId as string
        newStatus = request.data.status as string
    } catch (e) {
        throw new HttpsError("invalid-argument", "Function must be called with friendRequestId and status.")
    }

    const firestore = getFirestore()
    const userId = request.auth.uid
    const friendRequestSnapshot = await firestore
        .collection(Collection.USERS)
        .doc(userId)
        .collection(Collection.FRIEND_REQUESTS)
        .doc(friendRequestId)
        .get()

    if (!friendRequestSnapshot.exists) {
        throw new HttpsError("not-found", `No friend request was found for id ${friendRequestId}`)
    }
    const friendRequest = friendRequestSnapshot.data() as FriendRequest

    const isAccepted = newStatus === FriendRequestStatus[FriendRequestStatus.ACCEPTED]
    const isDeclined = newStatus === FriendRequestStatus[FriendRequestStatus.DECLINED]
    const isCanceled = newStatus === FriendRequestStatus[FriendRequestStatus.CANCELED]
    const senderId = friendRequest.sender.id
    const receiverId = friendRequest.receiver.id

    if (isCanceled && senderId != userId) {
        throw new HttpsError("permission-denied", "Only the sender can cancel a friend request.")
    }
    if ((isAccepted || isDeclined) && receiverId != userId) {
        throw new HttpsError("permission-denied", "Only the receiver can accept or decline a friend request.")
    }

    const batch = firestore.batch()
    if (isAccepted) {
        batch.update(firestore.collection(Collection.USERS).doc(senderId), {
            friendIds: FieldValue.arrayUnion(receiverId)
        })
        batch.update(firestore.collection(Collection.USERS).doc(receiverId), {
            friendIds: FieldValue.arrayUnion(senderId)
        })
    } else if (isCanceled || isDeclined) {
        // Nothing we just delete as we will do for accepted status
    }

    batch.delete(firestore.collection(Collection.USERS).doc(senderId).collection(Collection.FRIEND_REQUESTS).doc(friendRequestId))
    batch.delete(firestore.collection(Collection.USERS).doc(receiverId).collection(Collection.FRIEND_REQUESTS).doc(friendRequestId))
    await batch.commit()
});

const generateUniqueId = (idA: string, idB: string): string => {
    const sorted = [idA.slice(0, 15), idB.slice(0, 15)].sort()
    return sorted[0] + sorted[1]
}