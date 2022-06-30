package kso.repo.search.ui.repoSearchBox

import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.SubcomposeAsyncImage
import kso.repo.search.R
import kso.repo.search.app.NavPath
import kso.repo.search.model.Repo
import kso.repo.search.ui.common.SpannableText
import kso.repo.search.ui.state.RepoSearchBoxViewModelState
import kso.repo.search.viewModel.RepoSearchBoxViewModel


private const val TAG="RepoSearchBoxPage"

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun RepoSearchBoxPage(navHostController: NavHostController, repoSearchViewModel: RepoSearchBoxViewModel) {

    //normal collect
    val repoSearchModelState by repoSearchViewModel.repoSearchModelState.collectAsState(initial = RepoSearchBoxViewModelState.Empty)

    //normal effect
    LaunchedEffect(Unit) {

        Log.e(TAG, "LaunchedEffect")

    }

    DisposableEffect(Unit) {
        onDispose {
            Log.e(TAG, "onDispose")
        }
    }

    Log.e(TAG, "searchText: ${repoSearchModelState.searchText}")

    RepoSearchBoxView(
        searchText = repoSearchModelState.searchText,
        placeholderText = "Search Repo",
        onSearchTextChanged = { repoSearchViewModel.onSearchTextChanged(it) },
        onClearClick = { repoSearchViewModel.onSearchBoxClear() },
        onNavigateBack = {
            Log.e(TAG, "onNavigateBack")
            navHostController.popBackStack()
        },
        showProgress = repoSearchModelState.showProgressBar,
        matchesFound = repoSearchModelState.users.isNotEmpty()
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(5.dp)
        ) {

            items(items = repoSearchModelState.users) { repo ->
                RepoRow(repo = repo) {
                    val argRepoName = repo.name
                    Log.e(TAG, "Route: ${NavPath.HomePage.route}?repoName=$argRepoName")
                    navHostController.navigate(
                        route = "${NavPath.HomePage.route}?repoName=${argRepoName}"
                    )
                }
            }
        }
    }


}


@Composable
fun RepoRow(repo: Repo, onClick: () -> Unit) {

        Row(
            //horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clickable { onClick() }
        ) {

            SubcomposeAsyncImage(
                model = repo.owner?.avatarUrl,
                loading = {
                    CircularProgressIndicator()
                },
                contentDescription = stringResource(R.string.icon_img_text),
                modifier = Modifier
                    .clip(CircleShape)
                    .width(35.dp)
                    .height(35.dp)
            )
            Column(
                //verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(repo.name, fontSize = 13.sp)
                SpannableText(repo.url)
            }

        }

}






