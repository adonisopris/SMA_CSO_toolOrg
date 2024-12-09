package ro.upt.ac.tooler.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import ro.upt.ac.tooler.domain.Site


@Composable
fun SiteDetail(
    modifier: Modifier = Modifier,
    siteId: Int,
    siteDetailViewModel: SiteDetailViewModel
) {
    val site : Site = siteDetailViewModel.getSiteById(siteId)!!
    Column(
        modifier
            .fillMaxSize()
            .padding(25.dp)
            .verticalScroll(rememberScrollState()),

        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(site.name, fontSize = 30.sp, fontWeight = FontWeight.Bold)
        Text(site.type, fontSize = 30.sp)
        Text("Latitude: ${site.latitude}", fontSize = 20.sp)
        Text("Longitude: ${site.longitude}", fontSize = 20.sp)

    }
}