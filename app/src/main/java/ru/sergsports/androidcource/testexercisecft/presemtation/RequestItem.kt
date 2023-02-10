package ru.sergsports.androidcource.testexercisecft.presemtation

import android.util.Log
import android.view.View
import com.xwray.groupie.viewbinding.BindableItem
import ru.sergsports.androidcource.testexercisecft.R
import ru.sergsports.androidcource.testexercisecft.data.roomdata.BinEntity
import ru.sergsports.androidcource.testexercisecft.databinding.SimpleRequestBinding

class RequestItem(
    private val content: BinEntity,
    private val onClick: (binInsertObject: BinEntity) -> Unit
) : BindableItem<SimpleRequestBinding>() {
    override fun bind(viewBinding: SimpleRequestBinding, position: Int) {
        viewBinding.idRequest.text = content.id.toString()
        viewBinding.country.text = content.country
        viewBinding.scheme.text = content.scheme
        viewBinding.bankName.text = content.bankName
        Log.d("TAG", "START RequestItem----------------------------------")
        viewBinding.simpleRequest.setOnClickListener {
            onClick.invoke(content)
        }
    }
    override fun getLayout(): Int = R.layout.simple_request

    override fun initializeViewBinding(view: View) = SimpleRequestBinding.bind(view)
}