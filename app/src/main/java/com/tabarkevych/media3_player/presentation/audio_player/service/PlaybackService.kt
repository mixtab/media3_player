package com.tabarkevych.media3_player.presentation.audio_player.service

import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Intent
import androidx.media3.common.AudioAttributes
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.LibraryResult
import androidx.media3.session.LibraryResult.RESULT_ERROR_NOT_SUPPORTED
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.MediaSession
import com.google.common.collect.ImmutableList
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import com.tabarkevych.media3_player.MainActivity
import com.tabarkevych.media3_player.domain.mapper.buildMediaItem
import com.tabarkevych.media3_player.domain.mapper.toMediaItems
import com.tabarkevych.media3_player.domain.model.Album
import com.tabarkevych.media3_player.domain.repository.IPlayListRepository
import com.tabarkevych.media3_player.presentation.model.UiListItem
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@UnstableApi
@AndroidEntryPoint
class PlaybackService : MediaLibraryService() {

    @Inject
    lateinit var songsRepository: IPlayListRepository

    private lateinit var player: ExoPlayer
    private lateinit var mediaLibrarySession: MediaLibrarySession

    private val albums = mutableMapOf<String, List<MediaItem>>()
    var mainAlbums = listOf<Album>()

    override fun onCreate() {

        initializeSessionAndPlayer()
        super.onCreate()
    }


    override fun onDestroy() {
        super.onDestroy()
        mediaLibrarySession.run {
            release()
            if (player.playbackState != Player.STATE_IDLE) {
                player.seekTo(0)
                player.playWhenReady = false
                player.stop()
            }
        }
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaLibrarySession {
        return mediaLibrarySession
    }

    private fun initializeSessionAndPlayer() {
        player = ExoPlayer.Builder(this)
            .setAudioAttributes(AudioAttributes.DEFAULT, /* handleAudioFocus= */ true)
            .build()

        mediaLibrarySession =
            MediaLibrarySession.Builder(this, player, CustomMediaLibrarySessionCallback())
                .setSessionActivity(
                    PendingIntent.getActivity(
                        this, 0, Intent(this, MainActivity::class.java),
                        FLAG_IMMUTABLE or FLAG_UPDATE_CURRENT
                    )
                )
                .build()

        player.addListener(playerStateListener)
    }

    private inner class CustomMediaLibrarySessionCallback : MediaLibrarySession.Callback {

        override fun onConnect(
            session: MediaSession,
            controller: MediaSession.ControllerInfo
        ): MediaSession.ConnectionResult {
            val availableSessionCommands =
                MediaSession.ConnectionResult.DEFAULT_SESSION_AND_LIBRARY_COMMANDS.buildUpon()

            return MediaSession.ConnectionResult.AcceptedResultBuilder(session)
                .setAvailableSessionCommands(availableSessionCommands.build()).build()
        }

        override fun onGetLibraryRoot(
            session: MediaLibrarySession,
            browser: MediaSession.ControllerInfo,
            params: LibraryParams?
        ): ListenableFuture<LibraryResult<MediaItem>> {
            if (params != null && params.isRecent) {
                // The service currently does not support playback resumption. Tell System UI by returning
                // an error of type 'RESULT_ERROR_NOT_SUPPORTED' for a `params.isRecent` request. See
                // https://github.com/androidx/media/issues/355
                return Futures.immediateFuture(LibraryResult.ofError(RESULT_ERROR_NOT_SUPPORTED))
            }

            return Futures.immediateFuture(
                LibraryResult.ofItem(
                    buildMediaItem(
                        title = APP_ID,
                        mediaId = APP_ID,
                        isPlayable = false,
                        isBrowsable = true,
                        mediaType = MediaMetadata.MEDIA_TYPE_FOLDER_MIXED
                    ), params
                )
            )
        }


        override fun onGetChildren(
            session: MediaLibrarySession,
            browser: MediaSession.ControllerInfo,
            parentId: String,
            page: Int,
            pageSize: Int,
            params: LibraryParams?
        ): ListenableFuture<LibraryResult<ImmutableList<MediaItem>>> {
            val mediaList = mutableListOf<MediaItem>()

            when {

                parentId == APP_ID -> {
                    mainAlbums =   songsRepository.getPlayList()
                    mainAlbums.map { albums.put(it.title, it.songs.toMediaItems()) }
                    mediaList.addAll(mainAlbums.toMediaItems())
                }

                parentId.startsWith(ALBUM_ITEM_PREFIX) -> {
                    mediaList.addAll(songsRepository.getAlbum(parentId).songs.toMediaItems())
                }
            }

            return Futures.immediateFuture(
                LibraryResult.ofItemList(
                    mediaList, LibraryParams.Builder().build()
                )
            )
        }

        override fun onAddMediaItems(
            mediaSession: MediaSession,
            controller: MediaSession.ControllerInfo,
            mediaItems: MutableList<MediaItem>
        ): ListenableFuture<MutableList<MediaItem>> {
            if (albums.isEmpty()){
                mainAlbums = songsRepository.getPlayList()
                mainAlbums.map { albums.put(it.title, it.songs.toMediaItems()) }
            }

            val items =
                albums.filter { it.value.firstOrNull { mediaItem -> mediaItem.mediaId == mediaItems.first().mediaId } != null }.values.first()

            val item = items.first { it.mediaId == mediaItems.first().mediaId }

            return super.onAddMediaItems(mediaSession, controller, mutableListOf(item))
        }

    }


    private val playerStateListener = object : Player.Listener {

        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
            val items =
                albums.filter { it.value.firstOrNull { currentItem -> currentItem.mediaId == mediaItem?.mediaId } != null }.values.first()

            val index = items.indexOfFirst { it.mediaId == mediaItem?.mediaId }


            // Add the previous song in list to the playlist
            if (index != 0 && !player.hasPreviousMediaItem()) {
                player.addMediaItem(0, items[index - 1])
            }

            // Imitation of pagination, when player moving to the next track, we add 1 more track to the playlist
            if (!player.hasNextMediaItem() && index != items.size - 1) {
                albums.forEach { (_, albumItems) ->

                    if (albumItems.firstOrNull { it.mediaId == mediaItem?.mediaId } != null) {

                        player.addMediaItem(albumItems[index + 1])
                    }
                }
            }

            super.onMediaItemTransition(mediaItem, reason)
        }

    }

    companion object {

        const val APP_ID = "Media3 Player App"
        const val ALBUM_ITEM_PREFIX = "Album"
    }
}
