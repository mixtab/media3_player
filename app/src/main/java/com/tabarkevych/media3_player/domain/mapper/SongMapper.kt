package com.tabarkevych.media3_player.domain.mapper

import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.tabarkevych.media3_player.domain.model.Song

fun Song.toMediaItem() =
    MediaItem.Builder()
        .setMediaMetadata(
            MediaMetadata.Builder()
                .setArtworkUri(Uri.parse(imageUrl))
                .setTitle(this.title)
                .setArtist(this.artists)
                .setIsPlayable(true)
                .setIsBrowsable(false)
                .setMediaType(MediaMetadata.MEDIA_TYPE_MUSIC)
                .build()
        )
        .setUri(this.audioUrl)
        .setMediaId(this.audioUrl)
        .build()

fun List<Song>.toMediaItems() = this.map { it.toMediaItem() }


fun buildMediaItem(
    title: String,
    subtitle: String? = null,
    isPlayable: Boolean,
    isBrowsable: Boolean,
    mediaType: @MediaMetadata.MediaType Int,
    mediaId: String,
    albumArt: Uri? = null,
    audioTrackUri: Uri? = null,

): MediaItem {
    val metadata =
        MediaMetadata.Builder()
            .setTitle(title)
            .setSubtitle(subtitle)
            .setIsPlayable(isPlayable)
            .setIsBrowsable(isBrowsable)
            .setMediaType(mediaType)
            .setArtist(subtitle)
            .setArtworkUri(albumArt)
            .build()

    return MediaItem.Builder()
        .setMediaId(mediaId)
        .setMediaMetadata(metadata)
        .setUri(audioTrackUri)
        .build()
}