package pgdp.tictactoe;

import pgdp.tictactoe.ai.HumanPlayer;
import pgdp.tictactoe.ai.SimpleAI;

import java.awt.desktop.AboutEvent;
import java.util.*;

public class Game {

    public Field[][] board;
    private PenguAI firstPlayer;
    private PenguAI secondPlayer;
    private boolean[] firstPlayedPieces;
    private boolean[] secondPlayedPieces;
    private boolean turn; // true = first players turn.
    private PenguAI winner;

    public Game(PenguAI first, PenguAI second) {
        board = new Field[3][3];
        this.firstPlayer = first;
        this.secondPlayer = second;

        firstPlayedPieces = new boolean[9];
        secondPlayedPieces = new boolean[9];

        turn = true; // first players turm
        winner = null; // kein sieger
    }

    public PenguAI getWinner() {
        if (this.winner != null) return this.winner;

        Collection<int[]> checkMap = new ArrayList<>();
        checkMap.add(new int[] {0,0, 1,0, 2,0});
        checkMap.add(new int[] {0,1, 1,1, 2,1});
        checkMap.add(new int[] {0,2, 1,2, 2,2});
        checkMap.add(new int[] {0,0, 0,1, 0,2});
        checkMap.add(new int[] {1,0, 1,1, 1,2});
        checkMap.add(new int[] {2,0, 2,1, 2,2});
        checkMap.add(new int[] {0,0, 1,1, 2,2});
        checkMap.add(new int[] {0,2, 1,1, 2,0});

        PenguAI winner = null;
        for (int[] a : checkMap) {
            if (board[a[0]][a[1]] != null && board[a[2]][a[3]] != null && board[a[4]][a[5]] != null) {
                boolean checkup = board[a[0]][a[1]].firstPlayer();
                if (board[a[2]][a[3]].firstPlayer() == checkup && board[a[4]][a[5]].firstPlayer() == checkup) {
                    winner = (checkup ? firstPlayer : secondPlayer);
                }
            }
        }

        return winner;
    }

    public void playGame() {
        while (winner == null) {
            Move m = (turn ? firstPlayer : secondPlayer).makeMove(board, turn, firstPlayedPieces, secondPlayedPieces);

            if (m.x() > 2 || m.y() > 2 || m.x() < 0 || m.y() < 0 || m.value() > 8 || m.value() < 0) {
                winner = (turn ? secondPlayer : firstPlayer);
                break;
            }

            boolean illegal = (turn ? firstPlayedPieces : secondPlayedPieces)[m.value()];   // Stein schon gespielt
            if (board[m.x()][m.y()] != null) {
                if(board[m.x()][m.y()].firstPlayer() == turn) illegal = true;               // Eigener Stein auf Feld
                if(board[m.x()][m.y()].value() >= m.value()) illegal = true;                // Gegnerischer höherer Stein auf Feld
            }

            if (illegal) {
                winner = (turn ? secondPlayer : firstPlayer);
                break;
            }

            // zug schreiben
            board[m.x()][m.y()] = new Field(m.value(), turn);
            (turn ? firstPlayedPieces : secondPlayedPieces)[m.value()] = true;

            // drucken
            printBoard(this.board);
            System.out.println();

            // unentschieden
            if (allTrue(firstPlayedPieces) && allTrue(secondPlayedPieces)) {
                winner = null;
                break;
            }

            // gewinner
            winner = getWinner();
            if (!canNextPlay((turn ? secondPlayedPieces : firstPlayedPieces), !turn)) winner = turn ? firstPlayer : secondPlayer;
            if (winner != null) break;

            //seiten wechseln
            turn = !turn;
        }
    }

    public static boolean allTrue(boolean[] array)
    {
        for(boolean b : array) if(!b) return false;
        return true;
    }

    private boolean canNextPlay(boolean[] pieces, boolean player) {
        Field[] flat = new Field[9];
        int index = 0;
        for (int i = 0; i < 3; i++) {
            for (int a = 0; a < 3; a++) {
                flat[index] = board[i][a];
                index++;
            }
        }

        int maxHave = -1;
        for (int i = 8; i >= 0; i--) {
            if (!pieces[i]) {
                maxHave = i;
                break;
            }
        }
        final int fin = maxHave;

        if (Arrays.stream(flat).anyMatch(Objects::isNull)) return true;
        if (Arrays.stream(flat).filter(field -> field.firstPlayer() == !player).anyMatch(a -> a.value() < fin)) return true;
        return false;
    }

    public static void printBoard(Field[][] board) {
        System.out.println("┏━━━┳━━━┳━━━┓");
        for (int y = 0; y < board.length; y++) {
            System.out.print("┃");
            for (int x = 0; x < board.length; x++) {
                if (board[x][y] != null) {
                    System.out.print(board[x][y] + "┃");
                } else {
                    System.out.print("   ┃");
                }
            }
            System.out.println();
            if (y != board.length - 1) {
                System.out.println("┣━━━╋━━━╋━━━┫");
            }
        }
        System.out.println("┗━━━┻━━━┻━━━┛");
    }

    public static void main(String[] args) {
        PenguAI firstPlayer = new HumanPlayer();
        PenguAI secondPlayer = new SimpleAI();
        Game game = new Game(firstPlayer, secondPlayer);
        game.playGame();
        if(firstPlayer == game.getWinner()) {
            System.out.println("Herzlichen Glückwunsch erster Spieler");
        } else if(secondPlayer == game.getWinner()) {
            System.out.println("Herzlichen Glückwunsch zweiter Spieler");
        } else {
            System.out.println("Unentschieden");
        }
    }
}
