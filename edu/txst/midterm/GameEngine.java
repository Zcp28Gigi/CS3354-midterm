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

	/**
	 * Creates a game engine using the given board.
	 * It locates the player and the exit when the engine starts.
	 *
	 * @param board the board used for the game
	 */
	public GameEngine(Board board) {
		this.board = board;
		findPlayer();
		findExit();
	}

	/**
	 * Checks whether the player has reached the exit.
	 *
	 * @return true if the player is on the exit position, false otherwise
	 */
	public boolean playerWins() {
		return false;
	}

	/**
	 * Finds the player's starting position on the board.
	 */
	private void findPlayer() {
		for (int r = 0; r < 5; r++) {
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
		for (int r = 0; r < 5; r++) {
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
	public void movePlayer(int dRow, int dCol) {
		int targetRow = playerRow + dRow;
		int targetCol = playerCol + dCol;
		int targetCell = board.getCell(targetRow, targetCol);

		// Check for Walls or Out of Bounds
		if (targetCell == WALL || targetCell == -1) {
			return; // Movement blocked
		}

		// Move the Player
		// Current position becomes Floor (or Goal if player was standing on one)
		// Note: For simplicity, this engine assumes player replaces the cell.
		// If you want "Player on Goal", you'd add a 6th constant.
		board.setCell(playerRow, playerCol, FLOOR);

		playerRow = targetRow;
		playerCol = targetCol;
		board.setCell(playerRow, playerCol, PLAYER);

	}
}
