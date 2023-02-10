package ru.sergsports.androidcource.testexercisecft.presemtation.secondaction

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.request_item.view.*
import ru.sergsports.androidcource.testexercisecft.R
import ru.sergsports.androidcource.testexercisecft.data.roomdata.BinDatabase
import ru.sergsports.androidcource.testexercisecft.databinding.RequestItemBinding
import ru.sergsports.androidcource.testexercisecft.domain.extSingle
import timber.log.Timber

const val ID = "id"
class RecycleViewInRoom : AppCompatActivity() {
    lateinit var binding: RequestItemBinding
    @SuppressLint("CheckResult", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = RequestItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dababaseBin = BinDatabase.get(applicationContext)
        val id = intent.getIntExtra(ID,0)

        if (id != 0) {
            dababaseBin.BinDao().requestFromId(id).extSingle().subscribe({ entity ->
                binding.bankItem.text = entity.bankName
                binding.brandItem.text = entity.brand
                binding.countryItem.text = entity.country
                binding.numberItem.text = "Длинна:${entity.numberLength.toString()}" +
                        "  Лун:${entity.numberLuhn}"
                binding.prepaidItem.text = entity.prepaid
                binding.bankPhone.text = entity.bankPhone
                binding.schemeItem.text = entity.scheme
                binding.typeItem.text = entity.type
                binding.latitude.text = entity.latitude
                binding.longitude.text = entity.longitude

                binding.bankPhone.setTextColor(resources.getColor(R.color.teal_200))
                binding.countryItem.setTextColor(resources.getColor(R.color.teal_200))
                binding.longitude.setTextColor(resources.getColor(R.color.teal_200))
                binding.latitude.setTextColor(resources.getColor(R.color.teal_200))

            }, {
                    error -> Timber.tag("TAGERROR").e(error.toString())
            })
        }

        binding.bankPhone.setOnClickListener {
            if (binding.bankPhone.text != applicationContext.getText(R.string.placeholder)) {
                val number = it.bank_phone.text
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + number))
                startActivity(intent)
            }
        }

        binding.countryItem.setOnClickListener{
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
}