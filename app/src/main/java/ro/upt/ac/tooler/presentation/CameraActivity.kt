package ro.upt.ac.tooler.presentation

import android.net.Uri
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.width
import androidx.compose.ui.viewinterop.AndroidView
import java.util.concurrent.Executors
import androidx.compose.material3.Button
import androidx.compose.ui.Alignment
import ro.upt.ac.tooler.data.CameraFileUtils.takePicture
import androidx.compose.material3.Text
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter

/*@Composable
fun CameraScreen(navController: NavController) {
    val context = LocalContext.current
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    // Executor for background tasks, specifically for taking pictures in this context
    val executor = remember { Executors.newSingleThreadExecutor() }
    // State to hold the URI of the captured image. Initially null, updated after image capture
    val capturedImageUri = remember { mutableStateOf<Uri?>(null) }

    // Camera controller tied to the lifecycle of this composable
    val cameraController = remember {
        LifecycleCameraController(context).apply {
            bindToLifecycle(lifecycleOwner) // Binds the camera to the lifecycle of the lifecycleOwner
        }
    }

    Box {
        // PreviewView for the camera feed. Configured to fill the screen and display the camera output
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { ctx ->
                PreviewView(ctx).apply {
                    scaleType = PreviewView.ScaleType.FILL_START
                    implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                    controller = cameraController // Attach the lifecycle-aware camera controller.
                }
            },
            onRelease = {
                // Called when the PreviewView is removed from the composable hierarchy
                cameraController.unbind() // Unbinds the camera to free up resources
            }
        )

        // Button that triggers the image capture process
        Button(
            onClick = {
                // Calls a utility function to take a picture, handling success and error scenarios
                takePicture(cameraController, context, executor, { uri ->
                    capturedImageUri.value = uri // Update state with the URI of the captured image on success
                }, { exception ->
                    // Error handling logic for image capture failures
                }, )
                //capturedImageUri.value?.let(onImagePicked)
                navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.set("picture", capturedImageUri.value.toString())
                navController.popBackStack()
            },
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Text(text = "Take Picture")
        }

        // Displays the captured image if available
        capturedImageUri.value?.let { uri ->
            Image(
                // Asynchronously loads and displays the image from the URI
                painter = rememberAsyncImagePainter(uri),
                contentDescription = null,
                modifier = Modifier
                    .width(80.dp)
                    .align(Alignment.BottomEnd),
                contentScale = ContentScale.Crop
            )
        }
    }
}*/