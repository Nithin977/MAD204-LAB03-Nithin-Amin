/**
 * Course: F2025 MAD204-01 Java Development for Mobile Apps
 * Lab 3: Persistent Notes App
 * Author: Nithin Amin (A00194432)
 * Date: November 2025
 *
 * Description:
 * Allows users to add, delete, and persist notes using RecyclerView
 * and SharedPreferences (JSON via Gson). Notes remain after restart.
 */
package com.example.lab56

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import androidx.core.content.edit

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var editNote: EditText
    private lateinit var btnAdd: Button
    private lateinit var adapter: MyAdapter
    private var notesList = mutableListOf<Note>()
    private val gson = Gson()
    private val sharedPrefs by lazy {
        getSharedPreferences("notes_pref", Context.MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        editNote = findViewById(R.id.editNote)
        btnAdd = findViewById(R.id.btnAdd)

        loadNotes()

        adapter = MyAdapter(notesList) { position -> deleteNote(position) }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        btnAdd.setOnClickListener {
            val noteText = editNote.text.toString().trim()
            if (noteText.isNotEmpty()) {
                notesList.add(Note(noteText))
                adapter.notifyItemInserted(notesList.size - 1)
                editNote.text.clear()
                Toast.makeText(this, "Note added!", Toast.LENGTH_SHORT).show()
                saveNotes()
            }
        }
    }

    private fun deleteNote(position: Int) {
        val deletedNote = notesList[position]
        notesList.removeAt(position)
        adapter.notifyItemRemoved(position)
        saveNotes()

        Snackbar.make(recyclerView, "Note deleted", Snackbar.LENGTH_LONG)
            .setAction("UNDO") {
                notesList.add(position, deletedNote)
                adapter.notifyItemInserted(position)
                saveNotes()
            }.show()
    }

    private fun saveNotes() {
        val json = gson.toJson(notesList)
        sharedPrefs.edit { putString("notes_key", json) }
    }

    private fun loadNotes() {
        val json = sharedPrefs.getString("notes_key", null)
        if (json != null) {
            val type = object : TypeToken<MutableList<Note>>() {}.type
            notesList = gson.fromJson(json, type)
        }
    }

    override fun onPause() {
        super.onPause()
        saveNotes()
    }
}
