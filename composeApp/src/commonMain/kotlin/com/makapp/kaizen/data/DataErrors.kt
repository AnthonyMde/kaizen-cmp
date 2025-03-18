package com.makapp.kaizen.data

import dev.gitlive.firebase.functions.FirebaseFunctionsException
import dev.gitlive.firebase.functions.FunctionsExceptionCode
import dev.gitlive.firebase.functions.code
import com.makapp.kaizen.domain.exceptions.DomainException

fun Throwable.toDomainException(): DomainException {
    println("DEBUG: (DataErrors) Raw exception thrown: $this")

    return when(this) {
        is FirebaseFunctionsException -> {
            toDomainException(this.message)
        }

        else -> DomainException.Common.Unknown(this.message)
    }
}

private fun FirebaseFunctionsException.toDomainException(message: String?): DomainException {
    return when (this.code) {
        FunctionsExceptionCode.UNKNOWN -> DomainException.Common.Unknown(message)
        FunctionsExceptionCode.INVALID_ARGUMENT -> DomainException.Common.InvalidArguments
        FunctionsExceptionCode.NOT_FOUND -> DomainException.Common.NotFound
        FunctionsExceptionCode.PERMISSION_DENIED -> DomainException.Auth.UserNotAuthorized
        FunctionsExceptionCode.INTERNAL -> DomainException.Common.ServerInternalError
        FunctionsExceptionCode.UNAUTHENTICATED -> DomainException.Auth.UserNotAuthenticated
        else -> DomainException.Common.Unknown(message)
//                FunctionsExceptionCode.OK ->
//                FunctionsExceptionCode.CANCELLED ->
//                FunctionsExceptionCode.DEADLINE_EXCEEDED ->
//                FunctionsExceptionCode.ALREADY_EXISTS ->
//                FunctionsExceptionCode.RESOURCE_EXHAUSTED ->
//                FunctionsExceptionCode.FAILED_PRECONDITION ->
//                FunctionsExceptionCode.ABORTED ->
//                FunctionsExceptionCode.OUT_OF_RANGE ->
//                FunctionsExceptionCode.UNIMPLEMENTED ->
//                FunctionsExceptionCode.UNAVAILABLE ->
//                FunctionsExceptionCode.DATA_LOSS ->
    }
}
