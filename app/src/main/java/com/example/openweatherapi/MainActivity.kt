package com.example.openweatherapi

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


class MainActivity : AppCompatActivity() {

    var cityName : String = "Kurgan"
    private lateinit var textViewWeather : TextView
    private lateinit var button: Button
    var urlPage = "https://api.openweathermap.org/data/2.5/weather?q=$cityName&appid=2f48c568376720f272a2a56c3352195b&lang=ru&units=metric"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textViewWeather = findViewById(R.id.textViewWeather)
        button = findViewById(R.id.button)
        button.setOnClickListener { view: View? ->
            cityName = editTextCity.text.toString()
            urlPage = "https://api.openweathermap.org/data/2.5/weather?q=$cityName&appid=2f48c568376720f272a2a56c3352195b&lang=ru&units=metric"
        val task : DownloadTask = DownloadTask()
        try { val content: String = task.execute(urlPage).get()
            textViewWeather.text = content
        } catch (e: Exception){
        }
        }

    }


    private class DownloadTask : AsyncTask<String, Int, String>() {
        override fun doInBackground(vararg p0: String?): String? {
            val result = StringBuilder()
            var url: URL? = null
            var urlConnection: HttpURLConnection? = null
            try {
                url = URL(p0[0])
                urlConnection = url.openConnection() as HttpURLConnection
                val data = urlConnection.inputStream
                val reader = InputStreamReader(data)
                val bufferedReader = BufferedReader(reader)
                var line = bufferedReader.readLine()
                while (line != null) {
                    result.append(line)
                    line = bufferedReader.readLine()
                }
                var jsonObject = JSONObject(result.toString())
                val name = jsonObject.getString("name")
                val main = jsonObject.getJSONObject("main")
                val temp = main.getString("temp")
                val jsonArray = jsonObject.getJSONArray("weather")
                val weather = jsonArray.getJSONObject(0)
                val mainWeather = weather.getString("description")
                return "$name \nТемпература: $temp *С \nНа улице $mainWeather"
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect()
                }
            }
            return null
        }


    }
}