package br.com.devjmcn.shoplist.util.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class PreferenceSumValuesImpl(private val context: Context):PreferencesSumValuesInterface {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "preferences")

    object PreferencesKey{
        val SUM_VALUES = booleanPreferencesKey("SUM_VALUES")
    }

    override suspend fun saveData(status: Boolean) {
        context.dataStore.edit {preferences ->
            preferences[PreferencesKey.SUM_VALUES] = status
        }
    }

    override fun getData(): Flow<Boolean?> {
        return context.dataStore.data.catch {exception ->
            if (exception is IOException){
                emit(emptyPreferences())
            }else{
                throw exception
            }
        }.map {
            it[PreferencesKey.SUM_VALUES]
        }
    }
}