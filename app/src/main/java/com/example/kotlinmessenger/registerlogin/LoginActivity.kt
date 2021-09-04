package com.example.kotlinmessenger.registerlogin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlinmessenger.R
import com.example.kotlinmessenger.messages.LastestMessageActivity
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
                .addOnCompleteListener {
                    if(!it.isSuccessful) return@addOnCompleteListener
                    Log.d("Main","Successful login! with email: $email")
                    val intent = Intent(this,LastestMessageActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
                .addOnFailureListener {
                    Log.d("Main","Failure to login")
                    Toast.makeText(this,"Failed to login email: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        }

        findViewById<TextView>(R.id.back_to_register_button_login).setOnClickListener {
            finish()
        }
    }
}