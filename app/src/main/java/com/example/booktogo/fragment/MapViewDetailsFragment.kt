package com.example.booktogo.fragment

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import com.example.booktogo.Helper.HotelHelper
import com.example.booktogo.R
import com.example.booktogo.router.IGoogleAPI
import com.example.booktogo.router.RetrofitClient
import com.example.booktogo.router.SelectPlaceEvent
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_map_view_details.view.*
import org.json.JSONObject
import java.util.*
import kotlin.Exception
import kotlin.collections.ArrayList

class MapViewDetailsFragment : Fragment(), OnMapReadyCallback {
    lateinit var map: GoogleMap
    var lat: Double? = null
    var lng: Double? = null
    var mylat: Double? = null
    var mylng: Double? = null
    var hotelName: String? = null
    var rangePrice: String? = null

    private val compositeDisposable = CompositeDisposable()
    private lateinit var iGoogleAPI: IGoogleAPI
    private var blackPolyline: Polyline? = null
    private var greyPolyline: Polyline? = null
    private var polylineOptions: PolylineOptions? = null
    private var blackPolylineOptions: PolylineOptions? = null
    private var polylineList: ArrayList<LatLng?>? = null
    private var selectedPlaceEvent: SelectPlaceEvent? = null
    var mapFragment: SupportMapFragment? = null

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_map_view_details, container, false)
        initData()

            mapFragment = childFragmentManager.findFragmentById(R.id.fragment_map_details) as SupportMapFragment
        mapFragment!!.getMapAsync(this)
        view.tv_hotelName_mapDetails.text = hotelName
        view.tv_priceRange_mapDetails.text = "$rangePrice VNĐ"
        initListener(view)
        return view
    }

    @SuppressLint("MissingPermission")
    private fun initListener(view: View) {
        view.img_myLocation.setOnClickListener {
            val latLng = LatLng(mylat!!, mylng!!)
           map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15f))
        }

        view.img_hotel_location.setOnClickListener {
            val latLng = LatLng(lat!!, lng!!)
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
        }

        view.img_direct.setOnClickListener {
            openDirect()
        }
        view.img_back_mapDetails.setOnClickListener {
            activity!!.supportFragmentManager.popBackStack()
        }
    }

    private fun openDirect(){
        try {
            val uri : String = "http://maps.google.com/maps?f=d&hl=en&saddr="+mylat+","+mylng+"&daddr="+lat+","+lng
            val intent : Intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
            intent.`package` = "com.google.android.apps.maps"
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }catch (e : Exception){}
    }

    @SuppressLint("MissingPermission")
    private fun initData() {
        lat = HotelHelper.instance.lat!!.toDouble()
        lng = HotelHelper.instance.lng!!.toDouble()
        hotelName = HotelHelper.instance.nameHotel
        rangePrice = HotelHelper.instance.priceRange
        mylat = HotelHelper.instance.myLat
        mylng = HotelHelper.instance.myLng
        iGoogleAPI = RetrofitClient.instance!!.create(IGoogleAPI::class.java)
        val origin : LatLng = LatLng(mylat!!, mylng!!)
        val destination : LatLng = LatLng(lat!!, lng!!)
        selectedPlaceEvent = SelectPlaceEvent(origin,destination)

    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(p0: GoogleMap?) {
        map = p0!!
        val latlng: LatLng = LatLng(lat!!, lng!!)
        map.addMarker(
            MarkerOptions().position(latlng)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_hotel))
        )

        val mylocation: LatLng = LatLng(mylat!!, mylng!!)
        map.addMarker(
            MarkerOptions().position(mylocation)
        )
        map.moveCamera(CameraUpdateFactory.newLatLng(latlng))
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 15F))
        onDraw(selectedPlaceEvent!!)
    }

    private fun onDraw(selectPlaceEvent: SelectPlaceEvent) {
        compositeDisposable.add(
            iGoogleAPI.getDirections(
                "driving",
                "less_driving",
                selectPlaceEvent.originString,
                selectPlaceEvent.destinationString,"AIzaSyA1hV1qrPXdZMHp_6WAa3kHPQKdXLjHBlE"
            )!!.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe {
                returnResult->
                try {
                    val jsonObject = JSONObject(returnResult)
                    val jsonArray = jsonObject.getJSONArray("routes")
                    for (i in 0 until jsonArray.length()){
                        val route =jsonArray.getJSONObject(i)
                        val poly = route.getJSONObject("overview_polyline")
                        val polyLine = poly.getString("points")
                        polylineList = decodePoly(polyLine)
                    }
                    polylineOptions = PolylineOptions()
                    polylineOptions!!.color(Color.GRAY)
                    polylineOptions!!.width(12f)
                    polylineOptions!!.startCap(SquareCap())
                    polylineOptions!!.jointType(JointType.ROUND)
                    polylineOptions!!.addAll(polylineList)
                    greyPolyline = map.addPolyline(polylineOptions)

                    blackPolylineOptions = PolylineOptions()
                    blackPolylineOptions!!.color(Color.BLACK)
                    blackPolylineOptions!!.width(5f)
                    blackPolylineOptions!!.startCap(SquareCap())
                    blackPolylineOptions!!.jointType(JointType.ROUND)
                    blackPolylineOptions!!.addAll(polylineList)
                    blackPolyline = map.addPolyline(blackPolylineOptions)

                    //Animator
                    val valueAnimator = ValueAnimator.ofInt(0,100)
                    valueAnimator.duration = 1100
                    valueAnimator.repeatCount = ValueAnimator.INFINITE
                    valueAnimator.interpolator = LinearInterpolator()
                    valueAnimator.addUpdateListener { value ->
                        val poins =greyPolyline!!.points
                        val percentvalue =value.animatedValue.toString().toInt()
                        val size = poins.size
                        val newpoint = (size * (percentvalue / 100.0f)).toInt()
                        val p =poins.subList(0,newpoint)
                        blackPolyline!!.points = (p)
                    }
                    valueAnimator.start()
                    val latLngBounds = LatLngBounds.Builder().include(selectPlaceEvent.origin)
                        .include(selectPlaceEvent.destination).build()

                    map.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds,160))
                    map.moveCamera(CameraUpdateFactory.zoomTo(map.cameraPosition.zoom - 1))

                }catch (e : Exception){}
            }
        )
    }

    fun decodePoly(encoded: String): ArrayList<LatLng?>? {
        val poly = ArrayList<LatLng?>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0
        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat
            shift = 0
            result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng
            val p = LatLng(
                lat.toDouble() / 1E5,
                lng.toDouble() / 1E5
            )
            poly.add(p)
        }
        return poly
    }

}