package com.el3asas.note.ui.home


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.viewbinding.ViewBinding
import com.el3asas.note.databinding.FragmentHomeBinding
import com.el3asas.utils.binding.FragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment(override val bindingInflater: (LayoutInflater) -> ViewBinding=FragmentHomeBinding::inflate)
    : FragmentBinding<FragmentHomeBinding>() {

    private val viewModel by viewModels<HomeViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            viewModel=this@HomeFragment.viewModel
            lifecycleOwner=this@HomeFragment
        }
    }

}