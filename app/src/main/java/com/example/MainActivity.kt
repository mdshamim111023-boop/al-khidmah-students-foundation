package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.SomitiBottomNavBar
import com.example.ui.components.SomitiTopBar
import com.example.ui.dialogs.AdminPanelDialog
import com.example.ui.dialogs.CommentsDrawerSheet
import com.example.ui.dialogs.EditProfileDialog
import com.example.ui.dialogs.MemberDetailDialog
import com.example.ui.dialogs.NotificationsSheet
import com.example.ui.dialogs.PostCommentsSheet
import com.example.ui.dialogs.SettingsDrawerSheet
import com.example.ui.dialogs.SignupDialog
import com.example.ui.screens.FeedScreen
import com.example.ui.screens.MembersScreen
import com.example.ui.screens.MessagesScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.VideoLiveScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.SomitiViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val viewModel: SomitiViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                SomitiApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun SomitiApp(viewModel: SomitiViewModel) {
    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val currentRole by viewModel.currentRole.collectAsStateWithLifecycle()
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    val allUsers by viewModel.allUsers.collectAsStateWithLifecycle()
    val posts by viewModel.allPosts.collectAsStateWithLifecycle()
    val feedFilter by viewModel.feedFilter.collectAsStateWithLifecycle()
    val notifications by viewModel.allNotifications.collectAsStateWithLifecycle()
    val unreadNotifCount by viewModel.unreadNotifCount.collectAsStateWithLifecycle()
    val feedbacks by viewModel.publicFeedbacks.collectAsStateWithLifecycle()
    val currentMessages by viewModel.currentChannelMessages.collectAsStateWithLifecycle()
    val activeChatChannel by viewModel.activeChatChannel.collectAsStateWithLifecycle()
    val isLiveMeetingJoined by viewModel.isLiveMeetingJoined.collectAsStateWithLifecycle()

    val showSettingsDrawer by viewModel.showSettingsDrawer.collectAsStateWithLifecycle()
    val showNotifDrawer by viewModel.showNotifDrawer.collectAsStateWithLifecycle()
    val showCommentsDrawer by viewModel.showCommentsDrawer.collectAsStateWithLifecycle()
    val showAdminModal by viewModel.showAdminModal.collectAsStateWithLifecycle()
    val showSignupModal by viewModel.showSignupModal.collectAsStateWithLifecycle()
    val selectedPostForComments by viewModel.selectedPostForComments.collectAsStateWithLifecycle()
    val postComments by viewModel.currentPostComments.collectAsStateWithLifecycle()
    val selectedMemberForDetail by viewModel.selectedMemberForDetail.collectAsStateWithLifecycle()

    var showEditProfileDialog by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // Handle Back Press gracefully
    BackHandler(
        enabled = showSettingsDrawer || showNotifDrawer || showCommentsDrawer ||
                showAdminModal || showSignupModal || selectedPostForComments != null ||
                selectedMemberForDetail != null || showEditProfileDialog || currentTab != "feed"
    ) {
        when {
            showSettingsDrawer -> viewModel.toggleSettingsDrawer(false)
            showNotifDrawer -> viewModel.toggleNotifDrawer(false)
            showCommentsDrawer -> viewModel.toggleCommentsDrawer(false)
            showAdminModal -> viewModel.toggleAdminModal(false)
            showSignupModal -> viewModel.toggleSignupModal(false)
            selectedPostForComments != null -> viewModel.selectPostForComments(null)
            selectedMemberForDetail != null -> viewModel.selectMemberForDetail(null)
            showEditProfileDialog -> showEditProfileDialog = false
            currentTab != "feed" -> viewModel.setCurrentTab("feed")
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            SomitiTopBar(
                unreadNotifCount = unreadNotifCount,
                feedbackCount = feedbacks.size,
                onMenuClick = { viewModel.toggleSettingsDrawer(true) },
                onTitleClick = { viewModel.setCurrentTab("feed") },
                onCommentsClick = { viewModel.toggleCommentsDrawer(true) },
                onNotifClick = { viewModel.toggleNotifDrawer(true) }
            )
        },
        bottomBar = {
            SomitiBottomNavBar(
                currentTab = currentTab,
                onTabSelected = { viewModel.setCurrentTab(it) }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentTab) {
                "feed" -> {
                    FeedScreen(
                        currentRole = currentRole,
                        currentUser = currentUser,
                        posts = posts,
                        feedFilter = feedFilter,
                        onRoleSelected = { role ->
                            viewModel.switchRole(role)
                            coroutineScope.launch {
                                val roleText = when (role) {
                                    "admin" -> "এডমিন"
                                    "member" -> "সদস্য"
                                    else -> "সাধারণ মানুষ"
                                }
                                snackbarHostState.showSnackbar("ভিউ পরিবর্তিত হয়েছে: $roleText")
                            }
                        },
                        onFilterSelected = { viewModel.setFeedFilter(it) },
                        onCreatePost = { content, imageUrl, isNotice ->
                            viewModel.createPost(content, imageUrl, isNotice)
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("আপনার পোস্ট প্রকাশিত হয়েছে!")
                            }
                        },
                        onLikePost = { viewModel.toggleLike(it) },
                        onCommentPost = { viewModel.selectPostForComments(it) },
                        onDeletePost = {
                            viewModel.deletePost(it)
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("পোস্ট মুছে ফেলা হয়েছে")
                            }
                        },
                        onNavigateToLive = { viewModel.setCurrentTab("video") }
                    )
                }

                "video" -> {
                    VideoLiveScreen(
                        isLiveMeetingJoined = isLiveMeetingJoined,
                        onJoinLiveMeeting = { joined ->
                            viewModel.setLiveMeetingJoined(joined)
                            coroutineScope.launch {
                                if (joined) snackbarHostState.showSnackbar("সভায় সফলভাবে যুক্ত হয়েছেন!")
                                else snackbarHostState.showSnackbar("সভা থেকে প্রস্থান করেছেন")
                            }
                        }
                    )
                }

                "members" -> {
                    MembersScreen(
                        users = allUsers,
                        onMemberClick = { viewModel.selectMemberForDetail(it) },
                        onSendMessageToMember = { member ->
                            viewModel.setActiveChatChannel("dm_${member.id}")
                            viewModel.setCurrentTab("messages")
                        }
                    )
                }

                "messages" -> {
                    MessagesScreen(
                        activeChannel = activeChatChannel,
                        onlineUsers = allUsers.filter { it.id != currentUser.id },
                        messages = currentMessages,
                        onSelectChannel = { viewModel.setActiveChatChannel(it) },
                        onSendMessage = { text ->
                            viewModel.sendMessage(text)
                        }
                    )
                }

                "profile" -> {
                    ProfileScreen(
                        user = currentUser,
                        onOpenSignup = { viewModel.toggleSignupModal(true) },
                        onOpenEditProfile = { showEditProfileDialog = true }
                    )
                }
            }
        }
    }

    // Modal Sheets and Dialogs
    if (showSettingsDrawer) {
        SettingsDrawerSheet(
            isAdmin = currentRole == "admin",
            onDismiss = { viewModel.toggleSettingsDrawer(false) },
            onOpenAdminPanel = { viewModel.toggleAdminModal(true) },
            onOpenSignup = { viewModel.toggleSignupModal(true) }
        )
    }

    if (showNotifDrawer) {
        NotificationsSheet(
            notifications = notifications,
            onDismiss = { viewModel.toggleNotifDrawer(false) },
            onMarkAllRead = {
                viewModel.markAllNotificationsAsRead()
                coroutineScope.launch {
                    snackbarHostState.showSnackbar("সকল নোটিফিকেশন পড়া হয়েছে হিসেবে চিহ্নিত করা হয়েছে")
                }
            }
        )
    }

    if (showCommentsDrawer) {
        CommentsDrawerSheet(
            feedbacks = feedbacks,
            onDismiss = { viewModel.toggleCommentsDrawer(false) },
            onSubmitFeedback = { text ->
                viewModel.addPublicFeedback(text)
                coroutineScope.launch {
                    snackbarHostState.showSnackbar("আপনার মূল্যবান মতামত গ্রহণ করা হয়েছে!")
                }
            }
        )
    }

    if (showAdminModal) {
        AdminPanelDialog(
            users = allUsers,
            onDismiss = { viewModel.toggleAdminModal(false) },
            onSaveApproval = { userId, assignAsMember, designation ->
                viewModel.saveAdminApproval(userId, assignAsMember, designation)
                coroutineScope.launch {
                    snackbarHostState.showSnackbar("পদবী ও সদস্য তথ্য সফলভাবে সংরক্ষিত হয়েছে!")
                }
            }
        )
    }

    if (showSignupModal) {
        SignupDialog(
            onDismiss = { viewModel.toggleSignupModal(false) },
            onSignup = { name, phone, photo, address, profession, education ->
                viewModel.registerNewUser(name, phone, photo, address, profession, education)
                coroutineScope.launch {
                    snackbarHostState.showSnackbar("আপনার সাইন-আপ সম্পন্ন হয়েছে! এডমিন অনুমোদনের অপেক্ষায় রয়েছে।")
                }
            }
        )
    }

    if (selectedPostForComments != null) {
        PostCommentsSheet(
            post = selectedPostForComments!!,
            comments = postComments,
            onDismiss = { viewModel.selectPostForComments(null) },
            onAddComment = { text ->
                viewModel.addCommentToPost(selectedPostForComments!!.id, text)
                coroutineScope.launch {
                    snackbarHostState.showSnackbar("মন্তব্য প্রকাশিত হয়েছে")
                }
            }
        )
    }

    if (selectedMemberForDetail != null) {
        MemberDetailDialog(
            user = selectedMemberForDetail!!,
            onDismiss = { viewModel.selectMemberForDetail(null) },
            onSendMessage = {
                viewModel.setActiveChatChannel("dm_${selectedMemberForDetail!!.id}")
                viewModel.setCurrentTab("messages")
            }
        )
    }

    if (showEditProfileDialog) {
        EditProfileDialog(
            user = currentUser,
            onDismiss = { showEditProfileDialog = false },
            onSave = { name, photo, address, profession, education, phone ->
                viewModel.updateUserProfile(name, photo, address, profession, education, phone)
                coroutineScope.launch {
                    snackbarHostState.showSnackbar("প্রোফাইল তথ্য সফলভাবে আপডেট হয়েছে")
                }
            }
        )
    }
}
