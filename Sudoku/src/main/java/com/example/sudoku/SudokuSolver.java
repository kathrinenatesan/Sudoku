package com.example.sudoku;
// This class handles the functionality for actually solving a sudoku puzzle when
// passed as a 2D array parameter.
public class SudokuSolver {
    public static int[][] returnSolvedPuzzle(int[][] unsolvedBoard) {
        // creates a new copy of the unsolved board
        int[][] solved = new int[9][9];
        for (int i = 0; i < 9; i++) {
            solved[i] = unsolvedBoard[i].clone(); // Deep copy each row
        }
        // calls the solvePuzzle method on solved
        solvePuzzle(solved);

        return solved;
    }
    private static boolean solvePuzzle(int[][] board) {
        // traverses through the grid
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                // tries all numbers 1-9 in empty spot
                if (board[i][j] == 0) {
                    for (int k = 1; k <= 9; k++) {
                        if (isValid(board, k, i, j)) {
                            board[i][j] = k;

                            // tries solving the rest of the board with this new num attempt (k)
                            if (solvePuzzle(board))
                                // returns true if successfully traverses through entire board
                                return true;
                                // if something returned false, it resets the num attempt (k) and moves on
                            else {
                                board[i][j] = 0;
                            }
                        }
                    }
                    // returns false if none are valid
                    return false;
                }
            }
        }
    return true;
    }

    private static boolean existsInRow(int[][] board, int rowNum, int num) {
        for (int i = 0; i < 9; i++) {
            if (board[rowNum][i] == num)
                return true;
        }
        return false;
    }

    private static boolean existsInColumn(int[][] board, int colNum, int num) {
        for (int i = 0; i < 9; i++) {
            if (board[i][colNum] == num)
                return true;
        }
        return false;
    }

    // check 3 rows and 3 columns (entry before and entry after unless it is on the edge)

    private static boolean existsInBox(int[][] board, int rowNum, int colNum, int num) {
        int boxStartRow = rowNum - rowNum % 3;
        int boxStartCol = colNum - colNum % 3;

        for (int i = boxStartRow; i < boxStartRow + 3; i++) {
            for (int j = boxStartCol; j < boxStartCol + 3; j++)
                if (board[i][j] == num)
                    return true;
        }
        return false;
    }

    private static boolean isValid(int[][] board, int num, int rowNum, int colNum) {
        return !existsInBox(board, rowNum, colNum, num) && !existsInRow(board, rowNum, num)
                && !existsInColumn(board, colNum, num);
    }
}
