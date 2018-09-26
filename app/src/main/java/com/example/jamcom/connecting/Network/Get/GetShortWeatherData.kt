package com.example.jamcom.connecting.Network.Get

data class GetShortWeatherData (
        var sky : GetWeatherSkyData,
        var temperature : GetWeatherTemperatureData
)