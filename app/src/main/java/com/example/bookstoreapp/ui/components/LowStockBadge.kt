package com.example.bookstoreapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun LowStockBadge(
    quantity: Int,
    threshold: Int = 5,
    modifier: Modifier = Modifier
) {
    val (backgroundColor, textColor) = when {
        quantity <= 0 -> Color.Red to Color.White
        quantity <= threshold -> Color(0xFFFFA000) to Color.White
        else -> MaterialTheme.colorScheme.primary to MaterialTheme.colorScheme.onPrimary
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(backgroundColor)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = when {
                quantity <= 0 -> "Out of Stock"
                quantity <= threshold -> "Low Stock"
                else -> "In Stock"
            },
            color = textColor,
            style = MaterialTheme.typography.labelSmall
        )
    }
} 