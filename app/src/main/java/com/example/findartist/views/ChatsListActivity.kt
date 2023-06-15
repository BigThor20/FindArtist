package com.example.findartist.views

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.findartist.R
import com.example.findartist.adapter.ChatItemsAdapter
import com.example.findartist.model.ChatItemList
import com.example.findartist.model.ChatsListViewModel
import com.example.findartist.model.MyProfileViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChatsListActivity : AppCompatActivity() {
    private lateinit var chatsListViewModel: ChatsListViewModel
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var recyclerView: RecyclerView
    var userId: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chats)

        // GET CURRENT USER ID
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            userId = currentUser.uid
        } else {
            println("User not authenticated")
        }

        chatsListViewModel = ViewModelProvider(this).get(ChatsListViewModel::class.java)
        recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        chatsListViewModel.getReceiversForSender(userId)
        chatsListViewModel.chatItems.observe(this) { chatItems ->
            val adapter = ChatItemsAdapter(chatItems)
            recyclerView.adapter = adapter
            adapter.setOnChatItemClickListener(object : ChatItemsAdapter.OnChatItemClickListener {
                override fun onChatItemClick(chatItem: ChatItemList) {
                    val intent = Intent(this@ChatsListActivity, ChatActivity::class.java)
                    // Puteți adăuga date suplimentare la intent, cum ar fi ID-ul sau alte informații despre chatItem
                    intent.putExtra("userId", userId)
                    intent.putExtra("artistId", chatItem.artistId)
                    startActivity(intent)
                }
            })
            recyclerView.adapter = adapter
        }


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