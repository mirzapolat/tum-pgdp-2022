package pgdp.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * class representing the playing board
 * NOTE: IDs and indices of players and figures are off by one (1-based vs 0-based)
 * 
 * you may add any public or private attributes and/or methods to this class
 * if you need them to solve the exercise
 * 
 * you may also rewrite this class if you prefer to solve the exercise
 * with a different approach (see task description for more details)
 */
public class Board {
	public static final int EMPTY = 0;
	private static final int[] START_POSITIONS = { 0, 8, 16, 24 };
	private static final int[] GOAL_POSITIONS = { 31, 7, 15, 23 };

	private int[] boardFields;
	private Figure[][] figures;

	public Board() {
		boardFields = new int[32];
		Arrays.fill(boardFields, EMPTY);
		figures = new Figure[4][3];
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 3; j++) {
				figures[i][j] = new Figure(j);
			}
		}
	}

	/**
	 * returns the boards current value at given position
	 * @param position the position
	 * @return the boards value at position
	 */
	public int getBoardAt(int position) {
		if (position < 0 || position >= boardFields.length) {
			throw new IllegalArgumentException("Invalid value for parameter position: " + position);
		}
		return boardFields[position];
	}

	/**
	 * returns the board position of figure from player
	 * @param player the player [1,4]
	 * @param figure the figure [1,3]
	 * @return the figures position
	 */
	public int getFigurePosition(int player, int figure) {
		if (player < 1 || player > 4) {
			throw new IllegalArgumentException("Invalid value for parameter player: " + player);
		}
		if (figure < 1 || figure > 3) {
			throw new IllegalArgumentException("Invalid value for parameter figure: " + figure);
		}
		return figures[player - 1][figure - 1].position;
	}

	/**
	 * returns whether or not the figure of player is at home
	 * @param player the player [1,4]
	 * @param figure the figure [1,3]
	 * @return whether or not the figure is at home
	 */
	public boolean isFigureAtHome(int player, int figure) {
		if (player < 1 || player > 4) {
			throw new IllegalArgumentException("Invalid value for parameter player: " + player);
		}
		if (figure < 1 || figure > 3) {
			throw new IllegalArgumentException("Invalid value for parameter figure: " + figure);
		}
		return figures[player - 1][figure - 1].isHome;
	}

	/**
	 * returns whether or not the figure of player reached to goal
	 * @param player the player [1,4]
	 * @param figure the figure [1,3]
	 * @return whether or not the figure is at home
	 */
	public boolean isFigureAtGoal(int player, int figure) {
		if (player < 1 || player > 4) {
			throw new IllegalArgumentException("Invalid value for parameter player: " + player);
		}
		if (figure < 1 || figure > 3) {
			throw new IllegalArgumentException("Invalid value for parameter figure: " + figure);
		}
		return figures[player - 1][figure - 1].reachedGoal;
	}

	/**
	 * returns players start position
	 * @param player the player [1,4]
	 * @return the players start position
	 */
	public static int getPlayerStartPosition(int player) {
		if (player <= 0 || player > 4) {
			throw new IllegalArgumentException("Invalid value for parameter player: " + player);
		}
		return START_POSITIONS[player - 1];
	}

	/**
	 * returns players goal position
	 * @param player the player [1,4]
	 * @return the players goal position
	 */
	public static int getPlayerGoalPosition(int player) {
		if (player <= 0 || player > 4) {
			throw new IllegalArgumentException("Invalid value for parameter player: " + player);
		}
		return GOAL_POSITIONS[player - 1];
	}

	public boolean checkFigureOnBoard(int player) {
		boolean onBoard = false;
		for(Figure f : figures[player -1])
			if (!f.isHome && !f.reachedGoal) {
				onBoard = true;
				break;
			}
		return onBoard;
	}

	public List<Integer> atHome(int player) {
		List<Integer> list = new ArrayList<>();
		for(int i = 0; i < 3; i++)
			if (figures[player-1][i].isHome) {
				list.add(i+1);
			}
		return list;
	}

	public int fieldCalcPiece(int player, int figure, int plus) {
		return fieldCalc(figures[player-1][figure-1].position, plus);
	}

	public int fieldCalc(int field, int plus) {
		return (field + plus) % 32;
	}

	public int[] playStartPosition(int player, int figure) {
		return playAndKick(player, figure, START_POSITIONS[player-1]);
	}

	public int[] playAndKick(int player, int figure, int field) {
		if (!figures[player-1][figure-1].isHome && !figures[player-1][figure-1].reachedGoal) {
			boardFields[figures[player-1][figure-1].position] = EMPTY;
		}

		figures[player-1][figure-1].isHome = false;
		figures[player-1][figure-1].position = field;

		int[] ret = null;
		if (boardFields[field] != EMPTY) {
			int removingPlayer = boardFields[field] / 1000;
			int removingPiece = (boardFields[field] % 1000) / 100;
			ret = new int[]{removingPlayer, removingPiece};
			figures[removingPlayer-1][removingPiece-1].isHome = true;
		}
		boardFields[field] = BoardUtility.getBoardValue(player, figure);

		return ret;
	}

	public boolean setToFinish(int player, int figure) {
		figures[player-1][figure-1].isHome = false;
		figures[player-1][figure-1].reachedGoal = true;
		boardFields[figures[player-1][figure-1].position] = EMPTY;

		boolean win = true;
		for (Figure f : figures[player-1]) {
			if (!f.reachedGoal) {
				win = false;
				break;
			}
		}

		return win;
	}

	public void moveFromStart(int player, int figure, int places) {
		int goal = figures[player-1][figure-1].position + places;
		if (boardFields[goal] == EMPTY || boardFields[goal]/1000 != player) {		// passt genau
			int[] kick = playAndKick(player, figure, goal);
			if (kick != null) PinguGame.printStringPublic(21, kick[1], kick[0]);
		}
		else {
			for (int i = goal; i > figures[player-1][figure-1].position; i--) {		// Kleiner gehen
				if (boardFields[i] == EMPTY || boardFields[i]/1000 != player) {
					int[] kick = playAndKick(player, figure, i);
					if (kick != null) {
						PinguGame.printStringPublic(18, i - figures[player-1][figure-1].position);
						PinguGame.printStringPublic(21, kick[1], kick[0]);
					}
					else PinguGame.printStringPublic(17, i - figures[player-1][figure-1].position);
					return;
				}
			}
			for (int i = goal; i < 32; i++) {
				if (boardFields[i] == EMPTY || boardFields[i]/1000 != player) {		// Größer gehen
					int[] kick = playAndKick(player, figure, i);
					if (kick != null) {
						PinguGame.printStringPublic(20, i - figures[player-1][figure-1].position);
						PinguGame.printStringPublic(21, kick[1], kick[0]);
					}
					else PinguGame.printStringPublic(19, i - figures[player-1][figure-1].position);
					return;
				}
			}
		}

	}

	public int[] canMoveToFinish(int player, int dice) {
		List<Integer> results = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			if (!figures[player-1][i].isHome && !figures[player-1][i].reachedGoal)
				if (GOAL_POSITIONS[player-1] - figures[player-1][i].position <= dice)
					results.add(i+1);
		}

		if (results.isEmpty()) return new int[0];
		else return results.stream().mapToInt(value -> value).toArray();
	}

	public int[] canMoveAndThrow(int player, int dice) {
		List<Integer> results = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			if (!figures[player-1][i].isHome && !figures[player-1][i].reachedGoal){
				int goal = fieldCalc(figures[player-1][i].position, dice);
				if (boardFields[goal] != EMPTY) {
					if (boardFields[goal] / 1000 != player) {
						results.add(i+1);
					}
				}
			}
		}

		if (results.isEmpty()) return new int[0];
		else return results.stream().mapToInt(value -> value).toArray();
	}

	public int[] canMoveNormal(int player, int dice) {
		List<Integer> results = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			if (!figures[player-1][i].isHome && !figures[player-1][i].reachedGoal) {
				int goal = fieldCalc(figures[player-1][i].position, dice);
				if (boardFields[goal] == EMPTY) {
					results.add(i+1);
				}
			}
		}

		if (results.isEmpty()) return new int[0];
		else return results.stream().mapToInt(value -> value).toArray();
	}

	/**
	 * private class to store information about a figure
	 */
	private static class Figure {
		boolean isHome;
		boolean reachedGoal;
		int position;

		Figure(int homePosition) {
			position = homePosition;
			isHome = true;
			reachedGoal = false;
		}
	}
}
