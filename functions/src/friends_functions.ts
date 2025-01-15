import { getFirestore } from "firebase-admin/firestore";
import { HttpsError, onCall } from "firebase-functions/v2/https";
import { Collection } from "./collection";
import { Challenge } from "./dto/challenge";
import { Friend } from "./dto/friend";
import { FriendPreview } from "./dto/friend_preview";
import { logger } from "firebase-functions/v2";

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

export const getFriends = onCall(async (request) => {
    if (request.auth == null || request.auth.uid == null) {
        throw new HttpsError("unauthenticated", "You must be authenticated.")
    }

    const firestore = getFirestore()
    const userId = request.auth.uid
    const user = await firestore
        .collection(Collection.USERS)
        .doc(userId)
        .get()
        .then((snapshot) => snapshot.data() as User)

    const friendUsers = await Promise.all(user.friendIds.map(async (id) => {
        return await firestore
            .collection(Collection.USERS)
            .doc(id)
            .get()
            .then((snapshot) => snapshot.data() as User)
    }))
    const friendsWithChallengesPromises = friendUsers.map(async (friendUser) => {
        const challenges = await firestore
            .collection(Collection.USERS)
            .doc(friendUser.id)
            .collection(Collection.CHALLENGES)
            .get()
            .then((snapshot) => snapshot.docs.map((doc) => {
                return doc.data() as Challenge
            }))

        return {
            id: friendUser.id,
            name: friendUser.name,
            profilePictureIndex: friendUser.profilePictureIndex,
            challenges: challenges
        } as Friend
    })

    const result = await Promise.all(friendsWithChallengesPromises);
    logger.info(`Friends for user ${userId} are ${JSON.stringify(result)}`)
    return result
})