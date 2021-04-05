package com.example.booktogo.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.booktogo.Helper.HotelHelper
import com.example.booktogo.R
import com.example.booktogo.adapter.DetailsSliderAdapter
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView
import kotlinx.android.synthetic.main.fragment_image_details_slider_view.view.*


class ImageDetailsSliderViewFragment : Fragment() {
    lateinit var adapter : DetailsSliderAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view : View =  inflater.inflate(
            R.layout.fragment_image_details_slider_view,
            container,
            false
        )
        adapter = DetailsSliderAdapter(HotelHelper.instance.detailsList!!, context!!)
        view.imageSlider.setSliderAdapter(adapter)
        view.imageSlider.setIndicatorAnimation(IndicatorAnimationType.WORM)
        view.imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
        view.imageSlider.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH)
        view.imageSlider.setIndicatorSelectedColor(Color.WHITE)
        view.imageSlider.setIndicatorUnselectedColor(Color.GRAY)
        view.imageSlider.setScrollTimeInSec(3)
        view.imageSlider.setAutoCycle(true)
        view.imageSlider.startAutoCycle()
        view.imageSlider.currentPagePosition = HotelHelper.instance.detailsPotision!!

        return view
    }
}