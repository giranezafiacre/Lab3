@file:OptIn(ExperimentalFoundationApi::class)

package com.example.lab3.ui.compose

import android.content.Context
import android.widget.CheckBox
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lab3.R
import com.example.lab3.ui.theme.TodoTheme
import com.example.todo.model.Todo
import com.example.todo.model.TodoViewModel
import java.util.*

@Composable
fun TodoScreen() {
    val viewModel: TodoViewModel = viewModel()
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        backgroundColor = MaterialTheme.colors.surface,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {showDialog = true}
            )
            {
                Icon(Icons.Filled.Add, contentDescription = "")
            }
        },
        content = {
            if (showDialog){
                addTodoDialog(context, dismissDialog = {showDialog = false}, {viewModel.add(it)})
            }
            LazyColumn(
                contentPadding = PaddingValues(
                    vertical = 8.dp,
                    horizontal = 8.dp
                )
            )
            {
                items(viewModel.todoList, key={todo -> todo.id}) { todo ->
                    TodoItem(item = todo, context, {viewModel.delete(it)})
                }
            }
        }
    )
}

@Composable
fun addTodoDialog(context: Context, dismissDialog:() -> Unit, addTodo: (Todo) -> Unit){
    var todoTextField by remember {
        mutableStateOf("")
    }
    var descTextField by remember {
        mutableStateOf("")
    }

    AlertDialog(
        onDismissRequest = { dismissDialog},
        title={Text(text = stringResource(id = R.string.addTodo), style = MaterialTheme.typography.h6)},
        text = {
            Column(modifier = Modifier.padding(top=20.dp)) {
                TextField(label = {Text(text=stringResource(id = R.string.todoName))}, value = todoTextField, onValueChange = {todoTextField=it})
                Spacer(modifier = Modifier.height(10.dp))
                TextField(label = { Text(text=stringResource(id = R.string.desc)) },value = descTextField, onValueChange = {descTextField=it})
            }
        },
        confirmButton = {
            Button(onClick = {
                if(todoTextField.isNotEmpty()) {
                    val newID = UUID.randomUUID().toString();
                    addTodo(Todo(newID, todoTextField, descTextField))
                    Toast.makeText(
                        context,
                        context.resources.getString(R.string.todoAdded),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                dismissDialog()
            })
            {
                Text(text = stringResource(id = R.string.add))
            }
        },dismissButton = {
            Button(onClick = {
                dismissDialog()
            }) {
                Text(text = stringResource(id = R.string.cancel))
            }
        }
    )
}

@Composable
fun deleteTodoDialog(context: Context, dismissDialog:() -> Unit, item: Todo, deleteTodo: (Todo) -> Unit){
    AlertDialog(
        onDismissRequest = { dismissDialog},
        title={Text(text = stringResource(id = R.string.delete), style = MaterialTheme.typography.h6)},
        confirmButton = {
            Button(onClick = {
                deleteTodo(item)
                    Toast.makeText(
                        context,
                        context.resources.getString(R.string.deleteTodo),
                        Toast.LENGTH_SHORT
                    ).show()
                dismissDialog()
            })
            {
                Text(text = stringResource(id = R.string.yes))
            }
        },dismissButton = {
            Button(onClick = {
                dismissDialog()
            }) {
                Text(text = stringResource(id = R.string.no))
            }
        }
    )
}

@Composable
fun TodoItem(item: Todo, context: Context, deleteTodo: (Todo) -> Unit) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    var checkedvar by remember {
        mutableStateOf(false)
    }
    Card(
        elevation = 4.dp,
        shape = RoundedCornerShape(10.dp),
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = MaterialTheme.colors.onPrimary,
        border = BorderStroke(2.dp, color = MaterialTheme.colors.primaryVariant),
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .combinedClickable(
                onClick = {
                    Toast
                        .makeText(
                            context,
                            context.resources.getString(R.string.readmsg) + " " + item.todoName,
                            Toast.LENGTH_SHORT
                        )
                        .show()
                }
            )
    ) {
        Row() {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Checkbox( checked=checkedvar, onCheckedChange = { showDeleteDialog = true })
            }
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(text = item.todoName, style = MaterialTheme.typography.h6)
                Text(text = item.description, style = MaterialTheme.typography.body1)
            }

        }


    }
    if (showDeleteDialog){
        deleteTodoDialog(context, dismissDialog = {showDeleteDialog = false}, item, deleteTodo)
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TodoTheme {
        TodoScreen()
    }
}