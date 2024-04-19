package com.example.imagefeed.ui.list.presentation

sealed interface Event {
    data class OnImageItemClicked(val index: Int) : Event
    data object OnEndOfListReached : Event
    data object OnDismissed : Event
}