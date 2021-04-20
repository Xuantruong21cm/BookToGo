package com.example.booktogo.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.example.booktogo.Helper.AccountHelper
import com.example.booktogo.Helper.HotelHelper
import com.example.booktogo.Helper.TripHelper
import com.example.booktogo.R
import com.example.booktogo.adapter.HotelViewAdapter
import com.example.booktogo.model.HotelExplore
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_search.view.*
import java.text.Normalizer
import java.util.regex.Pattern

class SearchFragment : Fragment() {
    lateinit var listHotel_explore: ArrayList<HotelExplore>
    lateinit var hotelViewAdapter: HotelViewAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_search, container, false)
        if (TripHelper.instance.district != null && TripHelper.instance.adults != null && TripHelper.instance.children != null
            && TripHelper.instance.city != null && TripHelper.instance.days != null
        ) {
            initView(view)
            initDataHotel(view)
        }
        return view
    }

    private fun setDataHotel(view: View?) {
        hotelViewAdapter = HotelViewAdapter(listHotel_explore, requireContext())
        view!!.recyclerView_listHotel.setHasFixedSize(true)
        view.recyclerView_listHotel.adapter = hotelViewAdapter
        hotelViewAdapter.setHotelViewAdapter(object : HotelViewAdapter.HotelViewListener {
            override fun HotelViewwOnClick(hotelExplore: HotelExplore) {
                // Toast.makeText(context,hotelExplore.wifiFree,Toast.LENGTH_SHORT).show()
                HotelHelper.instance.addressHotel = hotelExplore.addressHotel
                HotelHelper.instance.idHotel = hotelExplore.idHotel
                HotelHelper.instance.lat = hotelExplore.lat
                HotelHelper.instance.lng = hotelExplore.lng
                HotelHelper.instance.priceRange = hotelExplore.priceRange
                HotelHelper.instance.levelHotel = hotelExplore.levelHotel
                HotelHelper.instance.nameHotel = hotelExplore.nameHotel
                HotelHelper.instance.details1 = hotelExplore.details1
                HotelHelper.instance.details2 = hotelExplore.details2
                HotelHelper.instance.details3 = hotelExplore.details3
                HotelHelper.instance.details4 = hotelExplore.details4
                HotelHelper.instance.details5 = hotelExplore.details5
                HotelHelper.instance.details6 = hotelExplore.details6
                HotelHelper.instance.details7 = hotelExplore.details7
                HotelHelper.instance.details8 = hotelExplore.details8
                HotelHelper.instance.details9 = hotelExplore.details9
                HotelHelper.instance.nearby1 = hotelExplore.nearby1
                HotelHelper.instance.nearby2 = hotelExplore.nearby2
                HotelHelper.instance.nearby3 = hotelExplore.nearby3
                HotelHelper.instance.nearby4 = hotelExplore.nearby4
                HotelHelper.instance.nearby5 = hotelExplore.nearby5
                HotelHelper.instance.clean_room = hotelExplore.clean_room
                HotelHelper.instance.elevator = hotelExplore.elevator
                HotelHelper.instance.family = hotelExplore.family
                HotelHelper.instance.free_wifi = hotelExplore.free_wifi
                HotelHelper.instance.hot_tub = hotelExplore.hot_tub
                HotelHelper.instance.laundry = hotelExplore.laundry
                HotelHelper.instance.reception = hotelExplore.reception
                HotelHelper.instance.security_camera = hotelExplore.security_camera
                HotelHelper.instance.smoke = hotelExplore.smoke

                val manager = activity!!.supportFragmentManager
                val transition = manager.beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_left, 0, 0, R.anim.slide_out_left)
                val fragment = HotelDetailsFragment()
                transition.add(R.id.layout_home, fragment).commit()
                transition.addToBackStack(fragment::class.java.simpleName)
            }
        })
    }

    private fun initDataHotel(view: View) {
        listHotel_explore = ArrayList()
        val database: FirebaseDatabase = Firebase.database
        val reference = database.getReference(TripHelper.instance.cityHotel!!).child(
            convertString(TripHelper.instance.district!!.replace("\\s".toRegex(), ""))!!
        )
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (ds: DataSnapshot in snapshot.children) {
                    //  Log.d("okok", "onDataChange: "+ds.key)
                 //   Log.d("okok", "onDataChange: " + ds.child("thumbnail").child("1").value)
                    val addressHotel: String = ds.child("addressHotel").value.toString()
                    val idHotel: String = ds.child("idHotel").value.toString()
                    val levelHotel: String = ds.child("levelHotel").value.toString()
                    val nameHotel: String = ds.child("nameHotel").value.toString()
                    val priceRange: String = ds.child("priceRange").value.toString()
                    val lat: String = ds.child("lat").value.toString()
                    val lng: String = ds.child("lng").value.toString()
                    val details1: String = ds.child("details1").value.toString()
                    val details2: String = ds.child("details2").value.toString()
                    val details3: String = ds.child("details3").value.toString()
                    val details4: String = ds.child("details4").value.toString()
                    val details5: String = ds.child("details5").value.toString()
                    val details6: String = ds.child("details6").value.toString()
                    val details7: String = ds.child("details7").value.toString()
                    val details8: String = ds.child("details8").value.toString()
                    val details9: String = ds.child("details9").value.toString()
                    val nearby1: String = ds.child("nearby1").value.toString()
                    val nearby2: String = ds.child("nearby2").value.toString()
                    val nearby3: String = ds.child("nearby3").value.toString()
                    val nearby4: String = ds.child("nearby4").value.toString()
                    val nearby5: String = ds.child("nearby5").value.toString()
                    val clean_room = ds.child("clean_room").value.toString()
                    val elevator = ds.child("elevator").value.toString()
                    val family = ds.child("family").value.toString()
                    val free_wifi = ds.child("free_wifi").value.toString()
                    val hot_tub = ds.child("hot_tub").value.toString()
                    val laundry = ds.child("laundry").value.toString()
                    val reception = ds.child("reception").value.toString()
                    val security_camera = ds.child("security_camera").value.toString()
                    val smoke = ds.child("smoke").value.toString()

                    listHotel_explore.add(
                        HotelExplore(
                            addressHotel,
                            idHotel,
                            levelHotel,
                            nameHotel,
                            priceRange,
                            lat,
                            lng,
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
                    )
                    setDataHotel(view)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun initView(view: View) {
        activity!!.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        if (AccountHelper.instance.userName!!.length >= 10) {
            view.tv_username_guest.text = AccountHelper.instance.userName
            view.tv_username_guest.textSize = 16F
        } else {
            view.tv_username_guest.text = AccountHelper.instance.userName
            view.tv_username_guest.textSize = 18F
        }
        if (TripHelper.instance.children!!.trim().toInt() >= 10) {
            view.tv_children_number.text = TripHelper.instance.children
        } else {
            view.tv_children_number.text = "0" + TripHelper.instance.children
        }

        if (TripHelper.instance.adults!!.trim().toInt() >= 10) {
            view.tv_adults_number.text = TripHelper.instance.adults
        } else {
            view.tv_adults_number.text = "0" + TripHelper.instance.adults
        }

        if (TripHelper.instance.days!!.trim().toInt() >= 10) {
            view.tv_days_number.text = TripHelper.instance.days
        } else {
            view.tv_days_number.text = "0" + TripHelper.instance.days
        }
        view.edt_searchFragment.setText(TripHelper.instance.district + "," + TripHelper.instance.city)
        view.tv_district_member_info.text = TripHelper.instance.district + ","
        view.tv_city_member_info.text = TripHelper.instance.city
    }

    private fun convertString(value: String): String? {
        val temp: String = Normalizer.normalize(value, Normalizer.Form.NFD)
        val pattern: Pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+")
        return pattern.matcher(temp).replaceAll("")
    }

}