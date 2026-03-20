package com.qjprojects.AA_History.Repository;

import com.qjprojects.AA_History.Entity.LoginAttempt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginAttemptRepository extends JpaRepository<LoginAttempt,String> {
}
