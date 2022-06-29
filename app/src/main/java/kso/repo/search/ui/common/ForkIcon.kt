package kso.repo.search.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kso.repo.search.R

    @Composable
    fun ForkIcon() {
        val image: Painter = painterResource(id = R.drawable.ic_fork)
        Image(
            painter = image,
            colorFilter = ColorFilter.tint(color = Color.Blue),
            contentDescription = stringResource(id = R.string.icon_fork_text),
            modifier = Modifier
                //.clip(CircleShape)
                .width(20.dp)
                .height(20.dp),

            )
    }

