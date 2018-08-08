package com.example.jamcom.connecting.Network.Get

data class GetAddressMessage (

        var address_name : String?,
        var region_1dept_name : String?,
        var region_2depth_name : String?,
        var region_3depth_name : String?,
        var mountain_yn : String?,
        var main_address_no : String?,
        var sub_address_no : String?,
        var zip_code : String?
)