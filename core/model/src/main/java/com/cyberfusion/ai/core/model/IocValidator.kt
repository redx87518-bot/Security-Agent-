package com.cyberfusion.ai.core.model

object IocValidator {
    sealed class ValidationResult {
        data class Valid(val normalizedValue: String, val type: IndicatorType) : ValidationResult()
        data class Invalid(val reason: String) : ValidationResult()
    }

    fun validate(input: String): ValidationResult {
        val trimmed = input.trim()
        if (trimmed.isEmpty()) return ValidationResult.Invalid("Input is empty")

        val ipv4Regex = Regex("^((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$")
        val ipv6Regex = Regex("^([0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$")
        val domainRegex = Regex("^([a-zA-Z0-9]([a-zA-Z0-9\\-]{0,61}[a-zA-Z0-9])?\\.)+[a-zA-Z]{2,}$")
        val urlRegex = Regex("^(https?://)?([\\w-]+\\.)+[\\w-]+(/[\\w./?%&=-]*)?$")
        val sha256Regex = Regex("^[A-Fa-f0-9]{64}$")
        val sha1Regex = Regex("^[A-Fa-f0-9]{40}$")
        val md5Regex = Regex("^[A-Fa-f0-9]{32}$")
        val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")

        return when {
            ipv4Regex.matches(trimmed) -> ValidationResult.Valid(trimmed, IndicatorType.IPV4)
            ipv6Regex.matches(trimmed) -> ValidationResult.Valid(trimmed, IndicatorType.IPV6)
            sha256Regex.matches(trimmed) -> ValidationResult.Valid(trimmed, IndicatorType.SHA256)
            sha1Regex.matches(trimmed) -> ValidationResult.Valid(trimmed, IndicatorType.SHA1)
            md5Regex.matches(trimmed) -> ValidationResult.Valid(trimmed, IndicatorType.MD5)
            emailRegex.matches(trimmed) -> ValidationResult.Valid(trimmed, IndicatorType.EMAIL)
            urlRegex.matches(trimmed) -> ValidationResult.Valid(trimmed, IndicatorType.URL)
            domainRegex.matches(trimmed) -> ValidationResult.Valid(trimmed, IndicatorType.DOMAIN)
            else -> ValidationResult.Invalid("Unrecognized indicator format")
        }
    }
}
