package com.ttenushko.androidmvi.demo.presentation.screens.placedetails

import androidx.compose.foundation.layout.Row
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.ttenushko.androidmvi.demo.R
import com.ttenushko.androidmvi.demo.presentation.screens.placedetails.mvi.Store.State
import com.ttenushko.androidmvi.demo.presentation.theme.AppTheme

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

}