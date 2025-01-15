package ro.upt.ac.tooler.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import ro.upt.ac.tooler.domain.Site
import ro.upt.ac.tooler.domain.Tool
import java.text.SimpleDateFormat

@Composable
fun ToolDetail(
    modifier: Modifier = Modifier,
    toolId: Int,
    toolDetailViewModel: ToolDetailViewModel,
    navController: NavController
) {
    val tool: Tool = toolDetailViewModel.getToolById(toolId)!!
    var showAddDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    Column(
        modifier
            .fillMaxSize()
            .padding(25.dp)
            .verticalScroll(rememberScrollState()),

        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        Box(
            modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = rememberAsyncImagePainter(tool.image),
                contentDescription = tool.name,
                modifier
                    .clip(RoundedCornerShape(16.dp))
                    .heightIn(100.dp, 400.dp)//.height(400.dp)
            )
        }
        //Tool name
        Text(
            text = tool.name,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Divider(
            color = Color.Gray,
            thickness = 10.dp,
            modifier = Modifier
                .fillMaxWidth()
        )
        //On site..
        val site = toolDetailViewModel.getSiteOfTool(tool)
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
                    imageVector = Icons.Default.LocationCity,
                    contentDescription = "site",
                    Modifier.size(50.dp)
                )
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = 12.dp)
                ) {
                    Text(
                        text = "Status",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    if (site != null) {
                        Text(
                            text = "In usage on site:  ${site.name}",
                            fontSize = 20.sp
                        )
                    } else {
                        Text(
                            text = "Not in use",
                            fontSize = 20.sp,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }
                    if (site != null) {
                        val formatter = SimpleDateFormat("dd.MM.yyyy")
                        Text(
                            text = "In usage since: ${formatter.format(tool.startDate!!) ?: "not in use"}",
                            fontSize = 20.sp
                        )
                        Text(
                            text = "In usage until: ${formatter.format(tool.endDate!!) ?: "not in use"}",
                            fontSize = 20.sp
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        if (tool.siteId != null) {
                            Button(
                                onClick = {
                                    toolDetailViewModel.removeTool(tool)
                                    navController.popBackStack()
                                },
                                //modifier = Modifier.width(200.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(
                                        0xffcc1f1f
                                    )
                                )
                            ) {
                                Text("Remove Tool")
                            }
                        }
                        /*if (tool.siteId != null) {*/
                            Button(
                                onClick = {
                                    showAddDialog = true
                                },
                                //modifier = Modifier.width(200.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(
                                        0xFF81C784
                                    )
                                )
                            ) {
                                Text("Move Tool")
                            }
                        //}

                    }
                }
            }


        }

        //Details
        if (tool.details.isNotEmpty() || tool.type.isNotEmpty()) {
            Card(
                modifier = modifier.wrapContentSize(),
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
                        imageVector = Icons.Default.Info,
                        contentDescription = "details",
                        Modifier.size(50.dp)
                    )
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(vertical = 12.dp)
                    ) {
                        Text(text = "Type: ${tool.type}", fontSize = 30.sp, fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.CenterHorizontally))
                        Text(text = tool.details, fontSize = 20.sp)
                    }
                }
            }
        }

        //Address
        if (site != null) {
            val address =
                toolDetailViewModel.getAddressFromLatLng(context, site.latitude, site.longitude)
            if (address != null) {
                val city = address.locality ?: ""
                val street = address.thoroughfare ?: ""
                val name = address.featureName ?: ""
                val number = address.subThoroughfare ?: ""
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
                            if (name.isNotEmpty() && name.length > 3) Text(
                                text = name,
                                fontSize = 18.sp
                            )
                        }
                    }
                }

            }
        }


        if (showAddDialog) {
            toolDetailViewModel.retrieveSites()
            SelectSite(
                toolDetailViewModel = toolDetailViewModel,
                tool = tool,
                onDismiss = { showAddDialog = false },
                onSubmit = { site, t ->
                    toolDetailViewModel.updateTool(site.id, t)
                    showAddDialog = false
                }
            )
        }

    }
}

@Composable
fun SelectSite(
    toolDetailViewModel: ToolDetailViewModel,
    tool: Tool,
    onDismiss: () -> Unit,
    onSubmit: (site: Site, t: Tool) -> Unit
) {
    val sitesListState = toolDetailViewModel.sitesListState.collectAsState()
    var selectedSite: Site? = null
    var better: Tool? = null
    var showDialog by remember { mutableStateOf(false) }
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surface
        ) {
            LazyColumn {
                items(sitesListState.value) { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selectedSite = item
                                better =
                                    toolDetailViewModel.checkBetterSuggestion(tool, selectedSite!!)
                                if (better != null) {
                                    /*Toast.makeText(
                                        context,
                                        "There are Tools that are nearer to your place",
                                        Toast.LENGTH_SHORT
                                    ).show()*/
                                    showDialog = true
                                } else {
                                    onSubmit(selectedSite!!, tool)
                                }
                            }
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        NewSiteListItem(site = item)
                    }
                }
            }
            if (showDialog) {
                BetterToolDialog(
                    better!!,
                    toolDetailViewModel,
                    onAccept = {
                        onSubmit(selectedSite!!, better!!)
                        showDialog = false
                    },
                    onReject = {
                        onSubmit(selectedSite!!, tool)
                        showDialog = false
                    },
                    onDismiss = {
                        showDialog = false
                    }
                )
            }
            /*Box(modifier = Modifier.fillMaxSize()) {
                FloatingActionButton(
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.BottomEnd), contentColor = Black,
                    onClick = { selectedSite?.let { onSubmit(it) } }
                ) {
                    Icon(imageVector = Icons.Default.Check, contentDescription = null)
                }
            }*/
        }
    }
}

@Composable
fun BetterToolDialog(
    tool: Tool,
    toolDetailViewModel: ToolDetailViewModel,
    onAccept: () -> Unit,
    onReject: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "There is a tool that is closer: ${tool.name}",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "The Tool is on site: ${toolDetailViewModel.getSiteOfTool(tool)?.name}",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = onReject,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xffcc1f1f)),
                        modifier = Modifier.width(100.dp)
                    ) {
                        Text("Reject")
                    }
                    Button(
                        onClick = onAccept,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF348710)),
                        modifier = Modifier.width(100.dp)
                    ) {
                        Text("Accept")
                    }
                }
            }
        }
    }
}


@Composable
fun NewSiteListItem(
    site: Site,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .padding(10.dp)
            .wrapContentSize(),
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
        }
    }
}