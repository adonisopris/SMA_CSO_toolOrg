package ro.upt.ac.tooler.presentation

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.core.net.toUri
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import ro.upt.ac.tooler.data.CameraFileUtils.takePicture
import ro.upt.ac.tooler.domain.Tool
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.UUID
import java.util.concurrent.Executors


@Composable
fun FleetScreen(viewModel: FleetViewModel, navController: NavController) {
    val fleetListState = viewModel.fleetListState.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    Surface(color = Color.White) {

        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Search bar
                TextField(
                    value = searchQuery,
                    onValueChange = { query -> searchQuery = query },
                    label = { Text("Search Tools") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    singleLine = true,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null
                        )
                    }
                )
                LazyColumn {
                    items(fleetListState.value.filter {
                        it.name.toUpperCase().contains(searchQuery.toUpperCase()) ||
                                it.type.toUpperCase().contains(searchQuery.toUpperCase())
                    }) { tool ->
                        FleetListItem(
                            tool = tool,
                            navController = navController,
                            fleetViewModel = viewModel
                        )
                    }
                }
            }
        }
        Box(modifier = Modifier.fillMaxSize()) {
            FloatingActionButton(
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.BottomEnd), contentColor = Black,
                onClick = { showAddDialog = true }
            ) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = null)
            }
            if (showAddDialog) {
                AddToolDialog(
                    onDismiss = { showAddDialog = false },
                    onSubmit = { name, type, details, image ->
                        viewModel.addTool(name, type, details, image)
                        showAddDialog = false
                    }
                )
            }
        }
    }
}

@Composable
fun FleetListItem(
    tool: Tool,
    modifier: Modifier = Modifier,
    navController: NavController,
    fleetViewModel: FleetViewModel
) {
    Card(
        modifier = modifier
            .padding(10.dp)
            .wrapContentSize()
            .clickable {
                navController.navigate(route = "ToolDetail/${tool.id}")
            },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(tool.image),
                contentDescription = tool.name,
                modifier = Modifier.size(80.dp)
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 12.dp)
            ) {
                Text(text = tool.name, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text(text = tool.type, fontSize = 18.sp)
            }
            IconButton(
                onClick = { fleetViewModel.removeTool(tool) },
                modifier = Modifier
                    .size(36.dp)
                    .padding(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Color.Red,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
fun AddToolDialog(
    onDismiss: () -> Unit,
    onSubmit: (name: String, type: String, details: String, image: Uri) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("") }
    var details by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var takePicture by remember { mutableStateOf(false) }
    val defaultImageUri = Uri.parse("android.resource://ro.upt.ac.tooler/drawable/default_tool")

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Add New Tool", style = MaterialTheme.typography.titleLarge)

                // Name Input
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Tool Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Type Input
                TextField(
                    value = type,
                    onValueChange = { type = it },
                    label = { Text("Tool Type") },
                    modifier = Modifier.fillMaxWidth()
                )
                // Type Details
                TextField(
                    value = details,
                    onValueChange = { details = it },
                    label = { Text("Tool details") },
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {

                    Button(
                        onClick = {
                            takePicture = true
                        },
                        modifier = Modifier.width(100.dp)
                    ) {
                        // Text("Take picture")
                        Icon(
                            imageVector = Icons.Filled.Camera,
                            contentDescription = "Camera"
                        )
                    }


                    // Image Input
                    GalleryPicker(onImagePicked = { uri -> selectedImageUri = uri })
                }
                if (takePicture) {
                    CameraScreen(
                        onSubmit = { item ->
                            selectedImageUri = item
                            takePicture = false

                        }
                    )
                }

                // Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xffcc1f1f)),
                        modifier = Modifier.width(100.dp)
                    )
                    {
                        Text("Cancel")
                    }
                    Button(
                        onClick = {
                            //val imageId = image.toIntOrNull() ?: 0 // Default to 0 if invalid
                            if (name.isNotBlank() && type.isNotBlank() && selectedImageUri == null)
                                onSubmit(name, type, details, defaultImageUri)

                            if (name.isNotBlank() && type.isNotBlank() && selectedImageUri != null)
                                onSubmit(name, type, details, selectedImageUri!!)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF348710)),
                        modifier = Modifier.width(100.dp)
                    ) {
                        Text("Add")
                    }
                }
            }
        }
    }
}

@Composable
fun GalleryPicker(onImagePicked: (Uri) -> Unit) {
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current

    // Launcher for picking an image from the gallery
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            selectedImageUri = uri
            uri?.let {
                val savedImagePath = saveImageInAppFolder(
                    context = context,
                    uri = it,
                    folderName = "AppImages",
                    fileName = "image_${UUID.randomUUID()}.jpg"
                )
                savedImagePath?.let { path -> onImagePicked(path.toUri()) }
            }
        }
    )

    Button(onClick = { galleryLauncher.launch("image/*") }, modifier = Modifier.width(100.dp)) {
        Icon(
            imageVector = Icons.Filled.Photo,
            contentDescription = "get Image from Library"
        )
    }

    /*selectedImageUri?.let {
        Text(text = "Selected Image URI: $it")
    }*/
}

fun saveImageInAppFolder(
    context: Context,
    uri: Uri,
    folderName: String,
    fileName: String
): String? {
    // Create a folder inside the app's internal storage
    val folder = File(context.filesDir, folderName)
    if (!folder.exists()) {
        folder.mkdir()
    }

    val file = File(folder, fileName)
    val inputStream: InputStream? = context.contentResolver.openInputStream(uri)

    inputStream?.use { input ->
        FileOutputStream(file).use { output ->
            input.copyTo(output)
        }
    }

    return file.absolutePath //image path
}

@Composable
fun CameraScreen(onSubmit: (image: Uri) -> Unit) {
    val context = LocalContext.current
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current

    val executor = remember { Executors.newSingleThreadExecutor() }
    val capturedImageUri = remember { mutableStateOf<Uri?>(null) }
    val cameraController = remember {
        LifecycleCameraController(context).apply {
            bindToLifecycle(lifecycleOwner)
        }
    }
    Box {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { ctx ->
                PreviewView(ctx).apply {
                    scaleType = PreviewView.ScaleType.FILL_START
                    implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                    controller =
                        cameraController
                }
            },
            onRelease = {
                cameraController.unbind()
            }
        )

        Button(
            onClick = {
                takePicture(
                    cameraController, context, executor,
                    { uri ->
                        capturedImageUri.value = uri
                        onSubmit(uri)
                    },
                    { exception -> },
                )

            },
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Text(text = "Take Picture")
        }
    }
}

