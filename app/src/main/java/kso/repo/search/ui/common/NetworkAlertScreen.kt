package kso.repo.search.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kso.repo.search.R


@Composable
fun NetworkAlertScreen(connectionMessage: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(10.dp)
            .background(color = Color.Yellow)
    ) {
        Row(
            modifier = Modifier.align(Alignment.TopCenter),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround

        ) {
            Text(text = stringResource(id = R.string.network_connection_status), fontSize = 12.sp, modifier = Modifier.padding(10.dp))

            Text(text = connectionMessage, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colors.primaryVariant, fontSize = 12.sp, modifier = Modifier.padding(10.dp))

        }
    }
}
