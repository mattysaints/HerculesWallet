package com.example.herculeswallet.repository

import androidx.lifecycle.MutableLiveData
import com.example.herculeswallet.model.Crypto
import com.example.herculeswallet.model.User
import org.json.JSONArray
import org.json.JSONTokener
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.Charset

class CryptoRepository {

    private var crypto_list: MutableLiveData<List<Crypto>> = MutableLiveData<List<Crypto>>()

    fun getCryptoListRequest(
    ){
        var list : List<Crypto> = listOf<Crypto>()
        val url = URL("https://api.nomics.com/v1/currencies/ticker?key=e2f7b934dbc5c455fb90e291bba7272333a28f6d")
        (url.openConnection() as? HttpURLConnection)?.run {
            requestMethod = "GET"
            setRequestProperty("Content-Type", "application/json; utf-8")
            setRequestProperty("Accept", "application/json")
            doInput = true
            list = fromJsonToList(inputStreamToJson(this.inputStream))
        }
        crypto_list.postValue(list)
    }

    private fun fromJsonToList(jsonString: String): List<Crypto> {
        val jsonArray = JSONTokener(jsonString).nextValue() as JSONArray
        var crypto : MutableList<Crypto> = mutableListOf()
        for (i in 0 until jsonArray.length()) {
            //Create crypto and add to list
            val name : String = jsonArray.getJSONObject(i).getString("name")
            val asset_id : String = jsonArray.getJSONObject(i).getString("id")
            val price : Double = jsonArray.getJSONObject(i).getString("price").toDouble() //non tutte le crypto hanno questo valore
            val url : String = jsonArray.getJSONObject(i).getString("logo_url")
            val asset : Crypto = Crypto(name,asset_id,price,url,null)
            crypto.add(asset)
        }
        return crypto
    }

    private fun inputStreamToJson(inputStream: InputStream): String{
        var ris : StringBuilder = StringBuilder()
        val isr = InputStreamReader(inputStream, Charset.forName("UTF-8"))
        val reader = BufferedReader(isr)
        var line = reader.readLine()

        while (line != null){
            ris.append(line).append("\n")
            line = reader.readLine()
        }
        return ris.toString()
    }

    fun getCryptoList(): MutableLiveData<List<Crypto>> {
        return crypto_list
    }


}