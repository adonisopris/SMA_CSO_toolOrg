package ro.upt.ac.tooler.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.google.android.gms.maps.model.LatLng
import ro.upt.ac.tooler.data.database.ToolDao
import ro.upt.ac.tooler.domain.Site
import ro.upt.ac.tooler.domain.Tool

@Composable
fun ToolDetail(
    modifier: Modifier = Modifier,
    toolId: Int,
    toolDetailViewModel: ToolDetailViewModel,
    navController: NavController
) {
    val tool : Tool = toolDetailViewModel.getToolById(toolId)!!
    Column(
        modifier
            .fillMaxSize()
            .padding(25.dp)
            .verticalScroll(rememberScrollState()),

        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        Box(modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ){
            Image(painter = rememberAsyncImagePainter(tool.image),
                contentDescription = tool.name,
                modifier.clip(RoundedCornerShape(16.dp)).heightIn(100.dp, 400.dp)//.height(400.dp)
            )
        }
        if(tool.siteId != null) {
            Button(
                onClick = {
                    toolDetailViewModel.removeTool(tool)
                    navController.popBackStack()
                },
                modifier = Modifier.width(400.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xffcc1f1f))
            ) {
                Text("Remove Tool from site")
            }
        }
        if(tool.siteId != null) {
            Button(
                onClick = {
                    //toolDetailViewModel.removeTool(tool)
                    navController.popBackStack()
                },
                modifier = Modifier.width(400.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF81C784))
            ) {
                Text("Move Tool")
            }
        }
        Text(text = tool.name, fontSize = 30.sp, fontWeight = FontWeight.Bold)
        Text(text = tool.type, fontSize = 20.sp)
        Text(text = "In usage on site: ${toolDetailViewModel.getSiteOfTool(tool)?.name?:"not in use"}", fontSize = 30.sp)
        Text(text = "In usage since: ${tool.startDate?:"not in use"}", fontSize = 20.sp)
        Text(text = "In usage until: ${tool.endDate?:"not in use"}", fontSize = 20.sp)

    }
}

/*@Composable
fun SelectSite(viewModel: SitesViewModel, navController: NavController, latLngState: MutableState<LatLng>) {
    val siteListState = viewModel.sitesListState.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

    Surface(color = Color.White) {

        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn {
                items(siteListState.value) { site ->
                    SiteListItem(
                        site = site,
                        navController = navController,
                        siteViewModel = viewModel
                    )
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
}*/