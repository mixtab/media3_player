package com.tabarkevych.media3_player.presentation.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import com.tabarkevych.media3_player.domain.manager.IAudioController
import com.tabarkevych.media3_player.presentation.DevicePreviews
import com.tabarkevych.media3_player.presentation.model.UiListItem
import com.tabarkevych.media3_player.presentation.screens.home.components.BottomPlayer
import com.tabarkevych.media3_player.presentation.screens.home.components.MusicList
import com.tabarkevych.media3_player.presentation.theme.Media3_playerTheme
import kotlin.time.Duration.Companion.milliseconds


@Composable
fun MarkersListScreenRoute(
    viewModel: HomeViewModel = hiltViewModel()
) {

    val albums = viewModel.currentSelectedAlbumState.collectAsState(listOf())

    val playbackProgress =
        viewModel.playbackProgressPercentFlow.collectAsState(IAudioController.PlaybackProgress())

    val isPlaying = viewModel.isPlayingFlow.collectAsState(false)

    val isBuggering = viewModel.isBufferingFlow.collectAsState(false)

    HomeScreen(
        albums.value,
        playbackProgress.value,
        isBuggering.value,
        isPlaying.value,
        onItemSelected = {
            viewModel.handleSelectedItem(it)
        },
        onPlayClick = {
            viewModel.playPause()
        },
        onPauseClick = {

        },
        onSkipToPreviousClick = {
            viewModel.playPrevious()
        },
        onSkipToNextClick = {
            viewModel.playNext()
        },
        onSeek = {
            viewModel.seekTo(it)
        }
    )
}


@Composable
fun HomeScreen(
    playlistItems: List<UiListItem>,
    playbackProgress: IAudioController.PlaybackProgress,
    isBuffering: Boolean,
    isPlaying: Boolean,
    onItemSelected: (UiListItem) -> Unit,
    onPlayClick: () -> Unit,
    onPauseClick: () -> Unit,
    onSkipToPreviousClick: () -> Unit,
    onSkipToNextClick: () -> Unit,
    onSeek: (Long) -> Unit
) {

    Column(
        Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.2f))
    ) {
        MusicList(audioList = playlistItems, onClick = onItemSelected, currentPlayingIndex = 0)
        BottomPlayer(
            modifier = Modifier.fillMaxWidth(),
            currentDuration = playbackProgress.durationMillis,
            totalDurationInSeconds = playbackProgress.durationMillis.milliseconds.inWholeSeconds,
            totalDuration = playbackProgress.durationMillis.milliseconds.toString(),
            isBuffering = isBuffering,
            isPlaying = isPlaying,
            onPause = onPauseClick,
            onPlay = onPlayClick,
            onNext = onSkipToNextClick,
            onPrev = onSkipToPreviousClick,
            onSeek = onSeek,
        )
    }
}

@DevicePreviews
@Composable
private fun LoadingPlaceHolderPreview() {
    Media3_playerTheme {
        HomeScreen(
            listOf(),
            IAudioController.PlaybackProgress(),
            false,
            false,
            {},
            {},
            {},
            {},
            {},
            {}
        )
    }
}