package com.vertigo.simpletranslatorretrofitmoshi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private var inputText: EditText? = null
    private var btn: Button? = null
    private var translateText: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        inputText = findViewById(R.id.inputText)
        btn = findViewById(R.id.btn)
        translateText = findViewById(R.id.translateText)
        var text = ""
        var count = 0

        btn?.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                text = TranslateApiImpl.translateRuText(inputText?.text.toString())[0].text
                while (text.length < 1) {
                    count++
                    if (count > 250) break
                }
                if (text.length > 1) {
                    CoroutineScope(Dispatchers.Main).launch { translateText?.text = text }
                }
            }
        }
    }
}