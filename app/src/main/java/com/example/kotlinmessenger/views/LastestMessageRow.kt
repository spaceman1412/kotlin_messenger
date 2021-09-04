package com.example.kotlinmessenger.views

import android.content.Intent
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import com.example.kotlinmessenger.R
import com.example.kotlinmessenger.messages.LastestMessageActivity
import com.example.kotlinmessenger.messages.NewMessageActivity
import com.example.kotlinmessenger.models.ChatMessage
import com.example.kotlinmessenger.models.User
import com.example.kotlinmessenger.registerlogin.RegisterActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import de.hdodenhof.circleimageview.CircleImageView


class LastestMessageRow(val chatMessage: ChatMessage) : Item<GroupieViewHolder>(){

    var chatPartnerUser: User? = null

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.findViewById<TextView>(R.id.lastest_message_textView_lastest_message).text =
            chatMessage.text

        val chatPartnerId: String
        if(chatMessage.fromId == FirebaseAuth.getInstance().uid ){
            chatPartnerId = chatMessage.toId
        }else {
            chatPartnerId = chatMessage.fromId
        }

        val ref = FirebaseDatabase.getInstance().getReference("/users/$chatPartnerId")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                chatPartnerUser = snapshot.getValue(User::class.java)

                viewHolder.itemView.findViewById<TextView>(R.id.username_textView_lastest_message).text = chatPartnerUser?.username

                val imageView : CircleImageView = viewHolder.itemView.findViewById(R.id.imageView_lastest_messages)

                Picasso.get().load(chatPartnerUser?.profileImageUrl).into(imageView)
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })


    }

    override fun getLayout(): Int {
        return R.layout.lastest_messages_row
    }
}


