package com.example.todo.model

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

class TodoViewModel: ViewModel() {
    var todoList = mutableStateListOf<Todo>()

    fun add(newTodo: Todo){
        todoList.add(newTodo)
    }

    fun delete(todo: Todo){
        todoList.remove(todo)
    }
}