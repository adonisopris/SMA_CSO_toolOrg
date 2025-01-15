package ro.upt.ac.tooler.presentation

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

class MapActivity : AppCompatActivity() {

    @Composable
    fun MapActivityScreen(mapViewModel: MapViewModel, latLngState: MutableState<LatLng>, navController: NavController) {

        Column {
            Row(
                Modifier.weight(7f)
            ) {
                MapComposable(latLngState, mapViewModel, navController)
            }
                //LocationComposable(latLngState)
        }
    }

    @Composable
    private fun MapComposable(latLngState: MutableState<LatLng>, mapViewModel: MapViewModel, navController: NavController) {
        val mapListState = mapViewModel.siteListState.collectAsState()
        mapViewModel.retrieveTools()
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
            /*Marker(
                state = MarkerState(position = latLngState.value),
                title = "Your location",
            )*/
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

