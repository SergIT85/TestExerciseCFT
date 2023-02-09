package ru.sergsports.androidcource.testexercisecft

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ru.sergsports.androidcource.testexercisecft.data.dto.BinInsertObject
import ru.sergsports.androidcource.testexercisecft.data.network.BinApiClient
import ru.sergsports.androidcource.testexercisecft.data.roomdata.BinDatabase
import ru.sergsports.androidcource.testexercisecft.data.roomdata.BinEntity
import ru.sergsports.androidcource.testexercisecft.databinding.ActivityMainBinding
import timber.log.Timber

class MainActivity : AppCompatActivity() {


    lateinit var binding: ActivityMainBinding

    private val adapter by lazy {
        GroupAdapter<GroupieViewHolder>()
    }


    val compositeDisposable = CompositeDisposable()




    @SuppressLint("SetTextI18n", "CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val databaseBin = BinDatabase.get(applicationContext)

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
                        binding.scheme.text = result.scheme?: "Null"
                        binding.type.text = result.type?: "Null"
                        binding.brand.text = result.brand?: "Null"
                        binding.country.text = result.country.name?: "Null"
                        binding.bank.text = result.bank.name ?: "Неизвестно"
                        var lun = R.string.placeholder.toString()
                        if (result.number.luhn) {
                            lun = "Да"
                        } else {
                            lun = "Нет"
                        }
                        binding.number.text = "Длинна:${result.number.length.toString()}" +
                                "  Лун:${lun}"
                        var prep = R.string.placeholder.toString()
                        if (result.prepaid) {
                            prep = "Да"
                            binding.prepaid.text = prep

                        } else {
                            prep = "Нет"
                            binding.prepaid.text = prep

                        }

                        val binIEntity = BinEntity(
                        numberLength = result.number.length ?: 0,
                        numberLuhn =  lun ?: "Null",
                        country = result.country.name?: "Null",
                        scheme = result.scheme?: "Null",
                        type = result.type?: "Null",
                        brand = result.brand?: "Null",
                        prepaid = prep?: "Null",
                        bankName = result.bank.name?: "Null",
                        bankUrl = result.bank.url?: "Null",
                        bankPhone = result.bank.phone?: "Null",
                        bankCity = result.bank.city?: "Null"
                        )

                        databaseBin.BinDao().save(binIEntity)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({databaseBin.BinDao().save(binIEntity)},
                                {
                                        error -> Timber.tag("TAGERROR").e(error.toString())
                                })


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