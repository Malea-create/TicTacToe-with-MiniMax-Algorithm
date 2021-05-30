package ai;

import game.Board;
import static game.Mark.*;

public class MiniMaxAlgorithm {

    private static final int MAX_SEARCH_DEPTH = 9;

    private MiniMaxAlgorithm() {
    }

    /**
     * In this game, we will alternate playing X and O on the board
     * while analyzing the board each time to see what is the highest-value move for X
     * When a terminal node or the maximum search depth has been reached,
     * return the highest value move calculated for X.
     * @param board the board to play on
     * @param depth The maximum depth of the tree to search to
     * @param turnMaxPlayer Maximising or minimising player
     * @return Value of the board
     */
    public static int miniMax(Board board, int depth, boolean turnMaxPlayer) {
        int boardVal = evaluateBoard(board, depth);
        int highVal = Integer.MIN_VALUE;
        int lowVal = Integer.MAX_VALUE;

        // The maximum search depth or a Terminal node has been reached
        if (Math.abs(boardVal) > 0 || depth == 0 || !board.anyMovesAvailable()) {
            return boardVal;
        }
        // Determine the maximum possible value for the maximising player or the minimum possible value for the minimising player depending on the turn
        for (int row = 0; row < board.getWidth(); row++) {
            for (int col = 0; col < board.getWidth(); col++) {
                if (!board.isTileMarked(row, col)) {
                    if (turnMaxPlayer) {
                        board.setMarkAt(row, col, X);
                        highVal = Math.max(highVal, miniMax(board, depth - 1, false));
                    } else {
                        board.setMarkAt(row, col, O);
                        lowVal = Math.min(lowVal, miniMax(board, depth - 1, true));
                    }
                    board.setMarkAt(row, col, BLANK);
                }
            }
        }
        if (turnMaxPlayer) {
            return highVal;
        } else {
            return lowVal;
        }
    }

    /**
     * Getting every available move on the board and returning the best move.
     * @param board Board to evaluate
     * @return Coordinates of best move
     */
    public static int[] aiMove(Board board) {
        int[] bestPossibleMove = new int[]{-1, -1};
        int bestNodeValue = Integer.MIN_VALUE;

        for (int row = 0; row < board.getWidth(); row++) {
            for (int col = 0; col < board.getWidth(); col++) {
                if (!board.isTileMarked(row, col)) {
                    board.setMarkAt(row, col, X);
                    int moveValue = miniMax(board, MAX_SEARCH_DEPTH, false);
                    board.setMarkAt(row, col, BLANK);
                    if (moveValue > bestNodeValue) {
                        bestPossibleMove[0] = row;
                        bestPossibleMove[1] = col;
                        bestNodeValue = moveValue;
                    }
                }
            }
        }
        return bestPossibleMove;
    }

    /**
     * Using the perspective of the X player, evaluate the given board,
     * returning 10 for a win, -10 for a loss, and 0 for a tie
     * The value of a win/loss/draw is weighted according to how many moves it would take
     * to attain it, based on the depth of the board configuration
     * @param board Board to evaluate
     * @param depth depth of the game tree the board configuration is at
     * @return value of the board
     */
    private static int evaluateBoard(Board board, int depth) {
        int evalConstellation = 0;
        int widthBoard = board.getWidth();
        int isXwin = X.getMark() * widthBoard;
        int isOwin = O.getMark() * widthBoard;

        // Check columns for a winner
        evalConstellation = 0;
        for (int col = 0; col < widthBoard; col++) {
            for (int row = 0; row < widthBoard; row++) {
                evalConstellation += board.getMarkAt(row, col).getMark();
            }
            if (evalConstellation == isXwin) {
                return 10 + depth;
            } else if (evalConstellation == isOwin) {
                return -10 - depth;
            }
            evalConstellation = 0;
        }

        // Check rows for a winner
        for (int row = 0; row < widthBoard; row++) {
            for (int col = 0; col < widthBoard; col++) {
                evalConstellation += board.getMarkAt(row, col).getMark();
            }
            if (evalConstellation == isXwin) {
                return 10 + depth;
            } else if (evalConstellation == isOwin) {
                return -10 - depth;
            }
            evalConstellation = 0;
        }

        // Check top-left to bottom-right diagonal for a winner
        evalConstellation = 0;
        for (int i = 0; i < widthBoard; i++) {
            evalConstellation += board.getMarkAt(i, i).getMark();
        }
        if (evalConstellation == isXwin) {
            return 10 + depth;
        } else if (evalConstellation == isOwin) {
            return -10 - depth;
        }

        // Check top-right to bottom-left diagonal for a winner
        evalConstellation = 0;
        int indexMax = widthBoard - 1;
        for (int i = 0; i <= indexMax; i++) {
            evalConstellation += board.getMarkAt(i, indexMax - i).getMark();
        }
        if (evalConstellation == isXwin) {
            return 10 + depth;
        } else if (evalConstellation == isOwin) {
            return -10 - depth;
        }

        return 0; //Returns a Tie
    }
}
