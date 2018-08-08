package com.example.jamcom.connecting.Network.Get

data class GetRoadAddressMessage (

        var address_name : String?,
        var region_1dept_name : String?,
        var region_2depth_name : String?,
        var region_3depth_name : String?,
        var road_name : String?,
        var underground_yn : String?,
        var main_building_no : String?,
        var sub_building_no : String?,
        var building_name : String?,
        var zone_no : String?
)