package com.qjprojects.AA_History.Controller;

import com.qjprojects.AA_History.DTO.ClueCreateRequest;
import com.qjprojects.AA_History.DTO.ClueMapper;
import com.qjprojects.AA_History.DTO.ClueResponse;
import com.qjprojects.AA_History.Entity.Clue;
import com.qjprojects.AA_History.Service.ClueService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clues")
public class ClueController {

    private final ClueService clueService;

    public ClueController(ClueService clueService) {
        this.clueService = clueService;
    }



    @GetMapping("/puzzle/{puzzleId}")
    public List<ClueResponse> getCluesForPuzzle(@PathVariable String puzzleId) {
        return clueService.getCluesForPuzzle(puzzleId)
                .stream()
                .map(ClueMapper::toResponse)
                .toList();
    }
}