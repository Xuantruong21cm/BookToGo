package com.example.booktogo.fragment

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.booktogo.Helper.AccountHelper
import com.example.booktogo.Helper.ExploreHelper
import com.example.booktogo.Helper.HotelHelper
import com.example.booktogo.Helper.TripHelper
import com.example.booktogo.R
import com.example.booktogo.activity.HomeActivity
import com.example.booktogo.adapter.DiscountAdapter
import com.example.booktogo.adapter.ExploreAdapter
import com.example.booktogo.model.Discount
import com.example.booktogo.model.PlaceExplore
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.database.*
import com.jaredrummler.materialspinner.MaterialSpinner
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.collections.ArrayList


class HomeFragment : Fragment() {
    var uriImage: Uri? = null
    lateinit var cityList: ArrayList<String>
    lateinit var districtList_HaNoi: ArrayList<String>
    lateinit var districtList_HCM: ArrayList<String>
    lateinit var districtList_DN: ArrayList<String>
    lateinit var haNoiCityAdapter: ArrayAdapter<String>
    lateinit var HCMCityAdapter: ArrayAdapter<String>
    lateinit var DNCityAdapter: ArrayAdapter<String>
    lateinit var cityAdapter: ArrayAdapter<String>
    lateinit var district: String
    lateinit var exploreCity: String
    lateinit var cityName: String

    var mylat: Double? = null
    var mylng: Double? = null
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var address: MutableList<Address>

