package com.example.kotlinmessenger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        findViewById<Button>(R.id.register_Button_register).setOnClickListener {
            performRegister()
        }

        findViewById<TextView>(R.id.already_have_account_textView_register).setOnClickListener {
            Log.d("MainActivity","Try to show activity")

            //launch the login acitivity
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }
    }
    private fun performRegister(){
        val email = findViewById<EditText>(R.id.email_editText_register).text.toString()
        val password = findViewById<EditText>(R.id.passwd_editText_register).text.toString()

        if(email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this,"Please enter text in email/pw",Toast.LENGTH_SHORT).show()
            return
        }

        Log.d("MainActivity","Email is " + email)
        Log.d("MainActivity","Password is $password")

        //Firebase authentication to create a username and password
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener

                //else if successful
                Log.d("Main","Successfully created user with uid: ${it.result?.user?.uid}")
            }
            .addOnFailureListener {
                Log.d("Main","Failed to create user: ${it.message}")

                Toast.makeText(this,"Failed to create user: ${it.message}",Toast.LENGTH_SHORT).show()
            }
    }
}