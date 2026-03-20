package com.qjprojects.AA_History.Repository;

import com.qjprojects.AA_History.Entity.WordBankClue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WordBankClueRepository extends JpaRepository<WordBankClue, String> {
    List<WordBankClue> findByWordBankEntryEntryId(String entryId);
}