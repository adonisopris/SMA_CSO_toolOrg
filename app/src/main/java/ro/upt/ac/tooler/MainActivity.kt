package ro.upt.ac.tooler

import android.Manifest.permission
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.LatLng
import ro.upt.ac.tooler.data.database.RoomDatabase
import ro.upt.ac.tooler.data.database.SiteDbStore
import ro.upt.ac.tooler.data.database.ToolDbStore
import ro.upt.ac.tooler.domain.Tool
import ro.upt.ac.tooler.presentation.CameraScreen
import ro.upt.ac.tooler.presentation.FleetScreen
import ro.upt.ac.tooler.presentation.FleetViewModel
import ro.upt.ac.tooler.presentation.SiteDetail
import ro.upt.ac.tooler.presentation.SiteDetailViewModel

import ro.upt.ac.tooler.presentation.SitesViewModel
import ro.upt.ac.tooler.presentation.ToolDetail
import ro.upt.ac.tooler.presentation.ToolDetailViewModel
import ro.upt.ac.tooler.location.LocationHandler
import ro.upt.ac.tooler.presentation.MapActivity
import ro.upt.ac.tooler.presentation.MapViewModel
import ro.upt.ac.tooler.presentation.SiteActivity

class MainActivity : ComponentActivity(){

    private val latLngState = mutableStateOf(LatLng(45.9442858, 25.0094303))
    private lateinit var locationHandler: LocationHandler
    private var locationCallback: LocationCallback? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val fleetViewModel = FleetViewModel(ToolDbStore(RoomDatabase.getDb(this)))
        val sitesViewModel = SitesViewModel(SiteDbStore(RoomDatabase.getDb(this)))
        val mapViewModel = MapViewModel(SiteDbStore(RoomDatabase.getDb(this)))
        val toolDetailViewModel = ToolDetailViewModel(ToolDbStore(RoomDatabase.getDb(this)))
        val siteDetailViewModel = SiteDetailViewModel(SiteDbStore(RoomDatabase.getDb(this)), ToolDbStore(RoomDatabase.getDb(this)) )
        this.locationHandler = LocationHandler(this)
        setContent {
            MainScreen(fleetViewModel, sitesViewModel, toolDetailViewModel, siteDetailViewModel,mapViewModel, latLngState)
        }
    }
    override fun onResume() {
        super.onResume()

        if (!isLocationPermissionGranted) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_ID
            )
        }

        setupLocation()
    }

    override fun onPause() {
        super.onPause()

        if (locationCallback != null) {
            locationHandler.unregisterLocationListener(locationCallback!!)
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_ID -> {
                checkAndShowToast(grantResults, R.string.toast_location_permission)
            }

        }
    }
    private fun checkAndShowToast(grantResults: IntArray, @StringRes toastResId: Int) {
        if (grantResults.isNotEmpty() && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, toastResId, Toast.LENGTH_SHORT)
                .show()
        }
    }
    private fun setupLocation() {
        this.locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let {
                    latLngState.value = LatLng(it.latitude, it.longitude)
                }
            }
        }
        locationHandler.registerLocationListener(locationCallback!!)
    }

    private val isLocationPermissionGranted: Boolean
        get() = ContextCompat.checkSelfPermission(
            this,
            permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_ID = 111
    }

}

@Composable
fun MainScreen(fleetViewModel: FleetViewModel, sitesViewModel: SitesViewModel, toolDetailViewModel: ToolDetailViewModel, siteDetailViewModel: SiteDetailViewModel,mapViewModel: MapViewModel, latLngState: MutableState<LatLng>) {
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
            composable("map") {  MapActivity().MapActivityScreen(mapViewModel, latLngState, navController) }
            composable("sites") { SiteActivity().SiteScreen(sitesViewModel, navController, latLngState) }
            composable("fleet") { FleetScreen(fleetViewModel, navController) }
            composable(route = "ToolDetail/{toolId}", arguments = listOf(navArgument("toolId"){type = NavType.IntType})){
                backStackEntry ->
                val toolId = backStackEntry.arguments?.getInt("toolId") ?: 0
                ToolDetail(toolId = toolId, toolDetailViewModel = toolDetailViewModel, navController = navController)
            }
            composable(route = "SiteDetail/{siteId}", arguments = listOf(navArgument("siteId"){type = NavType.IntType})){
                    backStackEntry ->
                val siteId = backStackEntry.arguments?.getInt("siteId") ?: 0
                SiteDetail(siteId = siteId, siteDetailViewModel = siteDetailViewModel, fleetViewModel = fleetViewModel, navController = navController)
            }

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
