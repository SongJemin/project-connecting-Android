package com.example.jamcom.connecting.Network.Get

data class GetChangeLocationMessage (

        var road_address : GetRoadAddressMessage?,
        var address : GetAddressMessage?

)