package br.com.kmdev.randomlytalksapp.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import org.koin.core.component.KoinComponent

class DataStoreManager(private val context: Context, private val prefsName: String) :
    KoinComponent {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = prefsName)
    fun getDataStore() = context.dataStore

}