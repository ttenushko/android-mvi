package com.ttenushko.androidmvi.demo.presentation.screens.places

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ttenushko.androidmvi.demo.R
import com.ttenushko.androidmvi.demo.domain.weather.model.Location
import com.ttenushko.androidmvi.demo.domain.weather.model.Place
import com.ttenushko.androidmvi.demo.presentation.screens.places.mvi.Store.State
import com.ttenushko.androidmvi.demo.presentation.theme.AppTheme
import com.ttenushko.androidmvi.demo.presentation.theme.PlaceCards
import com.ttenushko.androidmvi.demo.presentation.theme.SemitransparentProgress

@Composable
fun PlacesView(
    state: State,
    addPlaceClickHandler: () -> Unit = {},
    placeClickHandler: (Place) -> Unit = {}
) {
    AppTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(text = stringResource(id = R.string.places_title))
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(onClick = addPlaceClickHandler) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_add_24dp),
                        contentDescription = null
                    )
                }
            }

        ) {
            Content(state, placeClickHandler)
        }
    }
}

@Composable
private fun Content(state: State, placeClickHandler: (Place) -> Unit) {
    if (null == state.error) {
        DataContent(
            places = state.places ?: listOf(),
            isLoading = state.isLoading,
            placeClickHandler
        )
    } else {
        ErrorContent(error = state.error, isLoading = state.isLoading)
    }
}

@Composable
private fun DataContent(
    places: List<Place>,
    isLoading: Boolean,
    placeClickHandler: (Place) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        PlaceCards(
            modifier = Modifier
                .fillMaxHeight(),
            places = places,
            placeClickHandler = placeClickHandler
        )
        if (isLoading) {
            SemitransparentProgress()
        }
    }
}

@Composable
private fun ErrorContent(@Suppress("UNUSED_PARAMETER") error: Throwable?, isLoading: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(16.dp),
            text = stringResource(id = R.string.error_unknown)
        )
        if (isLoading) {
            SemitransparentProgress()
        }
    }
}

@Preview
@Composable
private fun PreviewContent() {
    PlacesView(
        State(
            places = listOf(
                Place(id = 1, name = "Place1", countyCode = "AB", location = Location(0f, 0f)),
                Place(id = 2, name = "Place2", countyCode = "CD", location = Location(0f, 0f)),
            ),
            error = null,
            isLoading = false
        )
    )
}

@Preview
@Composable
private fun PreviewError() {
    PlacesView(
        State(
            places = null,
            error = IllegalStateException(),
            isLoading = false
        )
    )
}