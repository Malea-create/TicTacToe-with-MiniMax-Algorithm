package ai;

import java.util.concurrent.ThreadLocalRandom;
import game.Board;

public class Random {

    public Random() {
    }

    /**
     * Creates an allowed random move.
     * @param board Board for checking the individual places for free
     * @return Coordinates of best move
     */
    public static int[] randomMove(Board board) {
        int[] rndMove = new int[]{0, 0};
        int boardWidth = board.getWidth(); //Größe des TikTacToe Feldes hohlen
        int row = ThreadLocalRandom.current().nextInt(0, boardWidth); //Initiale Zufallszahlen zwischen 0 und der Feldgröße erstellen
        int col = ThreadLocalRandom.current().nextInt(0, boardWidth); //Initiale Zufallszahlen zwischen 0 und der Feldgröße erstellen
        while (board.isTileMarked(row, col)) { //Freies Feld
            row = ThreadLocalRandom.current().nextInt(0, boardWidth); //Zufallszahlen zwischen 0 und der Feldgröße erstellen
            col = ThreadLocalRandom.current().nextInt(0, boardWidth); //Zufallszahlen zwischen 0 und der Feldgröße erstellen
        }
        rndMove[0] = row;
        rndMove[1] = col;
        return rndMove;
    }
}


