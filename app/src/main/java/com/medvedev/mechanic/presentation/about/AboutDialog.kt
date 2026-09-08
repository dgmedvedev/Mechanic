package com.medvedev.mechanic.presentation.about

import android.content.ClipData
import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.toClipEntry
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.paneTitle
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.graphics.drawable.toBitmap
import com.medvedev.mechanic.BuildConfig
import com.medvedev.mechanic.R
import com.medvedev.mechanic.presentation.preview.PreviewMechanicTheme
import kotlinx.coroutines.launch

@Composable
fun AboutDialog(
    onDismiss: () -> Unit,
    versionName: String = BuildConfig.VERSION_NAME,
) {
    Dialog(onDismissRequest = onDismiss) {
        AboutDialogContent(
            onDismiss = onDismiss,
            versionName = versionName,
        )
    }
}

@Composable
private fun AboutDialogContent(
    onDismiss: () -> Unit,
    versionName: String,
) {
    val clipboard = LocalClipboard.current
    val email = stringResource(R.string.developer_email)
    val aboutPaneTitle = stringResource(R.string.about)
    val copyLabel = stringResource(R.string.about_copy_email)
    val copiedMessage = stringResource(R.string.about_email_copied)
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Surface(
        modifier = Modifier
            .widthIn(min = 280.dp, max = 560.dp)
            .semantics { paneTitle = aboutPaneTitle },
        shape = AlertDialogDefaults.shape,
        color = AlertDialogDefaults.containerColor,
        tonalElevation = AlertDialogDefaults.TonalElevation,
    ) {
        Box {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                AboutAppIcon()
                Spacer(Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.app_name),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.about_description),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                )
                Spacer(Modifier.height(24.dp))
                HorizontalDivider(
                    thickness = 0.5.dp,
                    color = MaterialTheme.colorScheme.outline,
                )
                Spacer(Modifier.height(12.dp))
                AboutEmailRow(
                    email = email,
                    onClick = {
                        scope.launch {
                            clipboard.setClipEntry(
                                ClipData.newPlainText(copyLabel, email).toClipEntry(),
                            )
                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                                snackbarHostState.showSnackbar(copiedMessage)
                            }
                        }
                    },
                )
                Spacer(Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = stringResource(R.string.about_version, versionName),
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Text(
                        text = stringResource(R.string.about_copyright),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            IconButton(
                onClick = onDismiss,
                modifier = Modifier.align(Alignment.TopEnd),
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = stringResource(R.string.close),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.align(Alignment.BottomCenter),
            )
        }
    }
}

@Composable
private fun AboutEmailRow(
    email: String,
    onClick: () -> Unit,
) {
    val copyLabel = stringResource(R.string.about_copy_email)
    Row(
        modifier = Modifier
            .clickable(onClick = onClick)
            .semantics(mergeDescendants = true) {
                contentDescription = "$copyLabel, $email"
                role = Role.Button
            },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Outlined.Email,
            contentDescription = null,
            modifier = Modifier.size(18.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = email,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun AboutAppIcon() {
    val iconModifier = Modifier.size(72.dp)

    if (LocalInspectionMode.current) {
        Box(
            modifier = iconModifier
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
        )
        return
    }

    val context = LocalContext.current
    val imageBitmap = remember(context) {
        context.packageManager
            .getApplicationIcon(context.applicationInfo)
            .toBitmap(width = 192, height = 192)
            .asImageBitmap()
    }
    Image(
        bitmap = imageBitmap,
        contentDescription = null,
        modifier = iconModifier,
    )
}

@PreviewLightDark
@Composable
private fun AboutDialogPreview() {
    PreviewMechanicTheme {
        AboutDialogContent(
            onDismiss = {},
            versionName = "1.0.0",
        )
    }
}
