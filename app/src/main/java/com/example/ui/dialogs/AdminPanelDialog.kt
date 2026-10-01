package com.example.ui.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.UserEntity
import com.example.ui.theme.AmberBadge
import com.example.ui.theme.AmberBadgeBg
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.Slate100
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate800

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AdminPanelDialog(
    users: List<UserEntity>,
    onDismiss: () -> Unit,
    onSaveApproval: (userId: Long, assignAsMember: Boolean, designation: String) -> Unit
) {
    var selectedUser by remember { mutableStateOf(users.firstOrNull()) }
    var userDropdownExpanded by remember { mutableStateOf(false) }

    var isMemberChoice by remember(selectedUser) {
        mutableStateOf(selectedUser?.isMember ?: true)
    }

    var designationInput by remember(selectedUser) {
        mutableStateOf(selectedUser?.designation ?: "")
    }

    val presetDesignations = listOf(
        "সভাপতি",
        "সহ-সভাপতি",
        "সাধারণ সম্পাদক",
        "যুগ্ম সাধারণ সম্পাদক",
        "সাংগঠনিক সম্পাদক",
        "কোষাধ্যক্ষ",
        "প্রচার সম্পাদক",
        "সমাজকল্যাণ সম্পাদক",
        "সম্মানিত কার্যকরী সদস্য"
    )

    val scrollState = rememberScrollState()

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("admin_panel_modal"),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF14161F)),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF282B38))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
                    .verticalScroll(scrollState)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AdminPanelSettings,
                            contentDescription = null,
                            tint = Color(0xFFF59E0B),
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "এডমিন প্যানেল (সদস্য ও পদবী)",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFCD34D)
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "বন্ধ করুন",
                            tint = Slate400
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "এডমিন এখান থেকে সাধারণ ব্যবহারকারীকে \"সদস্য\" করবেন এবং পদবী নির্দিষ্ট করে দেবেন।",
                    fontSize = 11.5.sp,
                    color = Slate500
                )
                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider(color = Slate100, thickness = 1.dp)
                Spacer(modifier = Modifier.height(12.dp))

                // 1. User Selection Dropdown
                Text(
                    text = "ব্যবহারকারী নির্বাচন করুন:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Slate800
                )
                Spacer(modifier = Modifier.height(4.dp))

                Box(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .border(1.dp, Slate200, RoundedCornerShape(10.dp))
                            .clickable { userDropdownExpanded = true }
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = selectedUser?.let { "${it.name} (${if (it.isMember) it.designation.ifBlank { "সদস্য" } else "সাধারণ মানুষ"})" }
                                ?: "ব্যবহারকারী নির্বাচন করুন",
                            fontSize = 13.sp,
                            color = Slate800,
                            fontWeight = FontWeight.Medium
                        )
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = null,
                            tint = Slate500
                        )
                    }

                    DropdownMenu(
                        expanded = userDropdownExpanded,
                        onDismissRequest = { userDropdownExpanded = false }
                    ) {
                        users.forEach { user ->
                            DropdownMenuItem(
                                text = {
                                    Column {
                                        Text(user.name, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                        Text(
                                            text = if (user.isMember) "পদবী: ${user.designation.ifBlank { "সদস্য" }}" else "সাধারণ মানুষ",
                                            fontSize = 11.sp,
                                            color = if (user.isMember) AmberBadge else Slate500
                                        )
                                    }
                                },
                                onClick = {
                                    selectedUser = user
                                    userDropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // 2. Approval Type Radio Selection
                Text(
                    text = "অ্যাপ্রুভাল টাইপ:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Slate800
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clickable { isMemberChoice = true }
                            .padding(vertical = 4.dp)
                    ) {
                        RadioButton(
                            selected = isMemberChoice,
                            onClick = { isMemberChoice = true },
                            colors = RadioButtonDefaults.colors(selectedColor = Color(0xFFB45309))
                        )
                        Text(
                            text = "সদস্য বানান (Member)",
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Slate800
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clickable { isMemberChoice = false }
                            .padding(vertical = 4.dp)
                    ) {
                        RadioButton(
                            selected = !isMemberChoice,
                            onClick = { isMemberChoice = false },
                            colors = RadioButtonDefaults.colors(selectedColor = Color(0xFFB45309))
                        )
                        Text(
                            text = "সাধারণ মানুষ (General)",
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Slate800
                        )
                    }
                }

                // 3. Designation Input & Presets
                if (isMemberChoice) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "পদবী (Designation):",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate800
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                    OutlinedTextField(
                        value = designationInput,
                        onValueChange = { designationInput = it },
                        placeholder = { Text("যেমন: সভাপতি / সহ-সভাপতি / সাধারণ সম্পাদক", fontSize = 12.sp) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("admin_designation_input"),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "পদবীর তালিকা থেকে পছন্দ করুন:",
                        fontSize = 11.sp,
                        color = Slate500
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        presetDesignations.forEach { desig ->
                            Surface(
                                shape = RoundedCornerShape(50),
                                color = if (designationInput == desig) AmberBadgeBg else Slate100,
                                border = if (designationInput == desig) androidx.compose.foundation.BorderStroke(1.dp, AmberBadge) else null,
                                modifier = Modifier.clickable { designationInput = desig }
                            ) {
                                Text(
                                    text = desig,
                                    fontSize = 10.5.sp,
                                    fontWeight = if (designationInput == desig) FontWeight.Bold else FontWeight.Normal,
                                    color = if (designationInput == desig) AmberBadge else Slate800,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Save Button
                Button(
                    onClick = {
                        selectedUser?.let { user ->
                            onSaveApproval(user.id, isMemberChoice, designationInput)
                            onDismiss()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("admin_save_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF59E0B),
                        contentColor = Color.Black
                    )
                ) {
                    Text(
                        text = "পদবী সেভ করুন",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}
