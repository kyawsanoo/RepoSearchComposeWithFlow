package kso.repo.search.ui.common

import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

@Composable
fun GithubButton(onClick: () -> Unit = {}, text: String, modifier: Modifier) {


    Button(
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primaryVariant),
        shape= MaterialTheme.shapes.medium,
        onClick = onClick) {
        Text(text = text,
            modifier = Modifier.wrapContentWidth(),
            fontSize = 13.sp,
            color = Color.White,
            textAlign = TextAlign.Center)
    }
}