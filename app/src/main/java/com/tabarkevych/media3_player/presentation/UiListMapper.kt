package com.tabarkevych.media3_player.presentation

import com.tabarkevych.media3_player.domain.model.Album
import com.tabarkevych.media3_player.domain.model.Song
import com.tabarkevych.media3_player.presentation.model.UiListItem


fun UiListItem.Song.toDomain(): Song {
    return Song(this.audioUrl, this.imageUrl, this.artists, this.title)

}

fun UiListItem.Album.toDomain(): Album {
    return Album(this.id, this.title, this.songs)

}

fun Song.toUi(): UiListItem.Song {
    return UiListItem.Song(this.audioUrl, this.imageUrl, this.artists, this.title)

}

fun Album.toUi(): UiListItem.Album {
    return UiListItem.Album(this.id, this.title, this.songs)

}


