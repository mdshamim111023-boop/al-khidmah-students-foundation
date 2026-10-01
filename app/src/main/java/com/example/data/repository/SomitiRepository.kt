package com.example.data.repository

import com.example.data.local.SomitiDao
import com.example.data.model.CommentEntity
import com.example.data.model.MessageEntity
import com.example.data.model.NotificationEntity
import com.example.data.model.PostEntity
import com.example.data.model.PublicFeedbackEntity
import com.example.data.model.UserEntity
import kotlinx.coroutines.flow.Flow

class SomitiRepository(private val dao: SomitiDao) {

    // Users
    val allUsers: Flow<List<UserEntity>> = dao.getAllUsers()
    val members: Flow<List<UserEntity>> = dao.getMembers()

    fun getUserById(id: Long): Flow<UserEntity?> = dao.getUserById(id)

    suspend fun insertUser(user: UserEntity): Long = dao.insertUser(user)

    suspend fun updateUser(user: UserEntity) = dao.updateUser(user)

    suspend fun updateUserRoleAndDesignation(id: Long, role: String, isMember: Boolean, designation: String) {
        dao.updateUserRoleAndDesignation(id, role, isMember, designation)
    }

    suspend fun deleteUser(user: UserEntity) = dao.deleteUser(user)

    suspend fun ensureDatabaseSeeded() {
        if (dao.getUserCount() == 0) {
            com.example.data.local.AppDatabase.seedDatabase(dao)
        }
    }

    // Posts
    val allPosts: Flow<List<PostEntity>> = dao.getAllPosts()

    suspend fun insertPost(post: PostEntity): Long = dao.insertPost(post)

    suspend fun deletePost(post: PostEntity) = dao.deletePost(post)

    suspend fun togglePostLike(post: PostEntity) {
        val newIsLiked = !post.isLiked
        val newCount = if (newIsLiked) post.likesCount + 1 else (post.likesCount - 1).coerceAtLeast(0)
        dao.updatePostLike(post.id, newIsLiked, newCount)
    }

    // Comments
    fun getCommentsForPost(postId: Long): Flow<List<CommentEntity>> = dao.getCommentsForPost(postId)

    suspend fun addComment(comment: CommentEntity) {
        dao.insertComment(comment)
        dao.incrementCommentCount(comment.postId)
    }

    // Notifications
    val allNotifications: Flow<List<NotificationEntity>> = dao.getAllNotifications()
    val unreadNotifCount: Flow<Int> = dao.getUnreadNotificationCount()

    suspend fun markAllNotificationsAsRead() = dao.markAllNotificationsAsRead()

    // Public Feedback
    val allFeedback: Flow<List<PublicFeedbackEntity>> = dao.getAllFeedback()

    suspend fun addFeedback(feedback: PublicFeedbackEntity) = dao.insertFeedback(feedback)

    // Messages
    fun getMessagesForChannel(channelId: String): Flow<List<MessageEntity>> = dao.getMessagesForChannel(channelId)

    suspend fun sendMessage(message: MessageEntity) = dao.insertMessage(message)
}
