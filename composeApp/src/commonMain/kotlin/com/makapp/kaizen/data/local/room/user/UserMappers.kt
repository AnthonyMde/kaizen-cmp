package com.makapp.kaizen.data.local.room.user

import com.makapp.kaizen.data.remote.dto.UserDTO

fun UserDTO.toUserEntity(): UserEntity = UserEntity(
    id = id,
    email = email,
    name = name,
    displayName = displayName,
    profilePictureIndex = profilePictureIndex,
    friendIds = friendIds,
)

fun UserEntity.toUserDTO() = UserDTO(
    id = id,
    email = email,
    name = name,
    displayName = displayName,
    profilePictureIndex = profilePictureIndex,
    friendIds = friendIds,
)
