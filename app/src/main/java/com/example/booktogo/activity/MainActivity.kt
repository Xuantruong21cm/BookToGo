package com.example.booktogo.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.example.booktogo.R
import com.example.booktogo.fragment.RegisterFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var topAnim: Animation
    lateinit var bottomAnim: Animation
    lateinit var leftAnim: Animation
    lateinit var rightAnim: Animation


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.decorView.setOnSystemUiVisibilityChangeListener { visibility ->
            // Note that system bars will only be "visible" if none of the
            // LOW_PROFILE, HIDE_NAVIGATION, or FULLSCREEN flags are set.
            if (visibility and View.SYSTEM_UI_FLAG_FULLSCREEN == 0) {

            } else {
                // TODO: The system bars are NOT visible. Make any desired
                // adjustments to your UI, such as hiding the action bar or
                // other navigational controls.
            }
        }

        topAnim = AnimationUtils.loadAnimation(this@MainActivity, R.anim.top_animation)
        bottomAnim = AnimationUtils.loadAnimation(this@MainActivity, R.anim.bottom_animation)
        leftAnim = AnimationUtils.loadAnimation(this@MainActivity, R.anim.left_animation)
        rightAnim = AnimationUtils.loadAnimation(this@MainActivity, R.anim.right_animation)

        img_logo.animation = topAnim
        img_welcome.animation = topAnim
        img_let_explore.animation = topAnim
        edt_password.animation = leftAnim
        edt_username.animation = leftAnim
        img_login_with.animation = rightAnim
        img_facebook.animation = rightAnim
        img_google.animation = rightAnim
        img_signIn.animation = bottomAnim
        img_register.animation = bottomAnim

        img_register.setOnClickListener {
            var manager  = supportFragmentManager
            var transition = manager.beginTransaction().setCustomAnimations(R.anim.slide_in_left,0,0,R.anim.slide_out_left)
            var fragment = RegisterFragment()
            transition.add(R.id.layout_main,fragment).commit()
            transition.addToBackStack(fragment::class.java.simpleName)


        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            hideSystemUI()
        }
    }

    private fun hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                // Set the content to appear under the system bars so that the
                // content doesn't resize when the system bars hide and show.
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                // Hide the nav bar and status bar
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }

    // Shows the system bars by removing all the flags
// except for the ones that make the content appear under the system bars.
    private fun showSystemUI() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
    }

    fun noClick(view: View) {}
}