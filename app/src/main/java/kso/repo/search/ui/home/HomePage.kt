package kso.repo.search.ui.home

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kso.repo.search.R
import kso.repo.search.app.NavPath
import kso.repo.search.app.collectAsStateLifecycleAware
import kso.repo.search.viewModel.HomePageViewModel

private const val TAG: String = "HomePage"

@Composable
fun HomePage(
    navHostController: NavHostController,
    homePageViewModel: HomePageViewModel,
) {


    Scaffold(
        topBar = {
            TopAppBar(title = {Text(stringResource(id = R.string.home), fontSize = 14.sp)}, backgroundColor = MaterialTheme.colors.primary)
        },
        content = {
            paddingValues -> ContentView(
                paddingValues = paddingValues,
                homePageViewModel = homePageViewModel,
                navHostController = navHostController
            )
        }
    )

}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ContentView(
    paddingValues: PaddingValues,
    homePageViewModel: HomePageViewModel,
    navHostController: NavHostController
){
    val searchText by homePageViewModel.searchText.collectAsStateLifecycleAware("")
    val keyboardController = LocalSoftwareKeyboardController.current
    var showClearButton by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val context = LocalContext.current
    val toastMessage = stringResource(R.string.search_repo)

    Box(
        Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentAlignment = Center,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(15.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                stringResource(id = R.string.welcome_message),
                fontSize = 18.sp,
                color = MaterialTheme.colors.primaryVariant
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(align = Center)
                    .padding(vertical = 4.dp, horizontal = 8.dp)
                    .onFocusChanged { focusState ->
                        showClearButton = (focusState.isFocused)
                    }
                    .focusRequester(focusRequester),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        tint = MaterialTheme.colors.primaryVariant,
                        contentDescription = stringResource(id = R.string.search)
                    )
                },
                placeholder = {
                    Text(text = stringResource(id = R.string.search_repo), color = Color.Gray, fontSize = 13.sp)
                },
                value = searchText,
                textStyle = TextStyle(
                    fontSize = 13.sp,
                    color = MaterialTheme.colors.primary,
                    fontWeight = FontWeight.Normal,
                ),
                colors = TextFieldDefaults.textFieldColors(
                    textColor = MaterialTheme.colors.primaryVariant,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    backgroundColor = Color.LightGray,
                    cursorColor = MaterialTheme.colors.primaryVariant
                ),
                onValueChange = {
                    homePageViewModel.onSearchTextChanged(it)
                },
                maxLines = 1,
                singleLine = true,
                trailingIcon = {
                    AnimatedVisibility(
                        visible = showClearButton,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        IconButton(onClick = { homePageViewModel.onSearchBoxClear() }) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                tint = MaterialTheme.colors.primaryVariant,
                                contentDescription = stringResource(id = R.string.icn_search_clear_content_description)
                            )
                        }

                    }
                },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(
                    onSearch =  {

                        keyboardController?.hide()
                        if(searchText.isNotEmpty()){
                            navHostController.navigate(
                                route = "${NavPath.RepoListPage.route}?repoName=$searchText"
                            )
                            homePageViewModel.onKeyboardSearchClick(searchText)
                        }else{
                            Toast.makeText(
                                context,
                                toastMessage,
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    }
                ),

            )
        }
    }

}






