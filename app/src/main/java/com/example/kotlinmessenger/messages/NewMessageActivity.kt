package com.example.kotlinmessenger.messages

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinmessenger.R
import com.example.kotlinmessenger.models.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.*
import de.hdodenhof.circleimageview.CircleImageView

class NewMessageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)

        supportActionBar?.title = "Select User"
//        val adapter = GroupAdapter<GroupieViewHolder>()
//
//        adapter.add(UserItem())
//        adapter.add(UserItem())
//        adapter.add(UserItem())
//
//        recyclerView_new_message.adapter = adapter
//
        fetchUser()

    }

    companion object{
        val USER_KEY = "USER_KEY"
    }

    private fun fetchUser(){
        val ref = FirebaseDatabase.getInstance().getReference("/users")
        ref.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val adapter = GroupAdapter<GroupieViewHolder>()
                snapshot.children.forEach{
                    Log.d("NewMessage",it.toString())
                   val user = it.getValue(User::class.java)
                    if(user!=null) adapter.add(UserItem(user))
                }

                adapter.setOnItemClickListener { item, view ->

                    val userItem = item as UserItem
                    val intent = Intent(view.context,ChatLogActivity::class.java)
//                    intent.putExtra(USER_KEY ,userItem.user.username)
                    intent.putExtra(USER_KEY ,userItem.user)
                    startActivity(intent)

                    finish()    //finish NewMessageActivity Screen
                }

                findViewById<RecyclerView>(R.id.recyclerView_new_message).adapter = adapter
            }
            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
}

class UserItem(val user: User): Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        //will be called in our list for each user object later on..
        viewHolder.itemView.findViewById<TextView>(R.id.username_textView_userRow).text = user.username

        val imageView : CircleImageView = viewHolder.itemView.findViewById(R.id.imageView_new_message_row)

        Picasso.get().load(user.profileImageUrl).into(imageView)
    }

    override fun getLayout(): Int {
        return R.layout.user_row_new_message
    }
}


