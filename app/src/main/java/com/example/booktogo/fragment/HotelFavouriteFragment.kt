package com.example.booktogo.fragment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.booktogo.Helper.HotelHelper
import com.example.booktogo.Helper.TripHelper
import com.example.booktogo.R
import com.example.booktogo.activity.HomeActivity
import com.example.booktogo.adapter.NearbyAdapter
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.fragment_hotel_favourite.*
import kotlinx.android.synthetic.main.fragment_hotel_favourite.view.*
import kotlinx.android.synthetic.main.fragment_hotel_favourite.view.img_details_1
import kotlinx.android.synthetic.main.fragment_hotel_favourite.view.img_details_2
import kotlinx.android.synthetic.main.fragment_hotel_favourite.view.img_details_3
import kotlinx.android.synthetic.main.fragment_hotel_favourite.view.img_details_4
import kotlinx.android.synthetic.main.fragment_hotel_favourite.view.img_details_5
import kotlinx.android.synthetic.main.fragment_hotel_favourite.view.img_star_1
import kotlinx.android.synthetic.main.fragment_hotel_favourite.view.img_star_2
import kotlinx.android.synthetic.main.fragment_hotel_favourite.view.img_star_3
import kotlinx.android.synthetic.main.fragment_hotel_favourite.view.img_star_4
import kotlinx.android.synthetic.main.fragment_hotel_favourite.view.img_star_5
import kotlinx.android.synthetic.main.fragment_hotel_favourite.view.layout_clean_room
import kotlinx.android.synthetic.main.fragment_hotel_favourite.view.layout_elevator
import kotlinx.android.synthetic.main.fragment_hotel_favourite.view.layout_family_room
import kotlinx.android.synthetic.main.fragment_hotel_favourite.view.layout_hot_tub
import kotlinx.android.synthetic.main.fragment_hotel_favourite.view.layout_laundry
import kotlinx.android.synthetic.main.fragment_hotel_favourite.view.layout_reception
import kotlinx.android.synthetic.main.fragment_hotel_favourite.view.layout_security_camera
import kotlinx.android.synthetic.main.fragment_hotel_favourite.view.layout_smoke
import kotlinx.android.synthetic.main.fragment_hotel_favourite.view.layout_wifi_free
import kotlinx.android.synthetic.main.fragment_hotel_favourite.view.recyclerView_nearby_location
import kotlinx.android.synthetic.main.fragment_hotel_favourite.view.tv_address_Hotel_Details
import kotlinx.android.synthetic.main.fragment_hotel_favourite.view.tv_end_day
import kotlinx.android.synthetic.main.fragment_hotel_favourite.view.tv_hotelName_details
import kotlinx.android.synthetic.main.fragment_hotel_favourite.view.tv_priceRange_hotelDetails
import kotlinx.android.synthetic.main.fragment_hotel_favourite.view.tv_start_day
import java.text.DecimalFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*

class HotelFavouriteFragment : Fragment(),OnMapReadyCallback {
    lateinit var map: GoogleMap
    var addressHotel: String? = null
    var idHotel: String? = null
    var lat: Double? = null
    var lng: Double? = null
    var levelHotel: String? = null
    var nameHotel: String? = null
    var priceRange: String? = null
    var details1: String? = null
    var details2: String? = null
    var details3: String? = null
    var details4: String? = null
    var details5: String? = null
    var details6: String? = null
    var details7: String? = null
    var details8: String? = null
    var details9: String? = null
    var nearby1: String? = null
    var nearby2: String? = null
    var nearby3: String? = null
    var nearby4: String? = null
    var nearby5: String? = null

    var clean_room: String? = null
    var elevator: String? = null
    var family: String? = null
    var free_wifi: String? = null
    var hot_tub: String? = null
    var laundry: String? = null
    var reception: String? = null
    var security_camera: String? = null
    var smoke: String? = null
    lateinit var nearbyList: ArrayList<String>
    lateinit var adapter: NearbyAdapter

