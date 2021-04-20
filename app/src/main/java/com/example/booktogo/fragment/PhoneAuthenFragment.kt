package com.example.booktogo.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.booktogo.Helper.AccountHelper
import com.example.booktogo.R
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_info_user.view.*
import kotlinx.android.synthetic.main.fragment_info_user.view.img_firstname_label
import kotlinx.android.synthetic.main.fragment_phone_authen.view.*
import kotlinx.android.synthetic.main.fragment_register.view.*
import kotlinx.android.synthetic.main.fragment_register.view.img_markerLocation
import kotlinx.android.synthetic.main.fragment_register.view.tv_cityNameRegister
import java.util.concurrent.TimeUnit


class PhoneAuthenFragment : Fragment() {
    lateinit var topAnim: Animation
    lateinit var bottomAnim: Animation
    lateinit var leftAnim: Animation
    lateinit var rightAnim: Animation

    private lateinit var auth: FirebaseAuth
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    lateinit var reference: DatabaseReference
    lateinit var database : FirebaseDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_phone_authen, container, false)
        initView(view)
        auth = Firebase.auth
        database = FirebaseDatabase.getInstance()
        reference = database.getReference("Users")
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {

            }

            override fun onVerificationFailed(p0: FirebaseException) {

            }

            override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(p0, p1)
                var manager: FragmentManager = activity!!.supportFragmentManager
                var transition: FragmentTransaction = manager.beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_left, 0, 0, R.anim.slide_out_left)
                var fragment : Fragment = OTPFragment()
                AccountHelper.instance.codeOTP = p0
                transition.replace(R.id.layout_info_user,fragment).commit()
                transition.addToBackStack(fragment::class.java.simpleName)
            }
        }

        view.img_continue_Phone.setOnClickListener {
            if (view.otpPhone.text.length < 10 || view.otpPhone.text.length > 10) {
                Toast.makeText(context, "Incorrect Phone Number Format", Toast.LENGTH_SHORT).show()
            } else {
                reference.child("+84"+(view.otpPhone.text.substring(1)))
                reference.addListenerForSingleValueEvent(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.value == null){
                            sendVerificationPhoneNumber("+84"+(view.otpPhone.text.substring(1)))
                        }else{
                            Toast.makeText(context,"Phone number already exists",Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }

                })
                Log.d("phone", "+84"+(view.otpPhone.text.substring(1)))
            }
        }
        return view
    }

    private fun sendVerificationPhoneNumber(phone: String) {
        val options: PhoneAuthOptions = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phone)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(activity)                 // Activity (for callback binding)
            .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
        AccountHelper.instance.phone = phone
    }

    private fun initView(view: View) {
        activity!!.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        topAnim = AnimationUtils.loadAnimation(context, R.anim.top_animation)
        bottomAnim = AnimationUtils.loadAnimation(context, R.anim.bottom_animation)
        leftAnim = AnimationUtils.loadAnimation(context, R.anim.left_animation)
        rightAnim = AnimationUtils.loadAnimation(context, R.anim.right_animation)

        view.img_markerLocation.animation = topAnim
        view.tv_cityNameRegister.animation = topAnim
        view.img_authentication_label.animation = leftAnim
        view.textView.animation = leftAnim
        view.otpPhone.animation = rightAnim
        view.img_continue_Phone.animation = bottomAnim
    }
}