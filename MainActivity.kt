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

    val tokenv2 = "8cee58eb967055a399f14ccdd532b576504877c9968690fb6c53caf932fbca74abf4bbc8f24c48594e6abc1dbd330aded1ef659687136057bd292d581c3733ff5eed33d8566886630d383f6a8e8f"
    val notionPage = "https://www.notion.so/MiReader-4b0aca1e3798486fb87b63d6cd6058e3"

//    val wiki = "https://en.wikipedia.org"
//    var websiteLink = "$wiki/wiki/List_of_films_with_a_100%25_rating_on_Rotten_Tomatoes"

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


//            val notion = Notion.fromToken(
//                    token = tokenv2
//            )
        }
    }
}

//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//
//class MainActivity : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//    }
//}