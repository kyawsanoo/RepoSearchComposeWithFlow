package kso.repo.search.ui.home

import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import kso.repo.search.app.collectAsStateLifecycleAware
import kso.repo.search.model.Repo
import kso.repo.search.model.Resource
import kso.repo.search.ui.common.SpannableText
import kso.repo.search.viewModel.HomePageViewModel

const val TAG: String = "HomePage"

@OptIn(ExperimentalAnimationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun HomePage(
    navHostController: NavHostController,
    homePageViewModel: HomePageViewModel,
) {
    val repoSearchModelState by homePageViewModel.repoListResponseResource.collectAsStateLifecycleAware()
    val searchText by homePageViewModel.searchText.collectAsStateLifecycleAware(initial = "")

    var isLoading = false
    var errorMessage = ""
    var repoList: List<Repo> = listOf()

    when(repoSearchModelState){
        is Resource.Loading -> isLoading = repoSearchModelState.isLoading
        is Resource.Fail -> repoSearchModelState.errorMessage?.let {
            errorMessage = repoSearchModelState.errorMessage.orEmpty()
        }
        else -> {
            repoList = repoSearchModelState.data.orEmpty()
        }

    }

    RepoSearchBoxView(
        searchText = searchText,
        placeholderText = stringResource(id = R.string.search_repo),
        onSearchTextChanged = {
            homePageViewModel.onSearchTextChanged(it)
        },
        onClearClick = {
            homePageViewModel.onSearchBoxClear()
        },
        onSearchBarClick = {navHostController.navigate(route = NavPath.SearchBoxPage.route) },
        showProgress = isLoading,
        errorMessage = errorMessage,
        onRetryClick = {
            homePageViewModel.retry()
        },
        matchesFound = repoList.isNotEmpty()
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(5.dp)
        ) {

            items(items = repoList) { repo ->
                RepoRow(repo = repo) {
                    val argRepoName = repo.name
                    Log.e(TAG, "Route: ${NavPath.HomePage.route}?repoName=$argRepoName")
                    navHostController.navigate(route = "${NavPath.RepoDetail.route}?login=${repo.owner?.login}&repoName=${repo.name}")
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






