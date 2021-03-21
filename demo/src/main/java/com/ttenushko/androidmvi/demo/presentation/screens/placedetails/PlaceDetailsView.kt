package com.ttenushko.androidmvi.demo.presentation.screens.placedetails

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ttenushko.androidmvi.demo.R
import com.ttenushko.androidmvi.demo.presentation.screens.placedetails.mvi.Store.State
import com.ttenushko.androidmvi.demo.presentation.theme.AppTheme
import dev.chrisbanes.accompanist.picasso.PicassoImage

@Composable
fun PlaceDetailsView(
    state: State,
    navigationClickHandler: () -> Unit,
    deleteClickHandler: () -> Unit
) {
    AppTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    modifier = Modifier,
                    navigationIcon = {
                        IconButton(onClick = navigationClickHandler) {
                            Icon(Icons.Filled.ArrowBack, "")
                        }
                    },
                    title = {
                        Text(text = stringResource(id = R.string.place_details_title))
                    },
                    actions = {
                        Menu(state, deleteClickHandler)
                    }
                )
            }
        ) {
            Content(state)
        }
    }
}

@Composable
fun Menu(
    state: State,
    deleteClickHandler: () -> Unit
) {
    Row {
        if (state.isDeleteButtonVisible) {
            IconButton(onClick = deleteClickHandler) {
                Icon(Icons.Filled.Delete, "")
            }
        }
    }
}

@Composable
fun Content(state: State) {
    if (null != state.weather) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = "${state.weather.place.name}, ${state.weather.place.countyCode.toUpperCase()}",
                style = MaterialTheme.typography.h5,
                modifier = Modifier
                    .wrapContentSize(align = Alignment.TopStart)
            )
            WeatherIconAndCurrentTemperature(
                imageUrl = state.weather.descriptions.firstOrNull()?.iconUrl ?: "",
                currentTemp = "${state.weather.conditions.tempCurrent.toInt()}\u2103"
            )
            WeatherCondition(
                modifier = Modifier,
                conditionName = stringResource(id = R.string.place_details_temp_min),
                conditionValue = "${state.weather.conditions.tempMin.toInt()}\u2103"
            )
            WeatherCondition(
                modifier = Modifier,
                conditionName = stringResource(id = R.string.place_details_temp_max),
                conditionValue = "${state.weather.conditions.tempMax.toInt()}\u2103"
            )
            WeatherCondition(
                modifier = Modifier,
                conditionName = stringResource(id = R.string.place_details_humidity),
                conditionValue = "${state.weather.conditions.humidity}%"
            )
        }
    }
}

@Composable
fun WeatherIconAndCurrentTemperature(imageUrl: String, currentTemp: String) {
    Row(
        modifier = Modifier
            .height(72.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            PicassoImage(
                data = imageUrl,
                contentDescription = "",
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f, true)
                    .align(Alignment.CenterEnd)
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = currentTemp,
            style = MaterialTheme.typography.h3,
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        )
    }
}

@Composable
private fun WeatherCondition(modifier: Modifier, conditionName: String, conditionValue: String) {
    Row(
        modifier = modifier
            .wrapContentSize(align = Alignment.Center)
    ) {
        Text(
            text = conditionName,
            style = MaterialTheme.typography.body1,
            modifier = Modifier
                .wrapContentSize(align = Alignment.CenterEnd)
                .weight(1f)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = conditionValue,
            style = MaterialTheme.typography.body1,
            modifier = Modifier
                .wrapContentSize(align = Alignment.CenterStart)
                .weight(1f)
        )
    }
}