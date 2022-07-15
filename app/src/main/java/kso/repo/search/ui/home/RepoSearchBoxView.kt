package kso.repo.search.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kso.repo.search.R
import kso.repo.search.ui.common.ErrorScreen
import kso.repo.search.ui.common.LoadingScreen


@Composable
fun RepoSearchBoxView(
    searchText: String,
    showProgress: Boolean,
    apiErrorMessage: String,
    onRetryClick: () -> Unit = {},
    modifier: Modifier,
    matchesFound: Boolean,

    results: @Composable () -> Unit = {}
) {
    Box{
            Column(
                modifier = modifier
                    .fillMaxSize()
            ) {

                if (showProgress) {
                    LoadingScreen()
                }
                else {
                    if (apiErrorMessage.isNotEmpty()) {
                        ErrorScreen(
                            errorMessage = apiErrorMessage,
                            onRetryClick = onRetryClick
                        )
                    } else {
                        if (matchesFound) {
                            results()
                        } else {
                            NoSearchResults(onRetryClick = onRetryClick)
                        }
                    }
                }

            }

        }


}



@Composable
fun NoSearchResults(onRetryClick: () -> Unit = {},) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clip(RoundedCornerShape(16.dp))
            //.background(Color.LightGray)

    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(10.dp),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = CenterHorizontally
        ) {
            Text(stringResource(id = R.string.no_matched_repo_found), fontSize = 14.sp)
            ErrorScreen(
                errorMessage = stringResource(id = R.string.need_connection),
                onRetryClick = onRetryClick
            )


        }
    }
}

