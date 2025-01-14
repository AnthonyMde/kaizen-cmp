export interface FriendRequest {
    id: string,
    sender: FriendRequestProfile,
    receiver: FriendRequestProfile,
    status: string
}

export interface FriendRequestProfile {
    id: string,
    username: string,
    profilePictureIndex: number
}

export enum FriendRequestStatus {
    "PENDING", "DECLINED", "ACCEPTED", "CANCELED"
}
