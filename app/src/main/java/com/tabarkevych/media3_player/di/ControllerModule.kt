package com.tabarkevych.media3_player.di

import com.tabarkevych.media3_player.domain.manager.IAudioController
import com.tabarkevych.media3_player.presentation.audio_player.AudioController
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class ControllerModule {

    @Binds
    @Singleton
    abstract fun bindAudioController(controller:AudioController): IAudioController
}
