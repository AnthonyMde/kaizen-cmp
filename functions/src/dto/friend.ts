import { Challenge } from "./challenge"

export interface Friend {
    "id": string
    "name": string
    "profilePictureIndex": number
    "challenges": Array<Challenge>
}