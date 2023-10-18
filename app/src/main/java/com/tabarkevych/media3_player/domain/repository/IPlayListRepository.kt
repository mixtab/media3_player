package com.tabarkevych.media3_player.domain.repository

import com.tabarkevych.media3_player.domain.model.Album

interface IPlayListRepository {

    fun getPlayList():List<Album>

    fun getAlbum(id:String):Album
}