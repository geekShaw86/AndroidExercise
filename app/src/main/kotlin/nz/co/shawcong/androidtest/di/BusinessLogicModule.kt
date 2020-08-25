package nz.co.shawcong.androidtest.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import nz.co.shawcong.androidtest.dispatcher.DefaultDispatcherProvider
import nz.co.shawcong.androidtest.dispatcher.DispatcherProvider
import nz.co.shawcong.androidtest.manager.ConfigManager
import javax.inject.Singleton

/**
 *
 * Created by Shaw Cong on 25/08/20
 */
@InstallIn(ApplicationComponent::class)
@Module
object BusinessLogicModule {

    @Singleton
    @Provides
    fun provideConfigManager(): ConfigManager = ConfigManager()

    @Singleton
    @Provides
    fun provideCoroutineDispatcherProvider(): DispatcherProvider = DefaultDispatcherProvider()
}