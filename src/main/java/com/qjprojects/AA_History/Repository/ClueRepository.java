package com.qjprojects.AA_History.Repository;

import com.qjprojects.AA_History.Entity.Clue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClueRepository extends JpaRepository<Clue, String> {
    List<Clue> findByPuzzlePuzzleId(String puzzleId);
}