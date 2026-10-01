package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AmberBadge
import com.example.ui.theme.AmberBadgeBg
import com.example.ui.theme.AmberBadgeBorder
import com.example.ui.theme.Slate100
import com.example.ui.theme.Slate600

@Composable
fun DesignationBadge(
    designation: String,
    isMember: Boolean,
    modifier: Modifier = Modifier,
    large: Boolean = false
) {
    if (isMember && designation.isNotBlank()) {
        Row(
            modifier = modifier
                .clip(RoundedCornerShape(50))
                .background(AmberBadgeBg)
                .border(1.dp, AmberBadgeBorder, RoundedCornerShape(50))
                .padding(horizontal = if (large) 10.dp else 7.dp, vertical = if (large) 4.dp else 2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (large) Icons.Default.Shield else Icons.Default.MilitaryTech,
                contentDescription = "সদস্য পদবী",
                tint = AmberBadge,
                modifier = Modifier.size(if (large) 14.dp else 11.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = if (large) "পদবী: $designation" else designation,
                color = AmberBadge,
                fontSize = if (large) 12.sp else 10.sp,
                fontWeight = FontWeight.Bold
            )
        }
    } else {
        Row(
            modifier = modifier
                .clip(RoundedCornerShape(50))
                .background(Slate100)
                .padding(horizontal = if (large) 10.dp else 7.dp, vertical = if (large) 4.dp else 2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (large) "সাধারণ ব্যবহারকারী (কোন পদবী নেই)" else "সাধারণ ব্যবহারকারী",
                color = Slate600,
                fontSize = if (large) 12.sp else 10.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
