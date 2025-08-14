package dev.drivemode.techtest.viewmodel

import dev.drivemode.techtest.Works
import dev.drivemode.techtest.data.BookRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BookViewModel(private val repo: BookRepository) {
    private val scope = CoroutineScope(Dispatchers.Main)

    private val _state = MutableStateFlow<UiState>(UiState.Idle)
    val state = _state.asStateFlow()

    fun search(subject: String) {
        _state.value = UiState.Loading
        scope.launch {
            try {
                val data = repo.getBooksBySubject(subject)
                _state.value = UiState.Success(data)
            } catch (e: Exception) {
                _state.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    sealed class UiState {
        object Idle : UiState()
        object Loading : UiState()
        data class Success(val works: Works) : UiState()
        data class Error(val message: String) : UiState()
    }
}