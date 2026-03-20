package com.qjprojects.AA_History.Controller;

import com.qjprojects.AA_History.DTO.*;
import com.qjprojects.AA_History.Entity.*;
import com.qjprojects.AA_History.Service.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final CategoryService categoryService;
    private final WordBankEntryService wordBankEntryService;
    private final WordBankClueService wordBankClueService;
    private final UserService userService;
    private final PuzzleService puzzleService;
    private final ClueService clueService;

    public AdminController(CategoryService categoryService,
                           WordBankEntryService wordBankEntryService,
                           WordBankClueService wordBankClueService,
                           UserService userService,
                           PuzzleService puzzleService,
                           ClueService clueService) {
        this.categoryService = categoryService;
        this.wordBankEntryService = wordBankEntryService;
        this.wordBankClueService = wordBankClueService;
        this.userService = userService;
        this.puzzleService = puzzleService;
        this.clueService = clueService;
    }
    // ─── Categories ───────────────────────────────────────────

    @PostMapping("/categories")
    public ResponseEntity<CategoryResponse> createCategory(
            @Valid @RequestBody CategoryCreateRequest request) {
        Category category = categoryService.create(request.getName());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(WordBankMapper.toCategoryResponse(category));
    }

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        List<CategoryResponse> categories = categoryService.getAll()
                .stream()
                .map(WordBankMapper::toCategoryResponse)
                .toList();
        return ResponseEntity.ok(categories);
    }

    @DeleteMapping("/categories/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable String id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ─── Word Bank Entries ────────────────────────────────────

    @PostMapping("/word-bank/entries")
    public ResponseEntity<WordBankEntryResponse> createEntry(
            @Valid @RequestBody WordBankEntryCreateRequest request) {
        WordBankEntry entry = wordBankEntryService.create(
                request.getCategoryId(), request.getWord());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(WordBankMapper.toWordBankEntryResponse(entry));
    }

    @GetMapping("/word-bank/entries/{categoryId}")
    public ResponseEntity<List<WordBankEntryResponse>> getEntriesByCategory(
            @PathVariable String categoryId) {
        List<WordBankEntryResponse> entries = wordBankEntryService.getByCategory(categoryId)
                .stream()
                .map(WordBankMapper::toWordBankEntryResponse)
                .toList();
        return ResponseEntity.ok(entries);
    }

    @DeleteMapping("/word-bank/entries/{id}")
    public ResponseEntity<Void> deleteEntry(@PathVariable String id) {
        wordBankEntryService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ─── Word Bank Clues ──────────────────────────────────────

    @PostMapping("/word-bank/clues")
    public ResponseEntity<WordBankClueResponse> createClue(
            @Valid @RequestBody WordBankClueCreateRequest request) {
        WordBankClue clue = wordBankClueService.create(
                request.getEntryId(), request.getClueText());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(WordBankClueMapper.toResponse(clue));
    }

    @GetMapping("/word-bank/clues/{entryId}")
    public ResponseEntity<List<WordBankClueResponse>> getCluesByEntry(
            @PathVariable String entryId) {
        List<WordBankClueResponse> clues = wordBankClueService.getByEntry(entryId)
                .stream()
                .map(WordBankClueMapper::toResponse)
                .toList();
        return ResponseEntity.ok(clues);
    }

    @DeleteMapping("/word-bank/clues/{id}")
    public ResponseEntity<Void> deleteClue(@PathVariable String id) {
        wordBankClueService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ─── Users ────────────────────────────────────────────────

    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> users = userService.getAll()
                .stream()
                .map(UserMapper::toResponse)
                .toList();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable String id) {
        return ResponseEntity.ok(UserMapper.toResponse(userService.getById(id)));
    }

    @PatchMapping("/users/{id}/deactivate")
    public ResponseEntity<UserResponse> deactivateUser(@PathVariable String id) {
        return ResponseEntity.ok(UserMapper.toResponse(userService.deactivate(id)));
    }

    @PatchMapping("/users/{id}/ban")
    public ResponseEntity<UserResponse> banUser(@PathVariable String id) {
        return ResponseEntity.ok(UserMapper.toResponse(userService.ban(id)));
    }

    @PatchMapping("/users/{id}/unban")
    public ResponseEntity<UserResponse> unbanUser(@PathVariable String id) {
        return ResponseEntity.ok(UserMapper.toResponse(userService.unban(id)));
    }

    @PatchMapping("/users/{id}/roles")
    public ResponseEntity<UserResponse> assignRoles(
            @PathVariable String id,
            @RequestBody Set<String> roleNames) {
        return ResponseEntity.ok(UserMapper.toResponse(userService.assignRoles(id, roleNames)));
    }

    // ─── Puzzles ──────────────────────────────────────────────

    @DeleteMapping("/puzzles/{id}")
    public ResponseEntity<Void> deletePuzzle(@PathVariable String id) {
        puzzleService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/puzzles/{id}/approve")
    public ResponseEntity<PuzzleResponse> approvePuzzle(@PathVariable String id) {
        Puzzle puzzle = puzzleService.approve(id);
        List<ClueResponse> clues = clueService.getCluesForPuzzle(id)
                .stream()
                .map(ClueMapper::toResponse)
                .toList();
        return ResponseEntity.ok(PuzzleMapper.toResponse(puzzle, clues));
    }

    @PatchMapping("/puzzles/{id}/reject")
    public ResponseEntity<PuzzleResponse> rejectPuzzle(@PathVariable String id) {
        Puzzle puzzle = puzzleService.reject(id);
        List<ClueResponse> clues = clueService.getCluesForPuzzle(id)
                .stream()
                .map(ClueMapper::toResponse)
                .toList();
        return ResponseEntity.ok(PuzzleMapper.toResponse(puzzle, clues));
    }
}