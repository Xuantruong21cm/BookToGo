package com.example.booktogo.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.booktogo.Helper.AccountHelper
import com.example.booktogo.R
import kotlinx.android.synthetic.main.fragment_register.view.*


class RegisterFragment : Fragment() {
    lateinit var topAnim: Animation
    lateinit var bottomAnim: Animation
    lateinit var leftAnim: Animation
    lateinit var rightAnim: Animation
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_register, container, false)
        initView(view)
        view.img_sing_up.setOnClickListener {
            if (view.edt_newEmail.text.isEmpty() || view.edt_newUsername.text.isEmpty() || view.edt_newPassword.text.isEmpty()) {
                Toast.makeText(context, "Don't empty the data", Toast.LENGTH_SHORT).show()
            } else if (view.checkbox_term_condition.isChecked == false) {
                Toast.makeText(
                    context,
                    "Please agree to the Terms & Conditions",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                AccountHelper.instance.email = view.edt_newEmail.text.toString()
                AccountHelper.instance.userName = view.edt_newUsername.text.toString()
                AccountHelper.instance.passWord = view.edt_newPassword.text.toString()
                val manager: FragmentManager = activity!!.supportFragmentManager
                var transition: FragmentTransaction = manager.beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_left, 0, 0, R.anim.slide_out_left)
                var fragment: Fragment = InfoUserFragment()
                transition.replace(R.id.layout_regiter, fragment).commit()
                transition.addToBackStack(fragment::class.java.simpleName)
            }
        }

        view.img_have_account.setOnClickListener {
            for (fragment in activity!!.supportFragmentManager.fragments) {
                activity!!.supportFragmentManager.beginTransaction().remove(fragment!!)
                    .commit()
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
        view.img_getStarted.animation = topAnim
        view.img_email_label.animation = leftAnim
        view.edt_newEmail.animation = leftAnim
        view.img_name_label.animation = rightAnim
        view.checkbox_term_condition.animation = rightAnim
        view.img_tern_condition.animation = rightAnim
        view.img_sing_up.animation = bottomAnim
        view.img_have_account.animation = bottomAnim
        view.img_password_label.animation = bottomAnim
        view.edt_newPassword.animation = bottomAnim
    }
}