package ro.upt.ac.tooler.presentation

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import ro.upt.ac.tooler.domain.Site
import ro.upt.ac.tooler.domain.Tool
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TextField
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import kotlinx.coroutines.launch


@SuppressLint("MutableCollectionMutableState")
@Composable
fun SiteDetail(
    modifier: Modifier = Modifier,
    siteId: Int,
    siteDetailViewModel: SiteDetailViewModel,
    fleetViewModel: FleetViewModel,
) {
    val site : Site = siteDetailViewModel.getSiteById(siteId)!!
    var showAddDialog by remember { mutableStateOf(false) }
    var tools by remember { mutableStateOf(site.tools ?: mutableListOf()) }
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier
                    .fillMaxSize()
                    .padding(25.dp)
                    .verticalScroll(rememberScrollState())
                    .height(1000.dp)
                ,

                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Text(site.name, fontSize = 30.sp, fontWeight = FontWeight.Bold)
                Text(site.type, fontSize = 30.sp)
                Text("Latitude: ${site.latitude}", fontSize = 20.sp)
                Text("Longitude: ${site.longitude}", fontSize = 20.sp)
                Text("Tools:", fontSize = 30.sp, fontWeight = FontWeight.Bold)
                    LazyColumn {
                        items(tools) { toolId ->
                            fleetViewModel.getToolById(toolId)?.let { tool ->
                                ToolListItem(tool = tool, modifier = modifier)
                            }
                        }
                    }
            }
        }


    Box(modifier = Modifier.fillMaxSize()) {
        FloatingActionButton(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.BottomEnd)
                .size(100.dp,50.dp), contentColor = Black,
            onClick = { showAddDialog = true }

        ) {
            Text("Add Tools")
        }

        if (showAddDialog) {
            AddTools(
                fleetViewModel = fleetViewModel,
                onDismiss = { showAddDialog = false },
                onSubmit = { newTools ->
                    tools = (tools + newTools).toList()
                    siteDetailViewModel.updateTools(site.id, tools )
                    showAddDialog = false
                }
            )
        }
    }
}

@Composable
fun AddTools(
    fleetViewModel: FleetViewModel,
    onDismiss: () -> Unit,
    onSubmit: (tools: List<Int>) -> Unit
) {
    val fleetListState = fleetViewModel.fleetListState.collectAsState()
    val selectedTools = remember { mutableStateListOf<Int>() }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surface
        ) {
            LazyColumn {
                items(fleetListState.value) { item ->
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
                        ToolListItem(tool = item) // Assuming ToolListItem is a valid composable
                    }
                }
            }
            Box(modifier = Modifier.fillMaxSize()) {
                FloatingActionButton(
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.BottomEnd), contentColor = Black,
                    onClick = { onSubmit(selectedTools) }
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