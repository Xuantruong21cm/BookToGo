package com.example.booktogo.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.booktogo.R
import com.example.booktogo.activity.HomeActivity
import com.example.booktogo.adapter.HistoryViewPagerAdapter
import kotlinx.android.synthetic.main.fragment_history.view.*

class HistoryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_history, container, false)
        val adapter = HistoryViewPagerAdapter(
            getChildFragmentManager(),
            FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        )
        view.viewpager_history.adapter = adapter
        view.tablayout_history.setupWithViewPager(view.viewpager_history)
        view.viewpager_history.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                if (position == 0){
                    val transition = requireActivity().supportFragmentManager.beginTransaction()
                    transition.detach(WaitBookingFragment())
                    transition.attach(WaitBookingFragment())
                    transition.commit()
                }else if (position == 1){
                    val transition = requireActivity().supportFragmentManager.beginTransaction()
                    transition.detach(PresentFragment())
                    transition.attach(PresentFragment())
                    transition.commit()
                }else if (position == 2){
                    val transition = requireActivity().supportFragmentManager.beginTransaction()
                    transition.detach(HisrotyBookingFragment())
                    transition.attach(HisrotyBookingFragment())
                    transition.commit()
                }
            }

            override fun onPageScrollStateChanged(state: Int) {

            }

        })

        view.img_back_history.setOnClickListener {
            activity!!.supportFragmentManager.popBackStack()
        }
        return view
    }

    fun makeFragmentName(id: Int, position: Int): String {
        return "android:switcher:" + id + ":" + position
    }


    override fun onDestroyView() {
        HomeActivity.chip_navigationView.visibility = View.VISIBLE
        super.onDestroyView()
    }
}