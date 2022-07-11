package com.example.herculeswallet.repository

import androidx.lifecycle.MutableLiveData
import com.example.herculeswallet.model.Crypto
import org.json.JSONArray
import org.json.JSONTokener
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.Charset


object CryptoRepository {

    private var crypto_list: MutableLiveData<List<Crypto>> = MutableLiveData<List<Crypto>>()
    private var crypto_list_icon : HashMap<String,String> = HashMap()


    fun getCryptoListRequest(){
        var list: List<Crypto> = listOf<Crypto>()
        try {
            val url = URL("https://rest.coinapi.io/v1/assets")
            (url.openConnection() as? HttpURLConnection)?.run {
                requestMethod = "GET"
                setRequestProperty("Content-Type", "application/json; utf-8")
                setRequestProperty("Accept", "application/json")
                setRequestProperty("X-CoinAPI-Key", "B74D735E-8062-41E8-BC88-9C16C6E0CD35")
                doInput = true
                list = fromJsonToList(inputStreamToJson(this.inputStream))
            }
        }catch (e: FileNotFoundException){
            val url = URL("https://rest.coinapi.io/v1/assets")
            (url.openConnection() as? HttpURLConnection)?.run {
                requestMethod = "GET"
                setRequestProperty("Content-Type", "application/json; utf-8")
                setRequestProperty("Accept", "application/json")
                setRequestProperty("X-CoinAPI-Key", "CE56637C-B851-497F-90C3-E2C6691222A8")
                doInput = true
                list = fromJsonToList(inputStreamToJson(this.inputStream))
            }

        }
        crypto_list.postValue(list)
    }



    private fun fromJsonToList(jsonString: String): List<Crypto> {
        val jsonArray = JSONTokener(jsonString).nextValue() as JSONArray
        var crypto : MutableList<Crypto> = mutableListOf()
        for (i in 0 until jsonArray.length()) {
            //Create crypto and add to list
            val name : String = jsonArray.getJSONObject(i).getString("name")
            val asset_id : String = jsonArray.getJSONObject(i).getString("asset_id")
            val price : Double
            if(jsonArray.getJSONObject(i).has("price_usd")){
               price = jsonArray.getJSONObject(i).getString("price_usd").toDouble()
            } else {price = "0".toDouble()}
            val url : String = crypto_list_icon.get(asset_id).toString()
            val asset : Crypto = Crypto(name,asset_id,price,url,0.0)
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

    fun getCryptoIcon() : HashMap<String,String>{
        return crypto_list_icon
    }

    fun getCryptoIconRequest(){
        try {
            val url = URL("https://rest.coinapi.io/v1/assets/icons/32")
            (url.openConnection() as? HttpURLConnection)?.run {
                requestMethod = "GET"
                setRequestProperty("Content-Type", "application/json; utf-8")
                setRequestProperty("Accept", "application/json")
                setRequestProperty("X-CoinAPI-Key", "B74D735E-8062-41E8-BC88-9C16C6E0CD35")
                doInput = true
                fromJsonToHashmap(inputStreamToJson(this.inputStream))
            }
        }catch (e: FileNotFoundException){
            val url = URL("https://rest.coinapi.io/v1/assets/icons/32")
            (url.openConnection() as? HttpURLConnection)?.run {
                requestMethod = "GET"
                setRequestProperty("Content-Type", "application/json; utf-8")
                setRequestProperty("Accept", "application/json")
                setRequestProperty("X-CoinAPI-Key", "CE56637C-B851-497F-90C3-E2C6691222A8")
                doInput = true
                fromJsonToHashmap(inputStreamToJson(this.inputStream))
            }
        }
    }

    private fun fromJsonToHashmap(jsonString: String){
        val jsonArray = JSONTokener(jsonString).nextValue() as JSONArray
        for (i in 0 until jsonArray.length()) {
            val asset_id : String = jsonArray.getJSONObject(i).getString("asset_id")
            val url : String = jsonArray.getJSONObject(i).getString("url").toString()
            crypto_list_icon.put(asset_id,url)
        }
    }


}