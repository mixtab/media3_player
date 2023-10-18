package com.tabarkevych.media3_player.di

import com.tabarkevych.media3_player.data.repository.PlayListRepository
import com.tabarkevych.media3_player.domain.repository.IPlayListRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindPlayListRepository(repository: PlayListRepository): IPlayListRepository
}
