package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.data.model.PostEntity
import com.example.data.model.UserEntity
import com.example.ui.components.PostCard
import com.example.ui.components.RoleSwitcherBanner
import com.example.ui.theme.AmberBadgeBorder
import com.example.ui.theme.IndigoLight
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.RoseAlert
import com.example.ui.theme.Slate100
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate600
import com.example.ui.theme.Slate800

@Composable
fun FeedScreen(
    currentRole: String,
    currentUser: UserEntity,
    posts: List<PostEntity>,
    feedFilter: String,
    onRoleSelected: (String) -> Unit,
    onFilterSelected: (String) -> Unit,
    onCreatePost: (content: String, imageUrl: String, isNotice: Boolean) -> Unit,
    onLikePost: (PostEntity) -> Unit,
    onCommentPost: (PostEntity) -> Unit,
    onDeletePost: (PostEntity) -> Unit,
    onNavigateToLive: () -> Unit
) {
    val context = LocalContext.current
    var postText by remember { mutableStateOf("") }
    var imageUrlInput by remember { mutableStateOf("") }
    var showImageInput by remember { mutableStateOf(false) }
    var isNoticeSelected by remember { mutableStateOf(false) }

    val filteredPosts = remember(posts, feedFilter) {
        when (feedFilter) {
            "notices" -> posts.filter { it.isNotice }
            "members" -> posts.filter { it.authorIsMember }
            else -> posts
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("feed_screen"),
        contentPadding = PaddingValues(start = 14.dp, end = 14.dp, top = 10.dp, bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // 1. Role Switcher Testing Banner (from Prototype)
        item {
            RoleSwitcherBanner(
                currentRole = currentRole,
                onRoleSelected = onRoleSelected
            )
        }

        // 2. Create Post Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("create_post_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF14161F)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF282B38))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .border(2.dp, if (currentUser.isMember) AmberBadgeBorder else IndigoPrimary, CircleShape)
                        ) {
                            AsyncImage(
                                model = ImageRequest.Builder(context)
                                    .data(currentUser.photoUrl)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = "আপনার ছবি",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        TextField(
                            value = postText,
                            onValueChange = { postText = it },
                            placeholder = {
                                Text(
                                    text = "আপনার মনে কি আছে লিখুন...",
                                    fontSize = 13.5.sp,
                                    color = Color(0xFF94A3B8)
                                )
                            },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("post_input_field"),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color(0xFF1E212E),
                                unfocusedContainerColor = Color(0xFF1E212E),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                            shape = RoundedCornerShape(24.dp)
                        )
                    }

                    // Optional image attachment input
                    AnimatedVisibility(visible = showImageInput) {
                        Column(modifier = Modifier.padding(top = 10.dp)) {
                            OutlinedTextField(
                                value = imageUrlInput,
                                onValueChange = { imageUrlInput = it },
                                placeholder = { Text("ছবির লিঙ্ক (URL) দিন...", fontSize = 12.sp) },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp),
                                singleLine = true
                            )
                            // Quick preset suggestions
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 4.dp),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text("নমুনা:", fontSize = 11.sp, color = Slate500)
                                Text(
                                    text = "মিটিং ছবি",
                                    fontSize = 11.sp,
                                    color = IndigoPrimary,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.clickable {
                                        imageUrlInput = "https://images.unsplash.com/photo-1511578314322-379afb476865?w=800"
                                    }
                                )
                                Text(
                                    text = "উন্নয়ন ছবি",
                                    fontSize = 11.sp,
                                    color = IndigoPrimary,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.clickable {
                                        imageUrlInput = "https://images.unsplash.com/photo-1497366216548-37526070297c?w=800"
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider(color = Slate100, thickness = 1.dp)
                    Spacer(modifier = Modifier.height(8.dp))

                    // Action buttons row: লাইভ, ছবি, পোস্ট করুন
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Live Button
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { onNavigateToLive() }
                                .padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Videocam,
                                contentDescription = "লাইভ",
                                tint = RoseAlert,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "লাইভ",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Slate600
                            )
                        }

                        // Photo Button
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { showImageInput = !showImageInput }
                                .padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Image,
                                contentDescription = "ছবি",
                                tint = Color(0xFF10B981),
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "ছবি",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Slate600
                            )
                        }

                        // Post Button
                        Button(
                            onClick = {
                                if (postText.isNotBlank()) {
                                    onCreatePost(postText, imageUrlInput, isNoticeSelected)
                                    postText = ""
                                    imageUrlInput = ""
                                    showImageInput = false
                                }
                            },
                            enabled = postText.isNotBlank(),
                            shape = RoundedCornerShape(20.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFF59E0B),
                                contentColor = Color.Black,
                                disabledContainerColor = Color(0xFF2E2405),
                                disabledContentColor = Color(0xFF7A6836)
                            ),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                            modifier = Modifier.testTag("submit_post_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Send,
                                contentDescription = "পোস্ট",
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "পোস্ট করুন",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // 3. Filter Chips
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    FilterChip(
                        selected = feedFilter == "all",
                        onClick = { onFilterSelected("all") },
                        label = { Text("সব পোস্ট (${posts.size})", fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFFF59E0B),
                            selectedLabelColor = Color.Black,
                            containerColor = Color(0xFF14161F),
                            labelColor = Color(0xFFCBD5E1)
                        )
                    )
                }
                item {
                    FilterChip(
                        selected = feedFilter == "notices",
                        onClick = { onFilterSelected("notices") },
                        label = { Text("অফিসিয়াল নোটিশ", fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFFF59E0B),
                            selectedLabelColor = Color.Black,
                            containerColor = Color(0xFF14161F),
                            labelColor = Color(0xFFCBD5E1)
                        )
                    )
                }
                item {
                    FilterChip(
                        selected = feedFilter == "members",
                        onClick = { onFilterSelected("members") },
                        label = { Text("সদস্যদের পোস্ট", fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFFF59E0B),
                            selectedLabelColor = Color.Black,
                            containerColor = Color(0xFF14161F),
                            labelColor = Color(0xFFCBD5E1)
                        )
                    )
                }
            }
        }

        // 4. Posts List
        if (filteredPosts.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Campaign,
                            contentDescription = null,
                            tint = Slate400,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "কোন পোস্ট পাওয়া যায়নি",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Slate600
                        )
                    }
                }
            }
        } else {
            items(filteredPosts, key = { it.id }) { post ->
                PostCard(
                    post = post,
                    isAdmin = currentRole == "admin",
                    onLikeClick = { onLikePost(post) },
                    onCommentClick = { onCommentPost(post) },
                    onDeleteClick = { onDeletePost(post) }
                )
            }
        }
    }
}
