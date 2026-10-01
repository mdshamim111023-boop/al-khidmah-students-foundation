package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val phone: String,
    val password: String = "123456",
    val photoUrl: String,
    val address: String = "ঢাকা, বাংলাদেশ",
    val profession: String = "পেশাজীবী",
    val education: String = "স্নাতক",
    val isMember: Boolean = false,
    val designation: String = "", // e.g. "সভাপতি", "সাধারণ সম্পাদক", "কার্যকরী সদস্য"
    val role: String = "general", // "admin", "member", "general"
    val bloodGroup: String = "A+",
    val joinedDate: String = "২০২৫"
)

@Entity(tableName = "posts")
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val authorId: Long = 0,
    val authorName: String,
    val authorPhoto: String,
    val authorDesignation: String = "",
    val authorIsMember: Boolean = false,
    val content: String,
    val imageUrl: String = "",
    val timeAgo: String = "এইমাত্র",
    val timestamp: Long = System.currentTimeMillis(),
    val likesCount: Int = 0,
    val commentsCount: Int = 0,
    val isLiked: Boolean = false,
    val isNotice: Boolean = false
)

@Entity(tableName = "comments")
data class CommentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val postId: Long,
    val authorName: String,
    val authorPhoto: String,
    val authorDesignation: String = "",
    val text: String,
    val timestamp: Long = System.currentTimeMillis(),
    val timeAgo: String = "এইমাত্র"
)

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val message: String,
    val timeAgo: String,
    val isRead: Boolean = false,
    val type: String = "general", // "meeting", "notice", "admin"
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "public_feedback")
data class PublicFeedbackEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val authorName: String,
    val message: String,
    val timeAgo: String = "এইমাত্র",
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val channelId: String = "general", // "general", "committee", or specific userId
    val senderName: String,
    val senderPhoto: String,
    val senderDesignation: String = "",
    val text: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isFromCurrentUser: Boolean = false
)
