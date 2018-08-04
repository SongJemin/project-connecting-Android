package com.example.jamcom.connecting.Jemin.Item

data class HomeListItem (
        var roomID : Int,
        var roomName : String,
        var roomStartDate : String,
        var roomEndDate : String,
        var typeName : String,
        var participMember1: Int,
        var participMember2: Int,
        var img_url : String?
)