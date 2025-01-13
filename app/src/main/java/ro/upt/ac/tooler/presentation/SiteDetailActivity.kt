package ro.upt.ac.tooler.presentation

import android.net.Uri
import android.widget.Toast
import androidx.camera.core.impl.utils.ExifData.WhiteBalanceMode
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import ro.upt.ac.tooler.domain.Site
import ro.upt.ac.tooler.domain.Tool
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.AddLocation
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Details
import androidx.compose.material.icons.filled.Handyman
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ro.upt.ac.tooler.location.LocationHandler

@Composable
fun SiteDetail(
    modifier: Modifier = Modifier,
    siteId: Int,
    siteDetailViewModel: SiteDetailViewModel,
    fleetViewModel: FleetViewModel,
    navController: NavController
) {
    val context = LocalContext.current
    val site: Site = siteDetailViewModel.getSiteById(siteId)!!
    var showAddDialog by remember { mutableStateOf(false) }
    var tools by remember { mutableStateOf(siteDetailViewModel.getToolsForSite(site.id)) }
    val siteImageUri = Uri.parse("android.resource://ro.upt.ac.tooler/drawable/construction_site")
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier
                .fillMaxSize()
                .padding(25.dp)
                .verticalScroll(rememberScrollState())
                .height(1000.dp),

            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            //Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            ) {
                // Background Image
                Image(
                    painter = rememberAsyncImagePainter(siteImageUri),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer { alpha = 0.3f },
                    contentScale = ContentScale.Crop // Crop the image to fill the area
                )
                Card(
                    modifier = Modifier
                        .wrapContentSize()
                        .graphicsLayer(alpha = 0.7f)
                        .align(Alignment.Center)
                        .padding(5.dp),
                    colors = CardDefaults.cardColors(White),
                    elevation = CardDefaults.cardElevation(8.dp) // Optional: add elevation for shadow
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(10.dp)
                    ) {

                        Text(
                            text = site.name,
                            fontSize = 40.sp,
                            fontWeight = FontWeight.Bold,
                            color = Black,
                            textAlign = TextAlign.Center,
                        )
                        Text(
                            text = "- " + site.type + " -",
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.DarkGray,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
            //Adresss
            val address =
                siteDetailViewModel.getAddressFromLatLng(context, site.latitude, site.longitude)
            if (address != null) {
                val city = address.locality ?: ""
                val street = address.thoroughfare ?: ""
                val name = address.featureName ?: ""
                val number = address.subThoroughfare?:""
                val country = address.countryName ?: ""
                val adminArea = address.adminArea ?: ""

                Card(
                    modifier = modifier
                        .wrapContentSize(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xffcedbbf)),
                    elevation = CardDefaults.cardElevation(10.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(15.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "location",
                            Modifier.size(50.dp)
                        )
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(vertical = 12.dp)
                        ) {
                            if (street.isNotEmpty()) Text(
                                text = "Str. $street, Nr. $number",
                                fontSize = 20.sp
                            )
                            if (city.isNotEmpty()) Text(text = city, fontSize = 20.sp)
                            if (adminArea.isNotEmpty() || country.isNotEmpty()) Text(
                                text = "$adminArea $country",
                                fontSize = 20.sp
                            )
                            if(name.isNotEmpty() && name.length>3) Text(text = name, fontSize = 18.sp)
                        }
                    }
                }

            }

            //Details
            if (site.details.isNotEmpty()) {
                Card(
                    modifier = modifier
                        .wrapContentSize(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xffe6e7ca)),
                    elevation = CardDefaults.cardElevation(10.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(15.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "details",
                            Modifier.size(50.dp)
                        )
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(vertical = 12.dp)
                        ) {
                            Text(text = site.details, fontSize = 20.sp)
                        }
                    }
                }
            }

            //Tools on site
            Row(modifier = Modifier.align(Alignment.CenterHorizontally).offset(y=15.dp)) {
                Icon(
                    imageVector = Icons.Default.Handyman,
                    contentDescription = "tools",
                    Modifier.size(50.dp)
                )
                Text(
                    "TOOLS",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Divider(
                color = Color.Gray,
                thickness = 10.dp,
                modifier = Modifier
                    .fillMaxWidth()
            )
            Text(
                "Currently on site: ${tools.size}",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )


            LazyColumn {
                items(tools) { toolId ->
                    fleetViewModel.getToolById(toolId)?.let { tool ->
                        ToolListItem(
                            tool = tool,
                            tools = tools,
                            modifier = modifier,
                            navController = navController,
                            siteDetailViewModel = siteDetailViewModel,
                            fleetViewModel = fleetViewModel
                        )
                    }
                }
            }
        }



        Box(modifier = Modifier.fillMaxSize()) {
            FloatingActionButton(
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.BottomEnd)
                    .size(100.dp, 50.dp), contentColor = Black,
                onClick = { showAddDialog = true }

            ) {
                Text("Add Tools")
            }

            if (showAddDialog) {
                fleetViewModel.retrieveTools()
                AddTools(
                    fleetViewModel = fleetViewModel,
                    siteDetailViewModel = siteDetailViewModel,
                    site = site,
                    onDismiss = { showAddDialog = false },
                    onSubmit = { newTools ->
                        tools = (tools + newTools).toList()
                        siteDetailViewModel.updateTools(site.id, tools)
                        fleetViewModel.retrieveTools()
                        showAddDialog = false
                    }
                )
            }
        }
    }
}

