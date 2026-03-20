package com.qjprojects.AA_History.Repository;

import com.qjprojects.AA_History.Entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository  extends JpaRepository<Role, String> {
    Optional<Role> findByName(String name);
}
