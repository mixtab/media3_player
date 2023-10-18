package com.tabarkevych.media3_player.domain.mapper

import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.tabarkevych.media3_player.domain.model.Album

fun Album.toMediaItem() =
    MediaItem.Builder()
        .setMediaMetadata(
            MediaMetadata.Builder()
                .setTitle(this.title)
                .setIsPlayable(false)
                .setIsBrowsable(true)
                .setMediaType(MediaMetadata.MEDIA_TYPE_ALBUM)
                .build()
        )
        .setMediaId(this.id)
        .build()

fun List<Album>.toMediaItems() = this.map { it.toMediaItem() }