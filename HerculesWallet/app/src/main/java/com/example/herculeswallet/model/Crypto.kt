package com.example.herculeswallet.model

data class Crypto(
    var name: String,
    val asset_id: String,
    var price_usd: Double?,
    var logo_url: String,
    var quantity_user: Double?
        ){

}