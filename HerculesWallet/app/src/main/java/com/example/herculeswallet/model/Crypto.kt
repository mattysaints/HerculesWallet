package com.example.herculeswallet.model

import java.net.Inet4Address

data class Crypto(
    val name: String,
    val asset_id: String,
    val price_usd: Double?,
    val logo_url: String,
    var quantity_user: Double?
        ){

}