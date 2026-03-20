package com.qjprojects.AA_History.Repository;

import com.qjprojects.AA_History.Entity.SecurityLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SecurityLogRepository extends JpaRepository<SecurityLog, String> {
    List<SecurityLog> findByUserIdOrderByCreatedAtDesc(String userId);
}