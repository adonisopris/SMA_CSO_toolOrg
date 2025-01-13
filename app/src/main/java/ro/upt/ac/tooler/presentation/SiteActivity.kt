package ro.upt.ac.tooler.presentation

import android.Manifest.permission
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
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
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Marker
import ro.upt.ac.tooler.data.database.RoomDatabase
import ro.upt.ac.tooler.data.database.SiteDbStore
import ro.upt.ac.tooler.domain.Site
import ro.upt.ac.tooler.domain.Tool
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MarkerState
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import ro.upt.ac.tooler.R
import ro.upt.ac.tooler.data.database.RoomDatabase.getDb
import ro.upt.ac.tooler.data.database.SiteTypeEntity
import ro.upt.ac.tooler.domain.SiteType
import ro.upt.ac.tooler.location.LocationHandler

class SiteActivity :AppCompatActivity() {


    @Composable
    fun SiteScreen(viewModel: SitesViewModel, navController: NavController, latLngState: MutableState<LatLng>) {
        val siteListState = viewModel.sitesListState.collectAsState()
        var showAddDialog by remember { mutableStateOf(false) }
        var searchQuery by remember { mutableStateOf("") }

        Surface(color = Color.White) {

            Box(modifier = Modifier.fillMaxSize()) {
                Column(modifier = Modifier.fillMaxSize()) {

                    // Search bar
                    TextField(
                        value = searchQuery,
                        onValueChange = { query -> searchQuery = query },
                        label = { Text("Search Sites") },
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
                        items(siteListState.value.filter {
                            it.name.toUpperCase().contains(searchQuery.toUpperCase()) ||
                                    it.type.toUpperCase().contains(searchQuery.toUpperCase())})
                        { site ->
                            SiteListItem(
                                site = site,
                                navController = navController,
                                siteViewModel = viewModel
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
                    AddSiteDialog(
                        navController = navController,
                        latLngState = latLngState,
                        viewModel = viewModel,
                        onDismiss = { showAddDialog = false },
                        onSubmit = { name, type, details, latitude, longitude ->
                            viewModel.addSite(
                                name = name,
                                type = type,
                                details = details,
                                latitude = latitude,
                                longitude = longitude
                            )
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
                val siteImage = Uri.parse("android.resource://ro.upt.ac.tooler/drawable/site")
                Image(
                    painter = rememberAsyncImagePainter(siteImage),
                    contentDescription = null,
                    modifier = Modifier.size(100.dp),
                    contentScale = ContentScale.Crop // Crop the image to fill the area
                )

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

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun AddSiteDialog(
        navController: NavController,
        latLngState: MutableState<LatLng>,
        viewModel: SitesViewModel,
        onDismiss: () -> Unit,
        onSubmit: (name: String, type: String, details: String, latitude: Double, longitude: Double) -> Unit
    ) {
        var name by remember { mutableStateOf("") }
        var selectedType by remember { mutableStateOf("") }
        var customType by remember { mutableStateOf("") }
        var details by remember { mutableStateOf("") }
        var latitude by remember { mutableStateOf("") }
        var longitude by remember { mutableStateOf("") }
        var expanded by remember { mutableStateOf(false) }
        val siteTypes by viewModel.siteTypes.collectAsState(initial = emptyList())
        val context = LocalContext.current
        /*val initialSiteTypes = listOf(
            "Construction",
            "Demolition",
            "Renovation",
            "Road Construction",
            "Residential Construction",
            "Commercial Construction",
            "Industrial Construction",
            "Airport Construction",
            "Railway Projects",
            "Seaport Construction",
            "Tunnel Construction",
        )
        initialSiteTypes.forEach{type ->  viewModel.addSiteType(type)}*/

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

                    // Type Selection Dropdown
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        ExposedDropdownMenuBox(
                            expanded = expanded,
                            onExpandedChange = {
                                expanded = !expanded
                            }
                        ) {
                            TextField(
                                value = selectedType,
                                onValueChange = { selectedType = it },
                                label = { Text(text = "Search type") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                                modifier = Modifier.menuAnchor().fillMaxWidth()
                            )

                            val filteredOptions =
                                siteTypes.filter { it.name.contains(selectedType, ignoreCase = true) }
                            if (filteredOptions.isNotEmpty()) {
                                ExposedDropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = {
                                        // We shouldn't hide the menu when the user enters/removes any character
                                    }
                                ) {
                                    filteredOptions.forEach { item ->
                                        DropdownMenuItem(
                                            text = { Text(text = item.name) },
                                            onClick = {
                                                selectedType = item.name
                                                expanded = false
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Type Details
                    TextField(
                        value = details,
                        onValueChange = { details = it },
                        label = { Text("Details") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text("Choose the location", fontSize = 30.sp)
                    Row(
                        Modifier.weight(7f)
                    ) {
                        MapComposable(latLngState)
                    }
                    Row(
                        Modifier
                            .weight(1f)
                            .align(Alignment.CenterHorizontally)
                            .padding(16.dp)
                    ) {
                        LocationComposable(latLngState, viewModel, context)
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
                        ) {
                            Text("Cancel")
                        }
                        Button(
                            onClick = {
                                latitude = latLngState.value.latitude.toString()
                                longitude = latLngState.value.longitude.toString()

                                var newType = true
                                siteTypes.forEach{siteType -> if(siteType.name == selectedType) newType = false}
                                if (newType)
                                    viewModel.addSiteType(selectedType)

                                if (name.isNotBlank() && selectedType.isNotBlank() && longitude.isNotBlank() && latitude.isNotBlank())
                                    onSubmit(
                                        name,
                                        selectedType,
                                        details,
                                        latitude.toDouble(),
                                        longitude.toDouble()
                                    )
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
    fun MapComposable(latLngState: MutableState<LatLng>) {
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(latLngState.value, 10f)
        }
        LaunchedEffect(latLngState.value) {
            cameraPositionState.animate(
                update = CameraUpdateFactory.newCameraPosition(
                    CameraPosition(latLngState.value, 15f, 0f, 0f)
                ),
                durationMs = 1000
            )
        }
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            onMapClick = { latLng ->
                // Update latLngState to move the marker
                latLngState.value = latLng
            }
        ) {
            Marker(
                state = MarkerState(position = latLngState.value),
                title = "State",
                snippet = "Marker in State"
            )
        }
    }
    @Composable
    private fun LocationComposable(latLngState: MutableState<LatLng>, viewModel: SitesViewModel, context: Context) {
        Text(text = viewModel.getAddressFromLatLng(context, latLngState.value.latitude, latLngState.value.longitude))
    }
}