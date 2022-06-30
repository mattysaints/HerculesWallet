package com.example.herculeswallet.model

data class Crypto(
    val name: String,
    val asset_id: String,
    val price_usd: Double?,
    val logo_url: String,
    var quantity_user: Double?
        ){

     fun getquantity_user(): Double? {
        return quantity_user
    }

}