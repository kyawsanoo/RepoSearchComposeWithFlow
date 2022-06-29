package kso.repo.search.ui.common

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun GithubButton(url: String, text: String, modifier: Modifier) {
    val context = LocalContext.current
    val intent = remember { Intent(Intent.ACTION_VIEW, Uri.parse(url)) }

    Button(
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primaryVariant),
        shape= MaterialTheme.shapes.medium,
        onClick = { context.startActivity(intent) }) {
        Text(text = text,
            modifier = Modifier.wrapContentWidth(),
            fontSize = 13.sp,
            color = Color.White,
            textAlign = TextAlign.Center)
    }
}