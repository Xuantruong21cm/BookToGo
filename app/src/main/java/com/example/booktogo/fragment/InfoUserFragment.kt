package com.example.booktogo.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTabHost
import androidx.fragment.app.FragmentTransaction
import com.example.booktogo.Helper.AccountHelper
import com.example.booktogo.R
import com.jaredrummler.materialspinner.MaterialSpinner
import kotlinx.android.synthetic.main.fragment_info_user.view.*
import kotlinx.android.synthetic.main.fragment_register.view.img_markerLocation
import kotlinx.android.synthetic.main.fragment_register.view.tv_cityNameRegister

class InfoUserFragment : Fragment() {
    lateinit var topAnim: Animation
    lateinit var bottomAnim: Animation
    lateinit var leftAnim: Animation
    lateinit var rightAnim: Animation
    lateinit var genderList: ArrayList<String>
    lateinit var genderAdapter: ArrayAdapter<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        genderList = ArrayList()
        genderList.add("Male")
        genderList.add("Female")
        genderAdapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, genderList)
        AccountHelper.instance.gender = "Male"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_info_user, container, false)
        initView(view)
        view.spinner_gender.setAdapter(genderAdapter)
        view.spinner_gender.setOnItemSelectedListener(object :
            MaterialSpinner.OnItemSelectedListener<String> {
            override fun onItemSelected(
                view: MaterialSpinner?,
                position: Int,
                id: Long,
                item: String?
            ) {
                AccountHelper.instance.gender = genderList[position]
            }

        })

        view.img_continue_signUp.setOnClickListener {
            if (view.edt_newFirstname.text.trim().isEmpty() || view.edt_newLastname.text.trim()
                    .isEmpty()
                || view.edt_newAge.text.trim().isEmpty() || AccountHelper.instance.gender == null
            ) {
                Toast.makeText(
                    context,
                    "Don't empty the data",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                AccountHelper.instance.firstname = view.edt_newFirstname.text.toString()
                AccountHelper.instance.lastname = view.edt_newLastname.text.toString()
                AccountHelper.instance.age = (view.edt_newAge.text.toString()).toInt()



                val manager: FragmentManager = activity!!.supportFragmentManager
                val transition: FragmentTransaction = manager.beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_left, 0, 0, R.anim.slide_out_left)
                val fragment : Fragment = PhoneAuthenFragment()
                transition.replace(R.id.layout_info_user,fragment).commit()
                transition.addToBackStack(fragment::class.java.simpleName)
            }
        }
        return view
    }

    private fun initView(view: View) {
        topAnim = AnimationUtils.loadAnimation(context, R.anim.top_animation)
        bottomAnim = AnimationUtils.loadAnimation(context, R.anim.bottom_animation)
        leftAnim = AnimationUtils.loadAnimation(context, R.anim.left_animation)
        rightAnim = AnimationUtils.loadAnimation(context, R.anim.right_animation)

        view.img_markerLocation.animation = topAnim
        view.tv_cityNameRegister.animation = topAnim
        view.img_firstname_label.animation = topAnim
        view.edt_newFirstname.animation = leftAnim
        view.img_lastname_label.animation = leftAnim
        view.edt_newLastname.animation = rightAnim
        view.img_age_label.animation = rightAnim
        view.edt_newAge.animation = rightAnim
        view.img_gender_label.animation = bottomAnim
        view.spinner_gender.animation = bottomAnim
        view.img_continue_signUp.animation = bottomAnim
    }
}