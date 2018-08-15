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
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.example.jamcom.connecting.Jemin.Activity.UserSelectActivity
import com.example.jamcom.connecting.Jemin.Adapter.HomeListAdapter
import com.example.jamcom.connecting.Jemin.Adapter.MyDibsAdapter
import com.example.jamcom.connecting.Jemin.Item.HomeListItem
import com.example.jamcom.connecting.Jemin.Item.MyDibsListItem
import com.example.jamcom.connecting.Network.Get.Response.GetRoomDetailRespnose
import com.example.jamcom.connecting.Network.Get.Response.GetUserImageUrlResponse
import com.example.jamcom.connecting.Network.NetworkService
import com.example.jamcom.connecting.Old.retrofit.ApiClient

import com.example.jamcom.connecting.R
import kotlinx.android.synthetic.main.activity_create.*
import kotlinx.android.synthetic.main.activity_room_inform.*
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

    override fun onClick(v: View?) {
        val idx : Int = mypage_list_recyclerview.getChildAdapterPosition(v)
        Log.v("TAG","마이페이지 감지 포지션 = " + idx)

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_mypage, container, false)

        mydibsListItem = ArrayList()
        mydibsListItem.add(MyDibsListItem(R.drawable.bg_sample))
        mydibsListItem.add(MyDibsListItem(R.drawable.bg_sample))
        mydibsListItem.add(MyDibsListItem(R.drawable.bg_sample))
        mydibsListItem.add(MyDibsListItem(R.drawable.bg_sample))
        mydibsListItem.add(MyDibsListItem(R.drawable.bg_sample))
        mydibsListItem.add(MyDibsListItem(R.drawable.bg_sample))



        myDibsAdapter = MyDibsAdapter(mydibsListItem)
        myDibsAdapter.setOnItemClickListener(this)
        v.mypage_list_recyclerview.layoutManager = LinearLayoutManager(v.context, LinearLayoutManager.HORIZONTAL, false)
        v.mypage_list_recyclerview.adapter = myDibsAdapter

        requestManager = Glide.with(this)

        getUserImageUrl()

        v.mypage_back_user_select_tv.setOnClickListener {
            var intent = Intent(activity, UserSelectActivity::class.java)
            startActivity(intent)
        }

        v.mypage_profile_img.setOnClickListener {
            changeImage()
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

                    ///RequestBody photoBody = RequestBody.create(MediaType.parse("image/jpg"), baos.toByteArray());
                    // MultipartBody.Part 실제 파일의 이름을 보내기 위해 사용!!


                    image = MultipartBody.Part.createFormData("image", img.name, photoBody)
                    Log.v("TAG", "이미지 값 = "+image)

                    //body = MultipartBody.Part.createFormData("image", photo.getName(), profile_pic);

                    Glide.with(this)
                            .load(data.data)
                            .centerCrop()
                            .into(mypage_profile_img)

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
                        Log.v("TAG","유저 이미지 값 갖고오기 성공")
                        response.body()!!.result[0].userImageUrl
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
}