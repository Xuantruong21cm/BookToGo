package com.example.booktogo.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.example.booktogo.Helper.AccountHelper
import com.example.booktogo.R
import com.example.booktogo.activity.HomeActivity
import com.example.booktogo.adapter.HistoryAdapter
import com.example.booktogo.model.BookingHistory
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_present.view.*

class PresentFragment : Fragment() {
    lateinit var list : ArrayList<BookingHistory>
    lateinit var adapter : HistoryAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view : View = inflater.inflate(R.layout.fragment_present, container, false)
        initView(view)

        return view
    }

    private fun initView(view: View) {
        activity!!.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        HomeActivity.chip_navigationView.visibility = View.GONE
        list = ArrayList()

        val reference : DatabaseReference = FirebaseDatabase.getInstance().getReference("Booking").child(
            AccountHelper.instance.phone!!
        )
        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (ds: DataSnapshot in snapshot.children) {
                        Log.d("snapshot", "onDataChange: "+ds.key)
                        val name = ds.child("nameHotel").value.toString()
                        val address = ds.child("addressHotel").value.toString()
                        val date = ds.child("date_time").value.toString()
                        val days = ds.child("days").value.toString()
                        val avatar = ds.child("detail_avatar").value.toString()
                        val price = ds.child("price").value.toString()

                        val history  = BookingHistory(avatar,name,address,date,days,price)
                        list.add(history)
                        setDataHistory(view)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })

    }

    private fun setDataHistory(view: View) {
        adapter = HistoryAdapter(list)
        view.recyclerView_booking_history.adapter = adapter
    }
}