package com.example.booktogo.fragment

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
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
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.mukesh.OnOtpCompletionListener
import kotlinx.android.synthetic.main.fragment_o_t_p.view.*
import java.io.ByteArrayOutputStream


class OTPFragment : Fragment(), OnOtpCompletionListener {
    lateinit var topAnim: Animation
    lateinit var bottomAnim: Animation
    lateinit var leftAnim: Animation
    lateinit var rightAnim: Animation
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var database: FirebaseDatabase
    lateinit var reference: DatabaseReference

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
                    AccountHelper.instance.codeOTP!!,
                    view.otpView.text.toString()
                )
                register(credential)
            }
        }
        view.otpView.setOtpCompletionListener(this)

        view.img_backOtp_fragment.setOnClickListener {
            activity!!.supportFragmentManager.popBackStack()

        }

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
                    var avatar: String = ""
                    if (gender.equals("Male")) {
                        val bm = BitmapFactory.decodeResource(context!!.resources, R.drawable.male)
                        val byteArrayOutputStream = ByteArrayOutputStream()
                        bm.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
                        val byteArray = byteArrayOutputStream.toByteArray()
                        avatar =Base64.encodeToString(byteArray,Base64.DEFAULT)

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

                    }else{
                        val bm = BitmapFactory.decodeResource(context!!.resources, R.drawable.female)
                        val byteArrayOutputStream = ByteArrayOutputStream()
                        bm.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
                        val byteArray = byteArrayOutputStream.toByteArray()
                        avatar =Base64.encodeToString(byteArray,Base64.DEFAULT)

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
                    }
                } else {
                    Toast.makeText(context, "OTP Is Not Correct", Toast.LENGTH_SHORT).show()
                }
            }

        })
    }

    fun decodedBitmap(source: String): Bitmap {
        val decodedString = Base64.decode(source, Base64.DEFAULT)
        val decodedByte: Bitmap =
            BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
        return decodedByte
    }


    private fun initView(view: View) {
        activity!!.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        topAnim = AnimationUtils.loadAnimation(context, R.anim.top_animation)
        bottomAnim = AnimationUtils.loadAnimation(context, R.anim.bottom_animation)
        leftAnim = AnimationUtils.loadAnimation(context, R.anim.left_animation)
        rightAnim = AnimationUtils.loadAnimation(context, R.anim.right_animation)

        view.img_backOtp_fragment.animation = topAnim
        view.img_authentication_label.animation = leftAnim
        view.textView.animation = leftAnim
        view.otpView.animation = rightAnim
        view.img_continue_OTP.animation = bottomAnim
    }

    override fun onOtpCompleted(otp: String?) {

    }
}