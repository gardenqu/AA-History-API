package com.qjprojects.AA_History.Repository;

import com.qjprojects.AA_History.Entity.SecurityLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SecurityLogRepository extends JpaRepository<SecurityLog, String> {
}
