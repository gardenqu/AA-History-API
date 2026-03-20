package com.qjprojects.AA_History.Repository;

import com.qjprojects.AA_History.Entity.WordBankEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WordBankEntryRepository extends JpaRepository<WordBankEntry, String> {
    List<WordBankEntry> findByCategoryCategoryId(String categoryId);
    boolean existsByCategoryCategoryIdAndWord(String categoryId, String word);
}