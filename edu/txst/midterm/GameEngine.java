package edu.txst.midterm;

/**
 * Controls the maze game logic.
 * This class keeps track of the board, the player's position,
 * and the exit position. It also handles player movement.
 */
public class GameEngine {
	private Board board;
	private int playerRow;
	private int playerCol;
	private int exitRow;
	private int exitCol;

	// Cell Type Constants
	private static final int FLOOR = 0;
	private static final int WALL = 1;
	private static final int COIN = 2;
	private static final int EXIT = 5;
	private static final int PLAYER = 6;
	private int coinsCollected = 0;

	public int getCoinsCollected() {
		return coinsCollected;
	}

	public GameEngine(Board board) {
		this.board = board;
		findPlayer();
		findExit();
	}

	/**
	 * Checks if the player has reached the exit.
	 * 
	 * @return true if player is on the exit cell, false otherwise
	 */
	public boolean playerWins() {
		return playerRow == exitRow && playerCol == exitCol;
	}

	/**
	 * Finds the player's starting position on the board.
	 */
	private void findPlayer() {
		for (int r = 0; r < 6; r++) {
			for (int c = 0; c < 10; c++) {
				if (board.getCell(r, c) == PLAYER) {
					playerRow = r;
					playerCol = c;
					return;
				}
			}
		}
	}

	/**
	 * Finds the exit position on the board.
	 */
	private void findExit() {
		for (int r = 0; r < 6; r++) {
			for (int c = 0; c < 10; c++) {
				if (board.getCell(r, c) == EXIT) {
					exitRow = r;
					exitCol = c;
					return;
				}
			}
		}
	}

	/**
	 * Attempts to move the player by the given row and column change.
	 * Movement is blocked if the target cell is a wall or out of bounds.
	 *
	 * @param dRow change in row
	 * @param dCol change in column
	 */
	public boolean movePlayer(int dRow, int dCol) {
		int targetRow = playerRow + dRow;
		int targetCol = playerCol + dCol;
		int targetCell = board.getCell(targetRow, targetCol);

		if (targetCell == WALL || targetCell == -1) {
			return false; // ❌ no move
		}

		if (targetCell == COIN) {
			coinsCollected++;
		}

		board.setCell(playerRow, playerCol, FLOOR);

		playerRow = targetRow;
		playerCol = targetCol;
		board.setCell(playerRow, playerCol, PLAYER);

		return true; // ✅ moved
	}
}
