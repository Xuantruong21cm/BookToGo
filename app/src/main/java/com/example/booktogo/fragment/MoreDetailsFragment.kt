package com.example.booktogo.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.booktogo.Helper.HotelHelper
import com.example.booktogo.Helper.TripHelper
import com.example.booktogo.R
import com.example.booktogo.adapter.AllDetailsImageAdapter
import kotlinx.android.synthetic.main.fragment_more_details.view.*


class MoreDetailsFragment : Fragment() {
    lateinit var imageList : ArrayList<String>
    lateinit var adapter : AllDetailsImageAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        imageList = ArrayList()
        imageList.add(HotelHelper.instance.details1!!)
        imageList.add(HotelHelper.instance.details2!!)
        imageList.add(HotelHelper.instance.details3!!)
        imageList.add(HotelHelper.instance.details4!!)
        imageList.add(HotelHelper.instance.details5!!)
        imageList.add(HotelHelper.instance.details6!!)
        imageList.add(HotelHelper.instance.details7!!)
        imageList.add(HotelHelper.instance.details8!!)
        imageList.add(HotelHelper.instance.details9!!)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view : View =  inflater.inflate(R.layout.fragment_more_details, container, false)
        adapter = AllDetailsImageAdapter(imageList, context!!)
        view.recyclerView_all_image_details.setHasFixedSize(true)
        view.recyclerView_all_image_details.adapter = adapter
        adapter.setAllImageOnClick(object : AllDetailsImageAdapter.AllImageClickListener{
            override fun onClick(position: Int) {
                   HotelHelper.instance.detailsList = imageList
                   HotelHelper.instance.detailsPotision = position
                val manager = activity!!.supportFragmentManager
                val transition = manager.beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_left, 0, 0, R.anim.slide_out_left)
                val fragment = ImageDetailsSliderViewFragment()
                transition.add(R.id.layout_home, fragment).commit()
                transition.addToBackStack(fragment::class.java.simpleName)
            }
        })

        view.img_close_more_details.setOnClickListener {
            activity!!.onBackPressed()

        }
        return view
    }
}