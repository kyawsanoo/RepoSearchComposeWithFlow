package kso.repo.search.ui.detail

import android.content.Intent
import android.net.Uri
import android.util.Log
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.SubcomposeAsyncImage
import kso.repo.search.R
import kso.repo.search.model.Owner
import kso.repo.search.ui.common.GithubButton
import kso.repo.search.viewModel.UserDetailPageViewModel

private const val TAG: String = "UserDetailPage"

@Composable
fun UserDetailPage(
    navHostController: NavHostController,
    userDetailViewModel: UserDetailPageViewModel
) {

    val owner by userDetailViewModel.user.collectAsState(initial = Owner())
    val context = LocalContext.current

    Scaffold(topBar = {
        UserDetailTopAppBar(
            title = "OwnerDetail",
            onBackClick = {
            navHostController.popBackStack()
            }
        )
    }) {
        paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {

            Column(modifier = Modifier.padding(16.dp)) {
                SubcomposeAsyncImage(
                    model = owner?.avatarUrl,
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
                    owner?.let { Text("Name : ", fontSize = 13.sp) }
                    owner?.let { it.login?.let { it1 -> Text(it1, color = Color.Black, fontSize = 13.sp) } }
                }
                Spacer(modifier = Modifier.height(20.dp))
                owner?.ownerHtmlUrl?.let {
                    val intent =  Intent(Intent.ACTION_VIEW, Uri.parse(it))

                    GithubButton(
                        onClick = {
                            Log.e(TAG, "github url: $it")
                            context.startActivity(intent)
                        },
                        text = "View his profile on Github",
                        modifier = Modifier.fillMaxWidth()
                    )
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