    var year: Int? = null
    var month: Int? = null
    var day: Int? = null
    lateinit var calendar: Calendar
    lateinit var discountAdapter: DiscountAdapter
    lateinit var discountList : ArrayList<Discount>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cityList = ArrayList()
        discountList = ArrayList()
        cityList.add("Hà Nội")
        cityList.add("Đà Nẵng")
        cityList.add("Hồ Chí Minh")
        cityAdapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, cityList)
        district_HaNoi()
        district_HCM()
        district_DN()
        calendar = Calendar.getInstance()
        initLocation()
    }

    @SuppressLint("MissingPermission")
    private fun initLocation() {
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
        fusedLocationProviderClient.lastLocation.addOnCompleteListener(object :
            OnCompleteListener<Location> {
            override fun onComplete(task: Task<Location>) {
                val location = task.result
                if (location != null) {
                    val geocoder = Geocoder(context, Locale.getDefault())
                    address = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                    mylat = address[0].latitude
                    mylng = address[0].longitude
                    HotelHelper.instance.myLat = mylat
                    HotelHelper.instance.myLng = mylng
                }
            }
        })
    }

    fun district_HaNoi() {
        districtList_HaNoi = ArrayList()
        districtList_HaNoi.add("Hoàn Kiếm")
        districtList_HaNoi.add("Đống Đa")
        districtList_HaNoi.add("Ba Đình")
        districtList_HaNoi.add("Hai Bà Trưng")
        districtList_HaNoi.add("Hoàng Mai")
        districtList_HaNoi.add("Thanh Xuân")
        districtList_HaNoi.add("Long Biên")
        districtList_HaNoi.add("Nam Từ Liêm")
        districtList_HaNoi.add("Bắc Từ Liêm")
        districtList_HaNoi.add("Tây Hồ")
        districtList_HaNoi.add("Cầu Giấy")
        districtList_HaNoi.add("Hà Đông")
        districtList_HaNoi.add("Sơn Tây")
        districtList_HaNoi.add("Ba Vì")
        districtList_HaNoi.add("Chương Mỹ")
        districtList_HaNoi.add("Phúc Thọ")
        districtList_HaNoi.add("Đan Phượng")
        districtList_HaNoi.add("Cầu Giấy")
        districtList_HaNoi.add("Đông Anh")
        districtList_HaNoi.add("Gia Lâm")
        districtList_HaNoi.add("Hoài Đức")
        districtList_HaNoi.add("Mê Linh")
        districtList_HaNoi.add("Phú Xuyên")
        districtList_HaNoi.add("Quốc Oai")
        districtList_HaNoi.add("Sóc Sơn")
        districtList_HaNoi.add("Thạch Thất")
        districtList_HaNoi.add("Thanh Oai")
        districtList_HaNoi.add("Thường Tín")
        districtList_HaNoi.add("Ứng Hòa")
        districtList_HaNoi.add("Thanh Trì")
        haNoiCityAdapter = ArrayAdapter(
            context!!,
            android.R.layout.simple_spinner_item,
            districtList_HaNoi
        )
    }

    fun district_HCM() {
        districtList_HCM = ArrayList()
        districtList_HCM.add("Quận 1")
        districtList_HCM.add("Quận 2")
        districtList_HCM.add("Quận 3")
        districtList_HCM.add("Quận 4")
        districtList_HCM.add("Quận 5")
        districtList_HCM.add("Quận 6")
        districtList_HCM.add("Quận 7")
        districtList_HCM.add("Quận 8")
        districtList_HCM.add("Quận 9")
        districtList_HCM.add("Quận 10")
        districtList_HCM.add("Quận 11")
        districtList_HCM.add("Quận 12")
        districtList_HCM.add("Quận Bình Thạnh")
        districtList_HCM.add("TP.Thủ Đức")
        districtList_HCM.add("Quận Gò Vấp")
        districtList_HCM.add("Quận Phú Nhuận")
        districtList_HCM.add("Quận Tân Bình")
        districtList_HCM.add("Quận Tân Phú")
        districtList_HCM.add("Quận Bình Tân")
        districtList_HCM.add("Huyện Nhà Bè")
        districtList_HCM.add("Huyện Hóc Môn")
        districtList_HCM.add("Huyện Bình Chánh")
        districtList_HCM.add("Huyện Củ Chi")
        districtList_HCM.add("Huyện Cần Giờ")
        HCMCityAdapter = ArrayAdapter(
            context!!,
            android.R.layout.simple_spinner_item,
            districtList_HCM
        )
    }

    fun district_DN() {
        districtList_DN = ArrayList()
        districtList_DN.add("Quận Hải Châu")
        districtList_DN.add("Quận Cẩm Lệ")
        districtList_DN.add("Quận Thanh Khê")
        districtList_DN.add("Quận Liên Chiểu")
        districtList_DN.add("Quận Ngũ Hành Sơn")
        districtList_DN.add("Quận Sơn Trà")
        districtList_DN.add("Huyện Hòa Vang")
        districtList_DN.add("uyện Hoàng Sa")
        DNCityAdapter = ArrayAdapter(
            context!!,
            android.R.layout.simple_spinner_item,
            districtList_DN
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_home, container, false)
        initDataView(view)
        initListener(view)
        initDiscount(view)
        initExplore(view, HomeActivity.listPlace_HaNoi)
        return view
    }

    private fun setDiscountData(view: View){
        discountAdapter = DiscountAdapter(discountList)
        view.recyclerView_discount.adapter = discountAdapter
    }

    private fun initDiscount(view: View) {
        val reference : DatabaseReference = FirebaseDatabase.getInstance().getReference("Discount")
        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (ds: DataSnapshot in snapshot.children) {
                        //Log.d("Discount", "onDataChange: "+ds.key)
                        val code = ds.child("code").value.toString()
                        val percent = ds.child("percent").value.toString()
                        val title = ds.child("title").value.toString()
                        discountList.add(Discount(code, percent, title))
                        setDiscountData(view)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    private fun initExplore(view: View, list: ArrayList<PlaceExplore>) {
        val exploreAdapter = ExploreAdapter(list, requireContext())
        view.recyclerView_district.setHasFixedSize(true)
        view.recyclerView_district.adapter = exploreAdapter
        exploreAdapter.setExploreClickListener(object : ExploreAdapter.ExplorerListener {
            override fun onClick(placeExplore: PlaceExplore) {
                //Toast.makeText(requireContext(),,Toast.LENGTH_SHORT).show()
                ExploreHelper.instance.exploreCity = exploreCity
                ExploreHelper.instance.district = placeExplore.district
                ExploreHelper.instance.imageDistrict = placeExplore.image
                ExploreHelper.instance.title = placeExplore.title
                val transition: FragmentTransaction =
                    activity!!.supportFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_left, 0, 0, R.anim.slide_out_left)
                transition.replace(R.id.layout_home_fragment, ExploreFragment()).commit()
                transition.addToBackStack(ExploreFragment::class.java.simpleName)
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun calculate(startDay: String, endDay: String): Long {
        val dateformater: DateTimeFormatter = DateTimeFormatter.ofPattern("d/M/u")
        val startDateValue = LocalDate.parse(startDay, dateformater)
        val endDateValue = LocalDate.parse(endDay, dateformater)
        val days: Long = ChronoUnit.DAYS.between(startDateValue, endDateValue)
        return days
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    private fun initListener(view: View) {
        view.img_avatar.setOnClickListener {
            choosePicture()
        }

        view.img_search_trip.setOnClickListener {
            if (tv_start_day.text.equals("--/--/----") || tv_end_day.text.equals("--/--/----")) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.select_a_reservation),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                if (calculate(tv_start_day.text.toString(), tv_end_day.text.toString()) < 0) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.at_least_one_day),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    TripHelper.instance.city = cityName
                    TripHelper.instance.district = district
                    TripHelper.instance.adults = edt_adults.text.toString()
                    TripHelper.instance.children = edt_children.text.toString()
                    TripHelper.instance.cityHotel = exploreCity
                    TripHelper.instance.days =
                        calculate(
                            tv_start_day.text.toString(),
                            tv_end_day.text.toString()
                        ).toString()
                    TripHelper.instance.starDay = tv_start_day.text.toString()
                    TripHelper.instance.endDay = tv_end_day.text.toString()
                    val manager: FragmentManager = activity!!.supportFragmentManager
                    val transition: FragmentTransaction = manager.beginTransaction()
                    val fragment: Fragment = SearchFragment()
                    transition.replace(R.id.layout_search, fragment).commit()
                    activity!!.chip_navigation.setItemSelected(R.id.bottom_nav_search)
                    true
                }
            }
        }

        view.tv_start_day.setOnClickListener {
            year = calendar[Calendar.YEAR]
            month = calendar[Calendar.MONTH]
            day = calendar[Calendar.DAY_OF_MONTH]

            val datePickerDialog: DatePickerDialog = DatePickerDialog(
                requireContext(),
                DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    requireView().tv_start_day.text =
                        "" + dayOfMonth + "/" + (month + 1) + "/" + year
                },
                year!!,
                month!!,
                day!!
            )
            datePickerDialog.show()
        }

        view.tv_end_day.setOnClickListener {
            year = calendar[Calendar.YEAR]
            month = calendar[Calendar.MONTH]
            day = calendar[Calendar.DAY_OF_MONTH]

            val datePickerDialog: DatePickerDialog = DatePickerDialog(
                requireContext(),
                DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    requireView().tv_end_day.text = "" + dayOfMonth + "/" + (month + 1) + "/" + year
                },
                year!!,
                month!!,
                day!!
            )
            datePickerDialog.show()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initDataView(view: View) {
        cityName = "Hà Nội"
        district = districtList_HaNoi[0]
        exploreCity = "HotelHaNoi"
        activity!!.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        view.tv_hi_name.text =
            "Hi ! " + AccountHelper.instance.firstname + " " + AccountHelper.instance.lastname
        if (AccountHelper.instance.avatar!!.length <= 1) {
            if (AccountHelper.instance.gender.equals("Male")) {
                view.img_avatar.setImageResource(R.drawable.male)
            } else {
                view.img_avatar.setImageResource(R.drawable.female)
            }
        } else {
            GlobalScope.launch(Dispatchers.IO) {
                val decodedString = Base64.decode(AccountHelper.instance.avatar, Base64.DEFAULT)
                val decodedByte: Bitmap =
                    BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
                withContext(Dispatchers.Main) {
                    view.img_avatar.setImageBitmap(decodedByte)
                }
            }
        }

        view.spinner_city.setAdapter(cityAdapter)
        view.spinner_district.setAdapter(haNoiCityAdapter)
        view.spinner_city.setOnItemSelectedListener(object :
            MaterialSpinner.OnItemSelectedListener<String> {
            override fun onItemSelected(
                view: MaterialSpinner?,
                position: Int,
                id: Long,
                item: String?
            ) {
                if (item.equals("Hà Nội")) {
                    requireView().spinner_district.setAdapter(haNoiCityAdapter)
                    district = districtList_HaNoi[0]
                    exploreCity = "HotelHaNoi"
                    cityName = item.toString()
                    initExplore(requireView(), HomeActivity.listPlace_HaNoi)
                } else if (item.equals("Đà Nẵng")) {
                    requireView().spinner_district.setAdapter(DNCityAdapter)
                    district = districtList_DN[0]
                    exploreCity = "HotelDaNang"
                    cityName = item.toString()
                    initExplore(requireView(), HomeActivity.listPlace_DN)
                } else if (item.equals("Hồ Chí Minh")) {
                    requireView().spinner_district.setAdapter(HCMCityAdapter)
                    district = districtList_HCM[0]
                    exploreCity = "HotelHCM"
                    cityName = item.toString()
                    initExplore(requireView(), HomeActivity.listPlace_HCM)
                }
            }
        })

        view.spinner_district.setOnItemSelectedListener(object :
            MaterialSpinner.OnItemSelectedListener<String> {
            override fun onItemSelected(
                view: MaterialSpinner?,
                position: Int,
                id: Long,
                item: String?
            ) {
                district = item!!
            }
        })
    }

    private fun choosePicture() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.data != null) {
            uriImage = data.data
            if (uriImage != null) {
                requireView().img_avatar.setImageURI(uriImage)
                if (Build.VERSION.SDK_INT < 28) {
                    GlobalScope.launch(Dispatchers.IO) {
                        val bitmap: Bitmap =
                            MediaStore.Images.Media.getBitmap(context!!.contentResolver, uriImage)
                        val outputStream: ByteArrayOutputStream = ByteArrayOutputStream()
                        bitmap.compress(Bitmap.CompressFormat.PNG, 75, outputStream)
                        val byteArray = outputStream.toByteArray()
                        val encodedString: String = Base64.encodeToString(byteArray, Base64.DEFAULT)
                        FirebaseDatabase.getInstance().getReference("Users")
                            .child(AccountHelper.instance.phone!!).child("avatar")
                            .setValue(encodedString)
                    }
                } else {
                    GlobalScope.launch(Dispatchers.IO) {
                        val source =
                            ImageDecoder.createSource(context!!.contentResolver, uriImage!!)
                        val bitmap: Bitmap = ImageDecoder.decodeBitmap(source)
                        val outputStream: ByteArrayOutputStream = ByteArrayOutputStream()
                        bitmap.compress(Bitmap.CompressFormat.PNG, 75, outputStream)
                        val byteArray = outputStream.toByteArray()
                        val encodedString: String = Base64.encodeToString(byteArray, Base64.DEFAULT)
                        FirebaseDatabase.getInstance().getReference("Users")
                            .child(AccountHelper.instance.phone!!).child("avatar")
                            .setValue(encodedString)
                    }
                }
            }
        }
    }
}