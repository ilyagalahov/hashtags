package com.example.applemacbookproretina.learningandroid1

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

import android.content.Intent



class DisplayMessageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_message)

        // Get the Intent that started this activity and extract the string
        val intent = intent
        val message = intent.getStringExtra(EXTRA_MESSAGE)

        // Capture the layout's TextView and set the string as its text
        val textView = findViewById<TextView>(R.id.textView)
        textView.text = message
    }
}
