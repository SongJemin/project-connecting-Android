package com.example.jamcom.connecting.Network.Get.Response

import com.example.jamcom.connecting.Network.Get.GetFavoriteListMessage
import com.example.jamcom.connecting.Network.Get.GetHeartPlaceListMessage

data class GetHeartPlaceListResponse (
        var result : ArrayList<GetHeartPlaceListMessage>
)