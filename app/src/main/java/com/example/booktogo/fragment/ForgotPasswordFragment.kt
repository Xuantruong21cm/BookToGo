package com.example.booktogo.fragment

import android.os.Bundle
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
import kotlinx.android.synthetic.main.fragment_forgot_password.view.*
import java.util.concurrent.TimeUnit

class ForgotPasswordFragment : Fragment() {
    lateinit var topAnim: Animation
    lateinit var bottomAnim: Animation
    lateinit var leftAnim: Animation
    lateinit var rightAnim: Animation
    lateinit var reference: DatabaseReference
    lateinit var database : FirebaseDatabase
    lateinit var auth : FirebaseAuth
    lateinit var callbacks : PhoneAuthProvider.OnVerificationStateChangedCallbacks
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view : View =  inflater.inflate(R.layout.fragment_forgot_password, container, false)
        initView(view)
        initListener(view)
        return view
    }

    private fun initListener(view: View) {
        auth = Firebase.auth
        database = FirebaseDatabase.getInstance()
        reference = database.getReference("User")

        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {

            }

            override fun onVerificationFailed(p0: FirebaseException) {

            }

            override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(p0, p1)
                AccountHelper.instance.phone = "+84"+(view.otpPhone.text.substring(1))
                val manager: FragmentManager = activity!!.supportFragmentManager
                val transition: FragmentTransaction = manager.beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_left, 0, 0, R.anim.slide_out_left)
                val fragment : Fragment = OTPForgotPassFragment()
                AccountHelper.instance.codeOTP = p0
                transition.replace(R.id.layout_forgot_pass,fragment).commit()
                transition.addToBackStack(fragment::class.java.simpleName)
            }

        }

        view.img_continue_Phone.setOnClickListener {
            if (view.otpPhone.text.length < 10 || view.otpPhone.text.length > 10) {
                Toast.makeText(context, "Incorrect Phone Number Format", Toast.LENGTH_SHORT).show()
            }else{
                reference.child("+84"+(view.otpPhone.text.substring(1)))
                reference.addListenerForSingleValueEvent(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (!snapshot.hasChild("+84"+(view.otpPhone.text.substring(1)))){
                            sendVerificationPhoneNumber("+84"+(view.otpPhone.text.substring(1)))
                        }else{
                            Toast.makeText(context,"Phone number already exists",Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }

                })
            }

        }

        view.img_backForgot.setOnClickListener {
            activity!!.supportFragmentManager.popBackStack()

        }

    }
    private fun sendVerificationPhoneNumber(phone : String){
        val options : PhoneAuthOptions = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phone)
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(activity!!)                 // Activity (for callback binding)
            .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun initView(view: View) {
        activity!!.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        topAnim = AnimationUtils.loadAnimation(context, R.anim.top_animation)
        bottomAnim = AnimationUtils.loadAnimation(context, R.anim.bottom_animation)
        leftAnim = AnimationUtils.loadAnimation(context, R.anim.left_animation)
        rightAnim = AnimationUtils.loadAnimation(context, R.anim.right_animation)

        view.img_backForgot.animation = topAnim
        view.img_authentication_label.animation = leftAnim
        view.textView.animation = leftAnim
        view.otpPhone.animation = rightAnim
        view.img_continue_Phone.animation = bottomAnim

    }
}