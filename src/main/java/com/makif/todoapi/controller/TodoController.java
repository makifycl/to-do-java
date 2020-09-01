package com.makif.todoapi.controller;

import com.makif.todoapi.entity.Todo;
import com.makif.todoapi.entity.TodoItem;
import com.makif.todoapi.exceptions.DependentItemException;
import com.makif.todoapi.service.TodoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@ControllerAdvice
@RestController
@RequestMapping("/todo")
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @PostMapping
    public Todo createTodo(@RequestBody Todo todo) {
        return todoService.createTodo(todo.getName());
    }

    @PutMapping("{newName}")
    public Todo updateTodo(@PathVariable(name = "newName") String newName,
                           @RequestBody Todo todo) {
        return todoService.updateTodo(newName, todo);
    }

    @GetMapping("/page")
    public Page<Todo> getTodosAsPage(Pageable pageable) {
        return todoService.getTodosAsPage(pageable);
    }

    @GetMapping("/by-name/{name}")
    public Todo getTodoByName(@PathVariable String name) {
        return todoService.getTodoByName(name);
    }

    @DeleteMapping("{name}")
    public void deleteTodo(@PathVariable String name) {
        todoService.deleteTodo(name);
    }

    @PostMapping("/item/{todoId}")
    public TodoItem createTodoItem(@PathVariable(name = "todoId") Long todoId,
                                   @RequestBody TodoItem todoItem) {
        return todoService.createTodoItem(todoId, todoItem);
    }

    @PutMapping("/item/complete/{itemId}")
    public ResponseEntity<TodoItem> setTodoItemComplete(@PathVariable Long itemId) {
        TodoItem todoItem = todoService.setTodoItemComplete(itemId);
        return new ResponseEntity<TodoItem>(todoItem, HttpStatus.OK);
    }

    @GetMapping("/item/{todoId}/{status}")
    public Page<TodoItem> getTodoItems(@PathVariable Long todoId,
                                       @PathVariable String status,
                                       Pageable pageable) {
        return todoService.getTodoItems(todoId, status, pageable);
    }

    @PutMapping("/item/{itemId}")
    public TodoItem updateTodoItem(@PathVariable Long itemId,
                                   @RequestBody TodoItem todoItem) {
        return todoService.updateTodoItem(itemId, todoItem);
    }

    @DeleteMapping("/item/{itemId}")
    public void deleteTodoItem(@PathVariable Long itemId) {
        todoService.deleteTodoItem(itemId);
    }
}
