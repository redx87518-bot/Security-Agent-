package com.cyberfusion.ai.core.common

interface Repository<T> {
    fun getAll(): kotlinx.coroutines.flow.Flow<List<T>>
    suspend fun getById(id: String): T?
    suspend fun insert(item: T)
    suspend fun update(item: T)
    suspend fun delete(item: T)
}
