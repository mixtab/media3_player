package com.tabarkevych.media3_player.presentation.screens.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.tabarkevych.media3_player.presentation.model.UiListItem
import com.tabarkevych.media3_player.presentation.screens.home.HomeViewModel

@Composable
fun AudioItem(
    song: UiListItem.Song,
    isPlaying: Boolean,
    onClick: () -> Unit
){
    val backgroundColor = if (isPlaying) Color.Blue else Color.DarkGray.copy(alpha = 0.3f)
    Row(
        modifier = Modifier
            .padding(vertical = 10.dp, horizontal = 10.dp)
            .background(backgroundColor, RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
        ,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ){
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(song.imageUrl)
                .crossfade(true)
                .build(),
            contentDescription = null,
            modifier = Modifier.padding(10.dp).size(50.dp).clip(RoundedCornerShape(15.dp))
        )
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(5.dp)){
            Text(song.title, fontSize = 16.sp, color = Color.White, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(song.artists, fontSize = 14.sp, fontWeight = FontWeight.Normal, color = Color.White)
        }
    }
}

@Composable
fun AlbumItem(
    song: UiListItem.Album,
    onClick: () -> Unit
){
    val backgroundColor = Color.DarkGray.copy(alpha = 0.3f)
    Row(
        modifier = Modifier
            .padding(vertical = 10.dp, horizontal = 10.dp)
            .background(backgroundColor, RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
        ,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ){
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(5.dp)){
            Text(song.title, fontSize = 16.sp, color = Color.White, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
    }
}