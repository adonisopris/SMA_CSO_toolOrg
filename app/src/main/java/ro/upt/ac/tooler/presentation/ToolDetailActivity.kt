package ro.upt.ac.tooler.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
    toolDetailViewModel: ToolDetailViewModel
) {
    val tool : Tool = toolDetailViewModel.getToolById(toolId)!!
    Column(
        modifier
            .fillMaxSize()
            .padding(25.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        Box(modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ){
            Image(painter = rememberAsyncImagePainter(tool.image),
                contentDescription = tool.name,
                modifier.clip(RoundedCornerShape(16.dp))
            )
        }
        Text(text = tool.type, fontSize = 30.sp, fontWeight = FontWeight.Bold)

    }
}