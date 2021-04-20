package com.example.booktogo.fragment

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.booktogo.Helper.ExploreHelper
import com.example.booktogo.Helper.HotelHelper
import com.example.booktogo.R
import com.example.booktogo.adapter.HotelExploreAdapter
import com.example.booktogo.model.HotelExplore
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_explore.view.*
import kotlinx.coroutines.*

class ExploreFragment : Fragment() {
    lateinit var listHotel_explore: ArrayList<HotelExplore>
    lateinit var thumbnailList: ArrayList<String>
    lateinit var hotelExploreAdapter: HotelExploreAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_explore, container, false)
        loadExplore(view)
        initListener(view)
        return view
    }

    private fun initListener(view: View?) {
        view?.img_back_explore!!.setOnClickListener {
            activity!!.onBackPressed()
        }
    }

    private fun setHotelExplore(view: View?) {
        hotelExploreAdapter = HotelExploreAdapter(listHotel_explore, activity!!.applicationContext)
        //view!!.recyclerView_explore_hotel_List.setHasFixedSize(true)
        view!!.recyclerView_explore_hotel_List.adapter = hotelExploreAdapter
        hotelExploreAdapter.setOnClickListener(object : HotelExploreAdapter.HotelExplorerListener {
            override fun onClickListener(hotel: HotelExplore) {
                HotelHelper.instance.addressHotel = hotel.addressHotel
                HotelHelper.instance.idHotel = hotel.idHotel
                HotelHelper.instance.lat = hotel.lat
                HotelHelper.instance.lng = hotel.lng
                HotelHelper.instance.levelHotel = hotel.levelHotel
                HotelHelper.instance.nameHotel = hotel.nameHotel
                HotelHelper.instance.priceRange = hotel.priceRange
                HotelHelper.instance.details1 = hotel.details1
                HotelHelper.instance.details2 = hotel.details2
                HotelHelper.instance.details3 = hotel.details3
                HotelHelper.instance.details4 = hotel.details4
                HotelHelper.instance.details5 = hotel.details5
                HotelHelper.instance.details6 = hotel.details6
                HotelHelper.instance.details7 = hotel.details7
                HotelHelper.instance.details8 = hotel.details8
                HotelHelper.instance.details9 = hotel.details9
                HotelHelper.instance.nearby1 = hotel.nearby1
                HotelHelper.instance.nearby2 = hotel.nearby2
                HotelHelper.instance.nearby3 = hotel.nearby3
                HotelHelper.instance.nearby4 = hotel.nearby4
                HotelHelper.instance.nearby5 = hotel.nearby5
                HotelHelper.instance.clean_room = hotel.clean_room
                HotelHelper.instance.elevator = hotel.elevator
                HotelHelper.instance.family = hotel.family
                HotelHelper.instance.free_wifi = hotel.free_wifi
                HotelHelper.instance.hot_tub = hotel.hot_tub
                HotelHelper.instance.laundry = hotel.laundry
                HotelHelper.instance.reception = hotel.reception
                HotelHelper.instance.security_camera = hotel.security_camera
                HotelHelper.instance.smoke = hotel.smoke

                val manager: FragmentManager = activity!!.supportFragmentManager
                val transition: FragmentTransaction = manager.beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_left, 0, 0, R.anim.slide_out_left)
                val fragment: Fragment = ExploreDetailsFragment()
                transition.replace(R.id.layout_home_fragment, fragment).commit()
                transition.addToBackStack(fragment::class.java.simpleName)
            }
        })
    }

    @SuppressLint("ResourceAsColor")
    private fun loadExplore(view: View?) {
        listHotel_explore = ArrayList()
        thumbnailList = ArrayList()
        val database: FirebaseDatabase = Firebase.database
        val reference = database.getReference(ExploreHelper.instance.exploreCity!!).child(
            ExploreHelper.instance.district!!
        )
        view!!.img_exploreHeader.setImageBitmap(decodedBitmap(ExploreHelper.instance.imageDistrict!!))
        view.collapsing_explore.title = ExploreHelper.instance.title
        view.collapsing_explore.setExpandedTitleTextAppearance(R.style.AppBarExpanded)
        view.collapsing_explore.setCollapsedTitleTextAppearance(R.style.AppBarCollapsed)
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (ds: DataSnapshot in snapshot.children) {
                    //  Log.d("okok", "onDataChange: "+ds.key)
                    Log.d("okok", "onDataChange: " + ds.child("thumbnail").child("1").value)
                    val addressHotel: String = ds.child("addressHotel").value.toString()
                    val idHotel: String = ds.child("idHotel").value.toString()
                    val levelHotel: String = ds.child("levelHotel").value.toString()
                    val nameHotel: String = ds.child("nameHotel").value.toString()
                    val priceRange: String = ds.child("priceRange").value.toString()
                    val lat: String = ds.child("lat").value.toString()
                    val lng: String = ds.child("lng").value.toString()
                    val details1 = ds.child("details1").value.toString()
                    val details2 = ds.child("details2").value.toString()
                    val details3 = ds.child("details3").value.toString()
                    val details4 = ds.child("details4").value.toString()
                    val details5 = ds.child("details5").value.toString()
                    val details6 = ds.child("details6").value.toString()
                    val details7 = ds.child("details7").value.toString()
                    val details8 = ds.child("details8").value.toString()
                    val details9 = ds.child("details9").value.toString()
                    val nearby1 = ds.child("nearby1").value.toString()
                    val nearby2 = ds.child("nearby2").value.toString()
                    val nearby3 = ds.child("nearby3").value.toString()
                    val nearby4 = ds.child("nearby4").value.toString()
                    val nearby5 = ds.child("nearby5").value.toString()
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
                }
                setHotelExplore(view)
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    fun decodedBitmap(source: String): Bitmap {
        val decodedString = Base64.decode(source, Base64.DEFAULT)
        val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
        return decodedByte!!
    }
}