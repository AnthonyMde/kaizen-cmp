import { getFirestore, Timestamp } from "firebase-admin/firestore";
import { HttpsError, onCall } from "firebase-functions/https";
import { Collection } from "./collection";
import { Challenge, ChallengeStatus } from "./dto/challenge";

export const createChallenge = onCall(async (request) => {
    if (request.auth == null || request.auth.uid == null) {
        throw new HttpsError("unauthenticated", "You must be authenticated.")
    }

    const userId = request.auth.uid
    const body = request.data
    const firestore = getFirestore()

    if (body.name == null) {
        throw new HttpsError("invalid-argument", "No challenge name provided.")
    } else if (body.maxFailures == null) {
        throw new HttpsError("invalid-argument", "No challenge maxFailures provided.")
    }

    const challenge = {
        name: body.name,
        createdAt: Timestamp.now(),
        status: ChallengeStatus[ChallengeStatus.ON_GOING],
        days: 1,
        isDoneForToday: false,
        failureCount: 0,
        maxAuthorizedFailures: body.maxFailures
    } as Challenge

    const collectionRef = firestore
        .collection(Collection.USERS)
        .doc(userId)
        .collection(Collection.CHALLENGES)

    const docRef = await collectionRef.add(challenge)

    await collectionRef.doc(docRef.id).update({
        id: docRef.id
    })
})
