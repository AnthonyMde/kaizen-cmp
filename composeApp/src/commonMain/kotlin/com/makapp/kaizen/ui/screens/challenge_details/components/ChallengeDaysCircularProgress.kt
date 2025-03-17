package com.makapp.kaizen.ui.screens.challenge_details.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import kaizen.composeapp.generated.resources.Res
import kaizen.composeapp.generated.resources.challenge_details_dashboard_day_count
import org.jetbrains.compose.resources.stringResource
import kotlin.math.min

@Composable
fun ChallengeDaysCircularProgress(
    count: Int,
) {
    val max = 365
    val safeCount = min(count, max)

    var animatedCountState by remember { mutableStateOf(0) }
    LaunchedEffect(safeCount) {
        animatedCountState = safeCount
    }

    val sweepAngle = (animatedCountState * 100) / max * 2.4
    val animatedSweep by animateFloatAsState(
        targetValue = sweepAngle.toFloat(),
        animationSpec = tween(durationMillis = 1000),
        label = "circular progression"
    )
    val progressColor = MaterialTheme.colorScheme.primary

    Box(
        Modifier
            .size(175.dp)
            .drawBehind {
                val componentSize = size
                val xOffSet = 0f
                val yOffSet = 20f
                drawCircularShape(
                    componentSize,
                    xOffSet,
                    yOffSet,
                    color = Color.Gray.copy(alpha = 0.1f)
                )
                drawCircularShape(
                    componentSize,
                    xOffSet,
                    yOffSet,
                    color = progressColor,
                    sweep = animatedSweep
                )
            },
        contentAlignment = Alignment.Center
    ) {
        val animatedCount by animateIntAsState(
            animatedCountState,
            animationSpec = tween(1000),
            label = "counter animation"
        )
        val animatedColor by animateColorAsState(
            targetValue = if (count == 0) Color.LightGray else Color.Black,
            animationSpec = tween(1000),
            label = "count color animation"
        )
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "$animatedCount",
                style = MaterialTheme.typography.displaySmall,
                color = animatedColor
            )
            Text(
                text = stringResource(Res.string.challenge_details_dashboard_day_count),
                style = MaterialTheme.typography.bodyLarge,
                color = animatedColor
            )
        }
    }
}

private fun DrawScope.drawCircularShape(
    componentSize: Size,
    xOffSet: Float,
    yOffSet: Float,
    sweep: Float = 240f,
    color: Color

) {
    drawArc(
        size = componentSize,
        color = color,
        startAngle = 150f,
        sweepAngle = sweep,
        useCenter = false,
        style = Stroke(width = 30f, cap = StrokeCap.Round),
        topLeft = Offset(x = xOffSet, y = yOffSet)
    )
}
