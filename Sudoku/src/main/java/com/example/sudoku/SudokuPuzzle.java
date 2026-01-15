package com.example.sudoku;

public class SudokuPuzzle {
    String difficulty;

    SudokuPuzzle(String difficulty) {
        this.difficulty = difficulty;
    }

    // returns a random unsolved puzzle from the puzzle bank's hashmaps for each difficulty level
    public int[][] randomPuzzle() {
        if (difficulty.equals("easy")) {
            int key = (int)(Math.random() * 10) + 1;
            return  PuzzleBank.easyPuzzles.get(key);
        }
        else if (difficulty.equals("medium")) {
            int key = (int)(Math.random() * 10) + 1;
            return  PuzzleBank.mediumPuzzles.get(key);
        }
        else { // difficulty is hard
            int key = (int)(Math.random() * 10) + 1;
            return  PuzzleBank.hardPuzzles.get(key);
        }
    }
}
