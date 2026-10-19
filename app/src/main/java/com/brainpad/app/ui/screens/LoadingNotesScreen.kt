package com.brainpad.app.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.brainpad.app.R
import com.brainpad.app.ui.theme.*

@Composable
fun LoadingNotesView(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = AppSpacing.screenPadding, vertical = AppSpacing.xl),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.xl)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.your_notes),
                    style = MaterialTheme.typography.headlineSmall,
                )
                IconButton(onClick = { }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Sort,
                        contentDescription = stringResource(R.string.cd_sort),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            repeat(3) {
                SkeletonCard()
            }
        }

        Surface(
            shape = AppShapes.loadingDialog,
            color = MaterialTheme.colorScheme.surfaceContainer,
            modifier = Modifier
                .size(width = AppSizes.loadingDialogWidth, height = AppSizes.loadingDialogHeight)
                .align(Alignment.Center)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.secondaryContainer,
                    strokeWidth = AppSizes.strokeMedium,
                    modifier = Modifier.size(AppSizes.progressIndicatorSize)
                )

                Spacer(modifier = Modifier.height(AppSpacing.screenPadding))

                Text(
                    text = stringResource(R.string.loading_notes),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun SkeletonCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = AppShapes.card,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        ),
        border = BorderStroke(
            width = AppSizes.borderThin,
            color = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppSpacing.screenPadding),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.l)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(AppTokens.fractionTitle)
                        .height(AppSizes.skeletonTitleHeight)
                        .clip(AppShapes.skeletonLine)
                        .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth(AppTokens.fractionBadge)
                        .height(AppSizes.skeletonBadgeHeight)
                        .clip(AppShapes.skeletonLine)
                        .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                )
            }

            Spacer(modifier = Modifier.height(AppSpacing.m))

            Box(
                modifier = Modifier
                    .fillMaxWidth(AppTokens.fractionLineFull)
                    .height(AppSizes.skeletonLineHeight)
                    .clip(AppShapes.skeletonLine)
                    .background(MaterialTheme.colorScheme.surfaceContainerHigh)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth(AppTokens.fractionLineMedium)
                    .height(AppSizes.skeletonLineHeight)
                    .clip(AppShapes.skeletonLine)
                    .background(MaterialTheme.colorScheme.surfaceContainerHigh)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth(AppTokens.fractionLineMini)
                    .height(AppSizes.skeletonLineHeight)
                    .clip(AppShapes.skeletonLine)
                    .background(MaterialTheme.colorScheme.surfaceContainerHigh)
            )
        }
    }
}