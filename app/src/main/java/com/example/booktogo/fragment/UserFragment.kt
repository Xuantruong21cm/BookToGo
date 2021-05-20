package com.example.booktogo.fragment

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.example.booktogo.Helper.AccountHelper
import com.example.booktogo.R
import com.example.booktogo.activity.HomeActivity
import com.example.booktogo.activity.LoginActivity
import kotlinx.android.synthetic.main.fragment_user.view.*


class UserFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view : View =  inflater.inflate(R.layout.fragment_user, container, false)
        initView(view)
        initListener(view)
        return view
    }

    private fun initListener(view: View) {
        view.tv_infomation.setOnClickListener {
            val manager = activity!!.supportFragmentManager
            val transition = manager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in_left, 0, 0, R.anim.slide_out_left)
            val fragment = InformationFragment()
            transition.add(R.id.layout_user, fragment).commit()
            transition.addToBackStack(fragment::class.java.simpleName)
        }

        view.tv_booking_history.setOnClickListener {
            val manager = activity!!.supportFragmentManager
            val transition = manager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in_left, 0, 0, R.anim.slide_out_left)
            val fragment = HistoryFragment()
            transition.add(R.id.layout_user, fragment).commit()
            transition.addToBackStack(fragment::class.java.simpleName)
        }

        view.btn_logOut.setOnClickListener {
            val alertDialog = AlertDialog.Builder(activity!!)
            alertDialog.setCancelable(false)
            alertDialog.setTitle(getString(R.string.log_out))
            alertDialog.setMessage(getString(R.string.log_out_message))
            alertDialog.setPositiveButton("Log Out",object : DialogInterface.OnClickListener{
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    val intent = Intent(activity,LoginActivity::class.java)
                    startActivity(intent)
                    activity!!.finish()
                }
            })
            alertDialog.setNegativeButton("Cancel",object : DialogInterface.OnClickListener{
                override fun onClick(dialog: DialogInterface?, which: Int) {
                }
            })
            alertDialog.show()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initView(view: View) {
        if (decodedBitmap(AccountHelper.instance.avatar!!) != null){
            view.img_avatar.setImageBitmap(decodedBitmap(AccountHelper.instance.avatar!!))
        }
        view.tv_userName_info.text = AccountHelper.instance.firstname + " "+ AccountHelper.instance.lastname
    }

    fun decodedBitmap(source: String): Bitmap {
        val decodedString = Base64.decode(source, Base64.DEFAULT)
        val decodedByte: Bitmap =
            BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
        return decodedByte
    }
}