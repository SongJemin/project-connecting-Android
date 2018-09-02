package com.example.jamcom.connecting.Jemin.Fragment

/**
 * Created by JAMCOM on 2018-03-27.
 */
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.example.jamcom.connecting.Jemin.Activity.PlaceDetailActivity
import com.example.jamcom.connecting.Jemin.Activity.SettingActivity
import com.example.jamcom.connecting.Jemin.Activity.UserSelectActivity
import com.example.jamcom.connecting.Jemin.Adapter.HomeListAdapter
import com.example.jamcom.connecting.Jemin.Adapter.MyDibsAdapter
import com.example.jamcom.connecting.Jemin.Item.HomeListItem
import com.example.jamcom.connecting.Jemin.Item.MyDibsListItem
import com.example.jamcom.connecting.Network.Get.GetFavoriteListMessage
import com.example.jamcom.connecting.Network.Get.GetHomeListMessage
import com.example.jamcom.connecting.Network.Get.Response.GetFavoriteListResponse
import com.example.jamcom.connecting.Network.Get.Response.GetHomeListResponse
import com.example.jamcom.connecting.Network.Get.Response.GetRoomDetailRespnose
import com.example.jamcom.connecting.Network.Get.Response.GetUserImageUrlResponse
import com.example.jamcom.connecting.Network.NetworkService
import com.example.jamcom.connecting.Network.Post.Response.PostRoomTestResponse
import com.example.jamcom.connecting.Network.Post.Response.UpdateProfileImgResponse
import com.example.jamcom.connecting.Old.retrofit.ApiClient

import com.example.jamcom.connecting.R
import kotlinx.android.synthetic.main.activity_create.*
import kotlinx.android.synthetic.main.activity_room_inform.*
import kotlinx.android.synthetic.main.fragment_alarm.view.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.fragment_mypage.*
import kotlinx.android.synthetic.main.fragment_mypage.view.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileNotFoundException
import java.io.InputStream


/**
 * A simple [Fragment] subclass.
 */
class MyPageFragment : Fragment(), View.OnClickListener {

    private val REQ_CODE_SELECT_IMAGE = 100
    lateinit var networkService : NetworkService
    lateinit var mydibsListItem: ArrayList<MyDibsListItem>
    lateinit var myDibsAdapter: MyDibsAdapter

    lateinit var data : Uri
    private var image : MultipartBody.Part? = null
    lateinit var requestManager: RequestManager

    lateinit var favoriteListData : ArrayList<GetFavoriteListMessage>

    var userName : String = ""

    override fun onClick(v: View?) {
        val idx : Int = mypage_list_recyclerview.getChildAdapterPosition(v)
        Log.v("TAG","마이페이지 감지 포지션 = " + idx)

        var intent = Intent(activity!!, PlaceDetailActivity::class.java)
        intent.putExtra("selectedPlaceName", favoriteListData[idx].favoriteName)
        intent.putExtra("selectedPlaceHomepageUrl", favoriteListData[idx].favoriteHomepage)
        intent.putExtra("selectedRoadAddress", favoriteListData[idx].favoriteAddress)
        intent.putExtra("selectedPhoneNum", favoriteListData[idx].favoritePhone)
        intent.putExtra("selectedX", favoriteListData[idx].favoriteLon)
        intent.putExtra("selectedY", favoriteListData[idx].favoriteLat)
        intent.putExtra("selectedPlaceImgUrl", favoriteListData[idx].favoriteImgUrl)
        intent.putExtra("typeName", favoriteListData[idx].favoriteType)

        startActivity(intent)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_mypage, container, false)

        Log.v("tag", "홈리스트 데이터 숫자 = " + HomeFragment.homeFragment.dataCount)

        v.mypage_promise_number_tv.setText(HomeFragment.homeFragment.dataCount.toString())

        requestManager = Glide.with(this)

        val pref = this.activity!!.getSharedPreferences("auto", Activity.MODE_PRIVATE)
        userName = pref.getString("userName","")
        v.mypage_username_tv.text = userName

        getUserImageUrl()

        getFavoriteList(v)


        v.mypage_profile_img.setOnClickListener {
            changeImage()
        }

        v.mypage_setting_btn.setOnClickListener {
            var intent = Intent(activity!!, SettingActivity::class.java)
            startActivity(intent)
        }

        return v
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQ_CODE_SELECT_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    //if(ApplicationController.getInstance().is)
                    this.data = data!!.data
                    Log.v("이미지", this.data.toString())

                    val options = BitmapFactory.Options()

                    var input: InputStream? = null // here, you need to get your context.
                    try {
                        input = context!!.contentResolver.openInputStream(this.data)
                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                    }

