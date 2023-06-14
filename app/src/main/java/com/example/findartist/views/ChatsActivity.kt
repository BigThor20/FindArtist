package com.example.findartist.views

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.findartist.R
import com.example.findartist.adapter.ChatItemsAdapter
import com.example.findartist.adapter.ImageAdapter
import com.example.findartist.adapter.ItemAdapter
import com.example.findartist.model.ChatItemList
import com.google.android.material.bottomnavigation.BottomNavigationView

class ChatsActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var recyclerView: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chats)

        ///hardcodare pt design
        var chatItems = ArrayList<ChatItemList>()
        chatItems.add(ChatItemList("Georgiana Popescu", "https://firebasestorage.googleapis.com/v0/b/findartists-7dd64.appspot.com/o/profile_photos%2Fprofile5.jpeg?alt=media&token=c0798fde-cf03-4649-b984-4157bec44592" ))
        chatItems.add(ChatItemList("Georgiana Popescu", "https://firebasestorage.googleapis.com/v0/b/findartists-7dd64.appspot.com/o/profile_photos%2Fprofile5.jpeg?alt=media&token=c0798fde-cf03-4649-b984-4157bec44592" ))
        chatItems.add(ChatItemList("Georgiana Popescu", "https://firebasestorage.googleapis.com/v0/b/findartists-7dd64.appspot.com/o/profile_photos%2Fprofile5.jpeg?alt=media&token=c0798fde-cf03-4649-b984-4157bec44592" ))

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = ChatItemsAdapter(chatItems)

        val adapter = ChatItemsAdapter(chatItems)
        adapter.setOnChatItemClickListener(object : ChatItemsAdapter.OnChatItemClickListener {
            override fun onChatItemClick(chatItem: ChatItemList) {
                val intent = Intent(this@ChatsActivity, ChatActivity::class.java)
                // Puteți adăuga date suplimentare la intent, cum ar fi ID-ul sau alte informații despre chatItem
                startActivity(intent)
            }
        })
        recyclerView.adapter = adapter




        //nav
        bottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.selectedItemId = R.id.menu_chats
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_discover -> {
                    val intent = Intent(this, DiscoverActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.menu_chats -> {
                    true
                }
                R.id.menu_profile -> {
                    val intent = Intent(this, MyProfileActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
        //end nav
    }
}