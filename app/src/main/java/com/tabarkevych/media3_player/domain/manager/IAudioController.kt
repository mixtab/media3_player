package com.tabarkevych.media3_player.domain.manager

import com.tabarkevych.media3_player.domain.model.Song
import kotlinx.coroutines.flow.Flow

interface IAudioController {

    val playbackProgressPercentFlow: Flow<PlaybackProgress>


    val uiRepetitionCount: Flow<String>

    val playbackSpeedFlow: Flow<Float>

    val playbackErrorFlow: Flow<Unit?>

    val playbackNetworkErrorFlow: Flow<Boolean>

    val showLoadingProgressFlow: Flow<Boolean>

    val isPlayingFlow: Flow<Boolean>

    val isBufferingFlow: Flow<Boolean>

    val isPlaying: Boolean

    fun initialize()

    fun destroy()

    fun stop()

    fun play()

    fun play(song: Song)

    fun pause()

    fun playNext()

    fun playPrevious()

    suspend fun updateRepetitionCount()

    suspend fun updatePlaybackSpeed()

    fun setPlaybackProgress(progress: Long)

    fun startProgressTracking()

    data class PlaybackProgress(
        val durationMillis: Long = 0,
        val positionMillis: Long = 0,
        val playingChapter: Long = 0,
        val playingVerse: Long = 0,
        val playingVerseId: Long = 0,
        val isUiPlayerExpanded: Boolean = false
    ) {
        val progressPercent: Long =
            if (durationMillis > 0) positionMillis * 100L / durationMillis else 0L
    }
}