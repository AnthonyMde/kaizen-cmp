import { Timestamp } from "firebase-admin/firestore";

export interface Challenge {
    id: string,
    name: string,
    createdAt: Timestamp,
    updatedAt?: Timestamp,
    status: string,
    days: number,
    isDoneForToday: boolean,
    failureCount: number,
    maxAuthorizedFailures: number,
    isDeleted?: boolean,
    commitment?: string,
    expectations?: string,
    lastFailureDate?: Timestamp,
    didUseForgotFeatureToday?: boolean,
}

export enum ChallengeStatus {
    'ON_GOING', 'PAUSED', 'DONE', 'FAILED', 'ABANDONED'
}

export const NUMBER_OF_DAYS_FOR_DONE = 365
export const MAX_LIVES_ALLOWED = 12
export const MAX_TIME_TO_MARK_CHALLENGE_AS_FORGOT = 24 // hours