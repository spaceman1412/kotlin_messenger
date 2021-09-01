package com.example.kotlinmessenger

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.acitivity_login)

        findViewById<Button>(R.id.login_button_login).setOnClickListener {
            val email = findViewById<EditText>(R.id.email_editText_login).text.toString()
            val password = findViewById<EditText>(R.id.passwd_editText_login).text.toString()

            Log.d("Login","Attempt login with email/passwd: $email/***")

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)

        }

        findViewById<TextView>(R.id.back_to_register_button_login).setOnClickListener {
            finish()
        }
    }
}