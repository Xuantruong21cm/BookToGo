package com.example.booktogo.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.booktogo.Helper.AccountHelper
import com.example.booktogo.R
import com.example.booktogo.fragment.ForgotPasswordFragment
import com.example.booktogo.fragment.RegisterFragment
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first

val Context.dataStorePass: DataStore<Preferences> by preferencesDataStore(name = "rememberPass")
val Context.dataStoreUser: DataStore<Preferences> by preferencesDataStore(name = "rememberUser")
val Context.dataStoreCheck: DataStore<Preferences> by preferencesDataStore(name = "rememberCheck")

class LoginActivity : AppCompatActivity() {
    lateinit var topAnim: Animation
    lateinit var bottomAnim: Animation
    lateinit var leftAnim: Animation
    lateinit var rightAnim: Animation
    lateinit var userName: String
    lateinit var passWord: String
    private var doubleBackToExitPressedOnce : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initAnimation()
        //applicationContext.deleteDatabase("favourite")

        GlobalScope.launch(Dispatchers.IO) {
            val valuePass = read("rememberPass") ?: ""
            val valueUser = readUser("rememberUser") ?: ""
            val valueCheck = readCheck("rememberCheck") ?: false
            withContext(Dispatchers.Main) {
                edt_username.setText(valueUser)
                edt_password.setText(valuePass)
                checkbox_rememberPass.isChecked = valueCheck
            }
        }

        img_register.setOnClickListener {
            val manager = supportFragmentManager
            val transition = manager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in_left, 0, 0, R.anim.slide_out_left)
            val fragment = RegisterFragment()
            transition.add(R.id.layout_main, fragment).commit()
            transition.addToBackStack(fragment::class.java.simpleName)
        }

        img_signIn.setOnClickListener {
            userName = edt_username.text.toString().trim()
            passWord = edt_password.text.toString().trim()

            val reference: DatabaseReference = FirebaseDatabase.getInstance().getReference("Users")
            val checkUser: Query =
                reference.orderByChild("phone").equalTo("+84" + userName.substring(1))
            checkUser.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val passwordFromDB =
                            snapshot.child("+84" + userName.substring(1)).child("passWord")
                                .getValue(String::class.java)
                        if (passwordFromDB!!.equals(passWord)) {
                            AccountHelper.instance.firstname =
                                snapshot.child("+84" + userName.substring(1)).child("firstname")
                                    .getValue(String::class.java)
                            AccountHelper.instance.lastname =
                                snapshot.child("+84" + userName.substring(1)).child("lastname")
                                    .getValue(String::class.java)
                            AccountHelper.instance.email =
                                snapshot.child("+84" + userName.substring(1)).child("email")
                                    .getValue(String::class.java)
                            AccountHelper.instance.age =
                                snapshot.child("+84" + userName.substring(1)).child("age")
                                    .getValue(Int::class.java)
                            AccountHelper.instance.gender =
                                snapshot.child("+84" + userName.substring(1)).child("gender")
                                    .getValue(String::class.java)
                            AccountHelper.instance.userName =
                                snapshot.child("+84" + userName.substring(1)).child("userName")
                                    .getValue(String::class.java)
                            AccountHelper.instance.phone =
                                snapshot.child("+84" + userName.substring(1)).child("phone")
                                    .getValue(String::class.java)
                            AccountHelper.instance.passWord =
                                snapshot.child("+84" + userName.substring(1)).child("passWord")
                                    .getValue(String::class.java)
                            AccountHelper.instance.avatar =  snapshot.child("+84" + userName.substring(1)).child("avatar")
                                .getValue(String::class.java)

                            if (checkbox_rememberPass.isChecked) {
                                GlobalScope.launch(Dispatchers.IO) {
                                    save("rememberPass", edt_password.text.toString())
                                    saveUser("rememberUser", edt_username.text.toString())
                                    saveCheck("rememberCheck",true)
                                    withContext(Dispatchers.Main) {
                                        startActivity(
                                            Intent(
                                                this@LoginActivity,
                                                HomeActivity::class.java
                                            )
                                        )
                                        finish()
                                    }
                                }
                            } else {

                                GlobalScope.launch(Dispatchers.IO) {
                                    dataStorePass.edit { it.clear() }
                                    dataStoreUser.edit { it.clear() }
                                    dataStoreCheck.edit { it.clear() }
                                    withContext(Dispatchers.Main) {
                                        startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                                        finish()
                                    }
                                }
                            }
                        } else {
                            GlobalScope.launch(Dispatchers.IO) {
                                dataStorePass.edit { it.clear() }
                                dataStoreUser.edit { it.clear() }
                                dataStoreCheck.edit { it.clear() }
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(
                                        this@LoginActivity,
                                        "Wrong Account",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        }

        img_forgot_password.setOnClickListener {
            val manager = supportFragmentManager
            val transition = manager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in_left, 0, 0, R.anim.slide_out_left)
            val fragment = ForgotPasswordFragment()
            transition.add(R.id.layout_main, fragment).commit()
            transition.addToBackStack(fragment::class.java.simpleName)

        }

    }

    private suspend fun save(key: String, value: String) {
        val data = stringPreferencesKey(key)
        dataStorePass.edit { rememberPass ->
            rememberPass[data] = value
        }
    }

    private suspend fun saveUser(key: String, value: String) {
        val data = stringPreferencesKey(key)
        dataStoreUser.edit { rememberPass ->
            rememberPass[data] = value
        }
    }

    private suspend fun saveCheck(key: String, value: Boolean) {
        val data = booleanPreferencesKey(key)
        dataStoreCheck.edit { rememberCheck ->
            rememberCheck[data] = value
        }
    }

    private suspend fun read(key: String): String? {
        val dataStorekey = stringPreferencesKey(key)
        val preferences = dataStorePass.data.first()
        return preferences[dataStorekey]
    }
    private suspend fun readCheck(key: String): Boolean? {
        val dataStorekey = booleanPreferencesKey(key)
        val preferences = dataStoreCheck.data.first()
        return preferences[dataStorekey]
    }

    private suspend fun readUser(key: String): String? {
        val dataStorekey = stringPreferencesKey(key)
        val preferences = dataStoreUser.data.first()
        return preferences[dataStorekey]
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            hideSystemUI()
        }
    }

    private fun hideSystemUI() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R){
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN)
        }else{
            window.setDecorFitsSystemWindows(false)
            val controller : WindowInsetsController = window.insetsController!!
            controller.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
            controller.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    private fun initAnimation() {
        topAnim = AnimationUtils.loadAnimation(this@LoginActivity, R.anim.top_animation)
        bottomAnim = AnimationUtils.loadAnimation(this@LoginActivity, R.anim.bottom_animation)
        leftAnim = AnimationUtils.loadAnimation(this@LoginActivity, R.anim.left_animation)
        rightAnim = AnimationUtils.loadAnimation(this@LoginActivity, R.anim.right_animation)

        img_logo.animation = topAnim
        img_welcome.animation = topAnim
        img_let_explore.animation = topAnim
        edt_password.animation = leftAnim
        edt_username.animation = leftAnim
        img_signIn.animation = bottomAnim
        img_register.animation = bottomAnim
        checkbox_rememberPass.animation = leftAnim
        img_remember.animation = rightAnim
    }

    fun noClick(view: View) {}

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce){
            super.onBackPressed()
            return
        }
        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this@LoginActivity,getString(R.string.double_click_back),Toast.LENGTH_SHORT).show()
        GlobalScope.launch(Dispatchers.IO){
            delay(2000)
            doubleBackToExitPressedOnce = false
        }
    }
}