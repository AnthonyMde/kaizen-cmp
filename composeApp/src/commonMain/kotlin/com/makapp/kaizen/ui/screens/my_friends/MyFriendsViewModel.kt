package com.makapp.kaizen.ui.screens.my_friends

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.makapp.kaizen.domain.exceptions.DomainException
import com.makapp.kaizen.domain.models.Resource
import com.makapp.kaizen.domain.services.FriendPreviewsService
import com.makapp.kaizen.domain.services.FriendRequestsService
import com.makapp.kaizen.domain.services.FriendsService
import com.makapp.kaizen.domain.services.UsersService
import com.makapp.kaizen.domain.usecases.friend.GetFriendPreviewUseCase
import kaizen.composeapp.generated.resources.Res
import kaizen.composeapp.generated.resources.friends_search_cannot_find_friend_id
import kaizen.composeapp.generated.resources.friends_search_not_authorized_error
import kaizen.composeapp.generated.resources.friends_search_request_to_yourself
import kaizen.composeapp.generated.resources.friends_search_username_is_empty_error
import kaizen.composeapp.generated.resources.unknown_error
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MyFriendsViewModel(
    private val friendRequestsService: FriendRequestsService,
    private val getFriendPreviewUseCase: GetFriendPreviewUseCase,
    private val friendsService: FriendsService,
    private val friendPreviewsService: FriendPreviewsService,
    private val usersService: UsersService
) : ViewModel() {
    private val _myFriendsState = MutableStateFlow(MyFriendsState())
    val myFriendsState = _myFriendsState.asStateFlow()
        .onStart {
            viewModelScope.launch { friendPreviewsService.refreshFriendPreviews() }
            viewModelScope.launch { friendRequestsService.refreshFriendRequests() }
            watchFriendRequests()
            watchFriendPreviews()
        }

    fun onAction(action: MyFriendsAction) {
        when (action) {
            is MyFriendsAction.OnFriendUsernameInputChanged -> {
                _myFriendsState.update {
                    it.copy(
                        friendUsernameInputValue = action.username,
                        friendUsernameInputError = null
                    )
                }
            }

            is MyFriendsAction.OnSearchFriendProfile -> viewModelScope.launch {
                val username = _myFriendsState.value.friendUsernameInputValue
                getSearchFriendPreview(username)
            }

            is MyFriendsAction.OnFriendRequestSubmit -> viewModelScope.launch {
                createFriendRequest()
            }

            is MyFriendsAction.OnFriendRequestUpdated -> viewModelScope.launch {
                updateFriendRequest(action)
            }

            is MyFriendsAction.OnToggleFriendAsFavorite -> viewModelScope.launch {
                toggleFriendAsFavorite(action.friendId)
            }

            else -> {}
        }
    }

    private fun watchFriendRequests() = viewModelScope.launch {
        val user = usersService.getUser() ?: return@launch

        friendRequestsService.watchFriendRequests().collectLatest { result ->
            when (result) {
                is Resource.Error -> {
                    // TODO
                }

                is Resource.Loading -> {}
                is Resource.Success -> {
                    val sent = result.data?.filter { it.sender.id == user.id } ?: emptyList()
                    val received =
                        result.data?.filter { it.receiver.id == user.id } ?: emptyList()
                    _myFriendsState.update {
                        it.copy(
                            pendingSentRequests = sent,
                            pendingReceivedRequests = received
                        )
                    }
                }
            }
        }
    }

    private suspend fun createFriendRequest() {
        val friendId = _myFriendsState.value.friendSearchPreview?.id

        _myFriendsState.update {
            it.copy(
                isSendFriendRequestLoading = true,
                friendUsernameInputError = null
            )
        }

        friendRequestsService.createFriendRequest(friendId).let { result ->
            when (result) {
                is Resource.Error -> {
                    val errorMessage = when (result.throwable) {
                        DomainException.Auth.UserNotAuthenticated -> Res.string.friends_search_not_authorized_error
                        DomainException.Common.InvalidArguments -> Res.string.friends_search_cannot_find_friend_id
                        else -> Res.string.unknown_error
                    }
                    _myFriendsState.update { it.copy(friendUsernameInputError = errorMessage) }
                }

                is Resource.Success -> {
                    _myFriendsState.update {
                        it.copy(
                            friendSearchPreview = null,
                            friendUsernameInputValue = ""
                        )
                    }
                }

                is Resource.Loading -> {}
            }
        }
        _myFriendsState.update { it.copy(isSendFriendRequestLoading = false) }
    }

    private suspend fun updateFriendRequest(action: MyFriendsAction.OnFriendRequestUpdated) {
        friendRequestsService.updateFriendRequest(action.requestId, action.status)
            .collectLatest { result ->
                when (result) {
                    is Resource.Error -> {
                        // TODO: Add toast showing error
                        _myFriendsState.update {
                            it.copy(
                                requestIdsUnderUpdate =
                                getIdsUnderUpdate(
                                    _myFriendsState.value.requestIdsUnderUpdate,
                                    action.requestId
                                )
                            )
                        }
                    }

                    is Resource.Success -> {
                        _myFriendsState.update {
                            it.copy(
                                requestIdsUnderUpdate =
                                getIdsUnderUpdate(
                                    _myFriendsState.value.requestIdsUnderUpdate,
                                    action.requestId
                                )
                            )
                        }
                    }

                    is Resource.Loading -> {
                        _myFriendsState.update {
                            it.copy(
                                requestIdsUnderUpdate = getIdsUnderUpdate(
                                    _myFriendsState.value.requestIdsUnderUpdate,
                                    action.requestId,
                                    true
                                )
                            )
                        }
                    }
                }
            }
    }

    private fun getIdsUnderUpdate(
        entry: List<String>,
        id: String,
        adding: Boolean = false
    ): List<String> {
        val list = entry.toMutableList()
        if (adding) {
            list.add(id)
        } else {
            list.remove(id)
        }
        return list
    }

    private fun watchFriendPreviews() = viewModelScope.launch {
        friendPreviewsService.watchFriendPreviews().collectLatest { result ->
            when (result) {
                is Resource.Error -> {
                    _myFriendsState.update {
                        it.copy(
                            friendPreviews = result.data ?: emptyList(),
                        )
                    }
                }

                is Resource.Success -> {
                    _myFriendsState.update {
                        it.copy(
                            friendPreviews = result.data ?: emptyList(),
                        )
                    }
                }

                is Resource.Loading -> {}
            }
        }
    }

    private suspend fun getSearchFriendPreview(username: String) {
        getFriendPreviewUseCase(username).collectLatest { result ->
            when (result) {
                is Resource.Error -> {
                    val errorMessage = when (result.throwable) {
                        DomainException.Common.NotFound -> Res.string.friends_search_cannot_find_friend_id
                        DomainException.Auth.UserNotAuthenticated -> Res.string.friends_search_not_authorized_error
                        DomainException.Common.InvalidArguments -> Res.string.friends_search_username_is_empty_error
                        DomainException.Friend.CannotSearchForYourself -> Res.string.friends_search_request_to_yourself
                        else -> Res.string.unknown_error
                    }
                    _myFriendsState.update {
                        it.copy(
                            friendUsernameInputError = errorMessage,
                            friendSearchPreview = null,
                            isFriendPreviewLoading = false
                        )
                    }
                }

                is Resource.Success -> {
                    _myFriendsState.update {
                        it.copy(
                            friendSearchPreview = result.data,
                            isFriendPreviewLoading = false
                        )
                    }
                }

                is Resource.Loading -> {
                    _myFriendsState.update { it.copy(isFriendPreviewLoading = true) }
                }
            }
        }
    }

    private suspend fun toggleFriendAsFavorite(friendId: String) {
        friendsService.toggleFriendAsFavorite(friendId).collectLatest { result ->
            when (result) {
                is Resource.Error -> {
                    _myFriendsState.update {
                        it.copy(
                            friendIdsUnderUpdate = getIdsUnderUpdate(
                                it.friendIdsUnderUpdate,
                                friendId
                            )
                        )
                    }
                }

                is Resource.Success -> {
                    _myFriendsState.update {
                        it.copy(
                            friendIdsUnderUpdate = getIdsUnderUpdate(
                                it.friendIdsUnderUpdate,
                                friendId
                            )
                        )
                    }
                }

                is Resource.Loading -> {
                    _myFriendsState.update {
                        it.copy(
                            friendIdsUnderUpdate = getIdsUnderUpdate(
                                it.friendIdsUnderUpdate,
                                friendId,
                                adding = true
                            )
                        )
                    }
                }
            }
        }
    }
}
