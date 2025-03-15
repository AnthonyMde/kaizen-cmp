package com.makapp.kaizen.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.makapp.kaizen.ui.theme.DISABLED_ALPHA
import kaizen.composeapp.generated.resources.Res
import kaizen.composeapp.generated.resources.ic_add_circle_filled
import kaizen.composeapp.generated.resources.ic_minus_circle_filled
import org.jetbrains.compose.resources.painterResource

@Composable
fun Stepper(
    value: Int,
    onDecrease: (Int) -> Unit,
    onIncrease: (Int) -> Unit,
    max: Int = Int.MAX_VALUE,
    min: Int = 0,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        val increaseEnabled = value < max
        val decreaseEnabled = value > min

        IconButton(
            onClick = {
                onDecrease(value - 1)
            },
            enabled = decreaseEnabled,
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_minus_circle_filled),
                contentDescription = "Minus",
                tint = if (decreaseEnabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(
                    alpha = DISABLED_ALPHA
                ),
                modifier = Modifier.size(32.dp)
            )
        }
        Text(value.toString())
        IconButton(
            onClick = {
                onIncrease(value + 1)
            },
            enabled = increaseEnabled,
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_add_circle_filled),
                contentDescription = "Plus",
                tint = if (increaseEnabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(
                    alpha = DISABLED_ALPHA
                ),
                modifier = Modifier.size(32.dp)
            )
        }
    }
}