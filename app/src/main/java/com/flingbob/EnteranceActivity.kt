package com.flingbob

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button

class EnteranceActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enterance)

        findViewById<Button>(R.id.button1).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
        findViewById<Button>(R.id.button2).setOnClickListener {
            startActivity(Intent(this, MappingBobActivity::class.java))
        }
        findViewById<Button>(R.id.button3).setOnClickListener {
            startActivity(Intent(this, RenderBobGL20Activity::class.java))
        }
    }
}
