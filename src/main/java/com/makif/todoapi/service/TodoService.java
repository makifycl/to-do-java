package com.makif.todoapi.service;

import com.makif.todoapi.entity.Todo;
import com.makif.todoapi.entity.TodoItem;
import com.makif.todoapi.exceptions.DependentItemException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface TodoService {

    Todo createTodo(String name);
    Todo updateTodo(String newName, Todo todo);
    Todo getTodoByName(String name);
    Page<Todo> getTodosAsPage(Pageable pageable);

    void deleteTodo(String name);
    TodoItem createTodoItem(Long todoId, TodoItem todoItem);
    TodoItem setTodoItemComplete(Long todoItemId);

    Page<TodoItem> getTodoItems(Long todoId, String status, Pageable pageable);
    void deleteTodoItem(Long itemId);
    TodoItem updateTodoItem(Long itemId, TodoItem updatedTodoItem);
}
