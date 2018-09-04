package com.example.jamcom.connecting.Network.Get

data class GetLocationMessage (
    var userID : Int,
    var promiseLat : Double?,
    var promiseLon : Double?
)