package com.example.jamcom.connecting.Network.Get.Response

import com.example.jamcom.connecting.Network.Get.GetChangeLocationMessage
import com.example.jamcom.connecting.Network.Get.GetMetaMessage

data class GetChangeLocationResponse (
        var meta : GetMetaMessage,
        var documents : ArrayList<GetChangeLocationMessage>
)