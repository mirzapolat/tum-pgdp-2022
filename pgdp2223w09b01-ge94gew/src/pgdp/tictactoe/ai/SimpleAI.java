package pgdp.tictactoe.ai;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Random;

import pgdp.tictactoe.Field;
import pgdp.tictactoe.Move;
import pgdp.tictactoe.PenguAI;

public class SimpleAI extends PenguAI {

    private Random random;

    public SimpleAI() {
        random = new Random();
    }

    @Override
    public Move makeMove(Field[][] board, boolean firstPlayer, boolean[] firstPlayedPieces, boolean[] secondPlayedPieces) {
        State mystate = new State(board, firstPlayer, firstPlayedPieces, secondPlayedPieces);

        Move moving = checkForObviousMove(mystate, firstPlayer);
        if (moving != null) return moving;

        moving = checkForObviousMove(mystate, !firstPlayer);
        if (moving != null) return moving;

        while (true) {
            moving = new Move(random.nextInt(board.length), random.nextInt(board.length), nextValue(firstPlayer ? firstPlayedPieces : secondPlayedPieces));
            if (board[moving.x()][moving.y()] != null) {
                if (board[moving.x()][moving.y()].firstPlayer() == firstPlayer) continue;
                else if (board[moving.x()][moving.y()].value() >= nextValue(firstPlayer ? firstPlayedPieces : secondPlayedPieces)) continue;
            }
            break;
        }

        return moving;
    }

    private int nextValue(boolean[] playerPieces) {
        for (int i = 8; i > 0; i--) {
            if (!playerPieces[i]) return i;
        }
        return -1;
    }

    public boolean hypotheticalWinner(boolean player, Move mov, Field[][] board) {

        Field[][] copy = Arrays.stream(board).map(Field[]::clone).toArray(Field[][]::new);
        copy[mov.x()][mov.y()] = new Field(mov.value(), player);

        Collection<int[]> checkMap = new ArrayList<>();
        checkMap.add(new int[] {0,0, 1,0, 2,0});
        checkMap.add(new int[] {0,1, 1,1, 2,1});
        checkMap.add(new int[] {0,2, 1,2, 2,2});
        checkMap.add(new int[] {0,0, 0,1, 0,2});
        checkMap.add(new int[] {1,0, 1,1, 1,2});
        checkMap.add(new int[] {2,0, 2,1, 2,2});
        checkMap.add(new int[] {0,0, 1,1, 2,2});
        checkMap.add(new int[] {0,2, 1,1, 2,0});

        for (int[] a : checkMap) {
            if (board[a[0]][a[1]] != null && board[a[2]][a[3]] != null && board[a[4]][a[5]] != null) {
                boolean checkup = board[a[0]][a[1]].firstPlayer();
                if (board[a[2]][a[3]].firstPlayer() == checkup && board[a[4]][a[5]].firstPlayer() == checkup) {
                    return true;
                }
            }
        }

        return false;
    }

    private Move checkForObviousMove(State state, boolean player) {
        for (int i = 0; i < 3; i++) {
            for (int a = 0; a < 3; a++) {
                Move tr = new Move(i, a, nextValue(player ? state.firstPlayedPieces : state.secondPlayedPieces));       // Move wird gesetzt
                if (state.board[i][a] != null) {        // wenn das feld nicht leer ist
                    if (state.board[i][a].firstPlayer() == player) continue; // wenn auf dem feld schon ein eigener stein ist
                    else if (state.board[i][a].value() >= nextValue(player ? state.firstPlayedPieces : state.secondPlayedPieces)) continue; // wenn man den stein nicht übertrumpfen kann
                }

                return tr;
                //if (hypotheticalWinner(player, tr, state.board))
            }
        }

        return null;
    }

}

class State {
    public State(Field[][] board, boolean firstPlayer, boolean[] firstPlayedPieces, boolean[] secondPlayedPieces) {
        this.board = board;
        this.firstPlayer = firstPlayer;
        this.firstPlayedPieces = firstPlayedPieces;
        this.secondPlayedPieces = secondPlayedPieces;
    }

    public Field[][] board;
    public boolean firstPlayer;
    public boolean[] firstPlayedPieces;
    public boolean[] secondPlayedPieces;
}
