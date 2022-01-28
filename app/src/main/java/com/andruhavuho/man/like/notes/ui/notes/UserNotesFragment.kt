package com.andruhavuho.man.like.notes.ui.notes

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.andruhavuho.man.like.notes.App
import com.andruhavuho.man.like.notes.MainActivity
import com.andruhavuho.man.like.notes.R
import com.andruhavuho.man.like.notes.database.NoteEntity
import com.andruhavuho.man.like.notes.database.NotesViewModel
import com.andruhavuho.man.like.notes.models.UserNote
import kotlinx.coroutines.launch


class UserNotesFragment : Fragment(R.layout.fragment_notes) {

    private lateinit var tvNotesCountLabel: TextView
    private lateinit var userNotesAdapter: UserNotesAdapter
    private lateinit var vm : NotesViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        vm = (requireActivity().application as App).vm
        tvNotesCountLabel = view.findViewById(R.id.tvNotesCountLabel)

        setUpNotesRecyclerView(view)

        view.findViewById<Button>(R.id.btnAddNewNote).setOnClickListener {
            addNewEmptyNote()
        }
        vm.viewModelScope.launch {
            showNotes(vm.getNotes())
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_notes, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.mi_add -> {
                addNewEmptyNote()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setUpNotesRecyclerView(view: View) {
        userNotesAdapter = UserNotesAdapter(
            view.findViewById(R.id.clNotesFragment),
            { swipedNoteId ->
                deleteNoteFromDB(swipedNoteId)
                updateNotesCount()
            }, { recoveredNote ->
                returnNoteBackToDB(recoveredNote)
                updateNotesCount()
            }, { note ->
                (requireActivity() as MainActivity).showNoteInfoFragment(note.id)
            }
        )
        val rvUserArmies = view.findViewById<RecyclerView>(R.id.rvNotes)
        rvUserArmies.adapter = userNotesAdapter

        val deleteIcon = AppCompatResources.getDrawable(requireContext(), R.drawable.ic_baseline_delete_24)

        val itemTouchHelper = ItemTouchHelper(SwipeToDeleteCallback(userNotesAdapter, deleteIcon))
        itemTouchHelper.attachToRecyclerView(rvUserArmies)
    }

    private fun addNewEmptyNote() {
        vm.viewModelScope.launch {
            val newEntity = NoteEntity()
            val id = vm.insertNewEntity(newEntity)
            val newPos = userNotesAdapter.notes.size
            userNotesAdapter.notes.add(newEntity.toUserNote(id))
            userNotesAdapter.notifyItemInserted(newPos)
            updateNotesCount()
        }
    }

    private fun showNotes(notes: MutableList<UserNote>) {
        userNotesAdapter.notes = notes
        userNotesAdapter.notifyDataSetChanged()
        updateNotesCount()
    }

    private fun updateNotesCount() {
        tvNotesCountLabel.text = "Количество заметок: ${userNotesAdapter.itemCount}"
    }

    private fun deleteNoteFromDB(noteId: Long) {
        vm.viewModelScope.launch {
            vm.deleteNoteFromDB(noteId)
        }
    }

    private fun returnNoteBackToDB(recoveredNote: UserNote) {
        vm.viewModelScope.launch {
            vm.returnNoteBackToDB(recoveredNote)
        }
    }
}
