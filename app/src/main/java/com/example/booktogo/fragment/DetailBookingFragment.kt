package com.example.booktogo.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.booktogo.Helper.AccountHelper
import com.example.booktogo.Helper.HotelHelper
import com.example.booktogo.Helper.TripHelper
import com.example.booktogo.R
import com.example.booktogo.activity.HomeActivity
import com.example.booktogo.model.Booking
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.fragment_detail_booking.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.DecimalFormat
import java.util.concurrent.ThreadLocalRandom


class DetailBookingFragment : Fragment() {
    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_detail_booking, container, false)
        initView(view)
        initListener(view)

        return view
    }

    private fun initListener(view: View) {
        view.ing_back_detailBooking.setOnClickListener {
            activity!!.supportFragmentManager.popBackStack()
        }

        view.btn_booking_this_room.setOnClickListener {
            val idBooking = ThreadLocalRandom.current().nextInt(100000, 999999)
            val idHotel = HotelHelper.instance.idHotel
            val date_time = view.tv_startDay_detailBooking.text.toString() + " - " + view.tv_endDay_detailBooking.text.toString()
            val days = TripHelper.instance.days
            val nameHotel = HotelHelper.instance.nameHotel
            val addressHotel = HotelHelper.instance.addressHotel
            val adults = TripHelper.instance.adults
            val children = TripHelper.instance.children
            val fullname = AccountHelper.instance.firstname + " " + AccountHelper.instance.lastname
            val email = AccountHelper.instance.email
            val phone = AccountHelper.instance.phone
            val price = HotelHelper.instance.priceRange
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
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Booking Complete", Toast.LENGTH_SHORT).show()
                    val intent = Intent(context,HomeActivity::class.java)
                    startActivity(intent)
                    activity!!.finish()
                }
            }

        }
    }

    @SuppressLint("SetTextI18n")
    private fun initView(view: View) {
        HomeActivity.chip_navigationView.visibility = View.GONE
        activity!!.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
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
            decimalFormat.format(HotelHelper.instance.priceRange!!.toInt()) + " đ"

        database = FirebaseDatabase.getInstance()
        reference = database.getReference("Booking")
    }

    fun decodedBitmap(source: String): Bitmap {
        val decodedString = Base64.decode(source, Base64.DEFAULT)
        val decodedByte: Bitmap =
            BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
        return decodedByte
    }
}