package kso.repo.search.ui.detail

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kso.repo.search.R
import kso.repo.search.app.NavPath
import kso.repo.search.model.Repo
import kso.repo.search.ui.common.ErrorScreen
import kso.repo.search.ui.common.ForkIcon
import kso.repo.search.ui.common.GithubButton
import kso.repo.search.ui.common.LoadingScreen
import kso.repo.search.viewModel.RepoDetailPageViewModel

@Composable
    fun RepoDetailPage(navHostController: NavHostController, repoDetailViewModel: RepoDetailPageViewModel) {

        val isLoading by repoDetailViewModel.isLoading.collectAsState(initial = true)
        val repo by repoDetailViewModel.data.collectAsState(initial = Repo() )
        val isFail by repoDetailViewModel.isFail.collectAsState(initial = true)
        val errorMessage by repoDetailViewModel.errorMessage.collectAsState("")

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
        }) {
            if (isLoading) {
                LoadingScreen()
            } else if (isFail) {
                ErrorScreen(errorMessage = errorMessage, onRetryClick = {repoDetailViewModel.retry()} )
            } else {
                repo?.let {

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
                            ){
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                it.name,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                style = typography.h4,
                                //modifier = Modifier.align(Alignment.Center),
                                textAlign = TextAlign.Center
                            )
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
                                            //.clip(CircleShape)
                                            .width(20.dp)
                                            .height(20.dp),
                                        tint = Color.Blue,
                                        contentDescription = stringResource(id = R.string.icon_star_text)
                                    )
                                    Text("${repo?.stargazersCount}", fontSize = 13.sp)
                                }

                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    ForkIcon()
                                    Text("${repo?.forksCount}", fontSize = 13.sp,)
                                }

                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    WatcherIcon()
                                    Text("${repo?.watchers}", fontSize = 13.sp,)
                                }
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    repo?.language?.let {
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
                            repo?.let {
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

                                ){
                                    TextButton(onClick = { navHostController.navigate(route = "${NavPath.UserDetail.route}?login=${repo?.owner?.login}&repoName=${repo?.owner?.login}") }) {
                                        Row(verticalAlignment = Alignment.CenterVertically){
                                            Text("Owner: ", color = Color.Black, fontSize = 13.sp, textAlign = TextAlign.Center)
                                            Text("${repo?.owner?.login}", fontSize = 13.sp, textAlign = TextAlign.Center)
                                        }

                                    }

                                }
                                Spacer(modifier = Modifier.height(5.dp))
                        }

                        }

                        Spacer(modifier = Modifier.height(30.dp))

                        repo?.cloneUrl?.let { urlString -> GithubButton(url = urlString, text="View on Github", modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)) }


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
        Text(textValue,
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