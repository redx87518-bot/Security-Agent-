package com.cyberfusion.ai.core.network.di

import com.cyberfusion.ai.core.network.orchestrator.AIOrchestrator
import com.cyberfusion.ai.core.network.orchestrator.OutputValidator
import com.cyberfusion.ai.core.network.orchestrator.ToolRegistry
import com.cyberfusion.ai.core.network.provider.AIProvider
import com.cyberfusion.ai.core.network.provider.AIProviderManager
import com.cyberfusion.ai.core.network.provider.IntelligenceProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideAIProviderManager(
        intelligenceProvider: IntelligenceProvider,
        aiProvider: AIProvider
    ): AIProviderManager {
        return AIProviderManager().also {
            it.registerProvider(intelligenceProvider)
            it.registerProvider(aiProvider)
        }
    }

    @Provides
    @Singleton
    fun provideToolRegistry(): ToolRegistry = ToolRegistry()

    @Provides
    @Singleton
    fun provideOutputValidator(): OutputValidator = OutputValidator()

    @Provides
    @Singleton
    fun provideAIOrchestrator(
        providerManager: AIProviderManager,
        toolRegistry: ToolRegistry,
        outputValidator: OutputValidator
    ): AIOrchestrator = AIOrchestrator(providerManager, toolRegistry, outputValidator)
}
