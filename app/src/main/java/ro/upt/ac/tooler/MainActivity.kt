package ro.upt.ac.tooler

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ro.upt.ac.tooler.data.database.RoomDatabase
import ro.upt.ac.tooler.data.database.SiteDbStore
import ro.upt.ac.tooler.data.database.ToolDbStore
import ro.upt.ac.tooler.presentation.FleetScreen
import ro.upt.ac.tooler.presentation.FleetViewModel
import ro.upt.ac.tooler.presentation.MapScreen
import ro.upt.ac.tooler.presentation.SiteScreen
import ro.upt.ac.tooler.presentation.SitesViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val fleetViewModel = FleetViewModel(ToolDbStore(RoomDatabase.getDb(this)))
        val sitesViewModel = SitesViewModel(SiteDbStore(RoomDatabase.getDb(this)))
        setContent {
            MainScreen(fleetViewModel, sitesViewModel)
        }
    }
}

@Composable
fun MainScreen(fleetViewModel: FleetViewModel, sitesViewModel: SitesViewModel) {
    val navController = rememberNavController()

    Scaffold(
        topBar = { CustomAppBar(navController) }, // Custom top bar
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "map",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("map") { MapScreen() }
            composable("sites") { SiteScreen(sitesViewModel) }
            composable("fleet") { FleetScreen(fleetViewModel) }
        }
    }
}

@Composable
fun CustomAppBar(navController: NavController) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    val title = when (currentRoute) {
        "map" -> "Map Screen"
        "sites" -> "Sites Screen"
        "fleet" -> "Fleet Screen"
        else -> "Tooler App"
    }

    CustomBar(
        title = title,
        backgroundColor = Color(0xFF348710 ),
        textColor = Color.White
    )
}

@Composable
fun CustomBar(title: String, backgroundColor: Color, textColor: Color) {
    androidx.compose.material3.Surface(
        color = backgroundColor,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        androidx.compose.foundation.layout.Box(
            modifier = Modifier.padding(16.dp),
            contentAlignment = androidx.compose.ui.Alignment.CenterStart
        ) {
            Text(
                text = title,
                color = textColor,
                fontSize = 20.sp,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
            )
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    NavigationBar {
        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate("map") },
            label = { Text("Map") },
            icon = { Icon(Icons.Default.Place, contentDescription = "Map") }
        )
        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate("sites") },
            label = { Text("Sites") },
            icon = { Icon(Icons.Default.Home, contentDescription = "Sites") }
        )
        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate("fleet") },
            label = { Text("Fleet") },
            icon = { Icon(Icons.Default.Build, contentDescription = "Fleet") }
        )
    }
}