@Composable
fun AddTools(
    fleetViewModel: FleetViewModel,
    siteDetailViewModel: SiteDetailViewModel,
    site: Site,
    onDismiss: () -> Unit,
    onSubmit: (tools: List<Int>) -> Unit
) {
    val fleetListState = fleetViewModel.fleetListState.collectAsState()
    val selectedTools = remember { mutableStateListOf<Int>() }
    val context = LocalContext.current

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surface
        ) {
            LazyColumn {
                items(fleetListState.value.filter { tool -> tool.siteId == null }) { item ->
                    // Row for each item with a checkbox
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                if (selectedTools.contains(item.id)) {
                                    selectedTools.remove(item.id)
                                } else {
                                    selectedTools.add(item.id)
                                }
                            }
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = selectedTools.contains(item.id),
                            onCheckedChange = { isChecked ->
                                if (isChecked) {
                                    selectedTools.add(item.id)
                                } else {
                                    selectedTools.remove(item.id)
                                }
                            }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        ToolListItem2(tool = item) // Assuming ToolListItem is a valid composable
                    }
                }
            }
            Box(modifier = Modifier.fillMaxSize()) {
                FloatingActionButton(
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.BottomEnd), contentColor = Black,
                    onClick = {
                        /*val sugestion = siteDetailViewModel.checkBetterSuggestion(selectedTools, site)
                        if(sugestion.isNotEmpty()){
                            Toast.makeText(context, "There are Tools that are nearer to your place", Toast.LENGTH_SHORT).show()
                        } else {*/
                            onSubmit(selectedTools)
                        /*}*/
                    }
                ) {
                    Icon(imageVector = Icons.Default.Check, contentDescription = null)
                }
            }
        }
    }
}

@Composable
fun ToolListItem(
    tool: Tool,
    tools: List<Int>,
    modifier: Modifier = Modifier,
    navController: NavController,
    siteDetailViewModel: SiteDetailViewModel,
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
        elevation = CardDefaults.cardElevation(10.dp),
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
        }
    }
}

//not clickable for choosing tools
@Composable
fun ToolListItem2(
    tool: Tool,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .padding(10.dp)
            .wrapContentSize(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(10.dp),
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
                modifier = Modifier.size(80.dp) // Adjust size as needed
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 12.dp)
            ) {
                Text(text = tool.name, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text(text = tool.type, fontSize = 18.sp)
            }
        }
    }
}