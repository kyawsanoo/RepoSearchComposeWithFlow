package kso.repo.search.ui.searchBox

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
import kso.repo.search.model.User
import kso.repo.search.ui.common.SpannableText
import kso.repo.search.viewModel.SearchBoxViewModel
import kso.repo.search.ui.state.SearchBoxViewModelState


private const val TAG="UserSearchBoxPage"

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun SearchBoxPage(navHostController: NavHostController, userSearchViewModel: SearchBoxViewModel) {

    //normal collect
    val userSearchModelState by userSearchViewModel.userSearchModelState.collectAsState(initial = SearchBoxViewModelState.Empty)

    //normal effect
    LaunchedEffect(Unit) {

        Log.e(TAG, "LaunchedEffect")

    }

    DisposableEffect(Unit) {
        onDispose {
            Log.e(TAG, "onDispose")
        }
    }

    Log.e(TAG, "searchText: ${userSearchModelState.searchText}")

    SearchBoxView(
        searchText = userSearchModelState.searchText,
        placeholderText = "Search user",
        onSearchTextChanged = { userSearchViewModel.onSearchTextChanged(it) },
        onClearClick = { userSearchViewModel.onSearchBoxClear() },
        onNavigateBack = {
            Log.e(TAG, "onNavigateBack")
            navHostController.popBackStack()
        },
        showProgress = userSearchModelState.showProgressBar,
        matchesFound = userSearchModelState.users.isNotEmpty()
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(5.dp)
        ) {

            items(items = userSearchModelState.users) { user ->
                UserRow(user = user) {
                    val argLogin = user.login
                    Log.e(TAG, "Route: ${NavPath.HomePage.route}?login=$argLogin")
                    navHostController.navigate(
                        route = "${NavPath.HomePage.route}?login=${argLogin}"
                    )
                }
            }
        }
    }


}


@Composable
fun UserRow(user: User, onClick: () -> Unit) {

        Row(
            //horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clickable { onClick() }
        ) {

            SubcomposeAsyncImage(
                model = user.avatarUrl,
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
                Text(user.login, fontSize = 13.sp)
                SpannableText(user.url)
            }

        }

}






