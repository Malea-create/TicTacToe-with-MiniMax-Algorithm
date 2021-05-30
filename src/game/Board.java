package game;

import static game.Mark.*;

public class Board {

    private final Mark[][] board;
    private Mark winningMark;
    private final int boardWidth = 3;
    private boolean crossTurn, gameOver;
    private int availableMoves = boardWidth * boardWidth;


    public Board() {
        board = new Mark[boardWidth][boardWidth];
        /*
        * Anfangsspieler
        */
        crossTurn = true;
        gameOver = false;
        winningMark = BLANK;
        initialiseBoard();
    }

    private void initialiseBoard() {
        for (int row = 0; row < boardWidth; row++) {
            for (int col = 0; col < boardWidth; col++) {
                board[row][col] = BLANK;
            }
        }
    }

    public boolean placeMark(int row, int col) {
        if (row < 0 || row >= boardWidth || col < 0 || col >= boardWidth || isTileMarked(row, col) || gameOver) {
            return false;
        }
        availableMoves--;
        board[row][col] = crossTurn ? X : O;
        togglePlayer();
        checkWin(row, col);
        return true;
    }

    private void checkWin(int row, int col) {
        int rowSum = 0;
        // Check row for winner.
        for (int c = 0; c < boardWidth; c++) {
            rowSum += getMarkAt(row, c).getMark();
        }
        if (calcWinner(rowSum) != BLANK) {
            System.out.println(winningMark + " wins on row " + row);
            return;
        }

        // Check column for winner.
        rowSum = 0;
        for (int r = 0; r < boardWidth; r++) {
            rowSum += getMarkAt(r, col).getMark();
        }
        if (calcWinner(rowSum) != BLANK) {
            System.out.println(winningMark + " wins on column " + col);
            return;
        }

        // Top-left to bottom-right diagonal.
        rowSum = 0;
        for (int i = 0; i < boardWidth; i++) {
            rowSum += getMarkAt(i, i).getMark();
        }
        if (calcWinner(rowSum) != BLANK) {
            System.out.println(winningMark + " wins on the top-left to "
                    + "bottom-right diagonal");
            return;
        }

        // Top-right to bottom-left diagonal.
        rowSum = 0;
        int indexMax = boardWidth - 1;
        for (int i = 0; i <= indexMax; i++) {
            rowSum += getMarkAt(i, indexMax - i).getMark();
        }
        if (calcWinner(rowSum) != BLANK) {
            System.out.println(winningMark + " wins on the top-right to "
                    + "bottom-left diagonal.");
            return;
        }

        if (!anyMovesAvailable()) {
            gameOver = true;
            System.out.println("Tie!");
        }
    }

    private Mark calcWinner(int rowSum) {
        int Xwin = X.getMark() * boardWidth;
        int Owin = O.getMark() * boardWidth;
        if (rowSum == Xwin) {
            gameOver = true;
            winningMark = X;
            return X;
        } else if (rowSum == Owin) {
            gameOver = true;
            winningMark = O;
            return O;
        }
        return BLANK;
    }

    private void togglePlayer() {
        crossTurn = !crossTurn;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean anyMovesAvailable() {
        return availableMoves > 0;
    }

    public Mark getMarkAt(int row, int col) {
        return board[row][col];
    }

    public boolean isTileMarked(int row, int col) {
        return board[row][col].isMarked();
    }

    public void setMarkAt(int row, int col, Mark newMark) {
        board[row][col] = newMark;
    }

    @Override
    public String toString() {
        StringBuilder strBldr = new StringBuilder();
        for (Mark[] row : board) {
            for (Mark tile : row) {
                strBldr.append(tile).append(' ');
            }
            strBldr.append("\n");
        }
        return strBldr.toString();
    }

    public boolean isCrossTurn() {
        return crossTurn;
    }

    public int getWidth() {
        return boardWidth;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public Mark getWinningMark() {
        return winningMark;
    }

}
