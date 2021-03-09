package com.example.booktogo.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.booktogo.Helper.AccountHelper
import com.example.booktogo.R
import com.example.booktogo.model.User
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.*
import com.mukesh.OnOtpCompletionListener
import kotlinx.android.synthetic.main.fragment_o_t_p.view.*
import kotlinx.android.synthetic.main.fragment_phone_authen.view.*
import kotlinx.android.synthetic.main.fragment_phone_authen.view.img_authentication_label
import kotlinx.android.synthetic.main.fragment_phone_authen.view.textView
import kotlinx.android.synthetic.main.fragment_register.view.img_markerLocation
import kotlinx.android.synthetic.main.fragment_register.view.tv_cityNameRegister


class OTPFragment : Fragment(), OnOtpCompletionListener {
    lateinit var topAnim: Animation
    lateinit var bottomAnim: Animation
    lateinit var leftAnim: Animation
    lateinit var rightAnim: Animation
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var database: FirebaseDatabase
    lateinit var reference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_o_t_p, container, false)
        initView(view)
        view.textView.text =
            "An authentication code has been  \nsent to " + AccountHelper.instance.phone
        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        reference = database.getReference("Users")
        view.img_continue_OTP.setOnClickListener {
            if (view.otpView.text!!.length < 6) {
                Toast.makeText(context, "OTP Is Not In The Correct Format", Toast.LENGTH_SHORT)
                    .show()
            } else {
                val credential: PhoneAuthCredential = PhoneAuthProvider.getCredential(
                    AccountHelper.instance.codeOTP,
                    view.otpView.text.toString()
                )
                register(credential)
            }
        }
        view.otpView.setOtpCompletionListener(this)

        return view
    }

    private fun register(credential: PhoneAuthCredential) {
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(object :
            OnCompleteListener<AuthResult> {
            override fun onComplete(task: Task<AuthResult>) {
                if (task.isSuccessful) {
                    val email: String = AccountHelper.instance.email.toString()
                    val userName: String = AccountHelper.instance.userName.toString()
                    val passWord: String = AccountHelper.instance.passWord.toString()
                    val firstname: String = AccountHelper.instance.firstname.toString()
                    val lastname: String = AccountHelper.instance.lastname.toString()
                    val age: Int = AccountHelper.instance.age!!
                    val gender: String = AccountHelper.instance.gender.toString()
                    val phone: String = AccountHelper.instance.phone.toString()
                    val avatar : String = ""
                    val user: User = User(
                        email,
                        userName,
                        passWord,
                        firstname,
                        lastname,
                        age,
                        gender,
                        phone,
                        avatar
                    )
                    reference.child(AccountHelper.instance.phone!!).setValue(user)
                    Toast.makeText(context, "Sign Up Success", Toast.LENGTH_SHORT).show()
                    for (fragment in activity!!.supportFragmentManager.fragments) {
                        activity!!.supportFragmentManager.beginTransaction().remove(fragment!!)
                            .commit()
                    }
                } else {
                    Toast.makeText(context, "OTP Is Not Correct", Toast.LENGTH_SHORT).show()
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