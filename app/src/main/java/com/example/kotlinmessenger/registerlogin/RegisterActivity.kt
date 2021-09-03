package com.example.kotlinmessenger.registerlogin

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.kotlinmessenger.R
import com.example.kotlinmessenger.models.User
import com.example.kotlinmessenger.messages.LastestMessageActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)



        findViewById<Button>(R.id.register_Button_register).setOnClickListener {
            performRegister()
        }

        findViewById<TextView>(R.id.already_have_account_textView_register).setOnClickListener {
            Log.d("RegisterActivity", "Try to show activity")

            //launch the login acitivity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.select_photo_button_register).setOnClickListener {
            Log.d("RegisterActivity", "Try to select photo")

            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }
    }

    var selectedUri: Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            //proceed check what image selected was
            Log.d("RegisterActivity", "Photo selected")

            selectedUri = data.data

            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedUri)

            val select_photo = findViewById<CircleImageView>(R.id.selectphoto_imageView_register)

            select_photo.setImageBitmap(bitmap)

            findViewById<Button>(R.id.select_photo_button_register).alpha = 0f



//            val bitmapDrawable = BitmapDrawable(bitmap)
//
//            findViewById<Button>(R.id.select_photo_button_register).setBackgroundDrawable(bitmapDrawable)
        }
    }

    private fun performRegister() {
        val email = findViewById<EditText>(R.id.email_editText_register).text.toString()
        val password = findViewById<EditText>(R.id.passwd_editText_register).text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter text in email/pw", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d("RegisterActivity", "Email is " + email)
        Log.d("RegisterActivity", "Password is $password")

        //Firebase authentication to create a username and password
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener

                //else if successful
                Log.d(
                    "RegisterActivity",
                    "Successfully created user with uid: ${it.result?.user?.uid}"
                )

                uploadImageToFirebaseStorage()
            }
            .addOnFailureListener {
                Log.d("RegisterActivity", "Failed to create user: ${it.message}")

                Toast.makeText(this, "Failed to create user: ${it.message}", Toast.LENGTH_SHORT)
                    .show()
            }
    }

    private fun uploadImageToFirebaseStorage() {
        if (selectedUri == null) return

        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

        ref.putFile(selectedUri!!)
            .addOnSuccessListener {
                Log.d("RegisterActivity", "Sucessfully uploaded image: ${it.metadata?.path}")

                ref.downloadUrl.addOnSuccessListener {
                    Log.d("RegisterActivity", "File Location: ${it}")
                    savedUserToFireDatabase(it.toString())
                }
            }

    }

    private fun savedUserToFireDatabase(profileImageUrl: String) {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        val user = User(
            uid,
            findViewById<EditText>(R.id.username_editText_register).text.toString(),
            profileImageUrl
        )

        ref.setValue(user)
            .addOnSuccessListener {
                Log.d("RegisterActivity", "Finally save the user to firebase database")

                val intent = Intent(this, LastestMessageActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
    }
}

