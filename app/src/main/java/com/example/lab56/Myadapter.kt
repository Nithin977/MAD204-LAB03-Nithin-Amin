/**
 * RecyclerView Adapter for displaying and handling Note items.
 */

package com.example.lab56

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(
    private val notes: MutableList<Note>,
    private val onLongClick: (Int) -> Unit
) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    /** ViewHolder holds reference to the note TextView */
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textNote: TextView = itemView.findViewById(R.id.textNote)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_row, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val note = notes[position]
        holder.textNote.text = note.text

        // Long-press deletes the note
        holder.itemView.setOnLongClickListener {
            onLongClick(position)
            true
        }
    }

    override fun getItemCount(): Int = notes.size
}
