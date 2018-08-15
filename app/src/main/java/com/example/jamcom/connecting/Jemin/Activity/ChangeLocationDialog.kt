package com.example.jamcom.connecting.Jemin.Activity

import android.app.Dialog
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.jamcom.connecting.Jemin.Fragment.RoomMyInformTab
import com.example.jamcom.connecting.R

class ChangeLocationDialog(internal var ChangeLocationActivity: AppCompatActivity) : Dialog(ChangeLocationActivity) {

    override fun onCreate(savedInstanceState: Bundle) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_change_location)

    }
}
