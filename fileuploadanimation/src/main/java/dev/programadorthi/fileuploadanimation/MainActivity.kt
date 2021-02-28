package dev.programadorthi.fileuploadanimation

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import dev.programadorthi.fileuploadanimation.ui.theme.ChallengesTheme
import kotlin.math.pow
import kotlinx.coroutines.launch

private const val ANIM_DURATION = 4_000
private val decelerateEasing = Easing { fraction -> fraction.pow(2) }
private val easeIn = CubicBezierEasing(0.42f, 0f, 1f, 1f)
private val easeInOutQuintEasing = CubicBezierEasing(0.86f, 0f, 0.07f, 1f)
private val easeOutCircEasing = CubicBezierEasing(0.075f, 0.82f, 0.165f, 1f)

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChallengesTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    FileUpload()
                }
            }
        }
    }
}

@Composable
fun FileUpload() {
    val scope = rememberCoroutineScope()
    val progress = remember { Animatable(initialValue = 0f) }

    AnimatedContent(
        progress = progress.value,
        inProgress = progress.isRunning,
        onClick = {
            scope.launch {
                progress.apply {
                    snapTo(targetValue = 0f) // reset before start
                    animateTo(
                        targetValue = 1f,
                        animationSpec = tween(
                            durationMillis = ANIM_DURATION,
                            easing = easeInOutQuintEasing
                        )
                    )
                }
            }
        }
    )
}

@Composable
fun AnimatedContent(
    progress: Float,
    inProgress: Boolean,
    onClick: () -> Unit
) {
    val boxSize = 150.dp
    val cloudSize = 90.dp
    val halfBoxSize = boxSize / 2
    val halfCloudSize = cloudSize / 2

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .size(boxSize)
                .clip(CircleShape)
                .background(color = Color(0x9980D8FF))
        ) {
            MiddleCloud(
                cloudSize = cloudSize,
                halfBoxSize = halfBoxSize,
                halfCloudSize = halfCloudSize,
                inProgress = inProgress
            )
            LeftCloud(
                cloudSize = cloudSize,
                boxSize = boxSize,
                inProgress = inProgress
            )
            RightCloud(
                cloudSize = cloudSize,
                boxSize = boxSize,
                inProgress = inProgress
            )
            Arrow(halfBoxSize = halfBoxSize, inProgress = inProgress)
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawArc(
                    color = Color.Blue,
                    startAngle = -90f,
                    sweepAngle = 360f * progress,
                    useCenter = false,
                    alpha = if (progress < 0.98f) 1f else (1f - progress) / (1f - 0.98f),
                    style = Stroke(width = 8.dp.toPx())
                )
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = if (progress < 1f) "${(progress * 100).toInt()}%" else "Completed",
            style = MaterialTheme.typography.h4
        )
    }
}

@Composable
fun MiddleCloud(
    cloudSize: Dp,
    halfBoxSize: Dp,
    halfCloudSize: Dp,
    inProgress: Boolean
) {
    val targetValue = halfBoxSize - halfCloudSize
    val yPosAnim = remember {
        Animatable(initialValue = targetValue, typeConverter = Dp.VectorConverter)
    }
    LaunchedEffect(key1 = inProgress) {
        if (inProgress) {
            yPosAnim.animateTo(
                targetValue = targetValue,
                animationSpec = keyframes {
                    durationMillis = ANIM_DURATION
                    targetValue at 0
                    targetValue at (ANIM_DURATION * 0.2f).toInt() with FastOutLinearInEasing
                    targetValue * 5f at (ANIM_DURATION * 0.3f).toInt()
                    targetValue * -5f at (ANIM_DURATION * 0.3025f).toInt()
                    targetValue * -5f at (ANIM_DURATION * 0.7f).toInt() with FastOutSlowInEasing
                }
            )
        }
    }
    Cloud(
        offset = DpOffset(x = targetValue, y = yPosAnim.value),
        cloudSize = cloudSize,
        contentDescription = "Middle cloud"
    )
}

