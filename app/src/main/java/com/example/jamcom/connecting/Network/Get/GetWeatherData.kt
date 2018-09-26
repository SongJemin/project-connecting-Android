package com.example.jamcom.connecting.Network.Get

data class GetWeatherData (
        var timeRelease : String?,
        var fcst3hour : GetShortWeatherData

)