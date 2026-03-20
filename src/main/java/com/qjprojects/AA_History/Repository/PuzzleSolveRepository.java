package com.qjprojects.AA_History.Repository;

import com.qjprojects.AA_History.Entity.PuzzleSolve;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PuzzleSolveRepository extends JpaRepository<PuzzleSolve, String> {
    List<PuzzleSolve> findByUserId(String id);
    List<PuzzleSolve> findByPuzzlePuzzleId(String puzzleId);

}
