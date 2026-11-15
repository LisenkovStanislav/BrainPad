package com.brainpad.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.brainpad.app.R
import com.brainpad.app.ui.theme.*

@Composable
fun NoteNotFoundContent(
    noteId: String,
    onBackToAllNotesClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Ограничиваем длину ID, чтобы не растягивал экран
    val safeNoteId = if (noteId.length > 32) noteId.take(32) + "…" else noteId

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = AppSpacing.emptyScreenHorizontal),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier.size(AppSizes.illustrationBox),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(
                        width = AppSizes.illustrationBgWidth,
                        height = AppSizes.illustrationBgHeight
                    )
                    .clip(shape = AppShapes.illustrationBg)
                    .background(MaterialTheme.colorScheme.errorContainer.copy(alpha = AppTokens.alphaSpine))
            )

            Surface(
                modifier = Modifier.size(
                    width = AppSizes.notebookWidth,
                    height = AppSizes.notebookHeight
                ),
                shape = AppShapes.notebook,
                color = MaterialTheme.colorScheme.errorContainer,
                shadowElevation = AppSizes.elevationSmall
            ) {
                Row {
                    Box(
                        modifier = Modifier
                            .padding(start = AppSpacing.m)
                            .width(AppSizes.notebookSpineWidth)
                            .fillMaxHeight()
                            .alpha(AppTokens.alphaSpine)
                            .background(MaterialTheme.colorScheme.error)
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(
                                top = AppSpacing.notebookTopPadding,
                                start = AppSpacing.xxl,
                                end = AppSpacing.xl
                            ),
                        verticalArrangement = Arrangement.spacedBy(AppSpacing.s)
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(bottom = AppSpacing.xs)
                                .fillMaxWidth(AppTokens.fractionLineMedium)
                                .height(AppSizes.notebookLineThick)
                                .clip(AppShapes.lineMedium)
                                .background(MaterialTheme.colorScheme.error)
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(AppTokens.fractionLineWide)
                                .height(AppSizes.notebookLineThin)
                                .clip(AppShapes.lineSmall)
                                .alpha(AppTokens.alphaNotebookLine)
                                .background(MaterialTheme.colorScheme.error)
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(AppTokens.fractionLineShort)
                                .height(AppSizes.notebookLineThin)
                                .clip(AppShapes.lineSmall)
                                .alpha(AppTokens.alphaNotebookLine)
                                .background(MaterialTheme.colorScheme.error)
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(
                        x = AppSizes.audioBadgeOffsetX,
                        y = AppSizes.audioBadgeOffsetY
                    )
                    .size(AppSizes.audioBadgeSize)
                    .clip(AppShapes.audioBadge)
                    .background(MaterialTheme.colorScheme.error),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.WarningAmber,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onError,
                    modifier = Modifier.size(AppSizes.iconMedium)
                )
            }
        }

        Spacer(modifier = Modifier.height(AppSpacing.sectionSpacing))

        Text(
            text = stringResource(R.string.note_not_found_title),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(AppSpacing.xl))

        Text(
            text = stringResource(R.string.note_not_found_message, safeNoteId),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(AppSpacing.sectionSpacing))

        Button(
            onClick = onBackToAllNotesClick,
            modifier = Modifier.height(AppSizes.touchTargetMin),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer
            ),
            shape = AppShapes.pillButton
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = null,
                modifier = Modifier.size(AppSizes.iconSmall)
            )
            Spacer(modifier = Modifier.width(AppSpacing.m))
            Text(
                text = stringResource(R.string.back_to_all_notes),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}