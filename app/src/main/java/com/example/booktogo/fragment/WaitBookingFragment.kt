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
import kotlinx.android.synthetic.main.fragment_wait_booking.view.*


class WaitBookingFragment : Fragment() {
    lateinit var list: ArrayList<BookingHistory>
    lateinit var adapter: HistoryAdapter
    lateinit var reference: DatabaseReference
    lateinit var referenceHotel: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_wait_booking, container, false)
        initView(view)

        return view
    }

    private fun initView(view: View) {
        activity!!.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        HomeActivity.chip_navigationView.visibility = View.GONE
        list = ArrayList()

        val reference: DatabaseReference =
            FirebaseDatabase.getInstance().getReference("Booking").child(
                AccountHelper.instance.phone!!
            )
        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (ds: DataSnapshot in snapshot.children) {
                        Log.d("snapshot", "onDataChange: " + ds.key)
                        val idBooking = ds.child("idBooking").value.toString()
                        val name = ds.child("nameHotel").value.toString()
                        val address = ds.child("addressHotel").value.toString()
                        val date = ds.child("date_time").value.toString()
                        val days = ds.child("days").value.toString()
                        val avatar = ds.child("detail_avatar").value.toString()
                        val price = ds.child("price").value.toString()
                        val district = ds.child("district").value.toString()
                        val idHotel = ds.child("idHotel").value.toString()
                        val city = ds.child("cityname").value.toString()

                        val history =
                            BookingHistory(
                                idBooking,
                                avatar,
                                name,
                                address,
                                date,
                                days,
                                price,
                                district,
                                idHotel,
                                city
                            )
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
        adapter.ClickCancelBooking(object : HistoryAdapter.cancelBooking {
            override fun cancel(bookingHistory: BookingHistory, position: Int) {
                list.removeAt(position)
                adapter.notifyItemRemoved(position)
                adapter.notifyDataSetChanged()

                reference = FirebaseDatabase.getInstance().getReference("Booking")
                    .child(AccountHelper.instance.phone!!).child(bookingHistory.idBooking)


                reference.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            snapshot.ref.removeValue()
                            referenceHotel =
                                FirebaseDatabase.getInstance().getReference(bookingHistory.city)
                                    .child(bookingHistory.district).child(bookingHistory.idHotel).child("active")
                            referenceHotel.setValue("0")
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }
                })
            }
        })
    }
}