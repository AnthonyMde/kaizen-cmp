import * as admin from "firebase-admin";
import { onSchedule } from "firebase-functions/v2/scheduler";
import { Collection } from "./collection";
import { Challenge, ChallengeStatus, NUMBER_OF_DAYS_FOR_DONE } from "./dto/challenge";

export const checkUserChallengesCron = onSchedule({ schedule: "0 4 * * *", timeZone: "Europe/Paris" },
    async () => {
        const db = admin.firestore();
        const userCollection = await db.collection(Collection.USERS).get();

        for (const userDoc of userCollection.docs) {
            const challengeCollection = await userDoc.ref.collection(Collection.CHALLENGES).get();

            for (const challengeDoc of challengeCollection.docs) {
                const challenge = challengeDoc.data() as Challenge

                if (challenge.status !== ChallengeStatus[ChallengeStatus.ON_GOING]
                    || challenge.isDeleted === true) continue // skip not ongoing challenge.

                let failureCount: number = challenge.failureCount
                let status: string = challenge.status
                let days: number = challenge.days

                if (!challenge.isDoneForToday) {
                    failureCount++
                }

                const isFailed = () => failureCount > challenge.maxAuthorizedFailures
                const isDone = () => days === NUMBER_OF_DAYS_FOR_DONE

                if (isFailed()) {
                    status = ChallengeStatus[ChallengeStatus.FAILED]
                } else if (isDone()) {
                    status = ChallengeStatus[ChallengeStatus.DONE]
                } else {
                    days++
                }

                await challengeDoc.ref.update({
                    isDoneForToday: false,
                    failureCount: failureCount,
                    status: status,
                    days: days
                });
            }
        }
    });
