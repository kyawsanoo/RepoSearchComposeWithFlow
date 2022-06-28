package kso.repo.search.ui.home

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.SubcomposeAsyncImage
import kso.repo.search.R
import kso.repo.search.app.NavPath
import kso.repo.search.app.collectAsStateLifecycleAware
import kso.repo.search.app.rememberFlow
import kso.repo.search.app.rememberFlowWithLifecycle
import kso.repo.search.model.Repo
import kso.repo.search.model.Resource
import kso.repo.search.ui.common.ErrorScreen
import kso.repo.search.ui.common.LoadingScreen
import kso.repo.search.ui.common.SpannableText
import kso.repo.search.viewModel.HomePageViewModel

const val TAG: String = "HomePage"

@Composable
fun HomePage(
    navHostController: NavHostController,
    homePageViewModel: HomePageViewModel,
    scaffoldState: ScaffoldState = rememberScaffoldState()
) {
    //lifecycel aware collect(Method-4)

    val userName by homePageViewModel.userName.collectAsStateLifecycleAware(initial = "")
    val errorMessage by homePageViewModel.errorMessage.collectAsStateLifecycleAware(initial = "")
    val resource by homePageViewModel.responseResource.collectAsStateLifecycleAware(initial = Resource.Loading)


    //lifecycel aware collect(Method-3)

    /*
    val lifecycleAwareUserName = rememberFlowWithLifecycle( homePageViewModel.userName)
    val lifecycleAwareResponseResource = rememberFlowWithLifecycle( homePageViewModel.responseResource)
    val lifecycleAwareErrorMessage = rememberFlowWithLifecycle( homePageViewModel.errorMessage)
    val userName by lifecycleAwareUserName.collectAsState(initial = "")
    val resource by lifecycleAwareResponseResource.collectAsState(initial = Resource.Loading)
    val errorMessage by lifecycleAwareErrorMessage.collectAsState(initial = "")
    */



    //lifecycel aware collect(Method-2)

    /*
    val lifecycleAwareUserName = rememberFlow( homePageViewModel.userName)
    val lifecycleAwareResponseResource = rememberFlow( homePageViewModel.responseResource)
    val lifecycleAwareErrorMessage = rememberFlow( homePageViewModel.errorMessage)
    val userName by lifecycleAwareUserName.collectAsState(initial = "")
    val resource by lifecycleAwareResponseResource.collectAsState(initial = Resource.Loading)
    val errorMessage by lifecycleAwareErrorMessage.collectAsState(initial = "")
    */


    //lifecycel aware collect(Method-1)

    /*
    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycleAwareUserName = remember( homePageViewModel.userName, lifecycleOwner) {
        homePageViewModel.userName.flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.STARTED)
    }
    val lifecycleAwareResponseResource = remember( homePageViewModel.responseResource, lifecycleOwner) {
        homePageViewModel.responseResource.flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.STARTED)
    }
    val lifecycleAwareErrorMessage = remember( homePageViewModel.errorMessage, lifecycleOwner) {
        homePageViewModel.errorMessage.flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.STARTED)
    }
    val userName by lifecycleAwareUserName.collectAsState(initial = "")
    val resource by lifecycleAwareResponseResource.collectAsState(initial = Resource.Loading)
    val errorMessage by lifecycleAwareErrorMessage.collectAsState(initial = "")
    */


   //normal collect, lifecycle not aware, not recommended way

   /*
    val userName by homePageViewModel.userName.collectAsState(initial = "")
    val resource by homePageViewModel.responseResource.collectAsState(initial = Resource.Loading)
    val errorMessage by homePageViewModel.errorMessage.collectAsState(initial = "")
    */


    //normal effect
    /*LaunchedEffect(Unit) {

        Log.e(TAG, "LaunchedEffect")

        homePageViewModel.onResume()
    }
    DisposableEffect(Unit) {
        onDispose {
            Log.e(TAG, "onDispose")
        }
    }*/

    //lifecycle aware effect
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    var latestLifecycleEvent by remember { mutableStateOf(Lifecycle.Event.ON_ANY) }
    DisposableEffect(lifecycle) {
        val observer = LifecycleEventObserver { _, event ->
            latestLifecycleEvent = event
        }
        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
        }
    }
    if (latestLifecycleEvent == Lifecycle.Event.ON_RESUME) {
        LaunchedEffect(latestLifecycleEvent) {
            Log.e(TAG, "LaunchedEffect")

            homePageViewModel.onResume()
        }
    }

    Scaffold(
        topBar = {
            AppBar(userName, onSearchBarClick = {
                    navHostController.navigate(route = NavPath.SearchBoxPage.route)
                }
            )
        },
        scaffoldState = scaffoldState
    ) {

            when(resource){
                is Resource.Loading -> LoadingScreen()
                is Resource.Fail -> ErrorScreen(errorMessage = errorMessage, onRetryClick = {
                    Log.e(TAG, "Retry Click")
                    homePageViewModel.retry()}
                )
                else -> {
                    Log.e(TAG, "Success")
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(5.dp)
                    ) {

                        items(items = resource.data.orEmpty()) { repo ->
                            RepoRow(repo = repo) {
                                navHostController.navigate(route = "${NavPath.RepoDetail.route}?login=${userName}&repoName=${repo.name}")
                            }
                        }
                    }
                }

            }

            //the way of if condition
            /*
            if(resource.isLoading){
                Log.e(TAG, "Loading")
                LoadingScreen()
            }else if(resource.isFail){
                Log.e(TAG, "Fail")
               ErrorScreen(errorMessage = errorMessage, onRetryClick = {
                   Log.e(TAG, "Retry Click")
                   homePageViewModel.retry()}
               )
            }
            else {
                Log.e(TAG, "Success")
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(5.dp)
                ) {

                    items(items = resource.data.orEmpty()) { repo ->
                        RepoRow(repo = repo) {
                            navHostController.navigate(route = "${NavPath.RepoDetail.route}?login=${userName}&repoName=${repo.name}")
                        }
                    }
                }
            }
            */



    }

}

