package com.example.booktogo.fragment

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.room.Room
import com.example.booktogo.Helper.HotelHelper
import com.example.booktogo.Helper.TripHelper
import com.example.booktogo.R
import com.example.booktogo.activity.HomeActivity
import com.example.booktogo.adapter.ExploreAdapter
import com.example.booktogo.adapter.HotelFavouriteAdapter
import com.example.booktogo.adapter.NearbyAdapter
import com.example.booktogo.room.Hotel
import com.example.booktogo.room.HotelDatabase
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_hotel_details.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.DecimalFormat


class HotelDetailsFragment : Fragment(), OnMapReadyCallback {
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

    var adults: String? = null
    var chils: String? = null
    var days: String? = null

    lateinit var nearbyList: ArrayList<String>
    lateinit var adapter: NearbyAdapter

    var db: HotelDatabase? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_hotel_details, container, false)
        inintValue()
        initView(view)
        initListener(view)
        return view
    }

    private fun initListener(view: View) {

        view.img_favourite_hotel.setOnClickListener {
            val hotel: Hotel = Hotel(
                addressHotel,
                idHotel!!,
                levelHotel,
                nameHotel,
                priceRange,
                lat.toString(),
                lng.toString(),
                details1,
                details2,
                details3,
                details4,
                details5,
                details6,
                details7,
                details8,
                details9,
                nearby1,
                nearby2,
                nearby3,
                nearby4,
                nearby5,
                clean_room,
                elevator,
                family,
                free_wifi,
                hot_tub,
                laundry,
                reception,
                security_camera,
                smoke
            )

            val check = db!!.hotelDAO().findById(idHotel!!)
            if (check == null){
                val result = db!!.hotelDAO().insertAll(hotel)
                if (result != null){

                    Toast.makeText(requireContext(),"Added to favorites list",Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(requireContext(),"Add failure",Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(requireContext(),"Hotel has existed in favorites",Toast.LENGTH_SHORT).show()
            }
        }


        view.frame_more_details.setOnClickListener {
            val manager = activity!!.supportFragmentManager
            val transition = manager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in_left, 0, 0, R.anim.slide_out_left)
            val fragment = MoreDetailsFragment()
            transition.add(R.id.layout_home, fragment).commit()
            transition.addToBackStack(fragment::class.java.simpleName)
        }

        view.tv_view_location.setOnClickListener {
            val manager = activity!!.supportFragmentManager
            val transition = manager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in_left, 0, 0, R.anim.slide_out_left)
            val fragment = MapViewDetailsFragment()
            transition.add(R.id.layout_home, fragment).commit()
            transition.addToBackStack(fragment::class.java.simpleName)
        }

        view.btn_chooseThisHotel_hotelDetails.setOnClickListener {
            val manager = activity!!.supportFragmentManager
            val transition = manager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in_left, 0, 0, R.anim.slide_out_left)
            val fragment = DetailBookingFragment()
            transition.add(R.id.layout_home_detail, fragment).commit()
            transition.addToBackStack(fragment::class.java.simpleName)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initView(view: View) {
        val mapFragment: SupportMapFragment =
            childFragmentManager.findFragmentById(R.id.fragment_small_map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        HomeActivity.chip_navigationView.visibility = View.GONE

        view.img_details_1.setImageBitmap(decodedBitmap(details1.toString()))
        view.img_details_2.setImageBitmap(decodedBitmap(details2.toString()))
        view.img_details_3.setImageBitmap(decodedBitmap(details3.toString()))
        view.img_details_4.setImageBitmap(decodedBitmap(details4.toString()))
        view.img_details_5.setImageBitmap(decodedBitmap(details5.toString()))
        view.tv_address_Hotel_Details.text = addressHotel
        val decimalFormat = DecimalFormat("###,###,###")
        view.tv_priceRange_hotelDetails.text = decimalFormat.format(priceRange!!.toInt()) + " đ"
        view.tv_adults_count.text = "Adults : $adults"
        view.tv_child_count.text = "Children : $chils"
        view.tv_days_count.text = "Days : $days"

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

    override fun onMapReady(p0: GoogleMap?) {
        map = p0!!
        val latLng: LatLng = LatLng(lat!!, lng!!)
        map.addMarker(MarkerOptions().position(latLng))
        map.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15F))
    }

    private fun inintValue() {
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
        adults = TripHelper.instance.adults
        chils = TripHelper.instance.children
        days = TripHelper.instance.days

        nearbyList = ArrayList()
        nearbyList.add(nearby1!!)
        nearbyList.add(nearby2!!)
        nearbyList.add(nearby3!!)
        nearbyList.add(nearby4!!)
        nearbyList.add(nearby5!!)

        db = Room.databaseBuilder(
            activity!!.applicationContext,
            HotelDatabase::class.java,
            "favourite"
        ).allowMainThreadQueries().build()
    }

    fun decodedBitmap(source: String): Bitmap {
        val decodedString = Base64.decode(source, Base64.DEFAULT)
        val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
        return decodedByte!!
    }

    override fun onDestroyView() {
        super.onDestroyView()
        HomeActivity.chip_navigationView.visibility = View.VISIBLE
    }
}