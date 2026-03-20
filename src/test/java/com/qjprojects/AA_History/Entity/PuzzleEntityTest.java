package com.qjprojects.AA_History.Entity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class PuzzleEntityTest {

    @Autowired
    TestEntityManager entityManager;

    private Puzzle createPuzzle() {
        Puzzle puzzle = new Puzzle();
        puzzle.setTitle("Sample Puzzle");
        puzzle.setDifficulty("Easy");
        puzzle.setAuthor("Admin");
        puzzle.setWidth(5);
        puzzle.setHeight(5);
        puzzle.setGrid(Map.of("0,0", "A", "0,1", "B"));
        return puzzle;
    }

    private AppUser createUser() {
        // FIXED: use the ONLY constructor your AppUser entity actually has
        return new AppUser(
                "tester",
                "tester@example.com",
                "password123"
        );
    }

    @Test
    void canPersistPuzzle() {
        Puzzle puzzle = createPuzzle();

        Puzzle saved = entityManager.persistFlushFind(puzzle);

        assertNotNull(saved.getPuzzleId());
        assertEquals("Sample Puzzle", saved.getTitle());
        assertEquals("Easy", saved.getDifficulty());
        assertEquals(5, saved.getWidth());
        assertEquals(5, saved.getHeight());
        assertNotNull(saved.getGrid());
        assertEquals("A", saved.getGrid().get("0,0"));
    }

    @Test
    void puzzleRequiresMandatoryFields() {
        Puzzle puzzle = new Puzzle();

        assertThrows(Exception.class, () -> {
            entityManager.persistAndFlush(puzzle);
        });
    }

    @Test
    void canPersistPuzzleSolveRelationship() {
        AppUser user = createUser();
        entityManager.persist(user);

        Puzzle puzzle = createPuzzle();
        entityManager.persist(puzzle);

        PuzzleSolve solve = new PuzzleSolve(
                user,
                puzzle,
                120,
                1,
                false
        );

        entityManager.persistAndFlush(solve);

        PuzzleSolve saved = entityManager.find(PuzzleSolve.class, solve.getSolveId());

        assertNotNull(saved);
        assertEquals(120, saved.getTimeTaken());
        assertEquals(1, saved.getMistakes());
        assertFalse(saved.getUsedHints());
        assertEquals(user.getId(), saved.getUser().getId());
        assertEquals(puzzle.getPuzzleId(), saved.getPuzzle().getPuzzleId());
    }

    @Test
    void puzzleCascadeDeletesSolves() {
        AppUser user = createUser();
        entityManager.persist(user);

        Puzzle puzzle = createPuzzle();
        entityManager.persist(puzzle);

        PuzzleSolve solve = new PuzzleSolve(
                user,
                puzzle,
                90,
                0,
                false
        );

        puzzle.getSolves().add(solve);

        entityManager.persist(solve);
        entityManager.flush();

        entityManager.remove(puzzle);
        entityManager.flush();

        PuzzleSolve deleted = entityManager.find(PuzzleSolve.class, solve.getSolveId());
        assertNull(deleted);
    }

    @Test
    void puzzleSolveRequiresMandatoryFields() {
        AppUser user = createUser();
        entityManager.persist(user);

        Puzzle puzzle = createPuzzle();
        entityManager.persist(puzzle);

        PuzzleSolve solve = new PuzzleSolve();
        solve.setUser(user);
        solve.setPuzzle(puzzle);

        assertThrows(Exception.class, () -> {
            entityManager.persistAndFlush(solve);
        });
    }
}