package br.com.kmdev.randomlytalksapp.ui.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import br.com.kmdev.randomlytalksapp.R
import br.com.kmdev.randomlytalksapp.app.MainActivity
import br.com.kmdev.randomlytalksapp.databinding.ActivityOnboardingBinding
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {

            val pagerAdapter = ScreenSlidePagerAdapter(this@OnboardingActivity)
            pagerOnboarding.adapter = pagerAdapter
            pagerOnboarding.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    btActionJumpOnboarding.text =
                        getString(if (position < 2) R.string.label_action_jump_onboarding else R.string.label_action_finish_onboarding)
                }
            })
            TabLayoutMediator(tabOnboarding, pagerOnboarding) { _, _ -> }.attach()

            btActionJumpOnboarding.setOnClickListener {
                startActivity(Intent(this@OnboardingActivity, MainActivity::class.java))
                finish()
            }
        }
    }

    class ScreenSlidePagerAdapter(fragmentActivity: FragmentActivity) :
        FragmentStateAdapter(fragmentActivity) {

        override fun getItemCount(): Int = 3

        override fun createFragment(position: Int): Fragment {
            return OnboardingPageFragment().apply {
                arguments = bundleOf(
                    "ONBOARDING_PAGE" to position
                )
            }
        }
    }
}