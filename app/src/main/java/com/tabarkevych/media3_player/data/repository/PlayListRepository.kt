package com.tabarkevych.media3_player.data.repository

import com.tabarkevych.media3_player.domain.model.Album
import com.tabarkevych.media3_player.domain.model.Song
import com.tabarkevych.media3_player.domain.repository.IPlayListRepository
import javax.inject.Inject

class PlayListRepository @Inject constructor(): IPlayListRepository {

   private val playlist = listOf(
       Album(
           "Album 1",
           "Album 1",
           listOf(
               Song(
                   audioUrl = "https://cdn.pixabay.com/download/audio/2022/12/13/audio_a7eaf8e68b.mp3",
                   imageUrl = "https://storage.googleapis.com/uamp/The_Kyoto_Connection_-_Wake_Up/art.jpg",
                   artists = "Setze",
                   title = "Thinkin About You (Radio Edit)",
               ),
               Song(
                   audioUrl = "https://cdn.pixabay.com/download/audio/2022/04/29/audio_b41f9553b2.mp3",
                   imageUrl = "https://storage.googleapis.com/uamp/The_Kyoto_Connection_-_Wake_Up/art.jpg",
                   artists = "Leonell Cassio",
                   title = "Stuck In A Dream",
               ),
               Song(
                   audioUrl = "https://cdn.pixabay.com/download/audio/2022/01/26/audio_2694da1938.mp3",
                   imageUrl = "https://storage.googleapis.com/uamp/The_Kyoto_Connection_-_Wake_Up/art.jpg",
                   artists = "Gvidon",
                   title = "Success Starts With A Dream",
               ),
               Song(
                   audioUrl = "https://naijaloaded.store/assets/uploads/Asake-2-30.mp3",
                   imageUrl = "https://storage.googleapis.com/uamp/The_Kyoto_Connection_-_Wake_Up/art.jpg",
                   artists = "Asake",
                   title = "2:30",
               )
           )
       ),


       Album(
           "Album 2",
           "Album 2",
           listOf(
               Song(
                   audioUrl = "https://naijaloaded.store/wp-content/uploads/2022/02/Rema-%E2%80%93-Calm-Down.mp3",
                   imageUrl = "https://storage.googleapis.com/uamp/The_Kyoto_Connection_-_Wake_Up/art.jpg",
                   artists = "Rema",
                   title = "Calm down",
               ),
               Song(
                   audioUrl = "https://cdn.pixabay.com/download/audio/2023/04/12/audio_6922b1de38.mp3",
                   imageUrl = "https://storage.googleapis.com/uamp/The_Kyoto_Connection_-_Wake_Up/art.jpg",
                   artists = "Matthew Mark",
                   title = "Happy Tears",
               ),
               Song(
                   audioUrl = "https://cdn.pixabay.com/download/audio/2022/03/23/audio_41f668f967.mp3",
                   imageUrl = "https://storage.googleapis.com/uamp/The_Kyoto_Connection_-_Wake_Up/art.jpg",
                   artists = "Norished",
                   title = "Irish Sitar",
               ),
               Song(
                   audioUrl = "https://cdn.pixabay.com/download/audio/2022/11/06/audio_c1e6eebf61.mp3",
                   imageUrl = "https://storage.googleapis.com/uamp/The_Kyoto_Connection_-_Wake_Up/art.jpg",
                   artists = "SoulProd",
                   title = "Game",
               ),
               Song(
                   audioUrl = "https://cdn.pixabay.com/download/audio/2022/12/28/audio_e232e79ed8.mp3",
                   imageUrl = "https://storage.googleapis.com/uamp/The_Kyoto_Connection_-_Wake_Up/art.jpg",
                   artists = "SoulProd",
                   title = "Against the stream",
               )
           )
       )
   )


    override fun getPlayList(): List<Album> {
        return playlist
    }

    override fun getAlbum(id: String): Album {
       return playlist.first { it.id == id }
    }



}