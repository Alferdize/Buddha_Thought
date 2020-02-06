package com.alferdize.myapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class ToughtActivity : AppCompatActivity() {

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.container)

        //getting recyclerview from xml
        val recyclerView = findViewById(R.id.recyclerView) as RecyclerView

        //adding a layoutmanager
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)


        //crating an arraylist to store users using the data class user
        val users = ArrayList<Thought>()

        //adding some dummy data to the list
        users.add(Thought("There is no fear for one\nwhose mind is not filled\nwith desires."))
        users.add(Thought("Work out your own salvation. Do not depend on others."))
        users.add(Thought("If anything is worth doing, do it with all your heart."))
        users.add(Thought("A man is not called wise because he talks and talks again; but if he is peaceful, loving and fearless then he is in truth called wise."))

        //creating our adapter
        val adapter = ThoughtAdapter(users)

        //now adding the adapter to recyclerview
        recyclerView.adapter = adapter
    }
}