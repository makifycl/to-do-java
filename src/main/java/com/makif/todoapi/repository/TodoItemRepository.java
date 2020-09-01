package com.makif.todoapi.repository;

import com.makif.todoapi.entity.TodoItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TodoItemRepository extends JpaRepository<TodoItem, Long> {
    Page<TodoItem> findAllByTodoIdAndStatusEquals(Long todoId, String status, Pageable pageable);
    Page<TodoItem> findAllByTodoId(Long todoId, Pageable pageable);
    List<TodoItem> findAllByDependentItemId(Long dependentItemId);
}
