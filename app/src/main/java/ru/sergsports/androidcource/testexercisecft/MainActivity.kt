package ru.sergsports.androidcource.testexercisecft

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ru.sergsports.androidcource.testexercisecft.data.network.BinApiClient
import ru.sergsports.androidcource.testexercisecft.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {


    lateinit var binding: ActivityMainBinding

    private val adapter by lazy {
        GroupAdapter<GroupieViewHolder>()
    }

    val compositeDisposable = CompositeDisposable()


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //binding.editTextTextPersonName2.afterTextChanged
        val startRequest = binding.buttonStartRequest
        startRequest.setOnClickListener{
            val editText = binding.editTextTextPersonName2.text.toString()
            if (editText.isNotEmpty()){
                val getBinIfo = BinApiClient.apiClient.getBinIfo(editText)
                compositeDisposable.add(getBinIfo
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { result ->
                        binding.scheme.text = result.scheme
                        binding.type.text = result.type
                        binding.brand.text = result.brand
                        var lun = R.string.placeholder.toString()
                        if (result.number.luhn) {
                            lun = "Да"
                        } else {
                            lun = "Нет"
                        }
                        binding.number.text = "Длинна:${result.number.length.toString()}" +
                                "  Лун:${lun}"
                        if (result.prepaid) {
                            binding.prepaid.text = "Да"
                        } else {
                            binding.prepaid.text = "Нет"
                        }

                    }

                )
            }  else {

            }


        }

    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
}