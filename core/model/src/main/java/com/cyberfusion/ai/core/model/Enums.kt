package com.cyberfusion.ai.core.model

enum class Severity {
    INFORMATIONAL,
    LOW,
    MEDIUM,
    HIGH,
    CRITICAL
}

enum class IndicatorType {
    IPV4,
    IPV6,
    DOMAIN,
    URL,
    SHA256,
    SHA1,
    MD5,
    EMAIL,
    UNKNOWN
}

enum class InvestigationStatus {
    NEW,
    INVESTIGATING,
    CONTAINED,
    ERADICATION,
    RECOVERY,
    CLOSED
}

enum class RiskTreatment {
    MITIGATE,
    ACCEPT,
    TRANSFER,
    AVOID
}
