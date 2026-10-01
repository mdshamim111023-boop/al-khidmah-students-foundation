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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.PanTool
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.VideocamOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.RoseAlert
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate800

data class VideoItem(
    val id: String,
    val title: String,
    val speaker: String,
    val duration: String,
    val views: String,
    val thumbnailUrl: String
)

@Composable
fun VideoLiveScreen(
    isLiveMeetingJoined: Boolean,
    onJoinLiveMeeting: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isMicMuted by remember { mutableStateOf(false) }
    var isVideoOff by remember { mutableStateOf(false) }
    var isHandRaised by remember { mutableStateOf(false) }
    var selectedVideoForPlayback by remember { mutableStateOf<VideoItem?>(null) }
    var isPlayingVideo by remember { mutableStateOf(false) }

    val videoList = listOf(
        VideoItem(
            id = "1",
            title = "সমিতির উন্নয়নমূলক কাজের ভিডিও",
            speaker = "মোহাম্মদ রফিকুল ইসলাম (সভাপতি)",
            duration = "১২:৪৫",
            views = "১,২৪০ ভিউ",
            thumbnailUrl = "https://images.unsplash.com/photo-1516321318423-f06f85e504b3?w=800"
        ),
        VideoItem(
            id = "2",
            title = "বার্ষিক সাধারণ সভা ২০২৬ ও বাজেট অনুমোদন",
            speaker = "কমিটির নেতৃবৃন্দ",
            duration = "৪৫:২০",
            views = "৩,৫০০ ভিউ",
            thumbnailUrl = "https://images.unsplash.com/photo-1511578314322-379afb476865?w=800"
        ),
        VideoItem(
            id = "3",
            title = "বিনামূল্যে চিকিৎসা ও রক্তদান ক্যাম্পের কার্যক্রম",
            speaker = "স্বাস্থ্য ও সেবা উপকমিটি",
            duration = "০৮:১৫",
            views = "৯৮০ ভিউ",
            thumbnailUrl = "https://images.unsplash.com/photo-1584515979956-d9f6e5d09982?w=800"
        )
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("video_live_screen"),
        contentPadding = PaddingValues(start = 14.dp, end = 14.dp, top = 10.dp, bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // 1. Live Meeting Banner / Active Room
        item {
            if (!isLiveMeetingJoined) {
                // Gradient Live Meeting Card (from Prototype)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("live_meeting_banner"),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.horizontalGradient(
                                    listOf(Color(0xFFDC2626), Color(0xFFBE123C))
                                )
                            )
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(50))
                                        .background(Color.White.copy(alpha = 0.25f))
                                        .padding(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.RadioButtonChecked,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(10.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "সভার লাইভ",
                                        color = Color.White,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(
                                    text = "জরুরি আলোচনা সভা",
                                    color = Color.White,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(modifier = Modifier.height(2.dp))

                                Text(
                                    text = "অনলাইনে আছেন: ৮৫ জন",
                                    color = Color(0xFFFFE4E6),
                                    fontSize = 12.sp
                                )
                            }

                            Button(
                                onClick = { onJoinLiveMeeting(true) },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFF59E0B),
                                    contentColor = Color.Black
                                ),
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                                modifier = Modifier.testTag("join_live_button")
                            ) {
                                Text(
                                    text = "যুক্ত হন",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.5.sp
                                )
                            }
                        }
                    }
                }
            } else {
                // Interactive Meeting Room
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("live_meeting_room"),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF072338))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .background(RoseAlert, CircleShape)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "লাইভ সভা চলছে (৮৬ জন সংযুক্ত)",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = Color.White.copy(alpha = 0.15f)
                            ) {
                                Text(
                                    text = "১৪:২২",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Speaker video tile
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.Black)
                        ) {
                            AsyncImage(
                                model = ImageRequest.Builder(context)
                                    .data("https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=800")
                                    .crossfade(true)
                                    .build(),
                                contentDescription = "স্পিকার",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )

                            // Overlay Speaker Name
                            Surface(
                                color = Color.Black.copy(alpha = 0.65f),
                                shape = RoundedCornerShape(6.dp),
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .padding(8.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Mic,
                                        contentDescription = null,
                                        tint = Color(0xFF10B981),
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "মোহাম্মদ রফিকুল ইসলাম (বক্তব্য দিচ্ছেন)",
                                        color = Color.White,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Control Buttons Row: Mic, Camera, Raise Hand, Leave
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            FilledIconButton(
                                onClick = { isMicMuted = !isMicMuted },
                                colors = IconButtonDefaults.filledIconButtonColors(
                                    containerColor = if (isMicMuted) RoseAlert else Color.White.copy(alpha = 0.2f),
                                    contentColor = Color.White
                                )
                            ) {
                                Icon(
                                    imageVector = if (isMicMuted) Icons.Default.MicOff else Icons.Default.Mic,
                                    contentDescription = "মাইক"
                                )
                            }

                            FilledIconButton(
                                onClick = { isVideoOff = !isVideoOff },
                                colors = IconButtonDefaults.filledIconButtonColors(
                                    containerColor = if (isVideoOff) RoseAlert else Color.White.copy(alpha = 0.2f),
                                    contentColor = Color.White
                                )
                            ) {
                                Icon(
                                    imageVector = if (isVideoOff) Icons.Default.VideocamOff else Icons.Default.Videocam,
                                    contentDescription = "ক্যামেরা"
                                )
                            }

                            FilledIconButton(
                                onClick = { isHandRaised = !isHandRaised },
                                colors = IconButtonDefaults.filledIconButtonColors(
                                    containerColor = if (isHandRaised) Color(0xFFFBBF24) else Color.White.copy(alpha = 0.2f),
                                    contentColor = if (isHandRaised) Color.Black else Color.White
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PanTool,
                                    contentDescription = "হাত তুলুন"
                                )
                            }

                            FilledIconButton(
                                onClick = { onJoinLiveMeeting(false) },
                                colors = IconButtonDefaults.filledIconButtonColors(
                                    containerColor = RoseAlert,
                                    contentColor = Color.White
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CallEnd,
                                    contentDescription = "সভা ত্যাগ করুন"
                                )
                            }
                        }
                    }
                }
            }
        }

        // Section Title: ভিডিও আর্কাইভ
        item {
            Text(
                text = "সমিতির ভিডিও সমূহ",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Slate800,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        // Video Player Modal Card (if a video is selected)
        if (selectedVideoForPlayback != null) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Black)
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                        ) {
                            AsyncImage(
                                model = ImageRequest.Builder(context)
                                    .data(selectedVideoForPlayback!!.thumbnailUrl)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )

                            // Play/Pause Overlay
                            Box(
                                modifier = Modifier
                                    .size(54.dp)
                                    .clip(CircleShape)
                                    .background(RoseAlert.copy(alpha = 0.9f))
                                    .align(Alignment.Center)
                                    .clickable { isPlayingVideo = !isPlayingVideo },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (isPlayingVideo) Icons.Default.Videocam else Icons.Default.PlayArrow,
                                    contentDescription = "প্লে",
                                    tint = Color.White,
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }

                        if (isPlayingVideo) {
                            LinearProgressIndicator(
                                modifier = Modifier.fillMaxWidth(),
                                color = RoseAlert,
                                trackColor = Color.DarkGray
                            )
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = selectedVideoForPlayback!!.title,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.5.sp
                                )
                                Text(
                                    text = "${selectedVideoForPlayback!!.speaker} • ${if (isPlayingVideo) "প্লে হচ্ছে..." else "পজ করা আছে"}",
                                    color = Color.LightGray,
                                    fontSize = 11.sp
                                )
                            }

                            Button(
                                onClick = {
                                    selectedVideoForPlayback = null
                                    isPlayingVideo = false
                                },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.White.copy(alpha = 0.2f),
                                    contentColor = Color.White
                                ),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text("বন্ধ করুন", fontSize = 11.sp)
                            }
                        }
                    }
                }
            }
        }

        // 3. Recorded Videos List (from Prototype)
        items(videoList, key = { it.id }) { video ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        selectedVideoForPlayback = video
                        isPlayingVideo = true
                    }
                    .testTag("video_card_${video.id}"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, Slate200)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .background(Color.Black)
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(context)
                                .data(video.thumbnailUrl)
                                .crossfade(true)
                                .build(),
                            contentDescription = video.title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                        )

                        // Play Button
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(RoseAlert.copy(alpha = 0.9f))
                                .align(Alignment.Center),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "ভিডিও দেখুন",
                                tint = Color.White,
                                modifier = Modifier.size(28.dp)
                            )
                        }

                        // Duration Badge
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = Color.Black.copy(alpha = 0.75f),
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(8.dp)
                        ) {
                            Text(
                                text = video.duration,
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                    ) {
                        Text(
                            text = video.title,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Slate800
                        )

                        Spacer(modifier = Modifier.height(2.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = video.speaker,
                                fontSize = 11.5.sp,
                                color = Slate500
                            )
                            Text(
                                text = video.views,
                                fontSize = 11.sp,
                                color = IndigoPrimary,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }
}