                    val bitmap = BitmapFactory.decodeStream(input, null, options) // InputStream 으로부터 Bitmap 을 만들어 준다.
                    val baos = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos)
                    val photoBody = RequestBody.create(MediaType.parse("image/jpg"), baos.toByteArray())
                    val img = File(getRealPathFromURI(context!!,this.data).toString()) // 가져온 파일의 이름을 알아내려고 사용합니다

                    Log.v("TAG","이미지 이름 = " + img)
                    Log.v("TAG","이미지 바디 = " + photoBody.toString())


                    image = MultipartBody.Part.createFormData("image", img.name, photoBody)
                    Log.v("TAG", "이미지 값 = "+image)

                    //body = MultipartBody.Part.createFormData("image", photo.getName(), profile_pic);

                    Glide.with(this)
                            .load(data.data)
                            .centerCrop()
                            .into(mypage_profile_img)

                    updateProfileImg()

                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }
    }

    fun getRealPathFromURI(context: Context, contentUri: Uri): String {
        var cursor: Cursor? = null
        try {
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            cursor = context.contentResolver.query(contentUri, proj, null, null, null)
            val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            return cursor.getString(column_index)
        } finally {
            cursor?.close()
        }
    }

    fun changeImage(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = android.provider.MediaStore.Images.Media.CONTENT_TYPE
        intent.data = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        startActivityForResult(intent, REQ_CODE_SELECT_IMAGE)

    }

    fun getUserImageUrl(){

        try {

            networkService = ApiClient.getRetrofit().create(NetworkService::class.java)

            val pref = activity!!.getSharedPreferences("auto", Activity.MODE_PRIVATE)
            var userID : Int = 0
            userID = pref.getInt("userID",0)

            var getUserImageUrlResponse = networkService.getUserImageUrl(userID) // 네트워크 서비스의 getContent 함수를 받아옴

            getUserImageUrlResponse.enqueue(object : Callback<GetUserImageUrlResponse> {
                override fun onResponse(call: Call<GetUserImageUrlResponse>?, response: Response<GetUserImageUrlResponse>?) {
                    Log.v("TAG","유저 이미지 GET 통신 성공")
                    if(response!!.isSuccessful)
                    {
                        Log.v("TAG","유저 이미지 값 갖고오기 성공" + response!!.body().toString())
                        response.body()!!.result[0].userImageUrl
                        Log.v("TAG", "유저 이미지 url = " + response.body()!!.result[0].userImageUrl)
                        requestManager.load(response.body()!!.result[0].userImageUrl).into(mypage_profile_img)

                    }
                }

                override fun onFailure(call: Call<GetUserImageUrlResponse>?, t: Throwable?) {
                    Log.v("TAG","유저 이미지 통신 실패")
                }
            })
        } catch (e: Exception) {
        }

    }

    private fun getFavoriteList(v : View) {

        try {

            networkService = ApiClient.getRetrofit().create(NetworkService::class.java)

            val pref = activity!!.getSharedPreferences("auto", Activity.MODE_PRIVATE)
            var userID : Int = 0
            userID = pref.getInt("userID",0)
            mydibsListItem = ArrayList()

            var getFavoriteListResponse = networkService.getFavoriteList(userID) // 네트워크 서비스의 getContent 함수를 받아옴

            getFavoriteListResponse.enqueue(object : Callback<GetFavoriteListResponse> {
                override fun onResponse(call: Call<GetFavoriteListResponse>?, response: Response<GetFavoriteListResponse>?) {
                    Log.v("TAG","찜리스트 GET 통신 성공")
                    if(response!!.isSuccessful)
                    {
                        Log.v("TAG","찜리스트 값 갖고오기 성공")

                        if(response.body()!!.result.size == 0)
                        {

                        }
                        else
                        {
                            favoriteListData = response.body()!!.result
                            var test : String = ""
                            test = favoriteListData.toString()
                            Log.v("TAG","찜리스트 데이터 값"+ test)

                            for(i in 0..favoriteListData.size-1) {

                                mydibsListItem.add(MyDibsListItem(favoriteListData[i].favoriteName, favoriteListData[i].favoriteImgUrl!!, favoriteListData[i].favoriteAddress))
                                //projectItems.add(ProjectItem("https://project-cowalker.s3.ap-northeast-2.amazonaws.com/1531113346984.jpg", "ㅁㄴㅇㅎ", "ㅁㄴㅇㄹㄴㅁㅇㅎ", "ㅁㄴㅇㄹ", "ㅇㅎㅁㄴㅇㄹ"))
                                myDibsAdapter = MyDibsAdapter(mydibsListItem, requestManager)


                            }

                            myDibsAdapter.setOnItemClickListener(this@MyPageFragment)
                            v.mypage_list_recyclerview.layoutManager = GridLayoutManager(v.context, 2)
                            v.mypage_list_recyclerview.adapter = myDibsAdapter

                        }

                    }
                }

                override fun onFailure(call: Call<GetFavoriteListResponse>?, t: Throwable?) {
                    Log.v("TAG","찜리스트 통신 실패")
                }
            })
        } catch (e: Exception) {
        }

    }

    fun updateProfileImg() {
        val pref = activity!!.getSharedPreferences("auto", Activity.MODE_PRIVATE)
        var userIDValue : Int = 0
        userIDValue = pref.getInt("userID",0)
        networkService = ApiClient.getRetrofit().create(com.example.jamcom.connecting.Network.NetworkService::class.java)

        val userID = RequestBody.create(MediaType.parse("text.plain"), userIDValue.toString())

        val updateProfileImgResponse = networkService.updateProfileImg(userID, image)

        Log.v("TAG","프로필 이미지 수정 유저 아이디 = " + userIDValue)
        Log.v("TAG","이미지 = " + image)

        updateProfileImgResponse.enqueue(object : retrofit2.Callback<UpdateProfileImgResponse>{

            override fun onResponse(call: Call<UpdateProfileImgResponse>, response: Response<UpdateProfileImgResponse>) {
                Log.v("TAG", "프로필 이미지 수정 통신 성공")
                if(response.isSuccessful){
                    var message = response!!.body()

                    Log.v("TAG", "프로필 이미지 수정 성공"+ message.toString())

                }
                else{

                    Log.v("TAG", "프로필 이미지 수정 값 전달 실패"+ response!!.body().toString())
                }
            }

            override fun onFailure(call: Call<UpdateProfileImgResponse>, t: Throwable?) {
                Toast.makeText(context!!,"프로필 이미지 수정 서버 연결 실패", Toast.LENGTH_SHORT).show()
            }

        })
    }

}