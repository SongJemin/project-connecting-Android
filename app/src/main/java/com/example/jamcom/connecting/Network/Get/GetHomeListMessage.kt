package com.example.jamcom.connecting.Network.Get

data class GetHomeListMessage (
        var roomID : Int,
        var roomName : String?,
        var roomStartDate : String?,
        var roomEndDate : String?,
        var typeName : String?,
        var img_url : String?,
        var confirmedName : String?,
        var confirmedDate : String?,
        var roomStatus : Int?,
        var attendantArr : ArrayList<String>?
)
