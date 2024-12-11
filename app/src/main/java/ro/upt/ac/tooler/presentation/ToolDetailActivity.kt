package ro.upt.ac.tooler.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import ro.upt.ac.tooler.data.database.ToolDao
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
                modifier.clip(RoundedCornerShape(16.dp)).height(400.dp)
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
        Text(text = tool.name, fontSize = 30.sp, fontWeight = FontWeight.Bold)
        Text(text = tool.type, fontSize = 20.sp)
        Text(text = "In usage on site: ${toolDetailViewModel.getSiteOfTool(tool)?.name?:"not in use"}", fontSize = 30.sp)
        Text(text = "In usage since: ${tool.startDate?:"not in use"}", fontSize = 20.sp)
        Text(text = "In usage until: ${tool.endDate?:"not in use"}", fontSize = 20.sp)

    }
}