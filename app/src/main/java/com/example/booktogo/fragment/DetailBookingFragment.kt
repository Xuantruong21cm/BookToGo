package com.example.booktogo.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.booktogo.Helper.AccountHelper
import com.example.booktogo.Helper.HotelHelper
import com.example.booktogo.Helper.TripHelper
import com.example.booktogo.R
import com.example.booktogo.activity.HomeActivity
import com.example.booktogo.model.Booking
import com.example.booktogo.model.Discount
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.thecode.aestheticdialogs.*
import kotlinx.android.synthetic.main.fragment_detail_booking.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.DecimalFormat
import java.text.Normalizer
import java.util.concurrent.*
import java.util.regex.Pattern


class DetailBookingFragment : Fragment() {
    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference
    private lateinit var referenceHotel: DatabaseReference
    private var delay: Long = 1000
    private var last_text_edit: Long = 0
    private var handler = Handler(Looper.getMainLooper())
    private lateinit var input_finish_checker: Runnable
    private lateinit var clear_code: Runnable
    private var defaultPrice: Int = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_detail_booking, container, false)

        initView(view)
        initListener(view)
        callbackDiscount(view)

        return view
    }

    @SuppressLint("SetTextI18n")
    private fun callbackDiscount(view: View) {
        input_finish_checker = Runnable {
            run {
                val decimalFormat = DecimalFormat("###,###,###")
                var notFound = 0
                if (System.currentTimeMillis() > (last_text_edit + delay - 500)) {
                    for (discount: Discount in HomeFragment.discountList) {
                        if (discount.code.equals(view.edt_promotion.text.toString().trim())) {
                            notFound = 1
                            defaultPrice =
                                HotelHelper.instance.priceRange!!.toInt() - (HotelHelper.instance.priceRange!!.toInt() / 100 * discount.percent.toInt())
                            AestheticDialog.Builder(
                                activity!!,
                                DialogStyle.FLAT,
                                DialogType.SUCCESS
                            )
                                .setTitle("Discounted")
                                .setMessage(
                                    decimalFormat.format((HotelHelper.instance.priceRange!!.toInt() / 100 * discount.percent.toInt()))
                                        .toString()
                                )
                                .setCancelable(false)
                                .setDarkMode(false)
                                .setGravity(Gravity.CENTER)
                                .setAnimation(DialogAnimation.SHRINK)
                                .setOnClickListener(object : OnDialogClickListener {
                                    override fun onClick(dialog: AestheticDialog.Builder) {
                                        view.tv_price_detailBooking.text =
                                            decimalFormat.format(defaultPrice) + " đ"
                                        closeKeyboard()
                                        dialog.dismiss()
                                    }
                                }).show()
                            break
                        }
                    }
                    if (notFound == 0) {
                        AestheticDialog.Builder(
                            activity!!,
                            DialogStyle.FLAT,
                            DialogType.ERROR
                        )
                            .setTitle("ERRO")
                            .setMessage(getString(R.string.this_code_not_found))
                            .setCancelable(false)
                            .setDarkMode(false)
                            .setGravity(Gravity.CENTER)
                            .setAnimation(DialogAnimation.SHRINK)
                            .setOnClickListener(object : OnDialogClickListener {
                                override fun onClick(dialog: AestheticDialog.Builder) {
                                    view.edt_promotion.setTextColor(Color.parseColor("#F44336"))
                                    view.tv_price_detailBooking.text =
                                        decimalFormat.format(HotelHelper.instance.priceRange!!.toInt()) + " đ"
                                    closeKeyboard()
                                    dialog.dismiss()
                                }
                            }).show()
                    }
                }
            }
        }

        clear_code = Runnable {
            run {
                if (System.currentTimeMillis() > (last_text_edit + delay - 500)) {
                    val decimalFormat = DecimalFormat("###,###,###")
                    view.tv_price_detailBooking.text =
                        decimalFormat.format(HotelHelper.instance.priceRange!!.toInt()) + " đ"
                    closeKeyboard()
                }
            }
        }
    }

    private fun initListener(view: View) {
        view.ing_back_detailBooking.setOnClickListener {
            activity!!.supportFragmentManager.popBackStack()
        }

        view.btn_booking_this_room.setOnClickListener {
            val idBooking = ThreadLocalRandom.current().nextInt(100000, 999999)
            val idHotel = HotelHelper.instance.idHotel
            val date_time =
                view.tv_startDay_detailBooking.text.toString() + " - " + view.tv_endDay_detailBooking.text.toString()
            val days = TripHelper.instance.days
            val nameHotel = HotelHelper.instance.nameHotel
            val addressHotel = HotelHelper.instance.addressHotel
            val adults = TripHelper.instance.adults
            val children = TripHelper.instance.children
            val fullname = AccountHelper.instance.firstname + " " + AccountHelper.instance.lastname
            val email = AccountHelper.instance.email
            val phone = AccountHelper.instance.phone
            val price = defaultPrice.toString()
            val detail = HotelHelper.instance.details1
            val booking = Booking(
                idHotel,
                date_time,
                days,
                nameHotel,
                addressHotel,
                adults,
                children,
                fullname,
                email,
                phone,
                price,
                detail
            )
            GlobalScope.launch(Dispatchers.Default) {
                reference.child(AccountHelper.instance.phone!!).child(idBooking.toString())
                    .setValue(booking)
                referenceHotel.child(
                    convertString(
                        TripHelper.instance.district!!.replace(
                            "\\s".toRegex(),
                            ""
                        )
                    )!!
                ).child(HotelHelper.instance.idHotel!!).child("active").setValue("1")
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Booking Complete", Toast.LENGTH_SHORT).show()
                    val intent = Intent(context, HomeActivity::class.java)
                    startActivity(intent)
                    activity!!.finish()
                }
            }
        }

        view.edt_promotion.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                handler.removeCallbacks(input_finish_checker)
                view.edt_promotion.setTextColor(Color.parseColor("#2D3A4C"))
            }

            override fun afterTextChanged(s: Editable?) {
                if (s!!.length > 0) {
                    last_text_edit = System.currentTimeMillis()
                    handler.postDelayed(input_finish_checker, delay)
                } else if (s.length <= 0) {
                    last_text_edit = System.currentTimeMillis()
                    handler.postDelayed(clear_code, delay)
                }
            }
        })
    }

    private fun convertString(value: String): String? {
        val temp: String = Normalizer.normalize(value, Normalizer.Form.NFD)
        val pattern: Pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+")
        return pattern.matcher(temp).replaceAll("")
    }

    private fun closeKeyboard() {
        val view = activity!!.currentFocus
        if (view != null) {
            val imm: InputMethodManager =
                activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initView(view: View) {
        HomeActivity.chip_navigationView.visibility = View.GONE
        activity!!.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        defaultPrice = HotelHelper.instance.priceRange!!.toInt()
        view.edt_fullname.setText(AccountHelper.instance.firstname + " " + AccountHelper.instance.lastname)
        view.edt_email.setText(AccountHelper.instance.email)
        view.edt_phone.setText(AccountHelper.instance.phone)
        view.tv_startDay_detailBooking.text = TripHelper.instance.starDay
        view.tv_endDay_detailBooking.text = TripHelper.instance.endDay
        view.tv_days_Detail.text = "Days : " + TripHelper.instance.days
        view.tv_adults_detailsBooking.text = "Adults : " + TripHelper.instance.adults
        view.tv_children_detailsBooking.text = "Children : " + TripHelper.instance.adults
        view.tv_nameHotel_detailBooking.text = HotelHelper.instance.nameHotel
        view.tv_level_hotel_detailBooking.text = HotelHelper.instance.levelHotel
        view.img_avatar_detailBooking.setImageBitmap(decodedBitmap(HotelHelper.instance.details1!!))
        val decimalFormat = DecimalFormat("###,###,###")
        view.tv_price_detailBooking.text =
            decimalFormat.format(defaultPrice) + " đ"

        database = FirebaseDatabase.getInstance()
        reference = database.getReference("Booking")
        referenceHotel = database.getReference(TripHelper.instance.cityHotel!!)
    }

    fun decodedBitmap(source: String): Bitmap {
        val decodedString = Base64.decode(source, Base64.DEFAULT)
        val decodedByte: Bitmap =
            BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
        return decodedByte
    }
}