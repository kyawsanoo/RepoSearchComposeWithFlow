package kso.repo.search.ui.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kso.repo.search.R
import kso.repo.search.app.NavPath
import kso.repo.search.app.collectAsStateLifecycleAware
import kso.repo.search.model.Repo
import kso.repo.search.model.Resource
import kso.repo.search.ui.common.ErrorScreen
import kso.repo.search.ui.common.LoadingScreen
import kso.repo.search.viewModel.HomePageViewModel

val TAG: String = "HomePage"

@Composable
fun HomePage(
    navHostController: NavHostController,
    homePageViewModel: HomePageViewModel,
    scaffoldState: ScaffoldState = rememberScaffoldState()
) {
    val userName by homePageViewModel.userName.collectAsStateLifecycleAware(initial = "")
    val errorMessage by homePageViewModel.errorMessage.collectAsStateLifecycleAware(initial = "")
    val resource by homePageViewModel.responseResource.collectAsStateLifecycleAware(initial = Resource.Loading)

    /*val lifecycleAwareUserName = rememberFlowWithLifecycle( homePageViewModel.userName)
    val lifecycleAwareResponseResource = rememberFlowWithLifecycle( homePageViewModel.responseResource)
    val lifecycleAwareErrorMessage = rememberFlowWithLifecycle( homePageViewModel.errorMessage)*/

    /* val lifecycleAwareUserName = rememberFlow( homePageViewModel.userName)
     val lifecycleAwareResponseResource = rememberFlow( homePageViewModel.responseResource)
     val lifecycleAwareErrorMessage = rememberFlow( homePageViewModel.errorMessage)*/

    /*val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycleAwareUserName = remember( homePageViewModel.userName, lifecycleOwner) {
        homePageViewModel.userName.flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.STARTED)
    }
    val lifecycleAwareResponseResource = remember( homePageViewModel.responseResource, lifecycleOwner) {
        homePageViewModel.responseResource.flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.STARTED)
    }
    val lifecycleAwareErrorMessage = remember( homePageViewModel.errorMessage, lifecycleOwner) {
        homePageViewModel.errorMessage.flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.STARTED)
    }*/


    /*val userName by lifecycleAwareUserName.collectAsState(initial = "")
    val errorMessage by lifecycleAwareErrorMessage.collectAsState(initial = "")
    val resource by lifecycleAwareResponseResource.collectAsState(initial = Resource.Loading)*/



    //val userName by homePageViewModel.userName.collectAsState(initial = "")
    //val resource by homePageViewModel.responseResource.collectAsState(initial = Resource.Loading)
    //val isLoading by homePageViewModel.isLoading.collectAsState(initial = true)
    //al repos by homePageViewModel.repos.collectAsState(initial = listOf())
    //val isFail by homePageViewModel.isFail.collectAsState(initial = true)
    //val errorMessage by homePageViewModel.errorMessage.collectAsState(initial = "")

    LaunchedEffect(Unit) {

        Log.e(TAG, "LaunchedEffect")

        homePageViewModel.onResume()
    }
    DisposableEffect(Unit) {
        onDispose {
            Log.e(TAG, "onDispose")
        }
    }

    /*val lifecycle = LocalLifecycleOwner.current.lifecycle
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

            homePageViewModel.submit()
        }
    }*/

    Scaffold(
        topBar = {
            AppBar(userName, onSearchBarClick = {
                    navHostController.navigate(route = NavPath.SearchBoxPage.route)
                }
            )
        },
        scaffoldState = scaffoldState
    ) {

            if(resource.isLoading){
                Log.e(TAG, "Loading")
                LoadingScreen()
            }else if(resource.isFail){
                Log.e(TAG, "Fail")
               ErrorScreen(errorMessage = errorMessage, onRetryClick = {
                   Log.e(TAG, "Retry Click")
                   homePageViewModel.retry()}
               )
            }else {
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

}

@Composable
fun Home() {
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
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 10.dp)
            .clickable { onClick() }) {

            Text("Repo Name",  fontSize = 13.sp, style = typography.h3)
            Spacer(modifier = Modifier.height(2.dp))
            Text(repo.name, fontSize = 13.sp, fontWeight = FontWeight.Bold, style = typography.h4, modifier = Modifier.padding(horizontal = 10.dp))
            Spacer(modifier = Modifier.height(2.dp))
            Text("Description",  fontSize = 13.sp, style = typography.h3)
            Spacer(modifier = Modifier.height(2.dp))
            if(repo.description!= null){
                Text(repo.description, fontSize = 13.sp, fontWeight = FontWeight.Bold, style = typography.h4, modifier = Modifier.padding(horizontal = 10.dp))
            }else Text("-", fontSize = 13.sp, fontWeight = FontWeight.Bold, style = typography.h4,  modifier = Modifier.padding(horizontal = 10.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text("Language",  fontSize = 13.sp, style = typography.h3)
            Spacer(modifier = Modifier.height(2.dp))
            if(repo.language!= null){
                Text(repo.language, fontSize = 13.sp, fontWeight = FontWeight.Bold, style = typography.h4, modifier = Modifier.padding(horizontal = 10.dp))
            }else Text("-", fontSize = 13.sp, fontWeight = FontWeight.Bold, style = typography.h4,  modifier = Modifier.padding(horizontal = 10.dp))
            Spacer(modifier = Modifier.height(4.dp))
        }
    }

}
