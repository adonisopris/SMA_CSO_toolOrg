package ro.upt.ac.tooler.presentation

import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Delete
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import ro.upt.ac.tooler.data.database.RoomDatabase
import ro.upt.ac.tooler.data.database.SiteDbStore
import ro.upt.ac.tooler.domain.Site
import ro.upt.ac.tooler.domain.Tool

@Composable
fun SiteScreen(viewModel: SitesViewModel, navController: NavController) {
    val siteListState = viewModel.sitesListState.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

    Surface(color = Color.White) {

        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn {
                items(siteListState.value) { site -> SiteListItem(site = site, navController = navController, siteViewModel = viewModel) }
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
                AddSiteDialog(
                    navController = navController,
                    onDismiss = { showAddDialog = false },
                    onSubmit = { name, type, details, latitude, longitude->
                        viewModel.addSite(name = name, type = type, details = details, latitude = latitude, longitude = longitude)
                        showAddDialog = false
                    }
                )
            }
        }
    }
}

@Composable
fun SiteListItem(
    site: Site,
    modifier: Modifier = Modifier,
    navController: NavController,
    siteViewModel: SitesViewModel
) {
    Card(
        modifier = modifier
            .padding(10.dp)
            .wrapContentSize()
            .clickable {
                navController.navigate(route = "SiteDetail/${site.id}")
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
            Text(text = site.id.toString(), fontSize = 25.sp)

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 12.dp)
            ) {
                Text(text = site.name, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text(text = site.type, fontSize = 18.sp)
            }
            IconButton(
                onClick = { siteViewModel.removeSite(site) },
                modifier = Modifier
                    .size(36.dp) // Small button size
                    .padding(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Color.Red,
                    modifier = Modifier.size(24.dp) // Smaller icon size
                )
            }
        }
    }
}

@Composable
fun AddSiteDialog(
    navController: NavController,
    onDismiss: () -> Unit,
    onSubmit: (name: String, type: String, details: String, latitude: Double, longitude:Double) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("") }
    var details by remember { mutableStateOf("") }
    var latitude by remember { mutableStateOf("") }
    var longitude by remember { mutableStateOf("") }

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
                Text(text = "Add New Site", style = MaterialTheme.typography.titleLarge)

                // Name Input
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Site Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Type Input
                TextField(
                    value = type,
                    onValueChange = { type = it },
                    label = { Text("Site Type") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Type Latitude
                TextField(
                    value = latitude,
                    onValueChange = { latitude = it },
                    label = { Text("latitude") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Type Longitude
                TextField(
                    value = longitude,
                    onValueChange = { longitude = it },
                    label = { Text("longitude") },
                    modifier = Modifier.fillMaxWidth()
                )
                // Type Details
                TextField(
                    value = details,
                    onValueChange = { details = it },
                    label = { Text("details") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor =  Color(0xffcc1f1f )),
                        modifier = Modifier.width(100.dp))
                    {
                        Text("Cancel")
                    }
                    Button(
                        onClick = {
                            //val imageId = image.toIntOrNull() ?: 0 // Default to 0 if invalid
                            if (name.isNotBlank() && type.isNotBlank() && longitude.isNotBlank() && latitude.isNotBlank())
                                onSubmit(name, type, details, latitude.toDouble(), longitude.toDouble())
                        },
                        colors = ButtonDefaults.buttonColors(containerColor =  Color(0xFF348710 )),
                        modifier = Modifier.width(100.dp)
                    ) {
                        Text("Add")
                    }
                }
            }
        }
    }
}