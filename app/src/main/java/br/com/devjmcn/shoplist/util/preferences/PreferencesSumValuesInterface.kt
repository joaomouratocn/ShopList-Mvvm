package br.com.devjmcn.shoplist.util.preferences

import kotlinx.coroutines.flow.Flow

interface PreferencesSumValuesInterface{
    suspend fun saveData(status:Boolean)
    fun getData(): Flow<Boolean?>
}