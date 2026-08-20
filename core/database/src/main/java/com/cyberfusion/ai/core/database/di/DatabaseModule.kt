package com.cyberfusion.ai.core.database.di

import android.content.Context
import androidx.room.Room
import com.cyberfusion.ai.core.database.CyberFusionDatabase
import com.cyberfusion.ai.core.database.dao.CyberFusionDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideCyberFusionDatabase(
        @ApplicationContext context: Context
    ): CyberFusionDatabase {
        return Room.databaseBuilder(
            context,
            CyberFusionDatabase::class.java,
            "cyberfusion_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideCyberFusionDao(
        database: CyberFusionDatabase
    ): CyberFusionDao = database.cyberFusionDao()
}
