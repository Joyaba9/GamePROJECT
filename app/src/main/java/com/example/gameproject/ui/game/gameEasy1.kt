@file:OptIn(ExperimentalFoundationApi::class)

package edu.farmingdale.draganddropanim_demo

import android.content.ClipData
import android.content.ClipDescription
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.draganddrop.dragAndDropSource
import androidx.compose.foundation.draganddrop.dragAndDropTarget
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTarget
import androidx.compose.ui.draganddrop.DragAndDropTransferData
import androidx.compose.ui.draganddrop.mimeTypes
import androidx.compose.foundation.Canvas
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gameproject.ui.game.GameViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DragAndDropBoxes(modifier: Modifier = Modifier, viewModel: GameViewModel = viewModel()) {
    // Observing role and attempts from ViewModel
    val role by viewModel.role
    val attempts by viewModel.attempts

    Column(modifier = Modifier.fillMaxSize()) {
        // The drop boxes and drag icon
        Row(
            modifier = modifier
                .fillMaxWidth()
                .weight(0.2f)
        ) {
            val boxCount = 4
            var dragBoxIndex by remember { mutableIntStateOf(0) }

            repeat(boxCount) { index ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(10.dp)
                        .border(1.dp, Color.Black)
                        .dragAndDropTarget(
                            shouldStartDragAndDrop = { event ->
                                event
                                    .mimeTypes()
                                    .contains(ClipDescription.MIMETYPE_TEXT_PLAIN)
                            },
                            target = remember {
                                object : DragAndDropTarget {
                                    override fun onDrop(event: DragAndDropEvent): Boolean {
                                        dragBoxIndex = index
                                        return true
                                    }
                                }
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    this@Row.AnimatedVisibility(
                        visible = index == dragBoxIndex,
                        enter = scaleIn() + fadeIn(),
                        exit = scaleOut() + fadeOut()
                    ) {
                        DragAndDropIcon()
                    }
                }
            }
        }

        // Rotation Animation State
        val rotation = remember { Animatable(0f) }

        // Launch rotation animation
        LaunchedEffect(Unit) {
            rotation.animateTo(
                targetValue = 360f, // rotate once fully
                animationSpec = tween(durationMillis = 2000)
            )
        }

        var offsetX by remember { mutableStateOf(0f) }
        var offsetY by remember { mutableStateOf(0f) }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = {
                // Reset Position
                offsetX = 0f
                offsetY = 0f
            }) {
                Text(text = "Reset Position")
            }

            Button(onClick = {
                // Simulate checking goal:
                val success = checkIfReachedGoal(offsetX, offsetY)
                if (success) {
                    viewModel.addAttempt("Success")
                } else {
                    viewModel.addAttempt("Failure")
                }
            }) {
                Text("Run")
            }
        }


        val goalX = 900f
        val goalY = 400f
        val goalWidth = 200f
        val goalHeight = 200f

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.6f)
                .background(Color.White)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {

                drawRect(
                    color = Color.Green,
                    topLeft = Offset(goalX, goalY),
                    size = Size(goalWidth, goalHeight)
                )

                rotate(degrees = rotation.value, pivot = Offset(200f, 400f)) {
                    drawRect(Color.Blue, topLeft = Offset(200f + offsetX, 400f + offsetY), size = Size(50f, 50f))
                }
            }

            // Check last attempt:
            val lastAttempt = attempts.lastOrNull()
            if (lastAttempt == "Success") {
                Text(
                    text = "You Win!",
                    color = Color.Green,
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Center)
                )
            } else if (lastAttempt == "Failure") {
                Text(
                    text = "Try Again!",
                    color = Color.Red,
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }

        // Display attempts history only if the user is a parent
        if (role == "parent") {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.1f)
                    .padding(8.dp)
            ) {
                Text("Attempts History (Visible only to Parent):", fontWeight = FontWeight.Bold)
                attempts.forEachIndexed { i, attempt ->
                    Text("Attempt #${i+1}: $attempt")
                }
            }
        } else {
            // If child, no attempts are displayed.
        }
    }
}

fun checkIfReachedGoal(offsetX: Float, offsetY: Float): Boolean {

    return offsetX == 0f && offsetY == 0f
}

@Composable
fun DragAndDropIcon() {
    Icon(
        imageVector = Icons.Default.ArrowForward,
        contentDescription = "",
        modifier = Modifier
            .fillMaxSize()
            .dragAndDropSource {
                detectTapGestures(
                    onLongPress = { offset ->
                        startTransfer(
                            transferData = DragAndDropTransferData(
                                clipData = ClipData.newPlainText(
                                    "text",
                                    "UP"
                                )
                            )
                        )
                    }
                )
            }
    )
}
