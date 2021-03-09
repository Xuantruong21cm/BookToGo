package com.example.booktogo.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.booktogo.R
import com.example.booktogo.fragment.BookmarkFragment
import com.example.booktogo.fragment.HomeFragment
import com.example.booktogo.fragment.SearchFragment
import com.example.booktogo.fragment.UserFragment
import com.example.booktogo.model.PlaceExplore
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import kotlinx.android.synthetic.main.activity_home.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.log

class HomeActivity : AppCompatActivity() {
    companion object {
        lateinit var listPlace_HaNoi: ArrayList<PlaceExplore>
    }

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        supportFragmentManager.beginTransaction().replace(R.id.layout_home, HomeFragment()).commit()
        chip_navigation.setItemSelected(R.id.bottom_nav_home, true)
        bottom_navigation()
        initExplore_HaNoi()
        getCurrentLocation()
    }

    fun getCurrentLocation() {
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(this@HomeActivity)
        if (ActivityCompat.checkSelfPermission(
                this@HomeActivity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ){
            getLocation()
        }else{
            ActivityCompat.requestPermissions(this@HomeActivity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),1)
            if (ActivityCompat.checkSelfPermission(
                    this@HomeActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ){
                getLocation()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        fusedLocationProviderClient.lastLocation.addOnCompleteListener(object :
            OnCompleteListener<Location> {
            override fun onComplete(task: Task<Location>) {
                val location: Location? = task.result
                if (location != null) {
                    try {
                        val geocoder: Geocoder = Geocoder(this@HomeActivity, Locale.getDefault())
                        val address: List<Address> =
                            geocoder.getFromLocation(location.latitude, location.longitude, 1)
                    } catch (e: Exception) {
                    }
                }
            }
        })
    }

    fun initExplore_HaNoi() {
        listPlace_HaNoi = ArrayList()
        val database: FirebaseDatabase = Firebase.database
        val reference = database.getReference("Explore").child("HaNoi").child("Place")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (ds: DataSnapshot in snapshot.children) {
                    //   Log.d("valueSnap", ""+ds.key)
                    Log.d("valueSnap", "" + ds.child("Title").value)
                    listPlace_HaNoi.add(
                        PlaceExplore(
                            ds.child("Title").value.toString(),
                            ds.child("Image").value.toString(),
                            ds.child("District").value.toString()
                        )
                    )
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    fun bottom_navigation() {
        chip_navigation.setOnItemSelectedListener(object :
            ChipNavigationBar.OnItemSelectedListener {
            override fun onItemSelected(id: Int) {
                var fragment: Fragment? = null
                when (id) {
                    R.id.bottom_nav_home -> {
                        fragment = HomeFragment()
                    }
                    R.id.bottom_nav_search -> {
                        fragment = SearchFragment()
                    }
                    R.id.bottom_nav_bookmark -> {
                        fragment = BookmarkFragment()
                    }
                    R.id.bottom_nav_user -> {
                        fragment = UserFragment()
                    }
                }
                supportFragmentManager.beginTransaction().replace(R.id.layout_home, fragment!!)
                    .commit()
            }
        })
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

    fun noClick(view: View) {}
}