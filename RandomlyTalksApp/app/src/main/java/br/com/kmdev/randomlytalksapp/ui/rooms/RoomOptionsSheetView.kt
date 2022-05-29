package br.com.kmdev.randomlytalksapp.ui.rooms

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import br.com.kmdev.randomlytalksapp.R
import br.com.kmdev.randomlytalksapp.databinding.SheetViewRoomOptionsBinding
import com.maxkeppeler.sheets.core.Sheet
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.core.component.KoinComponent

@ExperimentalCoroutinesApi
class RoomOptionsSheetView : Sheet(), KoinComponent {

    private lateinit var binding: SheetViewRoomOptionsBinding
    private var fixOnTop: Boolean = false
    private var hideFromList: Boolean = false
    private var justClose: Boolean = true
    var actionCallback: ((fix: Boolean, hide: Boolean, justClose: Boolean) -> Unit)? = null

    override fun onCreateLayoutView(): View {
        return SheetViewRoomOptionsBinding.inflate(LayoutInflater.from(activity))
            .also { binding = it }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        displayButtonsView(false)
        setupLayout()
    }

    private fun setupLayout() {
        binding.apply {

            radioGroupRoomOptions.setOnCheckedChangeListener { _, selectedId ->
                fixOnTop = selectedId == R.id.rbOptionFixedOnTop
                hideFromList = selectedId == R.id.rbOptionHideFromList
                justClose = false
            }

            btRoomOptionsActionConfirm.setOnClickListener {
                actionCallback?.invoke(fixOnTop, hideFromList, justClose)
            }
        }
    }

    fun show(
        context: Context,
        width: Int? = null,
        func: RoomOptionsSheetView.() -> Unit
    ): RoomOptionsSheetView {
        this.windowContext = context
        this.width = width
        this.func()
        this.show()
        return this
    }

}