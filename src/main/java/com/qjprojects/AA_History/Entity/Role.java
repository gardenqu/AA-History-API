package com.qjprojects.AA_History.Entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name="role")
public class Role {

    @Id
    @Column( name="role_id",length=36, nullable = false, updatable = false)
    private String roleId= UUID.randomUUID().toString();

    @Column(length = 50, unique = true, nullable = false)
    private String name;

    @Column
    private String description;

    @ManyToMany(mappedBy = "roles")
    private Set<AppUser> appUsers = new HashSet<>();

    @ManyToMany
    @JoinTable( name="role_permission",
    joinColumns = @JoinColumn(name="role_id"),
    inverseJoinColumns = @JoinColumn(name="permission_id"))
    private Set<Permission> permissions= new HashSet<>();

    private LocalDateTime createdAt= LocalDateTime.now();
    public Role(){}
    public Role(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Override
    public String toString() {
        return "<Role "+ this.name+ ">";
    }

    //getter and setter


    public String getId() {
        return roleId;
    }

    public void setId(String id) {
        this.roleId = id;
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

    public Set<AppUser> getAppUsers() {
        return appUsers;
    }

    public void setAppUsers(Set<AppUser> appUsers) {
        this.appUsers = appUsers;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

}
