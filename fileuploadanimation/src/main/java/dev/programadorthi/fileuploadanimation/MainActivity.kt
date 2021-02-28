package dev.programadorthi.fileuploadanimation

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.util.lerp
import dev.programadorthi.fileuploadanimation.ui.theme.ChallengesTheme
import kotlin.math.pow
import kotlinx.coroutines.launch

private const val ANIM_DURATION = 4_000
private val DecelerateEasing = Easing { fraction -> fraction.pow(2) }
private val EaseOutCircEasing = CubicBezierEasing(0.075f, 0.82f, 0.165f, 1.0f)

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
    var inProgress by remember { mutableStateOf(false) }

    AnimatedContent(
        progress = progress.value,
        inProgress = inProgress,
        onClick = {
            scope.launch {
                progress.snapTo(targetValue = 0f)
                inProgress = true
                progress.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(
                        durationMillis = ANIM_DURATION,
                        easing = CubicBezierEasing(0.86f, 0f, 0.07f, 1f)
                    )
                )
                inProgress = false
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
            Cloud(
                offset = DpOffset(x = -halfCloudSize, y = -boxSize),
                cloudSize = cloudSize,
                contentDescription = "Left cloud"
            )
            Cloud(
                offset = DpOffset(x = boxSize - halfCloudSize, y = boxSize),
                cloudSize = cloudSize,
                contentDescription = "Right cloud"
            )
            Arrow(halfBoxSize = halfBoxSize, inProgress = inProgress)
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
    Cloud(
        offset = DpOffset(x = targetValue, y = targetValue + targetValue),
        cloudSize = cloudSize,
        contentDescription = "Middle cloud"
    )
}

@Composable
fun Arrow(halfBoxSize: Dp, inProgress: Boolean) {
    val targetValue = halfBoxSize - 15.dp
    val yPosAnim = remember {
        Animatable(initialValue = targetValue, typeConverter = Dp.VectorConverter)
    }
    LaunchedEffect(key1 = inProgress) {
        if (inProgress.not()) {
            yPosAnim.snapTo(targetValue)
        } else {
            yPosAnim.animateTo(
                targetValue = targetValue,
                animationSpec = keyframes {
                    durationMillis = ANIM_DURATION
                    targetValue at 0 with DecelerateEasing
                    targetValue * 1.1f at (ANIM_DURATION * 0.075f).toInt()
                    targetValue * 1.1f at (ANIM_DURATION * 0.2f).toInt() with EaseOutCircEasing
                    targetValue * 0.6f at (ANIM_DURATION * 0.7f).toInt()
                    targetValue at ANIM_DURATION with DecelerateEasing
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