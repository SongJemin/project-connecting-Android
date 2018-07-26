package com.example.jamcom.connecting.Jemin.Fragment

/**
 * Created by JAMCOM on 2018-03-27.
 */
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.jamcom.connecting.R


/**
 * A simple [Fragment] subclass.
 */
class AlarmFragment : Fragment() {

    internal lateinit var myToolbar: Toolbar

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_alarm, container, false)
        myToolbar = v.findViewById<View>(R.id.my_toolbar) as Toolbar

        return v
    }

}