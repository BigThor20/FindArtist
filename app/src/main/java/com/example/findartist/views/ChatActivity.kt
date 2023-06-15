package com.example.findartist.views

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.findartist.R
import com.example.findartist.adapter.MessageAdapter
import com.example.findartist.model.ChatViewModel
import com.example.findartist.model.MessageItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChatActivity : AppCompatActivity() {
    private val viewModel: ChatViewModel by viewModels()
    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var messageEditText: EditText
    private lateinit var nameTextView: TextView
    private lateinit var sendButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chat_view)

        // Inițializează elementele vizuale
        nameTextView = findViewById(R.id.receiverNameTextView)
        chatRecyclerView = findViewById(R.id.chatRecyclerView)
        messageEditText = findViewById(R.id.messageEditText)
        sendButton = findViewById(R.id.sendButton)

        //preia datele din intent
        val artistId = intent.getStringExtra("artistId") ?: ""
        val userId = intent.getStringExtra("userId") ?: ""

        viewModel.getReceiverName(artistId.toString()) { fieldValue ->
            if (fieldValue != null) {
                nameTextView.text = fieldValue
            } else {
                Log.e("FetchDB", "firstName doesn't exist for this user")
            }
        }
        //load messages
        val messageList = listOf(
            MessageItem("Hello", "YAR6C3TBO0bUR2iAbb2HvqqGDqm1", "2022-01-01"),

            MessageItem("Hi", "Alice", "2022-01-02"),
            MessageItem("How are you?", "YAR6C3TBO0bUR2iAbb2HvqqGDqm1", "2022-01-01"),
            // Adăugați mai multe elemente după nevoie
        )

        val messageAdapter = MessageAdapter(messageList, userId)
        chatRecyclerView.adapter = messageAdapter
        chatRecyclerView.layoutManager = LinearLayoutManager(this)

        sendButton.setOnClickListener {
            val message = messageEditText.text.toString()
            // Golește câmpul de introducere a mesajului
            messageEditText.text.clear()

            // Apelarea funcției addMessageToFirestore() în fundal folosind corutine
            CoroutineScope(Dispatchers.Main).launch {
                viewModel.addMessageToFirestore(userId, artistId, message)
            }
        }
    }
}
