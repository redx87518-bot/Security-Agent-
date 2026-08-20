package com.cyberfusion.ai.core.network.provider

import com.cyberfusion.ai.core.model.AIProviderConfig
import com.cyberfusion.ai.core.model.Result
import com.cyberfusion.ai.core.security.SecurePreferences
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AIProviderManager @Inject constructor(
    private val securePreferences: SecurePreferences
) {
    private val providers = mutableMapOf<String, AIProvider>()

    fun registerProvider(provider: AIProvider) {
        providers[provider.providerId] = provider
    }

    suspend fun getProvider(providerId: String): AIProvider? = providers[providerId]

    suspend fun getAvailableProvider(): AIProvider? {
        val storedProviderId = securePreferences.aiProvider.first()
        return if (storedProviderId != null) {
            providers[storedProviderId]
        } else {
            providers.values.firstOrNull { it.isAvailable() }
        }
    }

    suspend fun testProvider(providerId: String): Result<Boolean> {
        val provider = providers[providerId] ?: return Result.Error(Exception("Provider not found"))
        return provider.testConnection()
    }
}
