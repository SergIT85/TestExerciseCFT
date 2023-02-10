package ru.sergsports.androidcource.testexercisecft

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ru.sergsports.androidcource.testexercisecft.data.network.BinApiClient
import ru.sergsports.androidcource.testexercisecft.data.roomdata.BinDatabase
import ru.sergsports.androidcource.testexercisecft.data.roomdata.BinEntity
import ru.sergsports.androidcource.testexercisecft.databinding.ActivityMainBinding
import ru.sergsports.androidcource.testexercisecft.domain.extSingle
import ru.sergsports.androidcource.testexercisecft.presemtation.RequestItem
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

        var arrayList: ArrayList<RequestItem> = ArrayList()
        databaseBin.BinDao().getBinEntity()
            .extSingle()
            .subscribe({ bin ->
                arrayList.clear()
                arrayList = bin.map { item->
                    RequestItem(item) {action ->
                        openRequest(action)
                    }
                } as ArrayList<RequestItem>

                Log.d("TAG", "START RECycle----------------------------------")

                binding.saveRequestRecyclerView.adapter = adapter.apply { addAll(arrayList) }
            },
                {
                        error -> Timber.tag("TAGERROR").e(error.toString())
                })

        startRequest.setOnClickListener{
            val editText = binding.editTextTextPersonName2.text.toString()
            if (editText.isNotEmpty()){
                val getBinIfo = BinApiClient.apiClient.getBinIfo(editText)
                compositeDisposable.add(getBinIfo
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { result ->
                        if (result != null) {
                            binding.scheme.text = result.scheme?: "Null"
                            binding.type.text = result.type?: "Null"
                            binding.brand.text = result.brand?: "Null"
                            if(result.country != null) {
                                binding.country.text = result?.country?.name?: "Null"
                            } else{
                                binding.country.text = applicationContext.getText(R.string.placeholder)
                            }
                            if(result.bank != null) {
                                binding.bank.text = result?.bank?.name ?: "Неизвестно"
                                binding.bankPhone.text = result?.bank?.phone ?: "Неизвестно"
                            } else {
                                binding.bank.text = applicationContext.getText(R.string.placeholder)
                                binding.bankPhone.text = applicationContext.getText(R.string.placeholder)
                            }

                            var lun = R.string.placeholder.toString()
                            if(result.number != null) {
                                if (result.number.luhn) {
                                    lun = "Да"
                                } else {
                                    lun = "Нет"
                                }
                            }

                            binding.number.text = "Длинна:${result.number.length.toString()}" +
                                    "  Лун:${lun}"
                            var prep = R.string.placeholder.toString()
                            if (result.prepaid != null && result.prepaid) {
                                prep = "Да"
                                binding.prepaid.text = prep

                            } else {
                                prep = "Нет"
                                binding.prepaid.text = prep

                            }

                            val binIEntity = BinEntity(
                                numberLength = result?.number?.length ?: 0,
                                numberLuhn =  lun ?: "Null",
                                country = result?.country?.name?: "${applicationContext.getText(R.string.placeholder)}",
                                scheme = result?.scheme?: "${applicationContext.getText(R.string.placeholder)}",
                                type = result?.type?: "${applicationContext.getText(R.string.placeholder)}",
                                brand = result?.brand?: "${applicationContext.getText(R.string.placeholder)}",
                                prepaid = prep?: "Null",
                                bankName = result?.bank?.name?: "${applicationContext.getText(R.string.placeholder)}",
                                bankUrl = result?.bank?.url?: "${applicationContext.getText(R.string.placeholder)}",
                                bankPhone = result?.bank?.phone?: "${applicationContext.getText(R.string.placeholder)}",
                                bankCity = result?.bank?.city?: "${applicationContext.getText(R.string.placeholder)}"
                            )

                            databaseBin.BinDao().save(binIEntity)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe({databaseBin.BinDao().save(binIEntity)},
                                    {
                                            error -> Timber.tag("TAGERROR").e(error.toString())
                                    })
                        }
                    }
                )
            }  else {

            }

            restartDataSave(databaseBin)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

    @SuppressLint("CheckResult")
    fun restartDataSave(databaseBin: BinDatabase) {
        var arrayList: ArrayList<RequestItem> = ArrayList()
        databaseBin.BinDao().getBinEntity()
            .extSingle()
            .subscribe({ bin ->
                arrayList.clear()
                arrayList = bin.map { item->
                    RequestItem(item) {action ->
                        openRequest(action)
                    }
                } as ArrayList<RequestItem>

                adapter.clear()
                binding.saveRequestRecyclerView.adapter = adapter.apply { addAll(arrayList) }
            },
                {
                        error -> Timber.tag("TAGERROR").e(error.toString())
                })

    }

    private fun openRequest(action: BinEntity) {

    }
}