package kso.repo.search.ui.common

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp

@Composable
fun SpannableText(spanText: String) {
    val annotatedString = buildAnnotatedString {
        //append("I have read")
        withStyle(style = SpanStyle(color = Color.Black, fontSize = 13.sp)) {
            append(spanText)
        }
    }

    Text(text = annotatedString)
}