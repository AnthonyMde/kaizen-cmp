import { Timestamp } from "firebase-admin/firestore";

export interface Challenge {
    id: string,
    name: string,
    createdAt: Timestamp,
    isCompleted: boolean,
    failures: number,
    maxFailures: number
}