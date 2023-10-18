package com.tabarkevych.media3_player.presentation.audio_player

import android.content.ComponentName
import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.core.content.ContextCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackParameters
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaBrowser
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.ListenableFuture
import com.tabarkevych.media3_player.domain.manager.IAudioController
import com.tabarkevych.media3_player.domain.mapper.toMediaItem
import com.tabarkevych.media3_player.domain.model.Song
import com.tabarkevych.media3_player.presentation.audio_player.service.PlaybackService
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runInterruptible
import javax.inject.Inject

@UnstableApi
class AudioController @Inject constructor(
    @ApplicationContext private val context: Context,
) : IAudioController {

    override val playbackProgressPercentFlow: Flow<IAudioController.PlaybackProgress>
        get() = _playbackProgressPercentFlow.asStateFlow()
    override val uiRepetitionCount: Flow<String>
        get() = _uiRepetitionCount
    override val playbackSpeedFlow: Flow<Float>
        get() = _playbackSpeedFlow
    override val playbackErrorFlow: Flow<Unit?>
        get() = _playbackErrorFlow
    override val playbackNetworkErrorFlow: Flow<Boolean>
        get() = _playbackNetworkErrorFlow
    override val showLoadingProgressFlow: Flow<Boolean>
        get() = _showLoadingProgressFlow
    override val isPlayingFlow: Flow<Boolean>
        get() = _isPlayingFlow
    override val isBufferingFlow: Flow<Boolean>
        get() = _isBufferingFlow

    override val isPlaying: Boolean
        get() = _isPlaying


    private val _playbackProgressPercentFlow: MutableStateFlow<IAudioController.PlaybackProgress> =
        MutableStateFlow(IAudioController.PlaybackProgress())

    private val _uiRepetitionCount: MutableStateFlow<String> =
        MutableStateFlow("")

    private val _playbackErrorFlow: MutableStateFlow<Unit?> =
        MutableStateFlow(null)

    private val _playbackNetworkErrorFlow: MutableSharedFlow<Boolean> =
        MutableSharedFlow()

    private val _playbackSpeedFlow: MutableStateFlow<Float> =
        MutableStateFlow(1f)

    private val _showLoadingProgressFlow: MutableStateFlow<Boolean> =
        MutableStateFlow(false)

    private val _isPlayingFlow: MutableStateFlow<Boolean> =
        MutableStateFlow(false)

    private val _isBufferingFlow: MutableStateFlow<Boolean> =
        MutableStateFlow(false)

    private var _isPlaying: Boolean = false


    private var browserFuture: ListenableFuture<MediaBrowser>? = null
    private val controller: MediaBrowser?
        get() = if (browserFuture?.isDone == true) browserFuture?.get() else null


    override fun initialize() {
        browserFuture =
            MediaBrowser.Builder(
                context, SessionToken(context, ComponentName(context, PlaybackService::class.java))
            ).buildAsync()

        browserFuture?.addListener(
            {
                controller?.addListener(playerListener)
                _isPlaying = controller?.isPlaying ?: false
            },
            ContextCompat.getMainExecutor(context)
        )
    }

    override fun destroy() {
        controller?.release()
        browserFuture?.let { MediaBrowser.releaseFuture(it) }
    }

    override fun stop() {
        controller?.stop()
    }

    override fun play() {
        controller?.play()
    }

    override fun play(song: Song) {
        controller?.setMediaItem(song.toMediaItem())
        controller?.prepare()
        controller?.play()
    }

    override fun pause() {
        controller?.pause()
    }

    override fun playNext() {
        controller?.seekToNext()
    }

    override fun playPrevious() {
        controller?.seekToPrevious()
    }

    override suspend fun updateRepetitionCount() {
        // controller?.repeatMode = Player.REPEAT_MODE_ALL
    }

    override suspend fun updatePlaybackSpeed() {
        controller?.setPlaybackSpeed(1f)
    }

    override fun setPlaybackProgress(progress: Long) {
        controller?.seekTo(progress)
    }

    private val playbackPositionHandler = Handler(Looper.getMainLooper())

    override fun startProgressTracking() {
        playbackPositionHandler.removeCallbacksAndMessages(null)
        scheduleProgressTracking()
    }

    private fun scheduleProgressTracking() {
        _playbackProgressPercentFlow.tryEmit(
            IAudioController.PlaybackProgress(
                controller?.duration ?: 0,
                controller?.currentPosition ?: 0
            )
        )
        playbackPositionHandler.postDelayed({
            if (controller?.isPlaying == true) {
                scheduleProgressTracking()
            }
        }, 250)
    }

    private fun stopProgressTracking() {
        _playbackProgressPercentFlow.tryEmit(
            IAudioController.PlaybackProgress(
                controller?.duration ?: 0,
                controller?.currentPosition ?: 0
            )
        )
        playbackPositionHandler.removeCallbacksAndMessages(null)
    }


    private val playerListener = object : Player.Listener {

        override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters) {
            _playbackSpeedFlow.update { playbackParameters.speed }
            super.onPlaybackParametersChanged(playbackParameters)
        }

        override fun onPlaybackStateChanged(playbackState: Int) {
          CoroutineScope(Dispatchers.Main).launch{
                when (playbackState) {
                    Player.STATE_BUFFERING -> {
                        _isBufferingFlow.emit(true)
                    }

                    Player.STATE_READY -> {
                        _isBufferingFlow.emit(false)
                        _showLoadingProgressFlow.update { false }
                    }

                    else -> {}
                }
                super.onPlaybackStateChanged(playbackState)
            }
        }

        override fun onIsLoadingChanged(isLoading: Boolean) {
            super.onIsLoadingChanged(isLoading)
            _showLoadingProgressFlow.update { isLoading }
        }

        override fun onIsPlayingChanged(isPlaying: Boolean) {
            if (isPlaying) {
                startProgressTracking()
            } else {
                stopProgressTracking()
            }
            _isPlayingFlow.update { isPlaying }
            _isPlaying = isPlaying
            super.onIsPlayingChanged(isPlaying)
        }

    }

}