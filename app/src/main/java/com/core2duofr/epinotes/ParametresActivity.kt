package com.core2duofr.epinotes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL

class ParametresActivity : AppCompatActivity() {
    lateinit var url : String
    private  val verification_code = "djhfbezqilbfiuyezbf15q16qreqerg54bj654kuyl654iuys65v1q6fv5atr651grtb65ytrdgn1dsf6h5dhj4ds6b4dn4bds1s681";
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parametres)

    }

    fun request(mail_address: String?, request: String): String {
        try {
            url = "https://epinotes.core2duo.fr/connect_android.php?mail=" + mail_address + "&code_verification=" + verification_code + "&requete=" + request
            val memeURL = URL(url)
            val bufferedReader = BufferedReader(InputStreamReader(memeURL.openConnection().getInputStream()))
            var lines: String?
            val response = StringBuffer()
            while (bufferedReader.readLine().also { lines = it } != null) {
                response.append(lines)
            }
            return response.toString()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }
}