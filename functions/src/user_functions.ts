import { getFirestore } from "firebase-admin/firestore";
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