package com.cyberfusion.ai.core.database.converter

import androidx.room.TypeConverter
import com.cyberfusion.ai.core.model.Severity

class Converters {
    @TypeConverter
    fun fromSeverity(severity: Severity): String = severity.name

    @TypeConverter
    fun toSeverity(value: String): Severity = Severity.valueOf(value)
}
