package com.example.booktogo.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.room.Room
import com.example.booktogo.Helper.HotelHelper
import com.example.booktogo.R
import com.example.booktogo.adapter.HotelFavouriteAdapter
import com.example.booktogo.model.HotelExplore
import com.example.booktogo.room.Hotel
import com.example.booktogo.room.HotelDatabase
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.fragment_favourite.view.*
import java.util.*


class FavouriteFragment : Fragment() {
    lateinit var db: HotelDatabase
    lateinit var list : MutableList<Hotel>
    lateinit var adapter : HotelFavouriteAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view : View = inflater.inflate(R.layout.fragment_favourite, container, false)
        initData()
        initView(view)


        return view
    }

    private fun initData() {
        db = Room.databaseBuilder(
            activity!!.applicationContext,
            HotelDatabase::class.java,
            "favourite"
        ).allowMainThreadQueries().build()
        list = db.hotelDAO().getAll()


    }

    private fun initView(view: View) {
        adapter = HotelFavouriteAdapter(list,requireContext())
        if (list.size <=0){
            view.img_hotel_favourite_empty.visibility = View.VISIBLE
            view.recyclerView_favorite_hotel.visibility = View.GONE
        }else{
            view.img_hotel_favourite_empty.visibility = View.GONE
            view.recyclerView_favorite_hotel.visibility = View.VISIBLE
        }

        view.recyclerView_favorite_hotel.setHasFixedSize(true)
        view.recyclerView_favorite_hotel.adapter = adapter
        adapter.setHotelViewAdapter(object : HotelFavouriteAdapter.HotelViewListener{
            override fun HotelViewwOnClick(hotelExplore: Hotel) {
                HotelHelper.instance.addressHotel = hotelExplore.addressHotel
                HotelHelper.instance.idHotel = hotelExplore.idHotel
                HotelHelper.instance.nameHotel = hotelExplore.nameHotel
                HotelHelper.instance.priceRange = hotelExplore.priceRange
                HotelHelper.instance.levelHotel = hotelExplore.levelHotel
                HotelHelper.instance.lat = hotelExplore.lat
                HotelHelper.instance.lng = hotelExplore.lng
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

                val manager : FragmentManager = activity!!.supportFragmentManager
                val transition : FragmentTransaction = manager.beginTransaction().setCustomAnimations(R.anim.slide_in_left, 0, 0, R.anim.slide_out_left)
                val fragment : Fragment = HotelFavouriteFragment()
                transition.replace(R.id.layout_favoutite,fragment).commit()
                transition.addToBackStack(fragment::class.java.simpleName)
            }

            override fun DeleteFavourite(hotel: Hotel,position : Int) {
                db.hotelDAO().delete(hotel)
                list.removeAt(position)
                adapter.notifyItemRemoved(position)
                adapter.notifyDataSetChanged()
            }
        })
    }
}