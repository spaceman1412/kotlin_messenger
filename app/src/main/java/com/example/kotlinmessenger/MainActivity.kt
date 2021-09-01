package com.example.kotlinmessenger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        findViewById<Button>(R.id.register_Button_register).setOnClickListener {
            val email = findViewById<EditText>(R.id.email_editText_register).text.toString()
            val password = findViewById<EditText>(R.id.passwd_editText_register).text.toString()

            Log.d("MainActivity","Email is " + email)
            Log.d("MainActivity","Password is $password")

        }

        findViewById<TextView>(R.id.already_have_account_textView_register).setOnClickListener {
            Log.d("MainActivity","Try to show activity")

            //launch the login acitivity
            val intent = Intent(this,LoginActivity::class.java)

            startActivity(intent)

        }
    }
}