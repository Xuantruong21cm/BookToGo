package com.example.booktogo.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.booktogo.fragment.HisrotyBookingFragment
import com.example.booktogo.fragment.PresentFragment
import com.example.booktogo.fragment.WaitBookingFragment

class HistoryViewPagerAdapter(fm: FragmentManager, behavior: Int) :
    FragmentPagerAdapter(fm, behavior) {
    var list : ArrayList<Fragment> = ArrayList()

    init {
        list.add(WaitBookingFragment())
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
            0 -> "Wait Booking"
            1 -> "Present Booking"
            2 -> "History Booking"
            else -> ""
        }
    }
}