package com.example.ui.theme

import androidx.compose.ui.graphics.Color

// =========================================================================
// AL-KHEDMAH STUDENTS FOUNDATION Brand Palette
// 1. Rich Emerald Green: Behind AL-KHEDMAH STUDENTS FOUNDATION
// 2. Pure Black / Dark Interior: Deep luxury dark canvas and surfaces
// 3. Brilliant Golden Buttons: Radiant gold with black text on every button
// =========================================================================

// 1. Emerald Green Behind AL-KHEDMAH STUDENTS FOUNDATION
val EmeraldFoundationHeader = Color(0xFF046A38)      // Rich Islamic Emerald Green for Header
val EmeraldFoundationDark = Color(0xFF024423)        // Deep Forest Green
val EmeraldFoundationLight = Color(0xFF0A8749)       // Bright Emerald Green
val EmeraldActive = Color(0xFF059669)
val EmeraldLight = Color(0xFF133624)                 // Dark Emerald Tint
val EmeraldBorder = Color(0xFF10B981)

// 2. Golden Button Colors (Requested: "প্রতিটি বাটনের কালার গোল্ডেন করো")
val GoldenButtonPrimary = Color(0xFFF59E0B)          // Vibrant Rich Gold
val GoldenButtonDark = Color(0xFFD97706)             // Deep Metallic Amber Gold
val GoldenButtonLight = Color(0xFFFBBF24)            // Bright Golden
val GoldenButtonContent = Color(0xFF000000)          // Crisp Black text on Golden buttons for max legibility
val GoldenBadgeBg = Color(0xFF2D2205)                // Dark Gold Container
val GoldenBadgeBorder = Color(0xFFFCD34D)            // Metallic Gold Accent

// 3. Black App Interior (Requested: "এপের ভিতরে কালো কালার দাও")
val DarkAppBg = Color(0xFF090A0E)                    // Deep Black canvas
val DarkAppSurface = Color(0xFF14161F)               // Charcoal Black surface for cards
val DarkAppSurfaceElevated = Color(0xFF1B1E2B)       // Elevated card surface
val DarkAppBorder = Color(0xFF272A38)                // Dark subtle border
val DarkAppTextPrimary = Color(0xFFFFFFFF)           // Crisp white
val DarkAppTextSecondary = Color(0xFFE2E8F0)         // Soft white
val DarkAppTextMuted = Color(0xFF94A3B8)             // Muted slate gray

// Aliased Variables for seamless system compatibility
val BrandNavyPrimary = EmeraldFoundationHeader
val BrandNavyDark = EmeraldFoundationDark
val BrandNavyLight = DarkAppSurfaceElevated
val BrandOnPrimary = Color(0xFFFFFFFF)

val IndigoPrimary = EmeraldFoundationHeader
val IndigoPrimaryDark = EmeraldFoundationDark
val IndigoLight = DarkAppSurfaceElevated
val IndigoOnPrimary = Color(0xFFFFFFFF)

val AmberBadge = GoldenButtonPrimary
val AmberBadgeBg = GoldenBadgeBg
val AmberBadgeBorder = GoldenBadgeBorder

// Neutrals mapped to dark high-contrast palette
val Slate900 = DarkAppTextPrimary
val Slate800 = DarkAppTextSecondary
val Slate700 = Color(0xFFCBD5E1)
val Slate600 = DarkAppTextMuted
val Slate500 = Color(0xFF64748B)
val Slate400 = Color(0xFF475569)
val Slate200 = DarkAppBorder
val Slate100 = DarkAppSurfaceElevated
val Slate50 = DarkAppBg

// Urgent Notices / Alerts
val RoseAlert = Color(0xFFEF4444)                    // Vibrant Red Alert
val RoseLight = Color(0xFF3B1219)                    // Dark Rose Container

// Dark Theme Palette
val BrandNavyDarkTheme = GoldenButtonPrimary
val DarkBackground = DarkAppBg
val DarkSurface = DarkAppSurface
val DarkSurfaceVariant = DarkAppSurfaceElevated
