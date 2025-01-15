import { getFirestore } from "firebase-admin/firestore";
//import { logger } from "firebase-functions/v2";
import { HttpsError, onCall } from "firebase-functions/v2/https";
import { Collection } from "./collection";
import { Challenge } from "./dto/challenge";
import { Friend } from "./dto/friend";
import { FriendPreview } from "./dto/friend_preview";

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
    const friendDocs = await getFirestore()
        .collection(Collection.USERS)
        .where("name", "==", friendUsername)
        .limit(1)
        .get()
        .then((snapshot) => snapshot.docs)

    if (friendDocs.length == 0) {
        throw new HttpsError("not-found", "Friend was not found")
    }

    const user = friendDocs[0].data() as User

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

    const includeChallenges = request.data.includeChallenges || false

    if (includeChallenges) {
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

        return await Promise.all(friendsWithChallengesPromises);
    } else {
        return friendUsers.map((friend) => {
            return {
                id: friend.id,
                name: friend.name,
                profilePictureIndex: friend.profilePictureIndex,
            } as Friend
        })
    }
})