package kinyua.vivian.common.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kinyua.vivian.common.DefaultDispatchProvider
import kinyua.vivian.common.DispatcherProvider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DispatcherModule {

    @Binds
    @Singleton
    abstract fun bindDispatcherProvider(
        defaultDispatchProvider: DefaultDispatchProvider
    ): DispatcherProvider
}