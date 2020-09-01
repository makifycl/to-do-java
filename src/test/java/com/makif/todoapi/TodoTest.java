package com.makif.todoapi;

import com.makif.todoapi.entity.Todo;
import com.makif.todoapi.entity.TodoItem;
import com.makif.todoapi.entity.User;
import com.makif.todoapi.enums.TodoItemStatus;
import com.makif.todoapi.repository.TodoItemRepository;
import com.makif.todoapi.repository.TodoRepository;
import com.makif.todoapi.service.TodoServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
//import static org.assertj.core.api.Java6Assertions.*;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class TodoTest {

    @Mock
    private TodoRepository todoRepository;

    @Mock
    private TodoItemRepository todoItemRepository;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private TodoServiceImpl todoService;

    @Nested
    class WithMockUser {
        @BeforeEach
        public void setup() {
            User user = new User(1L, "aUserName", "aPassword");

            Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
            SecurityContextHolder.setContext(securityContext);
            when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(user);

        }

        @Test
        void save_todo_test() {

            Todo todo = new Todo("İhtiyaçlar", 1L);
            Mockito.when(todoRepository.save(any(Todo.class))).thenReturn(todo);
            String name = "İhtiyaçlar";
            Todo createdTodo = todoService.createTodo(name);
            assertEquals(createdTodo.getName(), name);
        }

        @Test
        void update_todo_test() {

            Todo todo = new Todo("İhtiyaçlar", 1L);
            Todo expectedTodo = new Todo("Okunacaklar", 1L);

            Optional<Todo> oTodo = Optional.of((Todo) todo);

            Mockito.when(todoRepository.findByNameAndUserId("Okunacaklar", 1L)).thenReturn(Optional.empty());
            Mockito.when(todoRepository.findByNameAndUserId("İhtiyaçlar", 1L)).thenReturn(oTodo);
            Mockito.when(todoRepository.save(any(Todo.class))).thenReturn(expectedTodo);

            String newName = "Okunacaklar";
            Todo updatedTodo = todoService.updateTodo(newName, todo);
            assertEquals(updatedTodo, expectedTodo);
        }
    }

    @Test
    void set_complete_test() {
        TodoItem dependentTodoItem = new TodoItem(2L,
                "Elma",
                "Pazardan elma al",
                new Date(),
                TodoItemStatus.NOT_COMPLETE.name(),
                1L,
                1L,
                new Todo());

        TodoItem todoItem = new TodoItem(1L,
                "Pazar",
                "Pazara git",
                new Date(),
                TodoItemStatus.NOT_COMPLETE.name(),
                1L,
                null,
                new Todo());

        Mockito.when(todoItemRepository.findById(2L)).thenReturn(Optional.of(dependentTodoItem));
        Mockito.when(todoItemRepository.findById(1L)).thenReturn(Optional.of(todoItem));

        Exception exception = assertThrows(RuntimeException.class, () -> todoService.setTodoItemComplete(2L));

        String expectedMessage = "You have to finish dependent item first";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}
