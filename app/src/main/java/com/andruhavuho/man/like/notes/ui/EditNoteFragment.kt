package com.andruhavuho.man.like.notes.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewModelScope
import com.andruhavuho.man.like.notes.App
import com.andruhavuho.man.like.notes.MainActivity
import com.andruhavuho.man.like.notes.R
import com.andruhavuho.man.like.notes.database.NotesViewModel
import kotlinx.coroutines.launch

class EditNoteFragment : Fragment(R.layout.fragment_edit_note) {

    private var currentNoteId: Long = 0

    private lateinit var etNoteTitle: TextView
    private lateinit var etNoteDescription: TextView

    private lateinit var vm : NotesViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        vm = (requireActivity().application as App).vm

        arguments?.getLong(NOTE_ID)?.takeIf { it > 0 }?.let { noteId ->
            currentNoteId = noteId
        } ?: run {
            Toast.makeText(requireActivity(), "Something Went Wrong", Toast.LENGTH_LONG).show()
            requireActivity().onBackPressed()
        }

        etNoteTitle = view.findViewById(R.id.etNoteTitle)
        etNoteDescription = view.findViewById(R.id.etNoteDescription)

        vm.viewModelScope.launch {
            val noteEntity = vm.getNote(currentNoteId)
            if (noteEntity.name != "") {
                etNoteTitle.text = noteEntity.name
            }
            if (noteEntity.description != "") {
                etNoteDescription.text = noteEntity.description
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_edit_note, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.mi_done -> {
                saveChangesAndReturn()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveChangesAndReturn() {
        vm.viewModelScope.launch {
            val noteEntity = vm.getNote(currentNoteId)
            noteEntity.apply {
                name = etNoteTitle.text.toString()
                description = etNoteDescription.text.toString()
                vm.updateNote(this)
            }
//            noteEntity.name = etNoteTitle.text.toString()
//            noteEntity.description = etNoteDescription.text.toString()
//            vm.updateNote(noteEntity)
            (requireActivity() as MainActivity).onBackPressed()
        }
    }

    companion object {

        const val NOTE_ID = "NOTE_ID"

        fun newInstance(noteId: Long): EditNoteFragment {
            return EditNoteFragment().apply {
                arguments = bundleOf(NOTE_ID to noteId)
            }
        }
    }
}
