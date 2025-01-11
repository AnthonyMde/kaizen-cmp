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
import { onRequest } from "firebase-functions/v2/https";
import { Collection } from "./collection";
import { ErrorMessage } from "./errors";

// Start writing functions
// https://firebase.google.com/docs/functions/typescript

initializeApp();

export const getFriendPreviewById = onRequest(async (request, response) => {
    if (request.method != "GET") {
        response.status(405).send({ error: ErrorMessage.METHOD_NOT_ALLOWED })
        return;
    }

    // TODO: check auth uid

    let friendId: String
    try {
        friendId = request.query.id as String;
    } catch (e) {
        response.status(400).send({ error: ErrorMessage.MISSING_REQUIRED_FIELDS + ":\"id\"" })
        return;
    }

    try {
        const snapshot = await getFirestore()
            .collection(Collection.USERS)
            .where("id", "==", friendId)
            .get()

        const user = snapshot.docs[0].data() as User

        response.status(200).send({
            id: user.id,
            name: user.name,
            profilePictureIndex: user.profilePictureIndex
        });
    } catch (err) {
        logger.info({ error: err });
        response.status(500).send({ error: ErrorMessage.SERVER_INTERNAL_ERROR + "while retrieving friend preview" });
    }
});