@Composable
fun LeftCloud(
    cloudSize: Dp,
    boxSize: Dp,
    inProgress: Boolean
) {
    val yPosAnim = remember {
        Animatable(initialValue = boxSize, typeConverter = Dp.VectorConverter)
    }
    LaunchedEffect(key1 = inProgress) {
        if (inProgress) {
            yPosAnim.animateTo(
                targetValue = boxSize,
                animationSpec = keyframes {
                    delayMillis = 300
                    durationMillis = ANIM_DURATION
                    -boxSize at 0
                    -boxSize at (ANIM_DURATION * 0.2f).toInt() with easeIn
                    boxSize at (ANIM_DURATION * 0.4f).toInt()
                    -boxSize at (ANIM_DURATION * 0.4025f).toInt()
                    -boxSize at (ANIM_DURATION * 0.5f).toInt() with easeIn
                    boxSize at (ANIM_DURATION * 0.7f).toInt()
                }
            )
        }
    }
    Cloud(
        offset = DpOffset(x = boxSize * -0.15f, y = yPosAnim.value),
        cloudSize = cloudSize,
        contentDescription = "Left cloud"
    )
}

@Composable
fun RightCloud(
    cloudSize: Dp,
    boxSize: Dp,
    inProgress: Boolean
) {
    val yPosAnim = remember {
        Animatable(initialValue = boxSize, typeConverter = Dp.VectorConverter)
    }
    LaunchedEffect(key1 = inProgress) {
        if (inProgress) {
            yPosAnim.animateTo(
                targetValue = boxSize,
                animationSpec = keyframes {
                    durationMillis = ANIM_DURATION
                    -boxSize at 0
                    -boxSize at (ANIM_DURATION * 0.2f).toInt() with easeIn
                    boxSize at (ANIM_DURATION * 0.4f).toInt()
                    -boxSize at (ANIM_DURATION * 0.4025f).toInt()
                    -boxSize at (ANIM_DURATION * 0.5f).toInt() with easeIn
                    boxSize at (ANIM_DURATION * 0.7f).toInt()
                }
            )
        }
    }
    Cloud(
        offset = DpOffset(x = boxSize * 0.55f, y = yPosAnim.value),
        cloudSize = cloudSize,
        contentDescription = "Right cloud"
    )
}

@Composable
fun Arrow(halfBoxSize: Dp, inProgress: Boolean) {
    val targetValue = halfBoxSize - 15.dp
    val yPosAnim = remember {
        Animatable(initialValue = targetValue, typeConverter = Dp.VectorConverter)
    }
    LaunchedEffect(key1 = inProgress) {
        if (inProgress) {
            yPosAnim.animateTo(
                targetValue = targetValue,
                animationSpec = keyframes {
                    durationMillis = ANIM_DURATION
                    targetValue at 0 with decelerateEasing
                    targetValue * 1.15f at (ANIM_DURATION * 0.075f).toInt()
                    targetValue * 1.15f at (ANIM_DURATION * 0.2f).toInt() with easeOutCircEasing
                    targetValue * 0.6f at (ANIM_DURATION * 0.8f).toInt() with FastOutSlowInEasing
                    targetValue at ANIM_DURATION
                }
            )
        }
    }
    Image(
        painter = painterResource(id = R.drawable.ic_arrow_up_bold),
        contentDescription = "Arrow up",
        modifier = Modifier
            .size(50.dp)
            .offset(x = halfBoxSize - 25.dp, y = yPosAnim.value)
    )
}

@Composable
fun Cloud(offset: DpOffset, cloudSize: Dp, contentDescription: String) {
    Image(
        painter = painterResource(id = R.drawable.ic_apple_icloud),
        contentDescription = contentDescription,
        modifier = Modifier
            .size(cloudSize)
            .offset(
                x = offset.x,
                y = offset.y
            )
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ChallengesTheme {
        FileUpload()
    }
}