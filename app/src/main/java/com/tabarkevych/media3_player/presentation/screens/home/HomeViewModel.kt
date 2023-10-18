package com.tabarkevych.media3_player.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tabarkevych.media3_player.domain.manager.IAudioController
import com.tabarkevych.media3_player.domain.model.Song
import com.tabarkevych.media3_player.domain.repository.IPlayListRepository
import com.tabarkevych.media3_player.presentation.model.UiListItem
import com.tabarkevych.media3_player.presentation.toDomain
import com.tabarkevych.media3_player.presentation.toUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val playListRepository: IPlayListRepository,
    private val audioController: IAudioController
) : ViewModel() {


    val currentSelectedAlbumState = MutableSharedFlow<List<UiListItem>>(1)


    val playbackProgressPercentFlow: Flow<IAudioController.PlaybackProgress> =
        audioController.playbackProgressPercentFlow.onStart {
            audioController.startProgressTracking()
        }

    val isPlayingFlow: Flow<Boolean> = audioController.isPlayingFlow

    val isBufferingFlow: Flow<Boolean> = audioController.isPlayingFlow


    init {
        audioController.initialize()
        viewModelScope.launch {
            val playlist =
                playListRepository.getPlayList().map { UiListItem.Album(it.id, it.title, it.songs) }

            currentSelectedAlbumState.emit(playlist)
        }
    }


    fun handleSelectedItem(item: UiListItem) {
        viewModelScope.launch {
            when (item) {
                is UiListItem.Album -> {
                    currentSelectedAlbumState.emit(item.songs.map { it.toUi() })
                }

                is UiListItem.Song -> {
                    play(item.toDomain())
                }
            }
        }
    }

    fun playPause() {
        if (audioController.isPlaying)
            audioController.play()
        else
            audioController.pause()
    }

    fun play(song: Song) {
        audioController.play(song)
    }


    fun playNext() {
        audioController.playNext()
    }

    fun playPrevious() {
        audioController.playPrevious()
    }

    fun seekTo(time: Long) {
        audioController.setPlaybackProgress(time)
    }

    override fun onCleared() {
        audioController.destroy()
        super.onCleared()
    }

}