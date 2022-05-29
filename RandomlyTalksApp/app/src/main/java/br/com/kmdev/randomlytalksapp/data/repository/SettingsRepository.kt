package br.com.kmdev.randomlytalksapp.data.repository

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {

    suspend fun fetchUsername(): Flow<String>

    suspend fun saveUsername(name: String)

    suspend fun onboardingDone(): Boolean

    suspend fun setOnboardingAsDone()

}