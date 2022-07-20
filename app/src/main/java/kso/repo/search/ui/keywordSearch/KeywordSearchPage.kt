package kso.repo.search.ui.keywordSearch

import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kso.repo.search.R
import kso.repo.search.app.NavPath
import kso.repo.search.app.collectAsStateLifecycleAware
import kso.repo.search.model.Keyword
import kso.repo.search.model.Resource
import kso.repo.search.viewModel.KeywordSearchPageViewModel


private const val TAG= "UserSearchPage"

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun KeywordSearchPage(navHostController: NavHostController, keywordSearchPageViewModel: KeywordSearchPageViewModel) {

    val searchText by keywordSearchPageViewModel.searchText.collectAsStateLifecycleAware(initial = "")
    val keywordListNBR by keywordSearchPageViewModel.keywordListNBR.collectAsStateLifecycleAware(Resource.Start)

    var isLoading = false
    var errorMessage = ""
    var keywordList: List<Keyword> = listOf()


    when (keywordListNBR) {
        Resource.Loading -> {
            Log.e(TAG, "keywordListNBR Loading")
            isLoading = keywordListNBR.isLoading
        }
        Resource.Fail("") -> {
            Log.e(TAG, "keywordListNBR  Fail")
            errorMessage = keywordListNBR.errorMessage.orEmpty()
        }
        else -> {
            Log.e(TAG, "keywordListNBR Success")
            keywordList = keywordListNBR.data.orEmpty()
            when(keywordListNBR.data.isNullOrEmpty()){
                true -> Log.e(TAG, "keyword list : NullOrEmpty")
                else -> {
                    Log.e(TAG, "first keyword : ${keywordList.first().name}")
                }

            }

        }
    }

    Log.e(TAG, "searchText: $searchText")

    KeywordSearchBoxView(
        searchText = searchText,
        placeholderText = stringResource(R.string.search_keyword),
        onSearchTextChanged = { keywordSearchPageViewModel.onSearchTextChanged(it) },
        onClearClick = { keywordSearchPageViewModel.onSearchBoxClear() },
        onNavigateBack = {
            Log.e(TAG, "onNavigateBack")
            navHostController.popBackStack()
        },
        showProgress = isLoading,
        errorMessage = errorMessage,
        matchesFound = keywordList.isNotEmpty(),
        onRetryClick = {
            keywordSearchPageViewModel.retry()
        },
        onKeywordClick = {
            navHostController.navigate(
                route = "${NavPath.RepoListPage.route}?repoName=$searchText"
            )
        }
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(5.dp)
        ) {

            items(items = keywordList) { keyword ->
                KeywordRow(keyword = keyword) {
                    val argRepoName = keyword.name
                    Log.e(TAG, "Route: ${NavPath.RepoListPage.route}?repoName=$argRepoName")
                    navHostController.navigate(
                        route = "${NavPath.RepoListPage.route}?repoName=${argRepoName}"
                    )
                }
            }
        }
    }


}


@Composable
fun KeywordRow(keyword: Keyword, onClick: () -> Unit) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clickable { onClick() }
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                tint = Color.LightGray,
                modifier = Modifier
                    .clip(CircleShape)
                    .width(35.dp)
                    .height(35.dp),
                contentDescription = stringResource(id = R.string.icn_search_clear_content_description)
            )
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                keyword.name?.let { Text(it, fontSize = 13.sp) }
            }

        }

}






