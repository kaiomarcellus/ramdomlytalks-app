package br.com.kmdev.randomlytalksapp.ui.splash

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.kmdev.randomlytalksapp.data.repository.SettingsRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashViewModel(private val settingsRepository: SettingsRepository) : ViewModel() {

    val userOnboardDone = MutableLiveData<Boolean>()

    fun checkOnboarding() {
        viewModelScope.launch {
            delay(1000)
            val done = settingsRepository.onboardingDone()
            userOnboardDone.postValue(done)
        }
    }

    fun setOnboardingDone() {
        viewModelScope.launch { settingsRepository.setOnboardingAsDone() }
    }

}