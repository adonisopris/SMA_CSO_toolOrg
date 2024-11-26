package ro.upt.ac.tooler.presentation

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ro.upt.ac.tooler.data.database.RoomDatabase
import ro.upt.ac.tooler.data.database.SiteDbStore
import ro.upt.ac.tooler.domain.Site


@Composable
fun SiteScreen(viewModel: SitesViewModel) {
    val sitesListState = viewModel.sitesListState.collectAsState()
    Surface(color = Color.White) {
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn {
                items(sitesListState.value) { site -> SiteListItem(site) }
            }
        }
    }
}

@Composable
fun SiteListItem(site: Site, modifier: Modifier = Modifier) {
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
            Column(modifier.padding(12.dp)) {
                Text(text = site.name, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text(text = site.type, fontSize = 18.sp)

            }
        }
    }
}
