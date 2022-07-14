package kso.repo.search.ui.home

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kso.repo.search.R
import kso.repo.search.ui.common.ErrorScreen
import kso.repo.search.ui.common.LoadingScreen


@Composable
fun RepoSearchBoxView(
    searchText: String,
    showProgress: Boolean,
    errorMessage: String,
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
                    if (errorMessage.isNotEmpty()) {
                        ErrorScreen(
                            errorMessage = errorMessage,
                            onRetryClick = onRetryClick
                        )
                    } else {
                        if (matchesFound) {
                            results()
                        } else {
                            if (searchText.isNotEmpty()) {
                                NoSearchResults()
                            }

                        }
                    }
                }

            }

        }


}



@Composable
fun NoSearchResults() {

    Column(
        modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center,
        horizontalAlignment = CenterHorizontally
    ) {
        Text(stringResource(id = R.string.no_matched_repo_found))
    }
}

