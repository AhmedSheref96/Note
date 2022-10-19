package com.el3asas.note.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.el3asas.note.R
import com.el3asas.note.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        performStates()
        binding.apply {
            viewModel = this@MainActivity.viewModel
            lifecycleOwner = this@MainActivity
            executePendingBindings()
        }

    }

    private fun performStates() {
        lifecycleScope.launch {
            viewModel.mainStateView.collect { state ->
                when (state) {
                    is MainViewsState.Loading -> {
                        viewModel.isLoading.value = true
                    }
                    is MainViewsState.NewNoteAdded -> {
                        Toast.makeText(this@MainActivity, "new note added", Toast.LENGTH_SHORT)
                            .show()
                    }
                    is MainViewsState.Error -> {
                        Toast.makeText(this@MainActivity, "error occurred", Toast.LENGTH_SHORT)
                            .show()
                    }
                    is MainViewsState.Idle -> {
                        viewModel.isLoading.value = false
                    }
                }
            }
        }
    }
}