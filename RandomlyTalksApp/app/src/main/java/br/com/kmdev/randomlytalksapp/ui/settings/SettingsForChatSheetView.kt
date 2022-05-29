package br.com.kmdev.randomlytalksapp.ui.settings

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import br.com.kmdev.randomlytalksapp.databinding.SheetViewSettingsForChatBinding
import com.maxkeppeler.sheets.core.Sheet
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinComponent

@ExperimentalCoroutinesApi
class SettingsForChatSheetView : Sheet(), KoinComponent {

    private val viewModel: SettingsForChatViewModel by viewModel()
    private lateinit var binding: SheetViewSettingsForChatBinding
    var actionCallback: (() -> Unit)? = null

    override fun onCreateLayoutView(): View {
        return SheetViewSettingsForChatBinding.inflate(LayoutInflater.from(activity))
            .also { binding = it }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        displayButtonsView(false)
        setupLayout()
        setupObservers()
    }

    private fun setupLayout() {
        binding.apply {
            btSettingsActionSave.setOnClickListener {
                val name = inputSettingsUserName.getContentText()
                if (name.isNotEmpty()) {
                    viewModel.saveUserName(name)
                    actionCallback?.invoke()
                } else
                    actionCallback?.invoke()
            }
        }
    }

    private fun setupObservers() {
        binding.apply {

            viewModel.savedUserName.observe(viewLifecycleOwner) {
                if (it.isNotEmpty()) inputSettingsUserName.text = it
            }
            viewModel.fetchUserName()
        }
    }

    fun show(
        context: Context,
        width: Int? = null,
        func: SettingsForChatSheetView.() -> Unit
    ): SettingsForChatSheetView {
        this.windowContext = context
        this.width = width
        this.func()
        this.show()
        return this
    }

}