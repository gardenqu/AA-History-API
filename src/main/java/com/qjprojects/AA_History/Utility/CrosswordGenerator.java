package com.qjprojects.AA_History.Utility;

import com.qjprojects.AA_History.DTO.ClueData;

import java.util.*;

public class CrosswordGenerator {

    private final int gridSize;
    private final String[][] grid;
    private final List<ClueData> clues = new ArrayList<>();
    private int nextClueNumber = 1;

    public CrosswordGenerator(int gridSize) {
        this.gridSize = gridSize;
        this.grid = new String[gridSize][gridSize];
        for (String[] row : grid) Arrays.fill(row, "");
    }

    public boolean generate(List<Map.Entry<String, String>> wordCluePairs) {
        List<Map.Entry<String, String>> shuffled = new ArrayList<>(wordCluePairs);
        Collections.shuffle(shuffled);

        for (int i = 0; i < shuffled.size(); i++) {
            String word = shuffled.get(i).getKey().toUpperCase();
            String clue = shuffled.get(i).getValue();

            ClueData placed;
            if (i == 0) {
                int row = gridSize / 2;
                int col = (gridSize - word.length()) / 2;
                placed = placeWordHorizontally(word, row, col, clue);
            } else {
                placed = tryPlaceWord(word, clue);
            }

            if (placed != null) {
                clues.add(placed);
            }
        }

        return !clues.isEmpty();
    }

    public String[][] getGrid() {
        // Fill empty cells with "#"
        String[][] result = new String[gridSize][gridSize];
        for (int r = 0; r < gridSize; r++) {
            for (int c = 0; c < gridSize; c++) {
                result[r][c] = grid[r][c].isEmpty() ? "#" : grid[r][c];
            }
        }
        return result;
    }

    public List<ClueData> getClues() {
        return clues;
    }

    private boolean isEmptyOrOut(int r, int c) {
        return r < 0 || r >= gridSize || c < 0 || c >= gridSize || grid[r][c].isEmpty();
    }

    private ClueData placeWordHorizontally(String word, int row, int col, String clue) {
        if (col < 0 || col + word.length() > gridSize) return null;
        if (!isEmptyOrOut(row, col - 1)) return null;
        if (!isEmptyOrOut(row, col + word.length())) return null;

        for (int i = 0; i < word.length(); i++) {
            String cell = grid[row][col + i];
            String letter = String.valueOf(word.charAt(i));
            if (!cell.isEmpty() && !cell.equals(letter)) return null;
            if (cell.isEmpty()) {
                if (!isEmptyOrOut(row - 1, col + i)) return null;
                if (!isEmptyOrOut(row + 1, col + i)) return null;
            }
        }

        Integer existingNumber = getClueNumberAt(row, col);
        int actualClueNumber = existingNumber != null ? existingNumber : nextClueNumber;

        for (int i = 0; i < word.length(); i++) {
            grid[row][col + i] = String.valueOf(word.charAt(i));
        }

        if (existingNumber == null) {
            setClueNumberAt(row, col, nextClueNumber);
            nextClueNumber++;
        }

        return new ClueData(actualClueNumber, "Across", clue, word, row, col);
    }

    private ClueData placeWordVertically(String word, int baseRow, int baseCol,
                                         int intersectIndex, String clue) {
        int startRow = baseRow - intersectIndex;
        if (startRow < 0 || startRow + word.length() > gridSize) return null;

        for (int i = 0; i < word.length(); i++) {
            String cell = grid[startRow + i][baseCol];
            String letter = String.valueOf(word.charAt(i));
            if (!cell.isEmpty() && !cell.equals(letter)) return null;
            if (cell.isEmpty()) {
                if (!isEmptyOrOut(startRow + i, baseCol - 1)) return null;
                if (!isEmptyOrOut(startRow + i, baseCol + 1)) return null;
            }
        }

        if (!isEmptyOrOut(startRow - 1, baseCol)) return null;
        if (!isEmptyOrOut(startRow + word.length(), baseCol)) return null;

        Integer existingNumber = getClueNumberAt(startRow, baseCol);
        int actualClueNumber = existingNumber != null ? existingNumber : nextClueNumber;

        for (int i = 0; i < word.length(); i++) {
            grid[startRow + i][baseCol] = String.valueOf(word.charAt(i));
        }

        if (existingNumber == null) {
            setClueNumberAt(startRow, baseCol, nextClueNumber);
            nextClueNumber++;
        }

        return new ClueData(actualClueNumber, "Down", clue, word, startRow, baseCol);
    }

    private ClueData tryPlaceWord(String word, String clue) {
        List<int[]> intersections = findIntersections(word);
        for (int[] intersection : intersections) {
            int row = intersection[0];
            int col = intersection[1];
            int index = intersection[2];

            ClueData v = placeWordVertically(word, row, col, index, clue);
            if (v != null) return v;

            ClueData h = placeWordHorizontally(word, row, col - index, clue);
            if (h != null) return h;
        }
        return null;
    }

    private List<int[]> findIntersections(String word) {
        List<int[]> intersections = new ArrayList<>();
        for (int r = 0; r < gridSize; r++) {
            for (int c = 0; c < gridSize; c++) {
                if (grid[r][c].isEmpty()) continue;
                int index = word.indexOf(grid[r][c]);
                if (index != -1) intersections.add(new int[]{r, c, index});
            }
        }
        return intersections;
    }

    // Track clue numbers separately
    private final Map<String, Integer> clueNumbers = new HashMap<>();

    private Integer getClueNumberAt(int row, int col) {
        return clueNumbers.get(row + "," + col);
    }

    private void setClueNumberAt(int row, int col, int number) {
        clueNumbers.put(row + "," + col, number);
    }
}