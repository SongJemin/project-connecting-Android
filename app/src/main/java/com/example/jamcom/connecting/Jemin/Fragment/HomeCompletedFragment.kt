package com.example.jamcom.connecting.Jemin.Fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.jamcom.connecting.R
import kotlinx.android.synthetic.main.fragment_homelist.*
import kotlinx.android.synthetic.main.fragment_homelist.view.*

class HomeCompletedFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_completed_home,container,false)

        return v
    }

}