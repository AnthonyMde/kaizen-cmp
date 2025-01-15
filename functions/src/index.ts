/**
 * Import function triggers from their respective submodules:
 *
 * import {onCall} from "firebase-functions/v2/https";
 * import {onDocumentWritten} from "firebase-functions/v2/firestore";
 *
 * See a full list of supported triggers at https://firebase.google.com/docs/functions
 */

import { initializeApp } from "firebase-admin/app";
import { createFriendRequest, getFriendRequests, updateFriendRequest } from "./friend_requests_functions";
import { getFriendPreviewById, getFriends } from "./friends_functions";

// Start writing functions
// https://firebase.google.com/docs/functions/typescript

initializeApp();

export {
    createFriendRequest,
    getFriendPreviewById,
    getFriendRequests,
    updateFriendRequest,
    getFriends
};

