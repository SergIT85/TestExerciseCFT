package ru.sergsports.androidcource.testexercisecft

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.util.Log
import android.widget.Toast
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.view.*
import ru.sergsports.androidcource.testexercisecft.data.network.BinApiClient
import ru.sergsports.androidcource.testexercisecft.data.roomdata.BinDatabase
import ru.sergsports.androidcource.testexercisecft.data.roomdata.BinEntity
import ru.sergsports.androidcource.testexercisecft.databinding.ActivityMainBinding
import ru.sergsports.androidcource.testexercisecft.domain.extSingle
import ru.sergsports.androidcource.testexercisecft.presemtation.RequestItem
import ru.sergsports.androidcource.testexercisecft.presemtation.secondaction.RecycleViewInRoom
import timber.log.Timber

const val ID = "id"
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
                    .subscribe ({ result ->
                        if (result != null) {
                            binding.scheme.text = result.scheme?: "Null"
                            binding.type.text = result.type?: "Null"
                            binding.brand.text = result.brand?: "Null"
                            if(result.country != null && result.country.name != null) {
                                val country = result.country.name
                                val spanCountry = SpannableString(country)
                                spanCountry.setSpan(UnderlineSpan(), 0 , spanCountry.length, 0)
                                binding.country.text = spanCountry ?: "Null"
                                binding.country.setTextColor(resources.getColor(R.color.teal_200))

                                if(result.country.latitude != null && result.country.longitude != null) {
                                    val latitude = result.country.latitude.toString()
                                    val spanLatitude = SpannableString(latitude)
                                    spanLatitude.setSpan(UnderlineSpan(), 0 , spanLatitude.length, 0)
                                    binding.latitude.text = spanLatitude
                                    binding.latitude.setTextColor(resources.getColor(R.color.teal_200))

                                    val longitude = result.country.longitude.toString()
                                    val spanLongitude = SpannableString(longitude)
                                    spanLongitude.setSpan(UnderlineSpan(), 0 , spanLongitude.length, 0)
                                    binding.longitude.text = spanLongitude
                                    binding.longitude.setTextColor(resources.getColor(R.color.teal_200))
                                }
                            } else{
                                binding.country.text = applicationContext.getText(R.string.placeholder)
                            }
                            if(result.bank != null) {
                                binding.bank.text = result?.bank?.name ?: "Неизвестно"

                                val bankPhone = result.bank.phone
                                val spanBankPhone = SpannableString(bankPhone)
                                spanBankPhone.setSpan(UnderlineSpan(), 0, spanBankPhone.length, 0)
                                binding.bankPhone.text = spanBankPhone ?: "Неизвестно"
                                binding.bankPhone.setTextColor(resources.getColor(R.color.teal_200))
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
                                bankCity = result?.bank?.city?: "${applicationContext.getText(R.string.placeholder)}",
                                latitude = result?.country?.latitude.toString() ?: "${applicationContext.getText(R.string.placeholder)}",
                                longitude = result?.country?.longitude.toString() ?: "${applicationContext.getText(R.string.placeholder)}"
                            )

                            databaseBin.BinDao().save(binIEntity)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe({databaseBin.BinDao().save(binIEntity)},
                                    {
                                            error -> Timber.tag("TAGERROR").e(error.toString())
                                    })
                        }
                    },
                        {

                                error -> Timber.tag("TAGERROR").e(error.toString())
                                Toast.makeText(applicationContext,
                                    resources.getText(R.string.error),
                                    Toast.LENGTH_SHORT).show()
                        })
                )
            }  else {

            }

            restartDataSave(databaseBin)
        }


        binding.bankPhone.setOnClickListener{
            val number = it.bank_phone.text
            //Log.d("TAG", "NUMBER-------------------------------$number")

            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + number))
            startActivity(intent)
        }

        binding.country.setOnClickListener{
            val coordinates = "geo:${binding.latitude.text},${binding.latitude.text}"
            val mapIntent = Intent(Intent.ACTION_VIEW, Uri.parse(coordinates))
            startActivity(mapIntent)
        }

        binding.longitude.setOnClickListener{
            val coordinates = "geo:${binding.latitude.text},${binding.latitude.text}"
            val mapIntent = Intent(Intent.ACTION_VIEW, Uri.parse(coordinates))
            startActivity(mapIntent)
        }

        binding.latitude.setOnClickListener{
            val coordinates = "geo:${binding.latitude.text},${binding.latitude.text}"
            val mapIntent = Intent(Intent.ACTION_VIEW, Uri.parse(coordinates))
            startActivity(mapIntent)
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
        val intent = Intent(this, RecycleViewInRoom::class.java).apply {
            putExtra(ID, action.id)
        }
        startActivity(intent)
    }
}