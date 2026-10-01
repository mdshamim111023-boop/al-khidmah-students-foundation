package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.CommentEntity
import com.example.data.model.MessageEntity
import com.example.data.model.NotificationEntity
import com.example.data.model.PostEntity
import com.example.data.model.PublicFeedbackEntity
import com.example.data.model.UserEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        UserEntity::class,
        PostEntity::class,
        CommentEntity::class,
        NotificationEntity::class,
        PublicFeedbackEntity::class,
        MessageEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun somitiDao(): SomitiDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "amader_somiti.db"
                )
                    .addCallback(DatabaseCallback(context))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(private val context: Context) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                CoroutineScope(Dispatchers.IO).launch {
                    val dao = getInstance(context).somitiDao()
                    seedDatabase(dao)
                }
            }
        }

        suspend fun seedDatabase(dao: SomitiDao) {
            // Seed Users
            val users = listOf(
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
                ),
                UserEntity(
                    id = 2,
                    name = "মোহাম্মদ রফিকুল ইসলাম",
                    phone = "০১৭১১-০০০০০১",
                    photoUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=200",
                    address = "উত্তরা, ঢাকা, বাংলাদেশ",
                    profession = "ব্যবসায়ী ও সমাজসেবক",
                    education = "এম.কম (ব্যবস্থাপনা)",
                    isMember = true,
                    designation = "সভাপতি",
                    role = "member",
                    bloodGroup = "A+",
                    joinedDate = "২০২০"
                ),
                UserEntity(
                    id = 3,
                    name = "প্রকৌশলী কামরুল হাসান",
                    phone = "০১৭২২-০০০০০২",
                    photoUrl = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=200",
                    address = "ধানমন্ডি, ঢাকা",
                    profession = "সিভিল ইঞ্জিনিয়ার",
                    education = "বি.এসসি ইঞ্জিনিয়ারিং",
                    isMember = true,
                    designation = "সাধারণ সম্পাদক",
                    role = "member",
                    bloodGroup = "O+",
                    joinedDate = "২০২১"
                ),
                UserEntity(
                    id = 4,
                    name = "মোসাঃ সেলিনা আক্তার",
                    phone = "০১৭৩৩-০০০০০৩",
                    photoUrl = "https://images.unsplash.com/photo-1573496359142-b8d87734a5a2?w=200",
                    address = "বনানী, ঢাকা",
                    profession = "কলেজ শিক্ষক",
                    education = "এম.এ (বাংলা)",
                    isMember = true,
                    designation = "সহ-সভাপতি",
                    role = "member",
                    bloodGroup = "AB+",
                    joinedDate = "২০২১"
                ),
                UserEntity(
                    id = 5,
                    name = "আহমেদ জুবায়ের",
                    phone = "০১৭৪৪-০০০০০৪",
                    photoUrl = "https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?w=200",
                    address = "মোহাম্মদপুর, ঢাকা",
                    profession = "চার্টার্ড অ্যাকাউন্ট্যান্ট",
                    education = "এফসিএ, এমবিএ",
                    isMember = true,
                    designation = "কোষাধ্যক্ষ",
                    role = "member",
                    bloodGroup = "O-",
                    joinedDate = "২০২২"
                ),
                UserEntity(
                    id = 6,
                    name = "মোসাম্মাৎ ফারহানা",
                    phone = "০১৭৫৫-০০০০০৫",
                    photoUrl = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=200",
                    address = "গুলশান, ঢাকা",
                    profession = "ব্যাংকার",
                    education = "বিবিএ, এমবিএ",
                    isMember = false,
                    designation = "",
                    role = "general",
                    bloodGroup = "A-",
                    joinedDate = "২০২৬"
                ),
                UserEntity(
                    id = 7,
                    name = "আব্দুর রহমান",
                    phone = "০১৭৬৬-০০০০০৬",
                    photoUrl = "https://images.unsplash.com/photo-1519085360753-af0119f7cbe7?w=200",
                    address = "যাত্রাবাড়ী, ঢাকা",
                    profession = "উদ্যোক্তা",
                    education = "স্নাতক",
                    isMember = false,
                    designation = "",
                    role = "general",
                    bloodGroup = "B-",
                    joinedDate = "২০২৬"
                )
            )
            dao.insertUsers(users)

            // Seed Posts
            val now = System.currentTimeMillis()
            val posts = listOf(
                PostEntity(
                    id = 1,
                    authorId = 2,
                    authorName = "মোহাম্মদ রফিকুল ইসলাম",
                    authorPhoto = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=200",
                    authorDesignation = "সভাপতি (সদস্য)",
                    authorIsMember = true,
                    content = "আজ AL-KHEDMAH STUDENTS FOUNDATION-এর বার্ষিক বাজেট, শিক্ষা বৃত্তি ও ছাত্রকল্যাণ বিষয়ক বিশেষ সাধারণ সভা অত্যন্ত সৌহার্দ্যপূর্ণ পরিবেশে সম্পন্ন হয়েছে। সকল সম্মানিত সদস্য ও শুভানুধ্যায়ীদের প্রতি আন্তরিক কৃতজ্ঞতা।",
                    imageUrl = "https://images.unsplash.com/photo-1511578314322-379afb476865?w=800",
                    timeAgo = "১০ মিনিট আগে",
                    timestamp = now - 600000,
                    likesCount = 24,
                    commentsCount = 8,
                    isLiked = true,
                    isNotice = true
                ),
                PostEntity(
                    id = 2,
                    authorId = 6,
                    authorName = "মোসাম্মাৎ ফারহানা",
                    authorPhoto = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=200",
                    authorDesignation = "",
                    authorIsMember = false,
                    content = "সবাই কেমন আছেন? আগামী শুক্রবারের স্বেচ্ছায় রক্তদান ও ক্যারিয়ার গাইডেন্স কর্মসূচিতে যোগ দিতে আগ্রহীরা কমেন্টে জানাবেন। AL-KHEDMAH STUDENTS FOUNDATION-এর এই উদ্যোগ সত্যিই প্রশংসনীয়!",
                    imageUrl = "https://images.unsplash.com/photo-1615461066841-6116e61058f4?w=800",
                    timeAgo = "১ ঘণ্টা আগে",
                    timestamp = now - 3600000,
                    likesCount = 12,
                    commentsCount = 3,
                    isLiked = false,
                    isNotice = false
                ),
                PostEntity(
                    id = 3,
                    authorId = 3,
                    authorName = "প্রকৌশলী কামরুল হাসান",
                    authorPhoto = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=200",
                    authorDesignation = "সাধারণ সম্পাদক (সদস্য)",
                    authorIsMember = true,
                    content = "জরুরি ঘোষণা: সমিতির নতুন পাঠাগার ও সমাজকল্যাণ ফান্ডের জন্য আর্থিক অনুদান সংগ্রহ শুরু হয়েছে। সদস্যগণ সরাসরি কোষাধ্যক্ষ মহোদয়ের নিকট অথবা অনলাইন ব্যাংকিংয়ে জমা দিতে পারেন।",
                    imageUrl = "",
                    timeAgo = "৩ ঘণ্টা আগে",
                    timestamp = now - 10800000,
                    likesCount = 35,
                    commentsCount = 12,
                    isLiked = false,
                    isNotice = true
                )
            )
            dao.insertPosts(posts)

            // Seed Comments
            val comments = listOf(
                CommentEntity(
                    postId = 1,
                    authorName = "প্রকৌশলী কামরুল হাসান",
                    authorPhoto = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=200",
                    authorDesignation = "সাধারণ সম্পাদক",
                    text = "খুবই সুন্দর আলোচনা হয়েছে। আগামী মাসের কার্যপরিকল্পনা অবিলম্বে বাস্তবায়ন করা হবে।",
                    timeAgo = "৫ মিনিট আগে",
                    timestamp = now - 300000
                ),
                CommentEntity(
                    postId = 1,
                    authorName = "মোঃ শামীম",
                    authorPhoto = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=200",
                    authorDesignation = "সম্মানিত কার্যকরী সদস্য",
                    text = "ডিজিটাল সমিতি ব্যবস্থাপনা অ্যাপ চালুর বিষয়টি সকল সদস্য সাধুবাদ জানিয়েছেন।",
                    timeAgo = "২ মিনিট আগে",
                    timestamp = now - 120000
                ),
                CommentEntity(
                    postId = 2,
                    authorName = "আহমেদ জুবায়ের",
                    authorPhoto = "https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?w=200",
                    authorDesignation = "কোষাধ্যক্ষ",
                    text = "আমি রক্তদানে অংশগ্রহণ করব ইনশাআল্লাহ। রক্ত গ্রুপ O+।",
                    timeAgo = "৩০ মিনিট আগে",
                    timestamp = now - 1800000
                )
            )
            dao.insertComments(comments)

            // Seed Notifications
            val notifs = listOf(
                NotificationEntity(
                    id = 1,
                    title = "জরুরি আলোচনা সভা",
                    message = "আজ রাত ৮:৩০ টায় ভার্চুয়াল আলোচনা সভা অনুষ্ঠিত হবে। সকল সদস্যকে উপস্থিত থাকতে অনুরোধ করা হচ্ছে।",
                    timeAgo = "১০ মিনিট আগে",
                    isRead = false,
                    type = "meeting"
                ),
                NotificationEntity(
                    id = 2,
                    title = "বার্ষিক সাধারণ সভা ২০২৬",
                    message = "বার্ষিক মিটিংয়ের রেজুলেশন ও বাজেট বিবরণী সমিতি অফিসে পাওয়া যাবে।",
                    timeAgo = "২ ঘণ্টা আগে",
                    isRead = false,
                    type = "notice"
                ),
                NotificationEntity(
                    id = 3,
                    title = "নতুন সদস্য আবেদন",
                    message = "আব্দুর রহমান সদস্যপদের জন্য আবেদন করেছেন। এডমিন অনুমোদন প্রয়োজন।",
                    timeAgo = "৫ ঘণ্টা আগে",
                    isRead = true,
                    type = "admin"
                ),
                NotificationEntity(
                    id = 4,
                    title = "রক্তদান কর্মসূচি",
                    message = "আগামী শুক্রবার সকাল ৯টায় বিনামূল্যে রক্তের গ্রুপ পরীক্ষা ও রক্তদান ক্যাম্প।",
                    timeAgo = "১ দিন আগে",
                    isRead = true,
                    type = "notice"
                ),
                NotificationEntity(
                    id = 5,
                    title = "মাসিক কল্যাণ চাঁদা",
                    message = "চলতি মাসের সদস্য চাঁদা পরিশোধের অনুরোধ জানানো হচ্ছে।",
                    timeAgo = "২ দিন আগে",
                    isRead = true,
                    type = "notice"
                )
            )
            dao.insertNotifications(notifs)

            // Seed Public Feedback
            val feedbacks = listOf(
                PublicFeedbackEntity(
                    id = 1,
                    authorName = "মোঃ করিম মিয়া",
                    message = "খুবই চমৎকার উদ্যোগ! সমিতি অ্যাপের মাধ্যমে সকল আপডেট সহজে পাওয়া যাচ্ছে।",
                    timeAgo = "১৫ মিনিট আগে"
                ),
                PublicFeedbackEntity(
                    id = 2,
                    authorName = "তাহমিনা বেগম",
                    message = "আমাদের এলাকায় একটি নারী কল্যাণ প্রশিক্ষণ কর্মশালা আয়োজনের অনুরোধ রইল।",
                    timeAgo = "১ ঘণ্টা আগে"
                ),
                PublicFeedbackEntity(
                    id = 3,
                    authorName = "সোহেল রানা",
                    message = "কমিটির নেতৃবৃন্দকে আন্তরিক ধন্যবাদ সুষ্ঠুভাবে কার্যক্রম পরিচালনার জন্য।",
                    timeAgo = "৩ ঘণ্টা আগে"
                )
            )
            dao.insertFeedbacks(feedbacks)

            // Seed Messages
            val messages = listOf(
                MessageEntity(
                    id = 1,
                    channelId = "general",
                    senderName = "মোহাম্মদ রফিকুল ইসলাম",
                    senderPhoto = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=200",
                    senderDesignation = "সভাপতি",
                    text = "আসসালামু আলাইকুম। সবাই কেমন আছেন? আজকের মিটিংয়ে সবাই যথাসময়ে যুক্ত হবেন।",
                    timestamp = now - 7200000,
                    isFromCurrentUser = false
                ),
                MessageEntity(
                    id = 2,
                    channelId = "general",
                    senderName = "মোঃ শামীম",
                    senderPhoto = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=200",
                    senderDesignation = "কার্যকরী সদস্য",
                    text = "ওয়ালাইকুম আসসালাম। আলহামদুলিল্লাহ, আমি যুক্ত থাকব।",
                    timestamp = now - 5400000,
                    isFromCurrentUser = true
                ),
                MessageEntity(
                    id = 3,
                    channelId = "general",
                    senderName = "মোসাঃ সেলিনা আক্তার",
                    senderPhoto = "https://images.unsplash.com/photo-1573496359142-b8d87734a5a2?w=200",
                    senderDesignation = "সহ-সভাপতি",
                    text = "মিটিংয়ের এজেন্ডা পয়েন্টগুলো গ্রুপের নোটিশ বোর্ডে আপলোড করা হয়েছে।",
                    timestamp = now - 1800000,
                    isFromCurrentUser = false
                )
            )
            dao.insertMessages(messages)
        }
    }
}
