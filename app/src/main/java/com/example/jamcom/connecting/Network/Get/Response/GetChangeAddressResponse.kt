package com.example.jamcom.connecting.Network.Get.Response

import com.example.jamcom.connecting.Network.Get.GetAddressMessage

data class GetChangeAddressResponse (

        var meta : String,
        var documents : ArrayList<GetAddressMessage>
)