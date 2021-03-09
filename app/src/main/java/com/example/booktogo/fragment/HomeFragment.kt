package com.example.booktogo.fragment

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.booktogo.Helper.AccountHelper
import com.example.booktogo.R
import com.example.booktogo.activity.HomeActivity
import com.example.booktogo.adapter.ExploreAdapter
import com.example.booktogo.model.PlaceExplore
import com.google.firebase.database.FirebaseDatabase
import com.jaredrummler.materialspinner.MaterialSpinner
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
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

    var year: Int? = null
    var month: Int? = null
    var day: Int? = null
    lateinit var calendar: Calendar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cityList = ArrayList()
        cityList.add("Hà Nội")
        cityList.add("Đà Nẵng")
        cityList.add("Hồ Chí Minh")
        cityAdapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, cityList)
        district_HaNoi()
        district_HCM()
        district_DN()
        calendar = Calendar.getInstance()
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

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_home, container, false)
        initDataView(view)
        initListener(view)
        initExplore(view)
        return view
    }

    private fun initExplore(view: View) {
        val exploreAdapter : ExploreAdapter = ExploreAdapter(HomeActivity.listPlace_HaNoi,requireContext())
        view.recyclerView_district.setHasFixedSize(true)
        view.recyclerView_district.adapter = exploreAdapter
        exploreAdapter.setExploreClickListener(object : ExploreAdapter.ExplorerListener{
            override fun onClick(placeExplore: PlaceExplore) {
                Toast.makeText(requireContext(),placeExplore.title,Toast.LENGTH_SHORT).show()
            }

        })

    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.N)
    private fun initListener(view: View) {
        view.img_avatar.setOnClickListener {
            choosePicture()
        }

        view.img_search_trip.setOnClickListener {
            Toast.makeText(requireContext(), district, Toast.LENGTH_SHORT).show()
        }

        view.tv_start_day.setOnClickListener {
            year = calendar[Calendar.YEAR]
            month = calendar[Calendar.MONTH]
            day = calendar[Calendar.DAY_OF_MONTH]

            val datePickerDialog: DatePickerDialog = DatePickerDialog(
                requireContext(),
                DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                        requireView().tv_start_day.text = ""+dayOfMonth+"/"+(month+1)+"/"+year
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
                    requireView().tv_end_day.text = ""+dayOfMonth+"/"+(month+1)+"/"+year
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
        district = districtList_HaNoi[0]
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
                } else if (item.equals("Đà Nẵng")) {
                    requireView().spinner_district.setAdapter(DNCityAdapter)
                    district = districtList_DN[0]
                } else if (item.equals("Hồ Chí Minh")) {
                    requireView().spinner_district.setAdapter(HCMCityAdapter)
                    district = districtList_HCM[0]
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
        val intent: Intent = Intent()
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