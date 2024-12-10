package ro.upt.ac.tooler.presentation

import android.Manifest.permission
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddLocation
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.google.android.gms.location.DetectedActivity
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import ro.upt.ac.tooler.R
import ro.upt.ac.tooler.domain.Site
import ro.upt.ac.tooler.location.LocationHandler


/*@Composable
fun MapScreen() {
    Scaffold(
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "This is the Map Screen")
            }
        }
    )
}*/
class MapActivity : AppCompatActivity() {

    @Composable
    fun MapActivityScreen(mapViewModel: MapViewModel, latLngState: MutableState<LatLng>, navController: NavController) {

        Column {
            Row(
                Modifier.weight(7f)
            ) {
                MapComposable(latLngState, mapViewModel, navController)
            }
            Row(
                Modifier
                    .weight(1f)
                    .align(Alignment.CenterHorizontally)
                    .padding(6.dp)
            ) {
                LocationComposable(latLngState)
            }
        }
    }

    @Composable
    private fun MapComposable(latLngState: MutableState<LatLng>, mapViewModel: MapViewModel, navController: NavController) {
        val mapListState = mapViewModel.siteListState.collectAsState()
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(latLngState.value, 10f)
        }

        LaunchedEffect(latLngState.value) {
            cameraPositionState.animate(
                update = CameraUpdateFactory.newCameraPosition(
                    CameraPosition(latLngState.value, 8f, 0f, 0f)
                ),
                durationMs = 1000
            )
        }
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        ) {
            Marker(
                state = MarkerState(position = latLngState.value),
                title = "Your location",
            )
            mapListState.value.forEach{ site ->
                Marker(
                    state = MarkerState(position = LatLng(site.latitude,site.longitude)),
                    title = site.name,
                    onClick = {
                        navController.navigate(route = "SiteDetail/${site.id}")
                        false
                    }
                 )
            }
        }
    }

    @Composable
    private fun LocationComposable(latLngState: MutableState<LatLng>) {
        Column {
            Text(
                text = "Your location coordinates are:",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(text = "Lat = ${latLngState.value.latitude}, Lng = ${latLngState.value.longitude}")
        }
    }

}

