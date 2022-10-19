package com.el3asas.note.ui.home

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

sealed class HomeIntents {
    data class Refresh(val swipeRefreshLayout: SwipeRefreshLayout) : HomeIntents()
}