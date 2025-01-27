import { getFirestore } from "firebase-admin/firestore";
//import { logger } from "firebase-functions/v2";
import { HttpsError, onCall } from "firebase-functions/v2/https";
import { Collection } from "./collection";
import { Challenge } from "./dto/challenge";
import { Friend } from "./dto/friend";
import { FriendSearchPreview } from "./dto/friend_search_preview";

export const getFriendSearchPreview = onCall(async (request) => {
    if (request.auth == null || request.auth.uid == null) {
        throw new HttpsError("unauthenticated", "You must be authenticated.")
    }

    let friendUsername: string
    try {
        friendUsername = request.data.username as string
    } catch (e) {
        throw new HttpsError("invalid-argument", "The function must be called with friend username.")
    }

    const lowercasedName = friendUsername.toLowerCase()
    const friendDocs = await getFirestore()
        .collection(Collection.USERS)
        .where("name", "==", lowercasedName)
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
        displayName: user.displayName,
        profilePictureIndex: user.profilePictureIndex
    } as FriendSearchPreview
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

    if (user.friendIds == null || user.friendIds.length == 0) {
        return []
    }

    const friendUsers = await Promise.all(user.friendIds.map(async (id) => {
        return await firestore
            .collection(Collection.USERS)
            .doc(id)
            .get()
            .then((snapshot) => snapshot.data() as User)
    }))

    const includeChallenges = request.data.includeChallenges || false

    if (includeChallenges) {
        const friendsWithChallengesPromises = friendUsers.map(async (friend) => {
            const challenges = await firestore
                .collection(Collection.USERS)
                .doc(friend.id)
                .collection(Collection.CHALLENGES)
                .get()
                .then((snapshot) => snapshot.docs.map((doc) => {
                    return doc.data() as Challenge
                }))

            return {
                id: friend.id,
                name: friend.name,
                displayName: friend.displayName,
                profilePictureIndex: friend.profilePictureIndex,
                challenges: challenges,
                isFavorite: user.favoriteFriends?.includes(friend.id) ?? false
            } as Friend
        })

        return await Promise.all(friendsWithChallengesPromises);
    } else {
        return friendUsers.map((friend) => {
            return {
                id: friend.id,
                name: friend.name,
                displayName: friend.displayName,
                profilePictureIndex: friend.profilePictureIndex,
                isFavorite: user.favoriteFriends?.includes(friend.id) ?? false
            } as Friend
        })
    }
})