@Composable
fun AppBar(userName: String, onSearchBarClick: () -> Unit) {
    TopAppBar(
        title = {
            TitleText(
                modifier = Modifier.fillMaxWidth(),
                textValue= userName,
                onClick = onSearchBarClick
            )
        },
        actions = {
            IconButton(
                modifier = Modifier,
                onClick = onSearchBarClick) {
                Icon(
                    Icons.Filled.Search,
                    contentDescription = stringResource(id = R.string.icon_default_search_text)
                )
            }
        }
    )
}

@Composable
fun TitleText(
    modifier: Modifier = Modifier,
    textValue: String,
    onClick: () -> Unit
) {
    Row(horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ){
        Text( text = "${stringResource(id = R.string.app_name)} : ",
            fontSize = 14.sp,
            style = typography.h5,
        )
        Box(
            modifier = Modifier
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(5.dp)
                )
                .clickable(onClick = onClick),
        ){
            Text(
                text = textValue,
                color = MaterialTheme.colors.primary,
                fontSize = 13.sp,
                modifier = modifier.padding(horizontal = 15.dp, vertical = 5.dp),
                textAlign = TextAlign.Start,
                style = typography.h4,
            )
        }
    }

}


@Composable
fun RepoRow(repo: Repo, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(10.dp),
        elevation = 5.dp,
        backgroundColor = MaterialTheme.colors.surface
    ) {
        Row(
            //horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top,
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
                contentDescription = stringResource(R.string.icon_img_text),
                modifier = Modifier
                    .clip(CircleShape)
                    .width(35.dp)
                    .height(35.dp)
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp),
            ) {
                repo.description?.let { Text(it, fontSize = 13.sp) }
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()

                ) {
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
                            Text("${repo.stargazersCount}")
                        }

                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            ForkIcon()
                            Text("${repo.forksCount}")
                        }

                    }
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.LightGray)
                        ) {
                            repo.language?.let {
                                Text(
                                    it,

                                    modifier = Modifier
                                        .padding(horizontal = 10.dp, vertical = 5.dp)
                                        .width(80.dp)
                                        .wrapContentHeight(),
                                    fontSize = 13.sp,
                                    color = Color.Black,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }

                    }
                }

            }

        }
    }

}

@Composable
fun ForkIcon() {
    val image: Painter = painterResource(id = R.drawable.directions_fork)
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