    var year: Int? = null
    var month: Int? = null
    var day: Int? = null
    lateinit var calendar: Calendar

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view : View = inflater.inflate(R.layout.fragment_hotel_favourite, container, false)
        initValue()
        initView(view)
        initListener(view)
        return view
    }

    @SuppressLint("SetTextI18n")
    private fun initView(view: View) {
        activity!!.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        HomeActivity.chip_navigationView.visibility = View.GONE
        val mapFragment: SupportMapFragment =
            childFragmentManager.findFragmentById(R.id.fragment_small_map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        view.img_details_1.setImageBitmap(decodedBitmap(details1.toString().trim()))
        view.img_details_2.setImageBitmap(decodedBitmap(details2.toString().trim()))
        view.img_details_3.setImageBitmap(decodedBitmap(details3.toString().trim()))
        view.img_details_4.setImageBitmap(decodedBitmap(details4.toString().trim()))
        view.img_details_5.setImageBitmap(decodedBitmap(details5.toString().trim()))
        view.tv_address_Hotel_Details.text = addressHotel
        val decimalFormat = DecimalFormat("###,###,###")
        view.tv_priceRange_hotelDetails.text =decimalFormat.format(priceRange!!.toInt()) + " đ"

        if (clean_room!!.trim().toInt() < 1) {
            view.layout_clean_room.visibility = View.GONE
        }
        if (elevator!!.trim().toInt() < 1) {
            view.layout_elevator.visibility = View.GONE
        }
        if (family!!.trim().toInt() < 1) {
            view.layout_family_room.visibility = View.GONE
        }
        if (free_wifi!!.trim().toInt() < 1) {
            view.layout_wifi_free.visibility = View.GONE
        }
        if (hot_tub!!.trim().toInt() < 1) {
            view.layout_hot_tub.visibility = View.GONE
        }
        if (laundry!!.trim().toInt() < 1) {
            view.layout_laundry.visibility = View.GONE
        }
        if (reception!!.trim().toInt() < 1) {
            view.layout_reception.visibility = View.GONE
        }
        if (security_camera!!.trim().toInt() < 1) {
            view.layout_security_camera.visibility = View.GONE
        }
        if (smoke!!.trim().toInt() < 1) {
            view.layout_smoke.visibility = View.GONE
        }


        view.tv_hotelName_details.text = nameHotel
        if (levelHotel!!.toInt() == 1) {
            view.img_star_1.visibility = View.VISIBLE
        } else if (levelHotel!!.toInt() == 2) {
            view.img_star_2.visibility = View.VISIBLE
        } else if (levelHotel!!.toInt() == 3) {
            view.img_star_3.visibility = View.VISIBLE
        } else if (levelHotel!!.toInt() == 4) {
            view.img_star_4.visibility = View.VISIBLE
        } else if (levelHotel!!.toInt() == 5) {
            view.img_star_5.visibility = View.VISIBLE
        }

        adapter = NearbyAdapter(nearbyList)
        view.recyclerView_nearby_location.setHasFixedSize(true)
        view.recyclerView_nearby_location.adapter = adapter
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initListener(view: View) {
        calendar = Calendar.getInstance()
        view.tv_start_day.setOnClickListener {
            year = calendar[Calendar.YEAR]
            month = calendar[Calendar.MONTH]
            day = calendar[Calendar.DAY_OF_MONTH]

            val datePickerDialog: DatePickerDialog = DatePickerDialog(
                requireContext(),
                DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    requireView().tv_start_day.text =
                        "" + dayOfMonth + "/" + (month + 1) + "/" + year
                },
                year!!,
                month!!,
                day!!
            )
            datePickerDialog.show()
        }
        view.tv_end_day.setOnClickListener {
            year = calendar[Calendar.YEAR]
            month = calendar[Calendar.MONTH]
            day = calendar[Calendar.DAY_OF_MONTH]

            val datePickerDialog: DatePickerDialog = DatePickerDialog(
                requireContext(),
                DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    requireView().tv_end_day.text = "" + dayOfMonth + "/" + (month + 1) + "/" + year
                },
                year!!,
                month!!,
                day!!
            )
            datePickerDialog.show()
        }
        view.tv_view_location.setOnClickListener {
            val manager = activity!!.supportFragmentManager
            val transition = manager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in_left, 0, 0, R.anim.slide_out_left)
            val fragment = MapViewDetailsFragment()
            transition.add(R.id.layout_favoutite, fragment).commit()
            transition.addToBackStack(fragment::class.java.simpleName)
        }

        view.btn_chooseThisHotel_hotelDetails.setOnClickListener {
            if (tv_start_day.text.equals("--/--/----") || tv_end_day.text.equals("--/--/----")) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.select_a_reservation),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                if (calculate(tv_start_day.text.toString(), tv_end_day.text.toString()) < 0) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.at_least_one_day),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {

                }
            }
        }

        view.frame_more_details.setOnClickListener {
            val manager = activity!!.supportFragmentManager
            val transition = manager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in_left, 0, 0, R.anim.slide_out_left)
            val fragment = MoreDetailsFragment()
            transition.add(R.id.layout_favoutite, fragment).commit()
            transition.addToBackStack(fragment::class.java.simpleName)
        }
    }

    private fun initValue() {
        addressHotel = HotelHelper.instance.addressHotel
        idHotel = HotelHelper.instance.idHotel
        lat = HotelHelper.instance.lat!!.toDouble()
        lng = HotelHelper.instance.lng!!.toDouble()
        levelHotel = HotelHelper.instance.levelHotel
        nameHotel = HotelHelper.instance.nameHotel
        priceRange = HotelHelper.instance.priceRange
        details1 = HotelHelper.instance.details1
        details2 = HotelHelper.instance.details2
        details3 = HotelHelper.instance.details3
        details4 = HotelHelper.instance.details4
        details5 = HotelHelper.instance.details5
        details6 = HotelHelper.instance.details6
        details7 = HotelHelper.instance.details7
        details8 = HotelHelper.instance.details8
        details9 = HotelHelper.instance.details9
        nearby1 = HotelHelper.instance.nearby1
        nearby2 = HotelHelper.instance.nearby2
        nearby3 = HotelHelper.instance.nearby3
        nearby4 = HotelHelper.instance.nearby4
        nearby5 = HotelHelper.instance.nearby5
        clean_room = HotelHelper.instance.clean_room
        elevator = HotelHelper.instance.elevator
        family = HotelHelper.instance.family
        free_wifi = HotelHelper.instance.free_wifi
        hot_tub = HotelHelper.instance.hot_tub
        laundry = HotelHelper.instance.laundry
        reception = HotelHelper.instance.reception
        security_camera = HotelHelper.instance.security_camera
        smoke = HotelHelper.instance.smoke

        nearbyList = ArrayList()
        nearbyList.add(nearby1!!)
        nearbyList.add(nearby2!!)
        nearbyList.add(nearby3!!)
        nearbyList.add(nearby4!!)
        nearbyList.add(nearby5!!)
    }
    override fun onMapReady(p0: GoogleMap?) {
        map = p0!!
        val latLng: LatLng = LatLng(lat!!, lng!!)
        map.addMarker(MarkerOptions().position(latLng))
        map.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15F))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun calculate(startDay : String, endDay : String) : Long{
        val dateformater : DateTimeFormatter = DateTimeFormatter.ofPattern("d/M/u")
        val startDateValue = LocalDate.parse(startDay,dateformater)
        val endDateValue = LocalDate.parse(endDay,dateformater)
        val days :Long = ChronoUnit.DAYS.between(startDateValue,endDateValue)
        return days
    }
    fun decodedBitmap(source: String): Bitmap {
        val decodedString = Base64.decode(source, Base64.DEFAULT)
        val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
        return decodedByte!!
    }
}