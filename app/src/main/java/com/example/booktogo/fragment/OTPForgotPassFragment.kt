package com.example.booktogo.fragment

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.booktogo.Helper.AccountHelper
import com.example.booktogo.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.*
import com.mukesh.OnOtpCompletionListener
import kotlinx.android.synthetic.main.fragment_o_t_p_forgot_pass.view.*

class OTPForgotPassFragment : Fragment(), OnOtpCompletionListener {
    lateinit var topAnim: Animation
    lateinit var bottomAnim: Animation
    lateinit var leftAnim: Animation
    lateinit var rightAnim: Animation
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var database: FirebaseDatabase
    lateinit var reference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view : View =  inflater.inflate(R.layout.fragment_o_t_p_forgot_pass, container, false)
        initView(view)
        initListener(view)

        return view
    }

    @SuppressLint("SetTextI18n")
    private fun initListener(view: View) {
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
                    AccountHelper.instance.codeOTP!!,
                    view.otpView.text.toString()
                )
                showPass(credential)
            }
        }
        view.otpView.setOtpCompletionListener(this)

        view.img_backOtp.setOnClickListener {
            activity!!.supportFragmentManager.popBackStack()
        }
    }

    private fun showPass(credential: PhoneAuthCredential) {
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(object :
            OnCompleteListener<AuthResult> {
            override fun onComplete(p0: Task<AuthResult>) {
                if (p0.isSuccessful) {
                    FirebaseDatabase.getInstance().getReference("Users")
                        .child(AccountHelper.instance.phone!!)
                        .addListenerForSingleValueEvent(object :
                            ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val dialog = AlertDialog.Builder(activity!!)
                                dialog.setTitle("Your Password")
                                    .setMessage(snapshot.child("passWord").value.toString())
                                    .setPositiveButton("OK",object : DialogInterface.OnClickListener{
                                        override fun onClick(dialog: DialogInterface?, which: Int) {
                                            for (fragment in activity!!.supportFragmentManager.fragments) {
                                                activity!!.supportFragmentManager.beginTransaction().remove(fragment!!)
                                                    .commit()
                                            }
                                        }
                                    }).show()
                              //  Log.d("Forgot", "onDataChange: "+snapshot.child("passWord").value.toString())
                            }

                            override fun onCancelled(error: DatabaseError) {
                            }

                        })
                }
            }

        })
    }

    private fun initView(view: View) {
        activity!!.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        topAnim = AnimationUtils.loadAnimation(context, R.anim.top_animation)
        bottomAnim = AnimationUtils.loadAnimation(context, R.anim.bottom_animation)
        leftAnim = AnimationUtils.loadAnimation(context, R.anim.left_animation)
        rightAnim = AnimationUtils.loadAnimation(context, R.anim.right_animation)

        view.img_backOtp.animation = topAnim
        view.img_authentication_label.animation = leftAnim
        view.textView.animation = leftAnim
        view.otpView.animation = rightAnim
        view.img_continue_OTP.animation = bottomAnim
    }

    override fun onOtpCompleted(otp: String?) {
    }

}