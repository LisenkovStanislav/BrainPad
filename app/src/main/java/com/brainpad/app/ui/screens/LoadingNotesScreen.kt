package com.brainpad.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
