package kso.repo.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kso.repo.search.model.User
import kso.repo.search.ui.common.ErrorScreen
import kso.repo.search.ui.common.LoadingScreen
import kso.repo.search.viewModel.UserDetailPageViewModel



@Composable
fun UserDetailPage(navHostController: NavHostController, userDetailViewModel: UserDetailPageViewModel) {

    val isLoading by userDetailViewModel.isLoading.collectAsState(initial = true)
    val isFail by userDetailViewModel.isFail.collectAsState(initial = true)
    val user by userDetailViewModel.data.collectAsState(initial = User() )

    if(isLoading){
        LoadingScreen()
    }else if(isFail){
        ErrorScreen("", {})
    }else{
        Column(modifier = Modifier.fillMaxSize()) {
            user?.let {
                UserDetailTopAppBar(title = it.login, onBackClick = {
                    navHostController.popBackStack()
                })
            }

            Column(modifier = Modifier.padding(16.dp)) {
                user?.let { Text(it.login, fontWeight = FontWeight.Bold, fontSize = 18.sp) }
                user?.let { Text(it.reposUrl) }
                user?.let { Text(it.url) }
            }


        }
    }


}

@Composable
fun UserDetailTopAppBar(title: String, onBackClick: () -> Unit) {
    TopAppBar(
        title = {
            Text(title, textAlign = TextAlign.Center)
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