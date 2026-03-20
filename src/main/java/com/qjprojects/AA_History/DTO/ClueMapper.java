package com.qjprojects.AA_History.DTO;


import com.qjprojects.AA_History.Entity.Clue;

public class ClueMapper {

    public static ClueResponse toResponse(Clue clue) {
        return new ClueResponse(
                clue.getClueId(),
                clue.getPuzzle().getPuzzleId(),
                clue.getDirection(),
                clue.getNumber(),
                clue.getClueText(),
                clue.getAnswer()
        );
    }
}
