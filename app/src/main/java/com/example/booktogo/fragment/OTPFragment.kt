package com.example.booktogo.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.example.booktogo.Helper.AccountHelper
import com.example.booktogo.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.mukesh.OnOtpCompletionListener
import kotlinx.android.synthetic.main.fragment_o_t_p.view.*
import kotlinx.android.synthetic.main.fragment_phone_authen.view.*
import kotlinx.android.synthetic.main.fragment_phone_authen.view.img_authentication_label
import kotlinx.android.synthetic.main.fragment_phone_authen.view.otpPhone
import kotlinx.android.synthetic.main.fragment_phone_authen.view.textView
import kotlinx.android.synthetic.main.fragment_register.view.*
import kotlinx.android.synthetic.main.fragment_register.view.img_markerLocation
import kotlinx.android.synthetic.main.fragment_register.view.tv_cityNameRegister


class OTPFragment : Fragment(),OnOtpCompletionListener {
    lateinit var topAnim: Animation
    lateinit var bottomAnim: Animation
    lateinit var leftAnim: Animation
    lateinit var rightAnim: Animation
    lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view : View = inflater.inflate(R.layout.fragment_o_t_p, container, false)
        initView(view)
        view.textView.text = "An authentication code has been  \nsent to "+AccountHelper.instance.phone
        firebaseAuth = FirebaseAuth.getInstance()
        view.img_continue_OTP.setOnClickListener {
            if (view.otpView.text!!.length <6){
                Toast.makeText(context,"OTP Is Not In The Correct Format",Toast.LENGTH_SHORT).show()
            }else{
                val credential : PhoneAuthCredential = PhoneAuthProvider.getCredential(AccountHelper.instance.codeOTP,
                    view.otpView.text.toString())
                    register(credential)
            }
        }

        return view
    }

    private fun register(credential: PhoneAuthCredential) {
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(object : OnCompleteListener<AuthResult>{
            override fun onComplete(task: Task<AuthResult>) {
                if (task.isSuccessful){
                    Toast.makeText(context,"OTP Is OK",Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(context,"OTP Is Not Correct",Toast.LENGTH_SHORT).show()
                }
            }

        })
    }


    private fun initView(view: View) {
        topAnim = AnimationUtils.loadAnimation(context, R.anim.top_animation)
        bottomAnim = AnimationUtils.loadAnimation(context, R.anim.bottom_animation)
        leftAnim = AnimationUtils.loadAnimation(context, R.anim.left_animation)
        rightAnim = AnimationUtils.loadAnimation(context, R.anim.right_animation)

        view.img_markerLocation.animation = topAnim
        view.tv_cityNameRegister.animation = topAnim
        view.img_authentication_label.animation = leftAnim
        view.textView.animation = leftAnim
        view.otpView.animation = rightAnim
        view.img_continue_OTP.animation = bottomAnim
    }

    override fun onOtpCompleted(otp: String?) {
        
    }
}