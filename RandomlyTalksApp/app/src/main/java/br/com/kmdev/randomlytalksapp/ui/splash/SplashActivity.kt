package br.com.kmdev.randomlytalksapp.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.kmdev.randomlytalksapp.R
import br.com.kmdev.randomlytalksapp.app.MainActivity
import br.com.kmdev.randomlytalksapp.ui.onboarding.OnboardingActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.viewModel

@ExperimentalCoroutinesApi
@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private val viewModel: SplashViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.userOnboardDone.observe(this) {
            if (it) startApp()
            else startOnboardingApp()
        }
        viewModel.checkOnboarding()
    }

    private fun startOnboardingApp() {
        viewModel.setOnboardingDone()
        startActivity(Intent(this@SplashActivity, OnboardingActivity::class.java))
        finish()
    }

    private fun startApp() {
        startActivity(Intent(this@SplashActivity, MainActivity::class.java))
        finish()
    }

}