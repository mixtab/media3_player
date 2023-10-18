package com.tabarkevych.media3_player.domain.model

data class Song(
    val audioUrl: String,
    val imageUrl: String,
    val artists: String,
    val title: String,
)