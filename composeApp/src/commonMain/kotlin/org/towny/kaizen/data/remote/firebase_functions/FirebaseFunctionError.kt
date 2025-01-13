package org.towny.kaizen.data.remote.firebase_functions

import dev.gitlive.firebase.functions.FirebaseFunctionsException
import dev.gitlive.firebase.functions.FunctionsExceptionCode
import dev.gitlive.firebase.functions.code
import org.towny.kaizen.domain.exceptions.DomainException

fun FirebaseFunctionsException.toDomainException(): DomainException {
    return when (this.code) {
        FunctionsExceptionCode.UNKNOWN -> DomainException.Common.Unknown
        FunctionsExceptionCode.INVALID_ARGUMENT -> DomainException.Common.InvalidArguments
        FunctionsExceptionCode.NOT_FOUND -> DomainException.Common.NotFound
        FunctionsExceptionCode.PERMISSION_DENIED -> DomainException.Auth.UserNotAuthorized
        FunctionsExceptionCode.INTERNAL -> DomainException.Common.ServerInternalError
        FunctionsExceptionCode.UNAUTHENTICATED -> DomainException.Auth.UserNotAuthenticated
        else -> DomainException.Common.Unknown
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
