import { getFirestore, Timestamp } from "firebase-admin/firestore";
import { HttpsError, onCall } from "firebase-functions/https";
import { Collection } from "./collection";
import { Challenge, ChallengeStatus, MAX_LIVES_ALLOWED, MAX_TIME_TO_MARK_CHALLENGE_AS_FORGOT } from "./dto/challenge";

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
        updatedAt: Timestamp.now(),
        status: ChallengeStatus[ChallengeStatus.ON_GOING],
        days: 1,
        isDoneForToday: false,
        failureCount: 0,
        maxAuthorizedFailures: body.maxFailures,
        isDeleted: false,
        commitment: body.commitment,
        expectations: body.expectations,
        didUseForgotFeatureToday: false,
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

export const updateChallenge = onCall(async (request) => {
    if (request.auth == null || request.auth.uid == null) {
        throw new HttpsError("unauthenticated", "You must be authenticated.")
    }

    const firestore = getFirestore()
    const userId = request.auth.uid
    let updatedChallenge: Partial<Challenge>

    try {
        updatedChallenge = request.data as Partial<Challenge>
    } catch (e) {
        throw new HttpsError("invalid-argument", "Some submitted fields are not correct.")
    }

    if (updatedChallenge.id === undefined) {
        throw new HttpsError("invalid-argument", "No challenge id provided.")
    }

    if (updatedChallenge.maxAuthorizedFailures !== undefined) {
        const formerChallenge = await firestore
            .collection(Collection.USERS)
            .doc(userId)
            .collection(Collection.CHALLENGES)
            .doc(updatedChallenge.id)
            .get()
            .then((doc) => doc.data() as Challenge)

        const formerMax = formerChallenge.maxAuthorizedFailures

        if (updatedChallenge.maxAuthorizedFailures < formerMax) {
            throw new HttpsError("invalid-argument", "You cannot decrease your maximum number of lives.")
        }

        if (updatedChallenge.maxAuthorizedFailures > MAX_LIVES_ALLOWED) {
            throw new HttpsError("invalid-argument", `You cannot have more than ${MAX_LIVES_ALLOWED} lives.`)
        }
    }

    updatedChallenge.updatedAt = Timestamp.now()

    await firestore
        .collection(Collection.USERS)
        .doc(userId)
        .collection(Collection.CHALLENGES)
        .doc(updatedChallenge.id)
        .update(updatedChallenge)
})

export const forgotToCheckChallenge = onCall(async (request) => {
    if (request.auth == null || request.auth.uid == null) {
        throw new HttpsError("unauthenticated", "You must be authenticated.")
    }

    const userId = request.auth.uid
    let challengeId: string

    try {
        challengeId = request.data.id as string
    } catch (e) {
        throw new HttpsError("invalid-argument", "No challenge id provided.")
    }

    const firestore = getFirestore()

    const challengesDoc = await firestore
        .collection(Collection.USERS)
        .doc(userId)
        .collection(Collection.CHALLENGES)
        .get()
        .then((snapshot) => snapshot.docs)

    const challenges = challengesDoc.map((challenge) => challenge.data() as Challenge)

    const targetChallenge = challenges
        .find((challenge) => challenge.id === challengeId)

    if (!targetChallenge) {
        throw new HttpsError("permission-denied", "You can only update your own challenges.")
    }

    if (targetChallenge.status !== ChallengeStatus[ChallengeStatus.ON_GOING] && 
        targetChallenge.status !== ChallengeStatus[ChallengeStatus.FAILED]
    ) {
        throw new HttpsError("invalid-argument", "You can only forgot an error of an ongoing or failed challenge.")
    }

    if (targetChallenge.didUseForgotFeatureToday) {
        throw new HttpsError("invalid-argument", "You cannot forgot an error twice a day.")
    }

    if (targetChallenge.lastFailureDate != null) {
        const now = Timestamp.now().toDate().getTime()
        const lastFailureTime = targetChallenge.lastFailureDate.toDate().getTime()
        const hoursSinceLastFailure = (now - lastFailureTime) / (1000*60*60) // Millis to seconds, to hours.

        if (hoursSinceLastFailure >= MAX_TIME_TO_MARK_CHALLENGE_AS_FORGOT) {
            throw new HttpsError("invalid-argument", "You cannot forgot an error after 24 hours.")
        }
    }

    // All conditions are met, we forgot the error.
    // If failed -> reset to ongoing challenge.
    // If not -> just decrease error count.
    let updatedFields: Partial<Challenge> = {}

    if (targetChallenge.status === ChallengeStatus[ChallengeStatus.FAILED]) {
        updatedFields = {
            days: targetChallenge.days + 1,
            status: ChallengeStatus[ChallengeStatus.ON_GOING],
        }
    }

    updatedFields.failureCount = targetChallenge.failureCount - 1
    updatedFields.didUseForgotFeatureToday = true
    updatedFields.updatedAt = Timestamp.now()

    await firestore
        .collection(Collection.USERS)
        .doc(userId)
        .collection(Collection.CHALLENGES)
        .doc(targetChallenge.id)
        .update(updatedFields)
})
