package com.makif.todoapi.service;

import com.makif.todoapi.entity.Todo;
import com.makif.todoapi.entity.TodoItem;
import com.makif.todoapi.entity.User;
import com.makif.todoapi.enums.TodoItemStatus;
import com.makif.todoapi.exceptions.DependentItemException;
import com.makif.todoapi.repository.TodoItemRepository;
import com.makif.todoapi.repository.TodoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.List;

@Service
public class TodoServiceImpl implements TodoService {

    private SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

    private final TodoRepository todoRepository;
    private final TodoItemRepository todoItemRepository;

    public TodoServiceImpl(TodoRepository todoRepository,
                           TodoItemRepository todoItemRepository) {
        this.todoRepository = todoRepository;
        this.todoItemRepository = todoItemRepository;
    }

    @Transactional
    @Override
    public Todo createTodo(String name) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User)auth.getPrincipal();

        if (currentUser == null) {
            throw new RuntimeException("User not found");
        }

        Todo todo = new Todo();

        todo.setName(name);
        todo.setUserId(currentUser.getId());

        return todoRepository.save(todo);
    }

    @Transactional
    @Override
    public Todo updateTodo(String newName, Todo reqTodo) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User)auth.getPrincipal();

        if (currentUser == null) {
            throw new RuntimeException("User not found");
        }

        todoRepository.findByNameAndUserId(newName, currentUser.getId()).ifPresent(td -> {
            throw new RuntimeException("List name already exist");
        });    //.orElseThrow(() -> new RuntimeException("List name already exist"));

        Todo todo = todoRepository.findByNameAndUserId(reqTodo.getName(), currentUser.getId()).orElseThrow(() -> new RuntimeException("Todo not found"));

        todo.setName(newName);
        return todoRepository.save(todo);
    }

    @Override
    public Page<Todo> getTodosAsPage(Pageable pageable) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User)auth.getPrincipal();

        if (currentUser == null) {
            throw new RuntimeException("User not found");
        }

        Page<Todo> todos = todoRepository.findAllByUserId(currentUser.getId(), pageable);
        return todos;
    }

    @Transactional
    public TodoItem createTodoItem(Long todoId, TodoItem todoItem) {
        Todo todo = todoRepository.findById(todoId).orElseThrow(() -> new RuntimeException("Todo list not found"));

        todoItem.setTodoId(todo.getId());
        todoItem.setStatus(TodoItemStatus.NOT_COMPLETE.name());

        return todoItemRepository.save(todoItem);
    }

    @Override
    public Todo getTodoByName(String name) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User)auth.getPrincipal();

        if (currentUser == null) {
            throw new RuntimeException("User not found");
        }

        return todoRepository.findByNameAndUserId(name, currentUser.getId()).orElseThrow(() -> new RuntimeException("Todo is not found"));
    }

    @Transactional
    @Override
    public void deleteTodo(String name) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User)auth.getPrincipal();

        if (currentUser == null) {
            throw new RuntimeException("User not found");
        }

        Todo todo = todoRepository.findByNameAndUserId(name, currentUser.getId()).orElseThrow(() -> new RuntimeException("Todo is not found"));
        todoRepository.delete(todo);
    }

    @Override
    public Page<TodoItem> getTodoItems(Long todoId, String status, Pageable pageable) {
        if (status.equals("all")) {
            return todoItemRepository.findAllByTodoId(todoId, pageable);
        }

        return todoItemRepository.findAllByTodoIdAndStatusEquals(todoId, status, pageable);
    }

    @Transactional
    @Override
    public TodoItem setTodoItemComplete(Long todoItemId) {

        TodoItem todoItem = todoItemRepository.findById(todoItemId).orElseThrow(() -> new RuntimeException("Todo item is not found"));

        if (todoItem.getStatus().equals(TodoItemStatus.NOT_COMPLETE.name())) {
            if (todoItem.getDependentItemId() != null) {
                TodoItem dependentTodoItem = todoItemRepository.findById(todoItem.getDependentItemId())
                        .orElseThrow(() -> new RuntimeException("Dependent todo item is not found"));
                if (dependentTodoItem.getStatus().equals(TodoItemStatus.NOT_COMPLETE.name())) {
                    throw new RuntimeException("You have to finish dependent item first");
                }
            }
            todoItem.setStatus(TodoItemStatus.COMPLETE.name());
        } else if (todoItem.getStatus().equals(TodoItemStatus.COMPLETE.name())) {
            List<TodoItem> dependentTodoItems = todoItemRepository.findAllByDependentItemId(todoItemId);
            dependentTodoItems.forEach(item -> {
                item.setStatus(TodoItemStatus.NOT_COMPLETE.name());
                todoItemRepository.save(item);
            });
            todoItem.setStatus(TodoItemStatus.NOT_COMPLETE.name());
        }

        return todoItemRepository.save(todoItem);
    }

    @Transactional
    @Override
    public void deleteTodoItem(Long itemId) {
        todoItemRepository.deleteById(itemId);
    }

    @Transactional
    @Override
    public TodoItem updateTodoItem(Long itemId, TodoItem updatedTodoItem) {
        TodoItem todoItem = todoItemRepository.findById(itemId).orElseThrow(() -> new RuntimeException("Todo item is not found"));

        todoItem.setItemName(updatedTodoItem.getItemName());
        todoItem.setDescription(updatedTodoItem.getDescription());
        todoItem.setDeadline(updatedTodoItem.getDeadline());

        return todoItemRepository.save(todoItem);
    }
}
