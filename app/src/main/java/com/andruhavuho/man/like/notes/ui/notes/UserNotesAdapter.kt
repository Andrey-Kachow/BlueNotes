package com.andruhavuho.man.like.notes.ui.notes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.andruhavuho.man.like.notes.R
import com.andruhavuho.man.like.notes.models.UserNote
import com.google.android.material.snackbar.Snackbar

class UserNotesAdapter(
    private val clNotesFragment: ConstraintLayout,
    private val onSwiped: (swipedNoteId: Long) -> Unit,
    private val onUndoDelete: (recoveredNote: UserNote) -> Unit,
    private val clickListener: (note: UserNote) -> Unit
) : RecyclerView.Adapter<UserNotesAdapter.ViewHolder>() {

    var notes: MutableList<UserNote> = mutableListOf()
    private var recentlyDeletedNote: UserNote? = null
    private var recentlyDeletedNotePosition: Int? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(notes[position], clickListener)
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    fun deleteItem(position: Int) {
        recentlyDeletedNotePosition = position
        recentlyDeletedNote = notes.removeAt(position)
        notifyItemRemoved(position)
        recentlyDeletedNote?.let {
            onSwiped.invoke(it.id)
        }
        showUndoSnackbar()
    }

    private fun showUndoSnackbar() {
        Snackbar
            .make(
                clNotesFragment,
                R.string.snack_bar_undo,
                Snackbar.LENGTH_LONG
            ).setAction(R.string.snack_bar_undo) { undoDelete() }
            .show()
    }

    private fun undoDelete() {
        recentlyDeletedNote?.let { thatNote ->
            recentlyDeletedNotePosition?.let { thatPos ->
                notes.add(thatPos, thatNote)
                notifyItemInserted(thatPos)
                onUndoDelete.invoke(thatNote)
            }
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val tvNoteItemTitle = itemView.findViewById<TextView>(R.id.tvNoteItemTitle)
        private val tvNoteItemDescription = itemView.findViewById<TextView>(R.id.tvNoteItemDescription)

        fun bind(note: UserNote, clickListenerFunction: (army: UserNote) -> Unit) {

            tvNoteItemTitle.text = note.getNameOrDefault()
            tvNoteItemDescription.text = note.getDescriptionOrDefault()

            itemView.setOnClickListener {
                clickListenerFunction.invoke(note)
            }
        }
    }
}