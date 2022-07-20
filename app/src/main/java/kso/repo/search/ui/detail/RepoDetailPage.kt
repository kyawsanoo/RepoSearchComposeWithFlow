package kso.repo.search.ui.detail

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.google.gson.Gson
import kso.repo.search.R
import kso.repo.search.app.NavPath
import kso.repo.search.app.collectAsStateLifecycleAware
import kso.repo.search.model.Repo
import kso.repo.search.model.Resource
import kso.repo.search.ui.common.ErrorScreen
import kso.repo.search.ui.common.ForkIcon
import kso.repo.search.ui.common.GithubButton
import kso.repo.search.ui.common.LoadingScreen
import kso.repo.search.viewModel.RepoDetailPageViewModel

private const val TAG: String = "RepoDetailPage"

@Composable
fun RepoDetailPage(
    navHostController: NavHostController,
    repoDetailViewModel: RepoDetailPageViewModel,
) {

    val repoResource by repoDetailViewModel.repoResource.collectAsState(initial = Resource.Loading)
    val isRefreshing by repoDetailViewModel.isRefreshing.collectAsStateLifecycleAware(false)
    val context = LocalContext.current
    var isLoading = false
    var errorMessage = ""
    var isSuccess = false
    var repo = Repo()

    when (repoResource) {
        Resource.Loading -> {
            Log.e(TAG, "Repo Loading")
            isLoading = repoResource.isLoading
        }
        Resource.Fail("") -> {
            Log.e(TAG, "Repo Fetch Fail")
            errorMessage = repoResource.errorMessage.orEmpty()
            repoDetailViewModel.onDoneCollectResource()
        }
        else -> {
            Log.e(TAG, "Repo Fetch Success")
            isSuccess = true
            repo = repoResource.data!!
            repoDetailViewModel.onDoneCollectResource()
        }
    }

    Scaffold(topBar = {
        TopAppBar(
            title = {
                TitleText(
                    textValue = "RepoDetail",
                )
            },
            modifier = Modifier.fillMaxWidth(),
            navigationIcon = {
                IconButton(onClick = { navHostController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        modifier = Modifier,
                        contentDescription = stringResource(id = R.string.icn_user_detail_app_bar_back_button)
                    )
                }
            }
        )
    }) { paddingValues ->
        repo.let {
            RepoDetailView(
                isLoading = isLoading,
                errorMessage = errorMessage,
                onRetryClick = {
                    repoDetailViewModel.retry()
                },
                modifier = Modifier.padding(paddingValues),
                isSuccess = isSuccess

            ) {
                SwipeRefresh(
                    state = rememberSwipeRefreshState(isRefreshing),
                    onRefresh = { repoDetailViewModel.refresh() }
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Card(
                            modifier = Modifier
                                .padding(10.dp)
                                .fillMaxWidth()
                                .wrapContentHeight(),
                            shape = RoundedCornerShape(10.dp),
                            elevation = 5.dp,
                            backgroundColor = MaterialTheme.colors.surface
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                Spacer(modifier = Modifier.height(8.dp))
                                it.name?.let { it1 ->
                                    Text(
                                        it1,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        style = typography.h4,
                                        textAlign = TextAlign.Center
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Divider(color = Color.LightGray, thickness = 2.dp)
                                Spacer(modifier = Modifier.height(8.dp))

                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .wrapContentWidth()

                                ) {
                                    Row(
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            Icons.Filled.Star,
                                            modifier = Modifier
                                                .width(20.dp)
                                                .height(20.dp),
                                            tint = Color.Blue,
                                            contentDescription = stringResource(id = R.string.icon_star_text)
                                        )
                                        Text("${it.stargazersCount}", fontSize = 13.sp)
                                    }

                                    Row(
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        ForkIcon()
                                        Text("${it.forksCount}", fontSize = 13.sp)
                                    }

                                    Row(
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        WatcherIcon()
                                        Text("${it.watchers}", fontSize = 13.sp)
                                    }
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        it.language?.let {
                                            Text(
                                                it,

                                                modifier = Modifier
                                                    .wrapContentWidth()
                                                    .wrapContentHeight(),
                                                fontSize = 13.sp,
                                                textAlign = TextAlign.Center,
                                                color = Color.Blue
                                            )
                                        }

                                    }

                                }
                                Spacer(modifier = Modifier.height(20.dp))
                                repo.let {
                                    it.description?.let { it1 ->
                                        Text(
                                            it1,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Normal,
                                            style = typography.h4,
                                            modifier = Modifier.padding(horizontal = 10.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(5.dp))
                                Column(
                                    modifier = Modifier.align(Alignment.End),

                                    ) {
                                    TextButton(
                                        onClick = {
                                            val argUser = repo.owner.toJson()
                                            Log.e(TAG, "user: $argUser")
                                            argUser?.let {
                                                navHostController.navigate(
                                                    route =
                                                    "${NavPath.UserDetailPage.route}?user=${argUser}"
                                                )
                                            }
                                        }
                                    )
                                    {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                "Owner: ",
                                                color = Color.Black,
                                                fontSize = 13.sp,
                                                textAlign = TextAlign.Center
                                            )
                                            Text(
                                                "${it.owner.login}",
                                                fontSize = 13.sp,
                                                textAlign = TextAlign.Center
                                            )
                                        }

                                    }

                                }
                                Spacer(modifier = Modifier.height(5.dp))
                            }

                        }

                        Spacer(modifier = Modifier.height(30.dp))

                        it.cloneUrl?.let { urlString ->
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(urlString))

                            GithubButton(
                                onClick = {
                                    Log.e(TAG, "github url: $urlString")
                                    context.startActivity(intent)
                                },
                                text = "View on Github",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp)
                            )
                        }


                    }

                }
            }
        }

    }

}

@Composable
fun WatcherIcon() {
    val image: Painter = painterResource(id = R.drawable.ic_watcher)
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


@Composable
fun TitleText(
    textValue: String,
) {
    Text(
        textValue,
        color = Color.White,
        fontSize = 13.sp,
        style = typography.h4,
    )
}

@Composable
fun RepoDetailAppBar(onBackClick: () -> Unit) {
    TopAppBar(
        title = {
            TitleText(
                textValue = "RepoDetail",
            )
        },
        modifier = Modifier.fillMaxWidth(),
        navigationIcon = {
            IconButton(onClick = { onBackClick() }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    modifier = Modifier,
                    contentDescription = stringResource(id = R.string.icn_user_detail_app_bar_back_button)
                )
            }
        }
    )
}

@Composable
fun RepoDetailView(
    isLoading: Boolean,
    errorMessage: String,
    onRetryClick: () -> Unit = {},
    modifier: Modifier,
    isSuccess: Boolean,
    results: @Composable () -> Unit = {}
) {
    Box {
        Column(
            modifier = modifier
                .fillMaxSize()
        ) {

            if (isLoading) {
                LoadingScreen()
            } else {
                if (errorMessage.isNotEmpty()) {
                    ErrorScreen(
                        errorMessage = errorMessage,
                        onRetryClick = onRetryClick
                    )
                } else {
                    if (isSuccess) {
                        results()
                    }
                }
            }
        }

    }

}


private fun <A> A.toJson(): String? {
    return Gson().toJson(this)
}