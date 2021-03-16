package com.ttenushko.androidmvi.demo.presentation.screens.places

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
        LazyColumn(
            modifier = Modifier
                .fillMaxHeight()
        ) {
            itemsIndexed(
                items = places,
                key = { _, place -> place.id }
            ) { index, place ->
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(if (0 == index) 8.dp else 16.dp)
                )
                PlaceItem(place, placeClickHandler)
            }
        }
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

@Composable
private fun PlaceItem(
    place: Place,
    placeClickHandler: (Place) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = MaterialTheme.shapes.medium,
        backgroundColor = MaterialTheme.colors.surface,
        elevation = 8.dp,
        modifier = modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .clickable(onClick = { placeClickHandler(place) })
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = "${place.name}, ${place.countyCode}",
                style = MaterialTheme.typography.body1
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "%1\$.6f, %2\$.6f".format(place.location.latitude, place.location.longitude),
                style = MaterialTheme.typography.body2
            )
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