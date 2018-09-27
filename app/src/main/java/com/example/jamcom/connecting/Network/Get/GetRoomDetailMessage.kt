package com.example.jamcom.connecting.Network.Get

data class GetRoomDetailMessage(

        var roomName : String,
        var roomStartDate : String,
        var roomEndDate : String,
        var typeName : String,
        var roomCreaterID : Int,
        var img_url : String?,
        var roomStatus : Int,
        var confirmedName : String?,
        var confirmedLat : Double?,
        var confirmedLon : Double?,
        var confirmedDate : String?,
        var confirmedTime : String?
)