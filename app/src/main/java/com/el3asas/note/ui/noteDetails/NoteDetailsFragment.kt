package com.el3asas.note.ui.noteDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.viewbinding.ViewBinding
import com.el3asas.note.databinding.FragmentNoteDetailsBinding
import com.el3asas.utils.binding.BottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoteDetailsFragment(override val bindingInflater: (LayoutInflater) -> ViewBinding = FragmentNoteDetailsBinding::inflate) :
    BottomSheetBinding<FragmentNoteDetailsBinding>() {

    private val viewModel by viewModels<NoteDetailsViewModel>()
    private val args by navArgs<NoteDetailsFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mDialog = dialog as BottomSheetDialog
        mDialog.behavior.apply {
            state = BottomSheetBehavior.STATE_EXPANDED
            skipCollapsed = true
            isFitToContents = false
        }
    }

}