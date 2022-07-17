package com.example.herculeswallet.model

data class Crypto(
    var name: String,
    val asset_id: String,
    var price_usd: Double?,
    var logo_url: String,
    var quantity_user: Double?
        ){

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Crypto

        if (name != other.name) return false
        if (asset_id != other.asset_id) return false
        if (price_usd != other.price_usd) return false
        if (logo_url != other.logo_url) return false
        if (quantity_user != other.quantity_user) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + asset_id.hashCode()
        result = 31 * result + (price_usd?.hashCode() ?: 0)
        result = 31 * result + logo_url.hashCode()
        result = 31 * result + (quantity_user?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "Crypto(name='$name', asset_id='$asset_id', price_usd=$price_usd, logo_url='$logo_url', quantity_user=$quantity_user)"
    }


}