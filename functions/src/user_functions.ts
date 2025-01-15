import { getAuth } from "firebase-admin/auth";
import { FieldValue, getFirestore } from "firebase-admin/firestore";
import { HttpsError, onCall } from "firebase-functions/https";
import { Collection } from "./collection";

export const isUsernameAvailable = onCall(async (request) => {
    if (request.auth == null || request.auth.uid == null) {
        throw new HttpsError("unauthenticated", "You must be authenticated.")
    }

    const username: string = request.data.username
    if (!username) {
        throw new HttpsError("invalid-argument", "Function must be called with username param.")
    }
    const docs = await getFirestore()
        .collection(Collection.USERS)
        .where("name", "==", username)
        .get()

    return {
        isAvailable: docs.size == 0
    } as isUsernameAvailable
});

export const deleteUserAccount = onCall(async (request) => {
    if (request.auth == null || request.auth.uid == null) {
        throw new HttpsError("unauthenticated", "You must be authenticated.")
    }

    const userId = request.auth.uid
    const firestore = getFirestore()
    const batch = firestore.batch()

    const userRef = firestore
        .collection(Collection.USERS)
        .doc(userId)
    const user = await userRef.get().then((snapshot) => snapshot.data() as User)

    // Delete user reference from friends' user
    if (user.friendIds && user.friendIds.length > 0) {
        const friendRefs = user.friendIds.map((friendId) => {
            return firestore
                .collection(Collection.USERS)
                .doc(friendId)
        })
        friendRefs.forEach((friendRef) => {
            batch.update(friendRef, {
                friendIds: FieldValue.arrayRemove(userId)
            })
        })
    }

    // Delete firestore user account.
    batch.delete(userRef)

    await batch.commit()

    // Delete auth user account.
    await getAuth().deleteUser(userId)
});