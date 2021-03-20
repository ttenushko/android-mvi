package com.ttenushko.androidmvi.demo.presentation.screens.addplace

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.material.color.MaterialColors
import com.ttenushko.androidmvi.demo.R
import com.ttenushko.androidmvi.demo.domain.weather.model.Place
import com.ttenushko.androidmvi.demo.presentation.screens.addplace.mvi.Store.State
import com.ttenushko.androidmvi.demo.presentation.theme.AppTheme
import com.ttenushko.androidmvi.demo.presentation.theme.PlaceCards
import com.ttenushko.androidmvi.demo.presentation.theme.SmallProgress

@Composable
fun AddPlaceView(
    state: State,
    navigationClickHandler: () -> Unit,
    searchChanged: (String) -> Unit,
    placeClickHandler: (Place) -> Unit
) {
    AppTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    modifier = Modifier,
                    title = {
                        SearchInput(
                            Modifier
                                .fillMaxWidth()
                                .wrapContentHeight(),
                            state.search,
                            searchChanged
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = navigationClickHandler) {
                            Icon(Icons.Filled.ArrowBack, "")
                        }
                    }
                )
            }
        ) {
            Content(state, placeClickHandler)
        }
    }
}

@Composable
fun SearchInput(modifier: Modifier, search: String, searchChanged: (String) -> Unit) {
    val focusRequester = remember { FocusRequester() }
    TextField(
        value = search,
        onValueChange = searchChanged,
        singleLine = true,
        leadingIcon = {
            Icon(Icons.Filled.Search, "")
        },
        modifier = modifier
            .focusRequester(focusRequester)
            .wrapContentHeight()
            .defaultMinSize(
                minWidth = TextFieldDefaults.MinWidth,
                minHeight = 20.dp
            ),
        textStyle = MaterialTheme.typography.subtitle2,
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.high),
            unfocusedIndicatorColor = MaterialTheme.colors.onSurface.copy(alpha = 0.42f),
            disabledIndicatorColor = MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.disabled),
            backgroundColor = Color.Transparent,
            cursorColor = MaterialTheme.colors.onPrimary
        ),
        placeholder = {
            Text(
                text = stringResource(id = R.string.add_place_search_hint),
                style = MaterialTheme.typography.subtitle2
            )
        },
    )
    DisposableEffect(Unit) {
        focusRequester.requestFocus()
        onDispose { }
    }
}

@Composable
fun Content(state: State, placeClickHandler: (Place) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        PlaceCards(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            places = (state.searchResult as? State.SearchResult.Success)?.places ?: emptyList(),
            placeClickHandler
        )

        val modifier = Modifier
            .padding(16.dp)
            .align(Alignment.TopCenter)
        val style = MaterialTheme.typography.subtitle2
        when {
            state.isShowSearchPrompt -> {
                Text(
                    text = stringResource(id = R.string.add_place_search_prompt),
                    modifier = modifier,
                    style = style
                )
            }
            state.isShowSearchNoResultsPrompt -> {
                Text(
                    text = stringResource(id = R.string.add_place_no_results_prompt),
                    modifier = modifier,
                    style = style
                )
            }
            state.isShowSearchErrorPrompt -> {
                Text(
                    text = stringResource(id = R.string.add_place_error_prompt),
                    modifier = modifier,
                    style = style
                )
            }
        }
        if (state.isSearching) {
            SmallProgress(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(8.dp)
            )
        }
    }
}
