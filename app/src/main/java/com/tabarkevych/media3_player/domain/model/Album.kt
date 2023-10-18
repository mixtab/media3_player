package com.tabarkevych.media3_player.domain.model

data class Album (
    val id:String,
    val title:String,
    val songs:List<Song>
)