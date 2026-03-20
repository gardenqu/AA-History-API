package com.qjprojects.AA_History.Entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name="permission")
public class Permission {
    @Id
    @Column(length=36, nullable = false, updatable = false)
    private String id= UUID.randomUUID().toString();

    @Column(length = 50,nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(length = 50, nullable = false)
    private String resource;

    @Column(length = 50,nullable = false)
    private String action;

    private LocalDateTime createdAt= LocalDateTime.now();

    @ManyToMany(mappedBy = "permissions")
    private Set<Role> roles = new HashSet<>();

    //getters and setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
