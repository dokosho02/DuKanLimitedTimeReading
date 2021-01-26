package com.example.webscrapkotlin

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.jsoup.Jsoup
import java.io.IOException
import java.lang.reflect.Type


@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    private lateinit var textView: TextView
    lateinit var button: Button

    var limitedTimeSite = "https://www.duokan.com/special/18956"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        title = "Duokan Limited Time Reading -- Weekly"
        textView = findViewById(R.id.textView)
        button = findViewById(R.id.btnView)

        button.setOnClickListener {
            WebScratch().execute()
        }
    }

    inner class WebScratch : AsyncTask<Void, Void, Void>() {
        private var words: String = ""

        override fun doInBackground(vararg params: Void): Void? {
            try {
                val document =  Jsoup.connect(limitedTimeSite).get()    //
                var bookList = document.getElementById("book_list")
                Log.v("Check", bookList.toString())
                var temp = bookList.text()

                val listType: Type = object : TypeToken<List<BookList?>?>() {}.type
                var sarray:List<BookList> = Gson().fromJson(temp, listType)


                for ( i in sarray.indices ) {
                    Log.v("Check", sarray[i].toString() )
                    var bk = Book( sarray[i].getURL()  )
                    var (title, datePublished, score, reviewCount, bookContent) = bk.bookPage()

                    words = "$words\n${i+1} .  $title\n\t\t$datePublished\n\t\t$score by $reviewCount\n\n$bookContent\n********\n"

                }


            } catch (e: IOException) {
                e.printStackTrace()
            }
            return null
        }
        override fun onPostExecute(aVoid: Void?) {
            super.onPostExecute(aVoid)
            textView.text = words
