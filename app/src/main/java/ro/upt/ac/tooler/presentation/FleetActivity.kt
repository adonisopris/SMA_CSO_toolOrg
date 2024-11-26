package ro.upt.ac.tooler.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.rememberAsyncImagePainter
import ro.upt.ac.tooler.data.database.RoomDatabase
import ro.upt.ac.tooler.data.database.ToolDbStore
import ro.upt.ac.tooler.domain.Tool
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts


@Composable
fun FleetScreen(viewModel: FleetViewModel) {
    val fleetListState = viewModel.fleetListState.collectAsState()
    val context = LocalContext.current.applicationContext
    var showAddDialog by remember { mutableStateOf(false) }

    Surface(color = Color.White) {

        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn {
                items(fleetListState.value) { tool -> FleetListItem(tool) }
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
                    onSubmit = { name, type, image->
                        viewModel.addTool(1,name, type, image)
                        showAddDialog = false
                    }
                )
            }
        }
    }
}

@Composable
fun FleetListItem(tool: Tool, modifier: Modifier = Modifier) {

    Card(
        modifier
            .padding(10.dp)
            .wrapContentSize(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(10.dp)
    ) {
        Row(
            modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(tool.image), //painterResource(id = R.drawable.crane1), //tool.id
                contentDescription = tool.name,
                modifier.size(140.dp)
            )
            Column(modifier.padding(12.dp)) {
                Text(text = tool.name, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text(text = tool.type, fontSize = 18.sp)

            }
        }
    }
}

@Composable
fun AddToolDialog(
    onDismiss: () -> Unit,
    onSubmit: (name: String, type: String, image: Uri) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("") }
    var image by remember { mutableStateOf("") }
    var available by remember { mutableStateOf(true) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

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

                // Image Input
                GalleryPicker(onImagePicked = { uri -> selectedImageUri = uri })

                // Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Button(
                        onClick = {
                            //val imageId = image.toIntOrNull() ?: 0 // Default to 0 if invalid
                            if (name.isNotBlank() && type.isNotBlank() && selectedImageUri != null)
                            onSubmit(name, type, selectedImageUri!!)
                        }
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

    // Launcher for picking an image from the gallery
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            selectedImageUri = uri
            uri?.let { onImagePicked(it) }
        }
    )

    Button(onClick = { galleryLauncher.launch("image/*") }) {
        Text("Pick Image from Gallery")
    }

    selectedImageUri?.let {
        Text(text = "Selected Image URI: $it")
    }
}