package com.example.jamcom.connecting.Network.Get

data class GetRoomDetailMessage(

        var roomName : String,
        var roomStartDate : String,
        var roomEndDate : String,
        var typeName : String,
        var roomCreaterID : Int
)