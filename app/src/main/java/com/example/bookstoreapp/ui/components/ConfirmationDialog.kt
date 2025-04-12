package com.example.bookstoreapp.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun ConfirmationDialog(
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
    title: String,
    message: String,
    confirmText: String = "Confirm",
    dismissText: String = "Cancel",
    icon: ImageVector? = null,
    isDestructive: Boolean = false,
    confirmButtonColors: ButtonColors = ButtonDefaults.textButtonColors(
        contentColor = if (isDestructive) Color.Red else MaterialTheme.colorScheme.primary
    ),
    dismissButtonColors: ButtonColors = ButtonDefaults.textButtonColors()
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        icon = icon?.let { { Icon(imageVector = it, contentDescription = null) } },
        title = { 
            Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        },
        text = { Text(text = message) },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm()
                    onDismissRequest()
                },
                colors = confirmButtonColors
            ) {
                Text(
                    text = confirmText,
                    fontWeight = if (isDestructive) FontWeight.Bold else FontWeight.Normal
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismissRequest,
                colors = dismissButtonColors
            ) {
                Text(text = dismissText)
            }
        }
    )
}

@Composable
fun DeleteConfirmationDialog(
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
    itemName: String
) {
    ConfirmationDialog(
        onDismissRequest = onDismissRequest,
        onConfirm = onConfirm,
        title = "Delete Confirmation",
        message = "Are you sure you want to delete this $itemName? This action cannot be undone.",
        confirmText = "Delete",
        icon = Icons.Default.Warning,
        isDestructive = true
    )
}