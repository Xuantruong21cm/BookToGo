package com.example.booktogo.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.booktogo.fragment.HisrotyBookingFragment
import com.example.booktogo.fragment.PresentFragment

class HistoryViewPagerAdapter(fm: FragmentManager, behavior: Int) :
    FragmentPagerAdapter(fm, behavior) {
    var list : ArrayList<Fragment> = ArrayList()

    init {
        list.add(PresentFragment())
        list.add(HisrotyBookingFragment())
    }


    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Fragment {
        return list[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "Present Booking"
            1 -> "History Booking"
            else -> ""
        }
    }
}