package com.tabarkevych.media3_player.presentation.screens.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tabarkevych.media3_player.R

@Composable
fun BottomPlayer(
    modifier: Modifier = Modifier,
    currentDuration: Long,
    totalDurationInSeconds: Long,
    totalDuration: String,
    isBuffering: Boolean,
    isPlaying: Boolean,
    onPause: () -> Unit,
    onPlay: () -> Unit,
    onNext: () -> Unit,
    onPrev: () -> Unit,
    onSeek: (Long) -> Unit
){
    Column(modifier = modifier.background(Color.Black).padding(horizontal = 20.dp), horizontalAlignment = Alignment.CenterHorizontally){
        SeekBarAndDuration(
            modifier = modifier,
            isPlaying = isPlaying,
            currentDuration = currentDuration,
            totalDurationInSeconds = totalDurationInSeconds,
            totalDuration = totalDuration,
            onSeek = onSeek
        )
        Controls(
            modifier = Modifier.padding(vertical = 10.dp).align(Alignment.CenterHorizontally),
            isBuffering = isBuffering,
            isPlaying = isPlaying,
            onPause = onPause,
            onPlay = onPlay,
            onNext = onNext,
            onPrev = onPrev
        )
    }
}

@Composable
fun Controls(
    modifier: Modifier = Modifier,
    isBuffering: Boolean,
    isPlaying: Boolean,
    onPause: () -> Unit,
    onPlay: () -> Unit,
    onNext: () -> Unit,
    onPrev: () -> Unit
){
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(15.dp), verticalAlignment = Alignment.CenterVertically){

        IconButton(onClick = onPrev,
            Modifier.size(35.dp).background(Color.DarkGray.copy(alpha = 0.5f), RoundedCornerShape(15.dp))
        ){
            Icon(
                painter = painterResource(R.drawable.ic_skip_previous),
                contentDescription = "",
                modifier = Modifier.size(24.dp),
                tint = Color.White
            )
        }
        Box(Modifier.size(70.dp)){
            if (!isBuffering){
                Button(onClick = { if (isPlaying) onPause() else onPlay() },
                    modifier = Modifier.size(70.dp),
                    shape = RoundedCornerShape(25.dp),
                    colors = ButtonDefaults.buttonColors(Color.Blue)
                ){
                    Icon(
                        painter = if (isPlaying) painterResource(R.drawable.ic_pause) else painterResource(R.drawable.ic_play),
                        contentDescription = "",
                        modifier = Modifier.size(50.dp),
                        tint = Color.White
                    )
                }
            }else{
                CircularProgressIndicator(color = Color.Blue, modifier = Modifier.align(Alignment.Center))
            }
        }
        IconButton(onClick = onNext,
            Modifier.size(35.dp).background(Color.DarkGray.copy(alpha = 0.5f), RoundedCornerShape(15.dp))
        ){
            Icon(
                painter = painterResource(R.drawable.ic_skip_next),
                contentDescription = "",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun SeekBarAndDuration(
    modifier: Modifier = Modifier,
    isPlaying: Boolean,
    currentDuration: Long,
    totalDurationInSeconds: Long,
    totalDuration: String,
    onSeek: (Long) -> Unit
){
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically){
        Text(parseDurationToTime(currentDuration, "", false), fontSize = 14.sp, color = Color.White)
        Slider(
            parseDurationToFloat(currentDuration, totalDurationInSeconds, totalDuration, isPlaying),
            modifier = Modifier.weight(1f),
            onValueChange = {
                val seekTime = parseFloatToDuration(it, totalDurationInSeconds, totalDuration, isPlaying)
                onSeek(seekTime)
            },
            colors = SliderDefaults.colors(thumbColor = Color.White, inactiveTrackColor = Color.Gray, activeTrackColor = Color.Blue)
        )
        Text(parseDurationToTime(totalDurationInSeconds, totalDuration, isPlaying), fontSize = 14.sp, color = Color.White)
    }
}

private fun parseDurationToTime(totalDuration: Long, otherTotalDuration: String, isPlaying: Boolean): String {
    val seconds = if (isPlaying && totalDuration == 0L) convertTimeToSeconds(otherTotalDuration) else totalDuration
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    val formattedSeconds = if (remainingSeconds < 10) "0$remainingSeconds" else remainingSeconds.toString()
    return "$minutes:$formattedSeconds"
}
private fun parseDurationToFloat(currentDuration: Long, max: Long, otherMax: String, isPlaying: Boolean): Float{
    val newMax =  if (isPlaying && max == 0L) convertTimeToSeconds(otherMax) else max
    val percentage =  (currentDuration.toFloat() / newMax.toFloat()).coerceIn(0f, 1f)
    return if (percentage.isNaN()) 0f else percentage
}
private fun parseFloatToDuration(value: Float, max: Long, otherMax: String, isPlaying: Boolean): Long{
    val newMax = if (isPlaying && max == 0L) convertTimeToSeconds(otherMax) else max
    return (newMax * value).toLong()
}
fun convertTimeToSeconds(time: String): Long {
    val parts = time.split(":")
    val minutes = parts[0].toLong()
    val seconds = parts[1].toLong()
    return (minutes * 60 + seconds) // total seconds
}