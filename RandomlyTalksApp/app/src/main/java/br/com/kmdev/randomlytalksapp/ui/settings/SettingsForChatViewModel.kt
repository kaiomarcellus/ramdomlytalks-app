package br.com.kmdev.randomlytalksapp.ui.settings

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.kmdev.randomlytalksapp.data.repository.SettingsRepository
import kotlinx.coroutines.launch

class SettingsForChatViewModel(private val settingsRepository: SettingsRepository) : ViewModel() {

    val savedUserName = MutableLiveData<String>()

    fun saveUserName(name: String) {
        viewModelScope.launch { settingsRepository.saveUsername(name) }
    }

    fun fetchUserName() {
        viewModelScope.launch {
            settingsRepository.fetchUsername().collect {
                savedUserName.value = it
            }
        }
    }

}