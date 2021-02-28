package dev.programadorthi.iosloadingindicator

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.programadorthi.iosloadingindicator.ui.theme.ChallengesTheme
import kotlin.math.floor

private val defaultRadius = 30.dp
private val alphaValues = listOf(
    0.18f,
    0.18f,
    0.18f,
    0.18f,
    0.25f,
    0.32f,
    0.38f,
    0.44f,
    0.51f,
    0.57f,
    0.18f,
    0.18f
)

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChallengesTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Column(modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center) {
                        CircularIOSIndicator()
                    }
                }
            }
        }
    }
}

@Composable
fun CircularIOSIndicator(radius: Dp = defaultRadius) {
    val tickCount = alphaValues.size
    val color = if (isSystemInDarkTheme()) Color(0xFFEBEBF5) else Color(0xFF3C3C44)
    val paint = remember(key1 = color) {
        Paint().apply {
            this.color = color
        }
    }
    val infiniteTransition = rememberInfiniteTransition()
    val progress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 700,
                easing = LinearEasing
            )
        )
    )
    Canvas(modifier = Modifier.size(radius * 3)) {
        val radiusPx = radius.toPx()
        val active = floor(tickCount * progress)
        drawIntoCanvas { canvas ->
            canvas.save()
            canvas.translate(dx = center.x, dy = center.y)
            for (count in 0 until tickCount) {
                val index = ((count - active) % tickCount).toInt()
                val alphaIndex = if (index < 0) tickCount + index else index
                paint.color = color.copy(alpha = alphaValues[alphaIndex])
                canvas.drawRoundRect(
                    left = -radiusPx * 0.1f,
                    top = -radiusPx * 1.1f,
                    right = radiusPx * 0.1f,
                    bottom = -radiusPx * 0.5f,
                    radiusX = radiusPx,
                    radiusY = radiusPx,
                    paint = paint
                )
                canvas.rotate(360f / tickCount)
            }
            canvas.restore()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ChallengesTheme {
        CircularIOSIndicator()
    }
}