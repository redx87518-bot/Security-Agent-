package com.cyberfusion.ai.core.security

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SecurityModule {
    @Provides
    @Singleton
    fun provideSecurePreferences(
        context: android.content.Context
    ): SecurePreferences {
        return SecurePreferences(context)
    }
}
