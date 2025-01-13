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

// Start writing functions
// https://firebase.google.com/docs/functions/typescript

initializeApp();

export const getFriendPreviewById = onCall(async (request) => {
    if (request.auth == null || request.auth.uid == null) {
        throw new HttpsError("unauthenticated", "You must be authenticated.")
    }

    let friendUsername: String
    try {
        friendUsername = request.data.username as String
    } catch (e) {
        throw new HttpsError("invalid-argument", "The function must be called with friend id.")
    }
    const snapshot = await getFirestore()
        .collection(Collection.USERS)
        .where("name", "==", friendUsername)
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
