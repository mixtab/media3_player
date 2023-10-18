package com.tabarkevych.media3_player.presentation.model

sealed class UiListItem() {
    data class Song(
        val audioUrl: String,
        val imageUrl: String,
        val artists: String,
        val title: String,
    ) : UiListItem()

    data class Album(
        val id: String,
        val title: String,
        val songs: List<com.tabarkevych.media3_player.domain.model.Song>,
    ) : UiListItem()
}