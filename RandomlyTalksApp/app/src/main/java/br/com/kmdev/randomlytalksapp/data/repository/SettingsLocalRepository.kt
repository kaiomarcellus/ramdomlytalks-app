package br.com.kmdev.randomlytalksapp.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import timber.log.Timber
import java.io.IOException

class SettingsLocalRepository(
    private val dataStore: DataStore<Preferences>,
    private val gson: Gson
) : SettingsRepository {

    companion object {
        val USERNAME_KEY = stringPreferencesKey("dataUsername")
        val ONBOARDING_KEY = booleanPreferencesKey("dataOnboardingDone")
    }

    override suspend fun fetchUsername(): Flow<String> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Timber.e("Error > ${exception.message}")
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { prefs ->
            prefs[USERNAME_KEY] ?: ""
        }

    override suspend fun saveUsername(name: String) {
        dataStore.edit { prefs ->
            prefs[USERNAME_KEY] = name
        }
    }

    override suspend fun onboardingDone(): Boolean {
        val dataPrefs = dataStore.data.firstOrNull()?.toPreferences()
        return dataPrefs?.get(ONBOARDING_KEY) ?: false
    }

    override suspend fun setOnboardingAsDone() {
        dataStore.edit { prefs ->
            prefs[ONBOARDING_KEY] = true
        }
    }
}