package com.cyberfusion.ai.core.network.di

import com.cyberfusion.ai.core.model.AIProviderConfig
import com.cyberfusion.ai.core.network.provider.AIProvider
import com.cyberfusion.ai.core.network.provider.HttpAIProvider
import com.cyberfusion.ai.core.network.provider.HttpIntelligenceProvider
import com.cyberfusion.ai.core.network.provider.IntelligenceProvider
import com.cyberfusion.ai.core.network.service.CyberFusionApi
import com.cyberfusion.ai.core.network.service.HttpCyberFusionApi
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
    fun provideCyberFusionApi(): CyberFusionApi {
        val apiKey = ""
        val baseUrl = "https://api.cyberfusion.ai"
        return HttpCyberFusionApi(baseUrl = baseUrl, apiKey = apiKey)
    }

    @Provides
    @Singleton
    fun provideIntelligenceProvider(): IntelligenceProvider {
        val apiKey = ""
        val providerId = "cyberfusion"
        return HttpIntelligenceProvider(
            providerId = providerId,
            displayName = "CyberFusion Intelligence",
            apiKey = apiKey,
            baseUrl = "https://api.cyberfusion.ai"
        )
    }

    @Provides
    @Singleton
    fun provideAIProviderConfig(): AIProviderConfig {
        val apiKey = ""
        val providerId = "cyberfusion"
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
    fun provideAIProvider(
        config: AIProviderConfig,
        api: CyberFusionApi
    ): AIProvider {
        return HttpAIProvider(config = config, api = api)
    }
}
