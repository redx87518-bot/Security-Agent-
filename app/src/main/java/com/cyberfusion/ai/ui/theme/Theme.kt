package com.cyberfusion.ai.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf

data class CyberFusionTheme(
    val icons: com.cyberfusion.ai.ui.component.CyberFusionIcons
)

val LocalCyberFusionTheme = staticCompositionLocalOf {
    CyberFusionTheme(
        icons = com.cyberfusion.ai.ui.component.CyberFusionIcons.Default
    )
}
