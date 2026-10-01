package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.CommentEntity
import com.example.data.model.MessageEntity
import com.example.data.model.NotificationEntity
import com.example.data.model.PostEntity
import com.example.data.model.PublicFeedbackEntity
import com.example.data.model.UserEntity
import com.example.data.repository.SomitiRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SomitiViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: SomitiRepository

    init {
        val dao = AppDatabase.getInstance(application).somitiDao()
        repository = SomitiRepository(dao)
        viewModelScope.launch {
            repository.ensureDatabaseSeeded()
        }
    }

    // Role View switching (Admin, Member, General user testing)
    private val _currentRole = MutableStateFlow("admin")
    val currentRole: StateFlow<String> = _currentRole.asStateFlow()

    // Current User
    private val _currentUser = MutableStateFlow(
        UserEntity(
            id = 1,
            name = "মোঃ শামীম",
            phone = "০১৭০০-০০০০০০",
            photoUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=200",
            address = "মিরপুর, ঢাকা, বাংলাদেশ",
            profession = "সফটওয়্যার প্রকৌশলী",
            education = "বি.এসসি (সিএসই)",
            isMember = true,
            designation = "সম্মানিত কার্যকরী সদস্য",
            role = "admin",
            bloodGroup = "B+",
            joinedDate = "২০২২"
        )
    )
    val currentUser: StateFlow<UserEntity> = _currentUser.asStateFlow()

    // Navigation Tab
    private val _currentTab = MutableStateFlow("feed") // "feed", "video", "members", "messages", "profile"
    val currentTab: StateFlow<String> = _currentTab.asStateFlow()

    fun setCurrentTab(tab: String) {
        _currentTab.value = tab
    }

    // Modal / Drawer state
    private val _showSettingsDrawer = MutableStateFlow(false)
    val showSettingsDrawer: StateFlow<Boolean> = _showSettingsDrawer.asStateFlow()

    private val _showNotifDrawer = MutableStateFlow(false)
    val showNotifDrawer: StateFlow<Boolean> = _showNotifDrawer.asStateFlow()

    private val _showCommentsDrawer = MutableStateFlow(false)
    val showCommentsDrawer: StateFlow<Boolean> = _showCommentsDrawer.asStateFlow()

    private val _showAdminModal = MutableStateFlow(false)
    val showAdminModal: StateFlow<Boolean> = _showAdminModal.asStateFlow()

    private val _showSignupModal = MutableStateFlow(false)
    val showSignupModal: StateFlow<Boolean> = _showSignupModal.asStateFlow()

    private val _selectedPostForComments = MutableStateFlow<PostEntity?>(null)
    val selectedPostForComments: StateFlow<PostEntity?> = _selectedPostForComments.asStateFlow()

    private val _selectedMemberForDetail = MutableStateFlow<UserEntity?>(null)
    val selectedMemberForDetail: StateFlow<UserEntity?> = _selectedMemberForDetail.asStateFlow()

    // Live Meeting State
    private val _isLiveMeetingJoined = MutableStateFlow(false)
    val isLiveMeetingJoined: StateFlow<Boolean> = _isLiveMeetingJoined.asStateFlow()

    // Active Channel for Chat
    private val _activeChatChannel = MutableStateFlow("general")
    val activeChatChannel: StateFlow<String> = _activeChatChannel.asStateFlow()

    // Feed Filter: "all", "members", "notices"
    private val _feedFilter = MutableStateFlow("all")
    val feedFilter: StateFlow<String> = _feedFilter.asStateFlow()

    fun setFeedFilter(filter: String) {
        _feedFilter.value = filter
    }

    // Repository Flows
    val allUsers: StateFlow<List<UserEntity>> = repository.allUsers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val members: StateFlow<List<UserEntity>> = repository.members
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allPosts: StateFlow<List<PostEntity>> = repository.allPosts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allNotifications: StateFlow<List<NotificationEntity>> = repository.allNotifications
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val unreadNotifCount: StateFlow<Int> = repository.unreadNotifCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val publicFeedbacks: StateFlow<List<PublicFeedbackEntity>> = repository.allFeedback
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    val currentChannelMessages: StateFlow<List<MessageEntity>> = _activeChatChannel
        .flatMapLatest { channelId -> repository.getMessagesForChannel(channelId) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    val currentPostComments: StateFlow<List<CommentEntity>> = _selectedPostForComments
        .flatMapLatest { post ->
            if (post != null) repository.getCommentsForPost(post.id)
            else kotlinx.coroutines.flow.flowOf(emptyList())
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Role Switching
    fun switchRole(role: String) {
        _currentRole.value = role
        val current = _currentUser.value
        when (role) {
            "admin" -> {
                _currentUser.value = current.copy(
                    role = "admin",
                    isMember = true,
                    designation = "সম্মানিত কার্যকরী সদস্য ও এডমিন"
                )
            }
            "member" -> {
                _currentUser.value = current.copy(
                    role = "member",
                    isMember = true,
                    designation = "সম্মানিত কার্যকরী সদস্য"
                )
            }
            else -> {
                _currentUser.value = current.copy(
                    role = "general",
                    isMember = false,
                    designation = ""
                )
            }
        }
    }

    // Drawer / Modal Controls
    fun toggleSettingsDrawer(show: Boolean) { _showSettingsDrawer.value = show }
    fun toggleNotifDrawer(show: Boolean) { _showNotifDrawer.value = show }
    fun toggleCommentsDrawer(show: Boolean) { _showCommentsDrawer.value = show }
    fun toggleAdminModal(show: Boolean) { _showAdminModal.value = show }
    fun toggleSignupModal(show: Boolean) { _showSignupModal.value = show }
    fun selectPostForComments(post: PostEntity?) { _selectedPostForComments.value = post }
    fun selectMemberForDetail(member: UserEntity?) { _selectedMemberForDetail.value = member }
    fun setLiveMeetingJoined(joined: Boolean) { _isLiveMeetingJoined.value = joined }
    fun setActiveChatChannel(channel: String) { _activeChatChannel.value = channel }

    // Actions
    fun createPost(content: String, imageUrl: String = "", isNotice: Boolean = false) {
        if (content.isBlank()) return
        viewModelScope.launch {
            val user = _currentUser.value
            val newPost = PostEntity(
                authorId = user.id,
                authorName = user.name,
                authorPhoto = user.photoUrl,
                authorDesignation = if (user.isMember) user.designation else "",
                authorIsMember = user.isMember,
                content = content.trim(),
                imageUrl = imageUrl.trim(),
                timeAgo = "এইমাত্র",
                timestamp = System.currentTimeMillis(),
                likesCount = 0,
                commentsCount = 0,
                isLiked = false,
                isNotice = isNotice
            )
            repository.insertPost(newPost)
        }
    }

    fun toggleLike(post: PostEntity) {
        viewModelScope.launch {
            repository.togglePostLike(post)
        }
    }

    fun deletePost(post: PostEntity) {
        viewModelScope.launch {
            repository.deletePost(post)
        }
    }

    fun addCommentToPost(postId: Long, text: String) {
        if (text.isBlank()) return
        viewModelScope.launch {
            val user = _currentUser.value
            val comment = CommentEntity(
                postId = postId,
                authorName = user.name,
                authorPhoto = user.photoUrl,
                authorDesignation = if (user.isMember) user.designation else "",
                text = text.trim(),
                timeAgo = "এইমাত্র",
                timestamp = System.currentTimeMillis()
            )
            repository.addComment(comment)
        }
    }

    fun addPublicFeedback(message: String) {
        if (message.isBlank()) return
        viewModelScope.launch {
            val feedback = PublicFeedbackEntity(
                authorName = _currentUser.value.name,
                message = message.trim(),
                timeAgo = "এইমাত্র"
            )
            repository.addFeedback(feedback)
        }
    }

    fun sendMessage(text: String) {
        if (text.isBlank()) return
        viewModelScope.launch {
            val user = _currentUser.value
            val message = MessageEntity(
                channelId = _activeChatChannel.value,
                senderName = user.name,
                senderPhoto = user.photoUrl,
                senderDesignation = user.designation,
                text = text.trim(),
                timestamp = System.currentTimeMillis(),
                isFromCurrentUser = true
            )
            repository.sendMessage(message)
        }
    }

    fun registerNewUser(
        name: String,
        phone: String,
        photoUrl: String,
        address: String,
        profession: String,
        education: String
    ) {
        viewModelScope.launch {
            val fallbackPhoto = if (photoUrl.isNotBlank()) photoUrl
            else "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=200"

            val newUser = UserEntity(
                name = name.trim(),
                phone = phone.trim(),
                photoUrl = fallbackPhoto,
                address = if (address.isNotBlank()) address.trim() else "অনির্দিষ্ট",
                profession = if (profession.isNotBlank()) profession.trim() else "অনির্দিষ্ট",
                education = if (education.isNotBlank()) education.trim() else "অনির্দিষ্ট",
                isMember = false,
                designation = "",
                role = "general",
                joinedDate = "২০২৬"
            )
            val newId = repository.insertUser(newUser)
            _currentUser.value = newUser.copy(id = newId)
            _currentRole.value = "general"
        }
    }

    fun saveAdminApproval(userId: Long, assignAsMember: Boolean, designation: String) {
        viewModelScope.launch {
            val role = if (assignAsMember) "member" else "general"
            val finalDesig = if (assignAsMember) {
                if (designation.isNotBlank()) designation.trim() else "সাধারণ সদস্য"
            } else ""

            repository.updateUserRoleAndDesignation(userId, role, assignAsMember, finalDesig)

            // If updating current user, refresh current user state too
            if (_currentUser.value.id == userId) {
                _currentUser.value = _currentUser.value.copy(
                    isMember = assignAsMember,
                    role = role,
                    designation = finalDesig
                )
            }
        }
    }

    fun markAllNotificationsAsRead() {
        viewModelScope.launch {
            repository.markAllNotificationsAsRead()
        }
    }

    fun updateUserProfile(
        name: String,
        photoUrl: String,
        address: String,
        profession: String,
        education: String,
        phone: String
    ) {
        viewModelScope.launch {
            val updated = _currentUser.value.copy(
                name = name.trim(),
                photoUrl = photoUrl.trim(),
                address = address.trim(),
                profession = profession.trim(),
                education = education.trim(),
                phone = phone.trim()
            )
            _currentUser.value = updated
            repository.updateUser(updated)
        }
    }
}
