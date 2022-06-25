package kso.repo.search.ui.common

import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun LoadingScreen() {
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(10.dp)) {
        Column(
            modifier = Modifier.align(Alignment.TopCenter),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround

        ) {
            Text(text = "Loading", fontSize = 12.sp, modifier = Modifier.padding(10.dp))

            CircularProgressIndicator(
                modifier = Modifier.size(30.dp),
                color = MaterialTheme.colors.primary,
                strokeWidth = 3.dp
            )
        }
    }
}

