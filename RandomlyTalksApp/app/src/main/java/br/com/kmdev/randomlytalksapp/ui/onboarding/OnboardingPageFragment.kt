package br.com.kmdev.randomlytalksapp.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.com.kmdev.randomlytalksapp.R
import br.com.kmdev.randomlytalksapp.databinding.FragmentOnboardingPageBinding

class OnboardingPageFragment : Fragment() {

    private lateinit var binding: FragmentOnboardingPageBinding
    private val page by lazy { arguments?.getInt("ONBOARDING_PAGE") ?: -1 }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        FragmentOnboardingPageBinding.inflate(inflater, container, false).also { binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            when (page) {
                0 -> {
                    tvOnboardingStep.text = getString(R.string.label_text_onboarding_one)
                    ivOnboardingStep.setImageResource(R.drawable.img_onboarding_step_01)
                }
                1 -> {
                    tvOnboardingStep.text = getString(R.string.label_text_onboarding_two)
                    ivOnboardingStep.setImageResource(R.drawable.img_onboarding_step_02)
                }
                else -> {
                    tvOnboardingStep.text = getString(R.string.label_text_onboarding_three)
                    ivOnboardingStep.setImageResource(R.drawable.img_onboarding_step_03)
                }
            }
        }
    }

}