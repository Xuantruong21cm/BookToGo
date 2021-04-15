package com.example.booktogo.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.booktogo.Helper.AccountHelper
import com.example.booktogo.R
import com.example.booktogo.activity.HomeActivity
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.fragment_information.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class InformationFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view : View =  inflater.inflate(R.layout.fragment_information, container, false)
        initView(view)
        initListener(view)
        return view
    }

    private fun initListener(view: View) {
        view.img_edit_pass.setOnClickListener {
                view.layout_changePass.visibility = View.VISIBLE
                view.img_edit_pass.visibility = View.GONE
        }

        view.img_cancel_changePass.setOnClickListener {
            view.layout_changePass.visibility = View.GONE
            view.img_edit_pass.visibility = View.VISIBLE
        }
        view.img_back_info.setOnClickListener {
            activity!!.supportFragmentManager.popBackStack()
            HomeActivity.chip_navigationView.visibility = View.VISIBLE
        }

        view.btn_changePass.setOnClickListener {
            if (view.edt_old_password.text.toString().trim().isEmpty() || view.edt_new_password.text.toString().trim().isEmpty() || view.tv_confirm_password.text.toString().trim().isEmpty()){
                Toast.makeText(requireContext(),getString(R.string.please_enter_enough_data),Toast.LENGTH_SHORT).show()
            }else{
                if (!view.edt_new_password.text.toString().trim().equals(view.edt_confirm_password.text.toString().trim())){
                    Toast.makeText(requireContext(),getString(R.string.new_password_not_match),Toast.LENGTH_SHORT).show()
                }else{
                    if (!view.edt_old_password.text.toString().trim().equals(AccountHelper.instance.passWord)){
                        Toast.makeText(requireContext(),getString(R.string.old_password_not_true),Toast.LENGTH_SHORT).show()
                    }else{
                        FirebaseDatabase.getInstance().getReference("Users").child(AccountHelper.instance.phone!!).child("passWord")
                            .setValue(view.edt_new_password.text.toString().trim())
                        AccountHelper.instance.passWord = view.edt_new_password.text.toString().trim()
                        view.tv_passs.text = view.edt_new_password.text.toString().trim()
                        view.edt_old_password.text = null
                        view.edt_new_password.text = null
                        view.edt_confirm_password.text = null
                        view.layout_changePass.visibility = View.GONE
                        view.img_edit_pass.visibility = View.VISIBLE
                        Toast.makeText(requireContext(),getString(R.string.change_password_complete),Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initView(view: View) {
        activity!!.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        HomeActivity.chip_navigationView.visibility = View.GONE
        view.tv_fullname.text = AccountHelper.instance.firstname + " " + AccountHelper.instance.lastname
        view.tv_username.text = AccountHelper.instance.userName
        view.tv_gender.text = AccountHelper.instance.gender
        view.tv_age.text = AccountHelper.instance.age.toString()
        view.tv_email.text = AccountHelper.instance.email
        view.tv_phone.text = AccountHelper.instance.phone
        view.tv_passs.text = AccountHelper.instance.passWord
    }
}