package com.andruhavuho.man.like.notes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.andruhavuho.man.like.notes.ui.EditNoteFragment
import com.andruhavuho.man.like.notes.ui.NoteInfoFragment
import com.andruhavuho.man.like.notes.ui.notes.UserNotesFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                replace<UserNotesFragment>(R.id.fragment_container_view)
                addToBackStack(null)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 1) {
            supportFragmentManager.popBackStack()
        } else {
            finish()
        }
    }

    fun showNoteInfoFragment(noteId: Long) {
        showFragment(NoteInfoFragment.newInstance(noteId))
    }

    fun showEditNoteFragment(noteId: Long) {
        showFragment(EditNoteFragment.newInstance(noteId))
    }

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager.commit {
            setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out)
            replace(R.id.fragment_container_view, fragment)
            addToBackStack(null)
        }
    }
}