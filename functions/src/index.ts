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
import * as logger from "firebase-functions/logger";
import { onCall, HttpsError } from "firebase-functions/v2/https";
import { Collection } from "./collection";
import { ErrorMessage } from "./errors";
import { FriendPreview } from "./dto/friend_preview";

// Start writing functions
// https://firebase.google.com/docs/functions/typescript

initializeApp();

export const getFriendPreviewById = onCall(async (request) => {
    if (request.auth == null || request.auth.uid == null) {
        throw new HttpsError("unauthenticated", "You must be authenticated.")
    }

    let friendId: String
    try {
        friendId = request.data.friendId as String
    } catch (e) {
        throw new HttpsError("invalid-argument", "The function must be called with friend id.")
    }

    try {
        const snapshot = await getFirestore()
            .collection(Collection.USERS)
            .where("id", "==", friendId)
            .get()

        const user = snapshot.docs[0].data() as User

        return {
            id: user.id,
            name: user.name,
            profilePictureIndex: user.profilePictureIndex
        } as FriendPreview
    } catch (err) {
        logger.info({ error: err });
        throw new HttpsError("internal", ErrorMessage.SERVER_INTERNAL_ERROR + "while retrieving friend preview")
    }
});
