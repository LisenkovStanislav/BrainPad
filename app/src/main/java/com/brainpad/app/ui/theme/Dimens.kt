package com.brainpad.app.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp

/**
 * Централизованные дизайн-токены отступов (Spacing)
 */
object AppSpacing {
    val xs = 4.dp
    val s = 6.dp
    val m = 8.dp
    val l = 10.dp
    val xl = 12.dp
    val xxl = 14.dp
    val screenPadding = 16.dp
    val cardPadding = 20.dp
    val sectionSpacing = 24.dp
    val emptyScreenHorizontal = 32.dp
    val notebookTopPadding = 18.dp
}

/**
 * Централизованные размеры элементов (Sizes)
 */
object AppSizes {
    val borderThin = 1.dp
    val strokeMedium = 4.dp

    // Кнопки и Touch Targets (минимум 48dp для доступности)
    val touchTargetMin = 48.dp
    val fabPrimarySize = 56.dp
    val fabSmallSize = 44.dp
    val iconSmall = 18.dp
    val iconMedium = 20.dp
    val iconLarge = 26.dp

    // Пустой экран (иллюстрация блокнота)
    val illustrationBox = 130.dp
    val illustrationBgWidth = 140.dp
    val illustrationBgHeight = 132.dp
    val notebookWidth = 80.dp
    val notebookHeight = 104.dp
    val notebookSpineWidth = 3.dp
    val notebookLineThick = 5.dp
    val notebookLineThin = 3.dp
    val audioBadgeSize = 38.dp
    val audioBadgeOffsetX = (-4).dp
    val audioBadgeOffsetY = (-8).dp
    val elevationSmall = 2.dp

    // Экран загрузки (Loading & Skeletons)
    val loadingDialogWidth = 208.dp
    val loadingDialogHeight = 136.dp
    val progressIndicatorSize = 40.dp
    val skeletonTitleHeight = 16.dp
    val skeletonBadgeHeight = 18.dp
    val skeletonLineHeight = 10.dp

    // Экран ошибки и редактирования
    val errorIconCircleSize = 52.dp
    val editorMinHeight = 250.dp
}

/**
 * Централизованные формы и скругления (Shapes)
 */
object AppShapes {
    val lineSmall = RoundedCornerShape(2.dp)
    val lineMedium = RoundedCornerShape(3.dp)
    val skeletonLine = RoundedCornerShape(4.dp)
    val dateBadge = RoundedCornerShape(6.dp)
    val fabSmall = RoundedCornerShape(12.dp)
    val notebook = RoundedCornerShape(12.dp)
    val audioBadge = RoundedCornerShape(14.dp)
    val card = RoundedCornerShape(16.dp)
    val fabPrimary = RoundedCornerShape(16.dp)
    val errorCard = RoundedCornerShape(20.dp)
    val loadingDialog = RoundedCornerShape(28.dp)
    val illustrationBg = RoundedCornerShape(56.dp)
    val pillButton = RoundedCornerShape(100.dp)
}

/**
 * Пропорции и прозрачности
 */
object AppTokens {
    const val alphaSpine = 0.35f
    const val alphaNotebookLine = 0.45f
    const val alphaErrorText = 0.9f

    const val fractionTitle = 0.6f
    const val fractionBadge = 0.35f
    const val fractionLineFull = 0.95f
    const val fractionLineWide = 0.9f
    const val fractionLineMedium = 0.7f
    const val fractionLineShort = 0.6f
    const val fractionLineMini = 0.4f
}