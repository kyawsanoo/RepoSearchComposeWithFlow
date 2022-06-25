package kso.repo.search.ui.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kso.repo.search.R
import kso.repo.search.model.Repo
import kso.repo.search.ui.common.ErrorScreen
import kso.repo.search.ui.common.LoadingScreen
import kso.repo.search.ui.common.SpannableText
import kso.repo.search.viewModel.RepoDetailPageViewModel

@Composable
    fun RepoDetailPage(navHostController: NavHostController, repoDetailViewModel: RepoDetailPageViewModel) {

        val isLoading by repoDetailViewModel.isLoading.collectAsState(initial = true)
        val repo by repoDetailViewModel.data.collectAsState(initial = Repo() )
        val isFail by repoDetailViewModel.isFail.collectAsState(initial = true)
        val errorMessage by repoDetailViewModel.errorMessage.collectAsState("")

        Scaffold(topBar = {
            RepoDetailAppBar(
                onBackClick = {
                    navHostController.popBackStack()
                }
            )
        }) {
            if (isLoading) {
                LoadingScreen()
            } else if (isFail) {
                ErrorScreen(errorMessage = errorMessage, onRetryClick = {repoDetailViewModel.retry()} )
            } else {
                repo?.let {
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
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.Start,
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "Repo Name",
                                fontSize = 13.sp,
                                style = typography.h3
                            )
                            repo?.let {
                                Text(
                                    it.name,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    style = typography.h4,
                                    modifier = Modifier.padding(horizontal = 10.dp)
                                )
                            }
                            Text(
                                "Full Name",
                                fontSize = 13.sp,
                                style = typography.h3
                            )
                            repo?.let {
                                Text(
                                    it.fullName,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    style = typography.h4,
                                    modifier = Modifier.padding(horizontal = 10.dp)
                                )
                            }
                            Text(
                                "Description",
                                fontSize = 13.sp,
                                style = typography.h3
                            )
                            repo?.let {
                                it.description?.let { it1 ->
                                    Text(
                                        it1,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        style = typography.h4,
                                        modifier = Modifier.padding(horizontal = 10.dp)
                                    )
                                }
                            }

                            Text(
                                "Language",
                                fontSize = 13.sp,
                                style = typography.h3
                            )
                            repo?.let {
                                it.language?.let { it1 ->
                                    Text(
                                        it1,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        style = typography.h4,
                                        modifier = Modifier.padding(horizontal = 10.dp)
                                    )
                                }
                            }
                            Text(
                                "Url",
                                fontSize = 13.sp,
                                style = typography.h3
                            )
                            repo?.let {
                                Box(modifier = Modifier.padding(horizontal = 10.dp)) {
                                    SpannableText(it.url)
                                }

                            }
                            Text(
                                "Owner",
                                fontSize = 13.sp,
                                style = typography.h3
                            )
                            repo?.owner?.let { it1 ->
                                Text(
                                    it1.login,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    style = typography.h4,
                                    modifier = Modifier.padding(horizontal = 10.dp)
                                )
                            }
                            Text(
                                "Default Branch",
                                fontSize = 13.sp,
                                style = typography.h3
                            )
                            Text(
                                "${repo?.defaultBranch}",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                style = typography.h4,
                                modifier = Modifier.padding(horizontal = 10.dp)
                            )
                            Text(
                                "Watcher Count",
                                fontSize = 13.sp,
                                style = typography.h3
                            )
                            Text(
                                "${repo?.watchers_count}",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                style = typography.h4,
                                modifier = Modifier.padding(horizontal = 10.dp)
                            )
                            Text(
                                "Watcher",
                                fontSize = 13.sp,
                                style = typography.h3
                            )
                            Text(
                                "${repo?.watchers}",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                style = typography.h4,
                                modifier = Modifier.padding(horizontal = 10.dp)
                            )
                            Text(
                                "Forks Count",
                                fontSize = 13.sp,
                                style = typography.h3
                            )
                            Text(
                                "${repo?.forksCount}",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                style = typography.h4,
                                modifier = Modifier.padding(horizontal = 10.dp)
                            )
                            Text(
                                "Forks",
                                fontSize = 13.sp,
                                style = typography.h3
                            )
                            Text(
                                "${repo?.forks}",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                style = typography.h4,
                                modifier = Modifier.padding(horizontal = 10.dp)
                            )
                            Text(
                                "Open Issues",
                                fontSize = 13.sp,
                                style = typography.h3
                            )
                            Text(
                                "${repo?.openIssues}",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                style = typography.h4,
                                modifier = Modifier.padding(horizontal = 10.dp)
                            )
                            Text(
                                "Open Issue Count",
                                fontSize = 13.sp,
                                style = typography.h3
                            )
                            Text(
                                "${repo?.openIssuesCount}",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                style = typography.h4,
                                modifier = Modifier.padding(horizontal = 10.dp)
                            )
                        }
                    }

                }
            }
        }

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