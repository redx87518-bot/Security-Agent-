package com.cyberfusion.ai.core.network.di

import com.cyberfusion.ai.core.model.AIProviderConfig
import com.cyberfusion.ai.core.network.provider.AIProvider
import com.cyberfusion.ai.core.network.provider.HttpAIProvider
import com.cyberfusion.ai.core.network.provider.HttpIntelligenceProvider
import com.cyberfusion.ai.core.network.provider.IntelligenceProvider
import com.cyberfusion.ai.core.network.service.CyberFusionApi
import com.cyberfusion.ai.core.network.service.HttpCyberFusionApi
import com.cyberfusion.ai.core.security.SecurePreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProviderModule {

    @Provides
    @Singleton
    suspend fun provideCyberFusionApi(
        securePreferences: SecurePreferences
    ): CyberFusionApi {
        val apiKey = securePreferences.aiApiKey.first() ?: ""
        val baseUrl = "https://api.cyberfusion.ai"
        return HttpCyberFusionApi(baseUrl = baseUrl, apiKey = apiKey)
    }

    @Provides
    @Singleton
    suspend fun provideIntelligenceProvider(
        securePreferences: SecurePreferences
    ): IntelligenceProvider {
        val apiKey = securePreferences.aiApiKey.first() ?: ""
        val providerId = securePreferences.aiProvider.first() ?: "cyberfusion"
        return HttpIntelligenceProvider(
            providerId = providerId,
            displayName = "CyberFusion Intelligence",
            apiKey = apiKey,
            baseUrl = "https://api.cyberfusion.ai"
        )
    }

    @Provides
    @Singleton
    suspend fun provideAIProviderConfig(
        securePreferences: SecurePreferences
    ): AIProviderConfig {
        val apiKey = securePreferences.aiApiKey.first() ?: ""
        val providerId = securePreferences.aiProvider.first() ?: "cyberfusion"
        return AIProviderConfig(
            providerId = providerId,
            displayName = "CyberFusion AI",
            model = "cyberfusion-v1",
            apiKey = apiKey,
            timeoutSeconds = 30
        )
    }

    @Provides
    @Singleton
    suspend fun provideAIProvider(
        config: AIProviderConfig,
        api: CyberFusionApi
    ): AIProvider {
        return HttpAIProvider(config = config, api = api)
    }
}
