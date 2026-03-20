package com.qjprojects.AA_History.Repository;

import com.qjprojects.AA_History.Entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission, String> {
}
