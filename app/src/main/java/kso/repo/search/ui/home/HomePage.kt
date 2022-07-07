package kso.repo.search.ui.home

import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.SubcomposeAsyncImage
import com.google.gson.Gson
import kso.repo.search.R
import kso.repo.search.app.NavPath
import kso.repo.search.app.collectAsStateLifecycleAware
import kso.repo.search.model.Repo
import kso.repo.search.model.Resource
import kso.repo.search.ui.common.SpannableText
import kso.repo.search.viewModel.HomePageViewModel

private const val TAG: String = "HomePage"

@OptIn(ExperimentalAnimationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun HomePage(
    navHostController: NavHostController,
    homePageViewModel: HomePageViewModel,
) {
    val searchText by homePageViewModel.searchText.collectAsStateLifecycleAware(initial = "")
    val repoListNBR by homePageViewModel.repoListNBR.collectAsStateLifecycleAware()

    var isLoading = false
    var errorMessage = ""
    var repoList: List<Repo> = listOf()


    when (repoListNBR) {
        Resource.Loading -> {
            Log.e(TAG, "NWBR Loading")
            isLoading = repoListNBR.isLoading
        }
        Resource.Fail("") -> {
            Log.e(TAG, "NWBR  Fail")
            errorMessage = repoListNBR.errorMessage.orEmpty()
        }
        else -> {
            Log.e(TAG, "NWBR Success")
            repoList = repoListNBR.data.orEmpty()
        }
    }
    RepoSearchBoxView(
        searchText = searchText,
        placeholderText = stringResource(id = R.string.search_repo),
        onSearchBarClick = {
            navHostController.navigate(
                route = NavPath.KeywordSearchPage.route
            )
        },
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
                    val argRepo = repo.toJson()
                    Log.e(TAG, "repo: $argRepo")
                    argRepo?.let {
                        navHostController.navigate(
                            route =
                            "${NavPath.RepoDetailPage.route}?repo=${argRepo}"
                        )
                    }

                }
            }
        }
    }


}

@Composable
fun RepoRow(repo: Repo, onClick: () -> Unit) {

    Row(
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
            error = {
                Icon(
                    //imageVector = Icons.Filled.Search,
                    painter = painterResource(id = R.drawable.ic_error_repo),
                    tint = MaterialTheme.colors.background,
                    contentDescription = stringResource(id = R.string.icn_search_clear_content_description)
                )

            },
            contentDescription = stringResource(R.string.icon_img_text),
            modifier = Modifier
                .clip(CircleShape)
                .width(35.dp)
                .height(35.dp)
        )
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            repo.name?.let { Text(it, fontSize = 13.sp) }
            repo.url?.let { SpannableText(it) }
        }

    }

}


private fun <A> A.toJson(): String? {
    return Gson().toJson(this)
}



