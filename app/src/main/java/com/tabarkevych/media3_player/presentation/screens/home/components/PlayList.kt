package com.tabarkevych.media3_player.presentation.screens.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.tabarkevych.media3_player.presentation.model.UiListItem

@Composable
fun ColumnScope.MusicList(
    modifier: Modifier = Modifier,
    audioList: List<UiListItem>,
    currentPlayingIndex: Int,
    onClick: (UiListItem) -> Unit
) {
    val state = rememberLazyListState()
    Column(modifier = modifier
        .fillMaxWidth()
        .weight(1f)
        .background(Color.Black)) {
        LazyColumn(state = state) {
            itemsIndexed(audioList) { index, item ->
                when (item) {
                    is UiListItem.Song -> {
                        AudioItem(item, index == currentPlayingIndex) {
                            onClick(item)
                        }
                    }

                    is UiListItem.Album -> {
                        AlbumItem(item) {
                            onClick(item)
                        }
                    }
                }

            }
        }
        LaunchedEffect(currentPlayingIndex) {
            if (currentPlayingIndex != -1) {
                // Check if item at index 5 is visible
                val visibleItems = state.layoutInfo.visibleItemsInfo
                val itemIsVisible = visibleItems.any { it.index == currentPlayingIndex }
                if (!itemIsVisible) {
                    state.animateScrollToItem(currentPlayingIndex)
                }
            }
        }
    }
}