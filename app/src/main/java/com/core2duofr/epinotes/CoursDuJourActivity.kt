package com.core2duofr.epinotes

import android.app.DownloadManager
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.StrictMode
import android.preference.PreferenceManager
import android.webkit.CookieManager
import android.webkit.URLUtil
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL


class CoursDuJourActivity : AppCompatActivity() {
    val webView: WebView? = null
    lateinit var url : String
    private  val verification_code = "djhfbezqilbfiuyezbf15q16qreqerg54bj654kuyl654iuys65v1q6fv5atr651grtb65ytrdgn1dsf6h5dhj4ds6b4dn4bds1s681";
    var url_activity = "https://login.microsoftonline.com/3534b3d7-316c-4bc9-9ede-605c860f49d2/oauth2/authorize?client_id=91cf8aca-0f01-4ae1-9f6b-3d234f55adae&response_type=code&redirect_uri=https://epita-share.core2duo.fr/connect.php"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cours_du_jour)

// Chargement du mail TEST
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        val mail = preferences.getString("user_mail", "not_connected")
// Requêtes au serveur
        val policy = StrictMode.ThreadPolicy.Builder().permitNetwork().build()
        StrictMode.setThreadPolicy(policy)
        var promo = request(mail, "promo")
        var id_student = request(mail, "id")

        if (promo == "2024")
        {
            url_activity = "https://epita-share.core2duo.fr/connect_spe.php?code_verification=" + verification_code + "&promo=" + promo.toString() + "&id=" + id_student.toString()

        }
        if (promo == "2025")
        {
            url_activity = "https://epita-share.core2duo.fr/connect_sup.php?code_verification=" + verification_code + "&promo=" + promo.toString() + "&id=" + id_student.toString()
        }


        val webView = findViewById<WebView>(R.id.webView)
        webView.webViewClient = WebViewClient()
        webView.settings.javaScriptEnabled = true


        webView.loadUrl(url_activity)

        webView.setDownloadListener(
                {
            url, userAgent, contentDisposition, mimeType, contentLength ->
            val request = DownloadManager.Request(Uri.parse(url))
            request.setMimeType(mimeType)
            request.addRequestHeader("cookie", CookieManager.getInstance().getCookie(url))
            request.addRequestHeader("User-Agent", userAgent)
            // Envoyer l'url entière du fichier, pour pouvoir le charger directement depuis PdfViewerActivity
            val filename = URLUtil.guessFileName(url, contentDisposition, mimeType)
            val intent = Intent(this, PdfViewerActivity::class.java)
            intent.putExtra("file_name_url", url)
            startActivity(intent)
                })

    }

    override fun onBackPressed() {

            super.onBackPressed()
    }


    fun request(mail_address: String?, request: String): String {
        try {
            url = "https://epinotes.core2duo.fr/connect_android.php?mail=" + mail_address + "&code_verification=" + verification_code + "&requete=" + request
            val memeURL = URL(url)
            val bufferedReader = BufferedReader(
                    InputStreamReader(
                            memeURL.openConnection().getInputStream()
                    )
            )
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