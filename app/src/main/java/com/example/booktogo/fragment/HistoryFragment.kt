package com.example.booktogo.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentPagerAdapter
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
        val adapter = HistoryViewPagerAdapter(getChildFragmentManager(),
            FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        )
        view.viewpager_history.adapter = adapter
        view.tablayout_history.setupWithViewPager(view.viewpager_history)

        view.img_back_history.setOnClickListener {
            activity!!.supportFragmentManager.popBackStack()
        }
        return view
    }


    override fun onDestroyView() {
        HomeActivity.chip_navigationView.visibility = View.VISIBLE
        super.onDestroyView()
    }
}