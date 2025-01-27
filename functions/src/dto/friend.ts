import { Challenge } from "./challenge"

export interface Friend {
    id: string
    displayName: string
    name: string
    profilePictureIndex: number
    challenges: Array<Challenge>
    isFavorite: boolean
}