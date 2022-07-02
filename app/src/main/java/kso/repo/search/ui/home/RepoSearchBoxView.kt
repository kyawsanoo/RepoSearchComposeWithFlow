package kso.repo.search.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
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
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kso.repo.search.R
import kso.repo.search.ui.common.ErrorScreen
import kso.repo.search.ui.common.LoadingScreen


@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@Composable
fun RepoSearchBoxView(
    searchText: String,
    placeholderText: String = "",
    onSearchTextChanged: (String) -> Unit = {},
    onClearClick: () -> Unit = {},
    onSearchBarClick: () -> Unit = {},
    showProgress: Boolean,
    errorMessage: String,
    onRetryClick: () -> Unit = {},
    matchesFound: Boolean,
    results: @Composable () -> Unit = {}
) {
    Scaffold(topBar = {
        AppBarWithSearchBox(
            searchText,
            placeholderText,
            onSearchTextChanged,
            onClearClick,
            onSearchBarClick
        )
    }) {
        Box {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {

                if(showProgress){
                    LoadingScreen()
                }
                else {
                    if(errorMessage.isNotEmpty()){
                        ErrorScreen(
                            errorMessage = errorMessage,
                            onRetryClick = onRetryClick
                        )
                    }else {
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
    onSearchBarClick: () -> Unit = {}
) {
    var showClearButton by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }

    TopAppBar(title = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    "Repo Search: ",
                    fontSize = 13.sp, color = Color.White
                )

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize(align = Alignment.CenterStart)
                        .padding(vertical = 4.dp)
                        .onFocusChanged { focusState ->
                            showClearButton = (focusState.isFocused)
                        }
                        .focusRequester(focusRequester),

                    textStyle = TextStyle(
                        fontSize = 13.sp,
                        color = MaterialTheme.colors.primary,
                        fontWeight = FontWeight.Normal,
                    ),
                    value = searchText,
                    onValueChange = onSearchTextChanged,
                    placeholder = {
                        Text(text = placeholderText, color = Color.Gray, fontSize = 13.sp)
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = MaterialTheme.colors.primaryVariant,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        backgroundColor = Color.White,
                        cursorColor = MaterialTheme.colors.primaryVariant/*LocalContentColor.current.copy(alpha = LocalContentAlpha.current)*/
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
                                    tint = MaterialTheme.colors.primaryVariant,
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
                    shape = MaterialTheme.shapes.small
                )

            }

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


    /*LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }*/
}


@Composable
fun NoSearchResults() {

    Column(
        modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center,
        horizontalAlignment = CenterHorizontally
    ) {
        Text(stringResource(id = R.string.no_matched_repo_found))
    }
}

