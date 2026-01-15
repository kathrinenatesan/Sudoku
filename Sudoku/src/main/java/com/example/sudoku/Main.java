package com.example.sudoku;

// This class handles all the various scenes and the stage that the
// user sees and interacts with. It contains the setup for
// the opening screen and the sudoku puzzle screen. It sets
// up the cells in the puzzle, the borders, and the various buttons.

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {
    Button easyButton;
    Button mediumButton;
    Button hardButton;
    Button backButton;
    Scene opening;
    Scene grid;
    BorderPane borderPane;
    GridPane gridPane;
    HBox topBar;
    HBox bottomBar;
    Label errorLabel;
    Button solveButton;
    Button restartButton;
    private TextField[][] cells = new TextField[9][9];
    // stores the current number of errors
    int errors;
    // stores the current puzzle + solved current puzzle
    int[][] currPuzzle;
    int[][] solvedPuzzle;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        // set blank opening screen
        Group layout = new Group();
        opening = new Scene(layout, 500, 500, Color.MISTYROSE);
        stage.setTitle("Sudoku");
        stage.setResizable(false);

        // set background + text for opening screen
        Text text = new Text();
        text.setText("Welcome to Sudoku!");
        text.setX(80);
        text.setY(70);
        text.setFont(Font.font("Georgia", FontWeight.BOLD, 30));
        text.setFill(Color.MIDNIGHTBLUE);
        layout.getChildren().add(text);

        // making easy button for opening screen
        easyButton = new Button();
        easyButton.setText("EASY");
        easyButton.setLayoutX(170);
        easyButton.setLayoutY(125);
        easyButton.setPrefSize(150,50);
        easyButton.setFont(Font.font("Georgia", 30));
        easyButton.setTextFill(Color.MIDNIGHTBLUE);
        layout.getChildren().add(easyButton);

        // making medium button for opening screen
        mediumButton = new Button();
        mediumButton.setText("MEDIUM");
        mediumButton.setLayoutX(170);
        mediumButton.setLayoutY(200);
        mediumButton.setPrefSize(150,50);
        mediumButton.setFont(Font.font("Georgia", 20));
        mediumButton.setTextFill(Color.MIDNIGHTBLUE);
        layout.getChildren().add(mediumButton);

        // making hard button for opening screen
        hardButton = new Button();
        hardButton.setText("HARD");
        hardButton.setLayoutX(170);
        hardButton.setLayoutY(275);
        hardButton.setPrefSize(150,50);
        hardButton.setFont(Font.font("Georgia", 30));
        hardButton.setTextFill(Color.MIDNIGHTBLUE);
        layout.getChildren().add(hardButton);


        // make the 2nd scene (grid scene)

        // top bar for 2nd scene (back button and error counters)
        topBar = new HBox(20);
        topBar.setPadding(new Insets(10));
        topBar.setAlignment(Pos.CENTER_LEFT);
        backButton = new Button("Back");
        backButton.setFont(Font.font("Georgia", 20));
        backButton.setTextFill(Color.MIDNIGHTBLUE);

        // backButton action - goes back to opening screen if clicked on + resets current puzzle
        backButton.setOnAction(e -> {
            currPuzzle = null;
            solvedPuzzle = null;

            stage.setScene(opening);
        });
        errorLabel = new Label("Errors: 0");
        errorLabel.setFont(Font.font("Georgia", 20));
        errorLabel.setTextFill(Color.MIDNIGHTBLUE);
        topBar.getChildren().addAll(backButton, errorLabel);

        // grid pane
        gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(0);
        gridPane.setVgap(0);

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                TextField cell = new TextField();
                cell.setPrefSize(50, 50);
                cell.setStyle("-fx-font-size: 18; -fx-alignment: center;");

                // ensures user can only type integers 1-9
                cell.setTextFormatter(new TextFormatter<>(change -> {
                    String userText = change.getControlNewText();
                    if (userText.matches("[1-9]?")) {
                        return change;
                    } else {
                        return null; // does not update
                    }
                }));
                double top = (row % 3 == 0) ? 3 : 1;
                double left = (col % 3 == 0) ? 3 : 1;
                double bottom = (row == 8) ? 3 : 1;
                double right = (col == 8) ? 3 : 1;

                cell.setBorder(new Border(new BorderStroke(
                        Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK,
                        BorderStrokeStyle.SOLID, BorderStrokeStyle.SOLID,
                        BorderStrokeStyle.SOLID, BorderStrokeStyle.SOLID,
                        CornerRadii.EMPTY,
                        new BorderWidths(top, right, bottom, left),
                        Insets.EMPTY
                )));

                final int finalRow = row;
                final int finalCol = col;
                cell.setOnKeyReleased(e -> {
                    if (cell.getText().isEmpty()) {
                        return;
                    }
                    int userInput = Integer.parseInt(cell.getText());
                    if (userInput != solvedPuzzle[finalRow][finalCol]) {
                        errors++;
                        errorLabel.setText("Errors: " + errors);

                        // cell remains empty because not correct + changes color
                        cell.clear();
                    }
                    else {
                        cell.setStyle("-fx-background-color: white; -fx-alignment: center; -fx-font-size: 18;");
                        // checks if this is the last cell input needed to win (checks if player is done)
                        if (win())
                            showWin();
                    }
                });
                gridPane.add(cell, col, row);
                cells[row][col] = cell; // store reference
            }
        }

    // bottom bar for second scene (solve, restart, and new buttons)
        bottomBar = new HBox(20);
        bottomBar.setPadding(new Insets(10));
        bottomBar.setAlignment(Pos.CENTER);
        solveButton = new Button("Solve");
        solveButton.setFont(Font.font("Georgia", 20));
        solveButton.setTextFill(Color.MIDNIGHTBLUE);
        restartButton = new Button("Restart");
        restartButton.setFont(Font.font("Georgia", 20));
        restartButton.setTextFill(Color.MIDNIGHTBLUE);
        bottomBar.getChildren().addAll(solveButton, restartButton);

    // borderPane (to construct the total 2nd scene)
        borderPane = new BorderPane();
        borderPane.setTop(topBar);
        borderPane.setCenter(gridPane);
        borderPane.setBottom(bottomBar);
        grid = new Scene(borderPane, 450, 550);

    // difficulty buttons go to the 2nd scene when clicked on

        // when easy button is clicked
        easyButton.setOnAction(e -> {
            // makes a new puzzle object
            SudokuPuzzle easyPuzzle = new SudokuPuzzle("easy");
            currPuzzle = easyPuzzle.randomPuzzle();
            solvedPuzzle = SudokuSolver.returnSolvedPuzzle(currPuzzle);

            if (currPuzzle == null)
                System.out.println("Puzzle not found");
            else {
                loadPuzzleIntoGrid(currPuzzle);
            }
            // sets error count
            errors = 0;
            errorLabel.setText("Errors: 0");

            stage.setScene(grid);
        });

        // when medium button is clicked
        mediumButton.setOnAction(e -> {
            // makes a new puzzle object
            SudokuPuzzle mediumPuzzle = new SudokuPuzzle("medium");
            currPuzzle = mediumPuzzle.randomPuzzle();
            solvedPuzzle = SudokuSolver.returnSolvedPuzzle(currPuzzle);

            if (currPuzzle == null)
                System.out.println("Puzzle not found");
            else {
                loadPuzzleIntoGrid(currPuzzle);
            }
            // sets error count
            errors = 0;
            errorLabel.setText("Errors: 0");

            stage.setScene(grid);
        });

        // when hard button is clicked
        hardButton.setOnAction(e -> {
            // makes a new puzzle object
            SudokuPuzzle hardPuzzle = new SudokuPuzzle("hard");
            currPuzzle = hardPuzzle.randomPuzzle();
            solvedPuzzle = SudokuSolver.returnSolvedPuzzle(currPuzzle);

            if (currPuzzle == null) {
                System.out.println("Puzzle not found");
            } else {
                loadPuzzleIntoGrid(currPuzzle);
            }
            // sets error count
            errors = 0;
            errorLabel.setText("Errors: 0");

            stage.setScene(grid);
        });

        // if solve button is pressed
        solveButton.setOnAction(e -> {
            if (solvedPuzzle == null) return;

            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    cells[i][j].setText(String.valueOf(solvedPuzzle[i][j]));
                    cells[i][j].setEditable(false);
                }
            }

            // can't restart once solved
            restartButton.setDisable(true);
        });

        stage.setScene(opening);
        stage.show();

        // if restart button is pressed
        restartButton.setOnAction(e -> {
            if (currPuzzle == null) return;

            loadPuzzleIntoGrid(currPuzzle);
            // resets error count
            errors = 0;
            errorLabel.setText("Errors: 0");
        });
    }

    // helper method to load of the puzzle the values into the grid
    private void loadPuzzleIntoGrid(int[][] currPuzzle) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                int num = currPuzzle[i][j];

                if (num != 0) {
                    cells[i][j].setText(String.valueOf(num));
                    cells[i][j].setEditable(false);
                    cells[i][j].setStyle("-fx-background-color: mistyrose; -fx-alignment: center;");
                } else {
                    cells[i][j].setText("");
                    cells[i][j].setEditable(true);
                    cells[i][j].setStyle("-fx-background-color: white; -fx-alignment: center;");
                }
            }
        }
    }

    // helper method to check if we win!
    private boolean win() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                // if it is one of the user input cells
                String text = cells[i][j].getText();
                if (text.isEmpty() || Integer.parseInt(text) != solvedPuzzle[i][j])
                        return false;
            }
        }
        return true;
    }

    // helper method to show win message!!
    private void showWin() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Yay!!");
        alert.setHeaderText("Congrats!");
        alert.setContentText("You solved the puzzle! To solve another one, press 'back'");
        alert.showAndWait();
    }
}


