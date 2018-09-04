package com.example.jamcom.connecting.Jemin.Item

data class HomeListItem (
        var roomID : Int,
        var roomName : String,
        var roomStartDate : String,
        var roomEndDate : String,
        var typeName : String,
        var participMember1: String?,
        var participMember2: String?,
        var img_url : String?,
        var roomSelectedDate : String?,
        var roomSelectedLocation : String?,
        var roomStatus : Int?
)