package com.example.bookstoreapp.ui.components

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class SnackbarManager(
    private val snackbarHostState: SnackbarHostState,
    private val scope: CoroutineScope
) {
    fun showSuccess(
        message: String,
        actionLabel: String? = null,
        onAction: (() -> Unit)? = null
    ) {
        show(message, actionLabel, SnackbarDuration.Short, onAction)
    }

    fun showError(
        message: String,
        actionLabel: String? = "Retry",
        onAction: (() -> Unit)? = null
    ) {
        show(message, actionLabel, SnackbarDuration.Long, onAction)
    }

    private fun show(
        message: String,
        actionLabel: String?,
        duration: SnackbarDuration,
        onAction: (() -> Unit)?
    ) {
        scope.launch {
            val result = snackbarHostState.showSnackbar(
                message = message,
                actionLabel = actionLabel,
                duration = duration
            )
            if (result == SnackbarResult.ActionPerformed) {
                onAction?.invoke()
            }
        }
    }
} 