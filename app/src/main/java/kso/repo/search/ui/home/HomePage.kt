package kso.repo.search.ui.home

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.SubcomposeAsyncImage
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.google.gson.Gson
import kso.repo.search.R
import kso.repo.search.app.NavPath
import kso.repo.search.app.NetworkStatus
import kso.repo.search.app.collectAsStateLifecycleAware
import kso.repo.search.model.Repo
import kso.repo.search.model.Resource
import kso.repo.search.ui.common.NetworkAlertScreen
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
    val isConnected by homePageViewModel.isConnected.collectAsStateLifecycleAware(initial = NetworkStatus.Available)
    val isRefreshing by homePageViewModel.isRefreshing.collectAsStateLifecycleAware()
    val isShowToast by homePageViewModel.showToast.collectAsStateLifecycleAware()
    var isLoading = false
    var errorMessage = ""
    var repoList: List<Repo> = listOf()
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    if(isShowToast){
        Toast.makeText(
            context,
            "Need connection",
            Toast.LENGTH_SHORT
        ).show()
        homePageViewModel.showToastCollected()
    }

    when (repoListNBR) {
        Resource.Loading -> {
            Log.e(TAG, "RepoSearch Fetch Loading")
            isLoading = repoListNBR.isLoading
        }
        Resource.Fail("") -> {
            Log.e(TAG, "RepoSearch Fetch Fail")
            errorMessage = repoListNBR.errorMessage.orEmpty()
        }
        else -> {
            Log.e(TAG, "RepoSearch Fetch Success")
            repoList = repoListNBR.data.orEmpty()
            homePageViewModel.onDoneCollectResource()
        }
    }

    Scaffold(topBar = {
        AppBarWithSearchBox(
            searchText,
            stringResource(id = R.string.search_repo),
            onSearchBarClick = {navHostController.navigate(
                route = NavPath.KeywordSearchPage.route,
            )},
            onSearchTextChanged = { homePageViewModel.onSearchTextChanged(it) },
            onClearClick = { homePageViewModel.onSearchBoxClear() },
            onKeyboardSearchTextChanged = {
                keyboardController?.hide()
                homePageViewModel.onKeyboardSearchClick(searchText)
            }
            )
    }) {
            paddingValues ->
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            NetworkAlertScreen(

                errorMessage = when (isConnected) {
                    true -> {
                        stringResource(
                            id = R.string.connected
                        )
                    }
                    else -> {
                        stringResource(
                            id = R.string.not_connected
                        )
                    }
                }

            )
            RepoSearchBoxView(
                searchText = searchText,
                showProgress = isLoading,
                apiErrorMessage = errorMessage,
                onRetryClick = {
                    homePageViewModel.retry()
                },
                modifier = Modifier.padding(paddingValues),
                matchesFound = repoList.isNotEmpty()
            ) {

                SwipeRefresh(
                    state = rememberSwipeRefreshState(isRefreshing),
                    onRefresh = {
                        homePageViewModel.refresh()
                    }
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
        }

    }


}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AppBarWithSearchBox(
    searchText: String,
    placeholderText: String = "",
    onSearchBarClick: () -> Unit = {},
    onSearchTextChanged: (String) -> Unit = {},
    onKeyboardSearchTextChanged:
    (KeyboardActionScope.() -> Unit)? =  {},
    onClearClick: () -> Unit = {},
) {
    var showClearButton by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
        TopAppBar(title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    stringResource(id = R.string.search),
                    fontSize = 13.sp, color = Color.White
                )

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize(align = Alignment.CenterStart)
                        .padding(vertical = 4.dp, horizontal = 8.dp)
                        //.clickable(onClick = {  } )
                        .onFocusChanged {
                           focusState ->
                                showClearButton = (focusState.isFocused)
                        }
                        .focusRequester(focusRequester),

                    textStyle = TextStyle(
                        fontSize = 13.sp,
                        color = MaterialTheme.colors.primary,
                        fontWeight = FontWeight.Normal,
                    ),
                    value = searchText,
                    onValueChange = onSearchTextChanged,
                    placeholder = {
                        Text(text = placeholderText, color = Color.Gray, fontSize = 13.sp)
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = MaterialTheme.colors.primaryVariant,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        backgroundColor = Color.White,
                        cursorColor = MaterialTheme.colors.primaryVariant
                    ),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            tint = MaterialTheme.colors.primaryVariant,
                            contentDescription = stringResource(id = R.string.icn_search_clear_content_description)
                        )
                    },
                    trailingIcon = {
                        AnimatedVisibility(
                            visible = showClearButton,
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            IconButton(onClick = { onClearClick() }) {
                                Icon(
                                    imageVector = Icons.Filled.Close,
                                    tint = MaterialTheme.colors.primaryVariant,
                                    contentDescription = stringResource(id = R.string.icn_search_clear_content_description)
                                )
                            }

                        }
                    },
                    maxLines = 1,
                    singleLine = true,
                    shape = MaterialTheme.shapes.small,
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(
                            onSearch =  onKeyboardSearchTextChanged
                    ),


                )



            }

        },

        actions = {
            IconButton(onClick = onSearchBarClick) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    tint = Color.White,
                    contentDescription = stringResource(id = R.string.add)
                )
            }

        }
    )


    /*LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }*/
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
            model = repo.owner.avatarUrl,
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



