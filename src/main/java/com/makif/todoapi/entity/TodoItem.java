package com.makif.todoapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name = "TODO_ITEM")
public class TodoItem extends AuditModel{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ITEM_NAME")
    private String itemName;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "DEADLINE")
    private Date deadline;

    private String simpleDeadline;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "TODO_ID")
    private Long todoId;

    @Column(name = "DEPENDENT_ITEM_ID")
    private Long dependentItemId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "TODO_ID", referencedColumnName = "ID", nullable = false, insertable=false, updatable=false)
    @JsonIgnore
    private Todo todo;

    public TodoItem() {
    }

    public TodoItem(Long id, String itemName, String description, Date deadline, String status, Long todoId, Long dependentItemId, Todo todo) {
        this.id = id;
        this.itemName = itemName;
        this.description = description;
        this.deadline = deadline;
        this.status = status;
        this.todoId = todoId;
        this.dependentItemId = dependentItemId;
        this.todo = todo;
    }

    public Long getId() {
        return id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getTodoId() {
        return todoId;
    }

    public void setTodoId(Long todoId) {
        this.todoId = todoId;
    }

    public Todo getTodo() {
        return todo;
    }

    public void setTodo(Todo todo) {
        this.todo = todo;
    }

    public String getSimpleDeadline() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(deadline);
    }

    public Long getDependentItemId() {
        return dependentItemId;
    }

    public void setDependentItemId(Long dependentItemId) {
        this.dependentItemId = dependentItemId;
    }
}
