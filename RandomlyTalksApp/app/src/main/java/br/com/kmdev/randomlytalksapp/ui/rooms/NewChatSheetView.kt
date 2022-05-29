package br.com.kmdev.randomlytalksapp.ui.rooms

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import br.com.kmdev.randomlytalksapp.R
import br.com.kmdev.randomlytalksapp.databinding.SheetViewNewChatBinding
import br.com.kmdev.ui.components.toast
import com.maxkeppeler.sheets.core.Sheet
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.core.component.KoinComponent

@ExperimentalCoroutinesApi
class NewChatSheetView : Sheet(), KoinComponent {

    private lateinit var binding: SheetViewNewChatBinding
    var actionCallback: ((chatName: String) -> Unit)? = null

    override fun onCreateLayoutView(): View {
        return SheetViewNewChatBinding.inflate(LayoutInflater.from(activity))
            .also { binding = it }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        displayButtonsView(false)
        setupLayout()
    }

    private fun setupLayout() {
        binding.apply {
            btNewChatAction.setOnClickListener {
                val name = inputNewChatName.getContentText()
                if (name.isEmpty()) {

                    inputNewChatName.toast(getString(R.string.title_label_set_chat_name))

                } else
                    actionCallback?.invoke(name)
            }
        }
    }

    fun show(
        context: Context,
        width: Int? = null,
        func: NewChatSheetView.() -> Unit
    ): NewChatSheetView {
        this.windowContext = context
        this.width = width
        this.func()
        this.show()
        return this
    }

}