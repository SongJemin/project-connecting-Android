package com.example.jamcom.connecting.Network.Get

data class GetFavoriteListMessage (
        var favoriteName : String?,
        var favoriteImgUrl : String?,
        var favoriteAddress : String?,
        var favoriteType : String?,
        var favoritePhone : String?,
        var favoriteHomepage : String?,
        var favoriteLat : String?,
        var favoriteLon : String?
)