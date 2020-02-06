package com.alferdize.myapplication

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.ColorSpace
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_view.view.*












class ThoughtAdapter(val userList: ArrayList<Thought>) : RecyclerView.Adapter<ThoughtAdapter.ViewHolder>() {
    private val mcon: Context? = null

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThoughtAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_view, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: ThoughtAdapter.ViewHolder, position: Int) {
        holder.bindItems(userList[position])
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return userList.size
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val MY_PREFS_NAME = "thought"
        private var thou : Thought? = null

        init{
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, Imageshow::class.java)
                intent.putExtra("Sholkas", thou!!.name)
                itemView.context.startActivity(intent)
            }
            itemView.share.setOnClickListener{
                val share = Intent(Intent.ACTION_SEND)
                share.type = "text/plain"
                share.putExtra(Intent.EXTRA_TEXT , thou!!.name)
                it.context.startActivity(Intent.createChooser(share, "label"))
            }
            itemView.copy.setOnClickListener {
                val Clipboard = it.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val myClip = ClipData.newPlainText("label" , thou!!.name)
                Clipboard.setPrimaryClip(myClip);
                Toast.makeText(it.context,"Text_Copied",Toast.LENGTH_SHORT).show()
            }
            itemView.like.setOnClickListener{
                Toast.makeText(it.context,"Text_Copied",Toast.LENGTH_SHORT).show()
            }
        }
        fun bindItems(thou: Thought) {
            val textViewName = itemView.findViewById(R.id.thought) as TextView
            textViewName.text = thou.name
            this.thou = thou
        }

    }
}