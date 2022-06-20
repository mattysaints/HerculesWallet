package com.example.herculeswallet.model

data class Crypto (
    val name : String,
    val asset_id : String,
    val price_usd : Long,
    val logo_url : String
        ){

    override fun toString(): String {
        return "Crypto(name='$name', asset_id='$asset_id', price_usd=$price_usd, logo_url='$logo_url')"
    }
}