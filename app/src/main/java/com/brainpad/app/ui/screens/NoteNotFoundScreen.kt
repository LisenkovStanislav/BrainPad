package com.brainpad.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.brainpad.app.R
import com.brainpad.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteNotFoundScreen(
    noteId: String,
    onBackToAllNotesClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.app_name),
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = stringResource(R.string.cd_search)
                        )
                    }
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = stringResource(R.string.cd_settings)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(AppSpacing.screenPadding)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = AppShapes.errorCard,
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(AppSpacing.cardPadding),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(AppSizes.errorIconCircleSize)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.onError),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Description,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onErrorContainer,
                            modifier = Modifier.size(AppSizes.iconLarge)
                        )
                    }

                    Spacer(modifier = Modifier.height(AppSpacing.cardPadding))

                    Text(
                        text = stringResource(R.string.note_not_found_title),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )

                    Spacer(modifier = Modifier.height(AppSpacing.xl))

                    Text(
                        text = stringResource(R.string.note_not_found_message, noteId),
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 4,
                        color = MaterialTheme.colorScheme.onErrorContainer.copy(alpha = AppTokens.alphaErrorText),
                        textAlign = TextAlign.Center,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(AppSpacing.cardPadding))

                    Button(
                        onClick = onBackToAllNotesClick,
                        shape = AppShapes.pillButton,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error,
                            contentColor = MaterialTheme.colorScheme.onError
                        ),
                        modifier = Modifier.height(AppSizes.touchTargetMin)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            modifier = Modifier.size(AppSizes.iconSmall)
                        )
                        Spacer(modifier = Modifier.width(AppSpacing.m))
                        Text(
                            text = stringResource(R.string.back_to_all_notes),
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}