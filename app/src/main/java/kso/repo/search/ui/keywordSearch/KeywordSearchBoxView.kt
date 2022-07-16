package kso.repo.search.ui.keywordSearch

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kso.repo.search.R
import kso.repo.search.ui.common.ErrorScreen
import kso.repo.search.ui.common.LoadingScreen


@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@Composable
fun KeywordSearchBoxView(
    searchText: String,
    placeholderText: String = "",
    onSearchTextChanged: (String) -> Unit = {},
    onClearClick: () -> Unit = {},
    onNavigateBack: () -> Unit = {},
    showProgress: Boolean,
    errorMessage: String,
    matchesFound: Boolean,
    onRetryClick: () -> Unit = {},
    onKeywordClick: () -> Unit = {},
    results: @Composable () -> Unit = {}
) {
    Scaffold(topBar = {
        AppBarWithSearchBox(
            searchText,
            placeholderText,
            onSearchTextChanged,
            onClearClick,
            onNavigateBack
        )
    }) {
        paddingValues ->
        Box {
            Column(
                modifier = Modifier
                    .fillMaxSize().padding(paddingValues)
            ) {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(45.dp)
                    .background(Color.LightGray),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ){
                        Text(
                            text = stringResource(id=R.string.select_keyword) ,
                            color = Color.Black,
                            textAlign = TextAlign.Center,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier.padding(horizontal = 10.dp)
                        )
                        Text(
                            text = searchText,
                            color = Color.Blue,
                            textAlign = TextAlign.Center,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier.padding(horizontal = 10.dp).clickable(onClick = onKeywordClick)

                        )
                    }

                }
                if(showProgress){
                    LoadingScreen()
                }else {
                    if (errorMessage.isNotEmpty()) {
                        ErrorScreen(
                            errorMessage = errorMessage,
                            onRetryClick = onRetryClick
                        )
                    } else {
                        if (matchesFound) {
                            results()
                        } else {
                            if (searchText.isNotEmpty()) {
                                NoSearchResults()
                            }

                        }
                    }
                }

            }

        }
    }

}


@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@Composable
fun AppBarWithSearchBox(
    searchText: String,
    placeholderText: String = "",
    onSearchTextChanged: (String) -> Unit = {},
    onClearClick: () -> Unit = {},
    onNavigateBack: () -> Unit = {}
) {
    var showClearButton by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }

    TopAppBar(title = { Text("") }, navigationIcon = {
        IconButton(onClick = { onNavigateBack() }) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                modifier = Modifier,
                contentDescription = stringResource(id = R.string.icn_search_back_content_description)
            )
        }
    }, actions = {

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 2.dp)
                .onFocusChanged { focusState ->
                    showClearButton = (focusState.isFocused)
                }
                .focusRequester(focusRequester),
            value = searchText,
            onValueChange = onSearchTextChanged,
            placeholder = {
                Text(text = placeholderText, color = Color.White, fontSize = 13.sp)
            },
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                backgroundColor = Color.Transparent,
                cursorColor = LocalContentColor.current.copy(alpha = LocalContentAlpha.current)
            ),
            trailingIcon = {
                AnimatedVisibility(
                    visible = showClearButton,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    IconButton(onClick = { onClearClick() }) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            tint = Color.White,
                            contentDescription = stringResource(id = R.string.icn_search_clear_content_description)
                        )
                    }

                }
            },
            maxLines = 1,
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                keyboardController?.hide()
            }),
        )


    })


    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}


@Composable
fun NoSearchResults() {

    Column(
        modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center,
        horizontalAlignment = CenterHorizontally
    ) {

        Text(stringResource(id = R.string.no_matched_keyword_found))

    }
}