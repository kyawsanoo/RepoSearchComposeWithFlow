package kso.repo.search

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.SubcomposeAsyncImage
import kso.repo.search.model.User
import kso.repo.search.ui.common.ErrorScreen
import kso.repo.search.ui.common.GithubButton
import kso.repo.search.ui.common.LoadingScreen
import kso.repo.search.ui.detail.TitleText
import kso.repo.search.viewModel.UserDetailPageViewModel


@Composable
fun UserDetailPage(
    navHostController: NavHostController,
    userDetailViewModel: UserDetailPageViewModel
) {

    val isLoading by userDetailViewModel.isLoading.collectAsState(initial = true)
    val isFail by userDetailViewModel.isFail.collectAsState(initial = true)
    val user by userDetailViewModel.data.collectAsState(initial = User())

    Scaffold(topBar = {
        UserDetailTopAppBar(
            title = "OwnerDetail",
            onBackClick = {
            navHostController.popBackStack()
            }
        )
    }) {
        if (isLoading) {
            LoadingScreen()
        } else if (isFail) {
            ErrorScreen("", {})
        } else {
            Column(modifier = Modifier.fillMaxSize()) {

                Column(modifier = Modifier.padding(16.dp)) {
                    SubcomposeAsyncImage(
                        model = user?.avatarUrl,
                        loading = {
                            CircularProgressIndicator()
                        },
                        contentDescription = stringResource(R.string.icon_img_text),
                        modifier = Modifier
                            .clip(CircleShape)
                            .width(55.dp)
                            .height(55.dp)
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        user?.let { Text("Name : ", fontSize = 13.sp) }
                        user?.let { Text(it.login, color = Color.Black, fontSize = 13.sp) }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    user?.url?.let {
                        GithubButton(
                            url = it,
                            text = "View his profile on Github",
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }


            }
        }
    }

}

@Composable
fun UserDetailTopAppBar(title: String, onBackClick: () -> Unit) {
    TopAppBar(
        title = {
            Text(title, fontSize = 13.sp, textAlign = TextAlign.Center)
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