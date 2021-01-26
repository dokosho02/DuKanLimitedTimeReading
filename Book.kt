package com.example.webscrapkotlin

import org.jsoup.Jsoup
import kotlin.properties.Delegates

class Book(
    private val url: String
) {
    fun bookPage(): Array<Any> {
        var title by Delegates.notNull<String>()
        var score by Delegates.notNull<Float>()
        var reviewCount by Delegates.notNull<Int>()
        var datePublished by Delegates.notNull<String>()

        val document = Jsoup.connect(url).get()

        title = document.getElementsByTag("title")
            .toString().split("【").first()
            .drop(7)

        // Content
        val bookContentElement = document.getElementById("book-content")
        val bkCon = Jsoup.parse(bookContentElement.toString())
        val temp = bkCon.getElementsByTag("p")
        val bookContent = temp.toString()
            .replace("<br>","\n")
            .drop(3).dropLast(4)
            .replace("<p>", "\n")
            .replace("</p>", "\n")

        // score
        val allEm = document.getElementsByTag("em")
        for (item in allEm){
            if (item.toString().contains("score") ){
                score = item.text().toFloat()
            }
            else {
                score = 0F
            }
        }

        // date published
        val allTd = document.getElementsByTag("td")
        for (item in allTd){
            if (item.toString().contains("datePublished") ){
                datePublished = item.text()
            }
        }

        // reviewCount
        val allSpan = document.getElementsByTag("span")
        for (item in allSpan){
            if (item.toString().contains("reviewCount") ){
                reviewCount = item.text().filter { it.isDigit() }.toInt()
            }
            else {
                reviewCount = 0
            }
        }
        return arrayOf(title, datePublished, score, reviewCount, bookContent)
    }
}