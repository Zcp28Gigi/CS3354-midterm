package edu.txst.midterm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;

/**
 * Graphical user interface for the maze game.
 * This class displays the maze, the game information,
 * and handles user keyboard input.
 */
public class MazeGUI extends JFrame {
	private Board originalBoard;
	private Board currentBoard;
	private GameEngine engine;
	private GamePanel gamePanel;
	private InfoPanel infoPanel;
	private JMenuItem resetItem;
	private int stepCounter;

	/**
	 * Creates the main maze game window and initializes the GUI components.
	 */
	public MazeGUI() {
		setTitle("16-Bit Maze");
		setSize(640, 480); // Adjusted for 10x5 grid with scaling
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		initMenu();

		infoPanel = new InfoPanel();
		gamePanel = new GamePanel();
		add(infoPanel, BorderLayout.NORTH);
		add(gamePanel, BorderLayout.CENTER);

		// Handle Keyboard Input
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {

				if (engine == null)
					return;
				boolean moved = false;

				switch (e.getKeyCode()) {
					case KeyEvent.VK_UP -> moved = engine.movePlayer(-1, 0);
					case KeyEvent.VK_DOWN -> moved = engine.movePlayer(1, 0);
					case KeyEvent.VK_LEFT -> moved = engine.movePlayer(0, -1);
					case KeyEvent.VK_RIGHT -> moved = engine.movePlayer(0, 1);
				}
				// ✅ only update if player actually moved
				if (moved) {
					stepCounter++;
					infoPanel.setInfoSteps(stepCounter);
					infoPanel.setInfoCoins(engine.getCoinsCollected());
				}

				gamePanel.repaint();

				// Check for victory
				if (engine.playerWins()) {
					int score = (infoPanel.getInfoSteps() * -1) + (infoPanel.getInfoCoins() * 5);

					JOptionPane.showMessageDialog(MazeGUI.this,
							"Congratulations! You found the exit.\nYour score: " + score,
							"Level Complete", JOptionPane.INFORMATION_MESSAGE);

					// Optional: Disable engine to prevent movement after win
					engine = null;
					resetItem.setEnabled(false);
				}
			}
		});
	}

	/**
	 * Initializes the game menu bar and its menu items.
	 */
	private void initMenu() {
		JMenuBar menuBar = new JMenuBar();
		JMenu gameMenu = new JMenu("Game");

		JMenuItem openItem = new JMenuItem("Open");
		resetItem = new JMenuItem("Reset");
		resetItem.setEnabled(false); // Disabled by default

		openItem.addActionListener(e -> openFile());
		resetItem.addActionListener(e -> resetGame());

		gameMenu.add(openItem);
		gameMenu.add(resetItem);
		menuBar.add(gameMenu);
		setJMenuBar(menuBar);
	}

	/**
	 * Opens a CSV level file and loads it into the game.
	 */
	private void openFile() {
		JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));
		int result = fileChooser.showOpenDialog(this);
		stepCounter = 0;
		infoPanel.setInfoSteps(stepCounter);

		if (result == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			CSVBoardLoader loader = new CSVBoardLoader();

			// Load and Store
			originalBoard = loader.load(selectedFile.getAbsolutePath());
			currentBoard = originalBoard.clone();
			engine = new GameEngine(currentBoard);

			resetItem.setEnabled(true);
			gamePanel.setBoard(currentBoard);
			gamePanel.repaint();
		}
	}

	/**
	 * Resets the game to its original loaded board.
	 */
	private void resetGame() {
		stepCounter = 0;
		infoPanel.setInfoSteps(stepCounter);
		if (originalBoard != null) {
			currentBoard = originalBoard.clone();
			engine = new GameEngine(currentBoard);
			gamePanel.setBoard(currentBoard);
			gamePanel.repaint();
		}
	}

	/**
	 * Panel that displays the number of steps and coins.
	 */
	private class InfoPanel extends JPanel {
		private JLabel infoSteps;
		private JLabel infoCoins;

		public InfoPanel() {
			this.setLayout(new FlowLayout());
			this.add(new JLabel("Steps: "));
			// infoRemainingSteps is a label which value can be changed using its method
			// called
			// setText
			infoSteps = new JLabel("0");
			this.add(infoSteps);
			this.add(new JLabel("Coins: "));
			// infoCoins is a label which value can be changed using its method called
			// setText
			infoCoins = new JLabel("0");
			this.add(infoCoins);
		}

		/**
		 * Updates the steps label.
		 *
		 * @param remainingSteps the number of steps to display
		 */
		public void setInfoSteps(int remainingSteps) {
			this.infoSteps.setText(Integer.toString(remainingSteps));
		}

		/**
		 * Gets the current steps value shown on the label.
		 *
		 * @return the displayed steps value
		 */
		public int getInfoSteps() {
			return Integer.parseInt(this.infoSteps.getText());
		}

		/**
		 * Updates the coins label.
		 *
		 * @param infoCoins the number of coins to display
		 */
		public void setInfoCoins(int infoCoins) {
			this.infoCoins.setText(Integer.toString(infoCoins));
		}

		/**
		 * Gets the current coins value shown on the label.
		 *
		 * @return the displayed coins value
		 */
		public int getInfoCoins() {
			return Integer.parseInt(this.infoCoins.getText());
		}
	}

	/**
	 * Custom panel used to draw the board.
	 */
	private class GamePanel extends JPanel {
		private Board board;
		private final int TILE_SIZE = 64; // Scale up for visibility

		/**
		 * Sets the board to be displayed.
		 *
		 * @param board the board to draw
		 */
		public void setBoard(Board board) {
			this.board = board;
		}

		/**
		 * Paints the board and all of its tiles.
		 *
		 * @param g the graphics object used for drawing
		 */
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			if (board == null)
				return;

			for (int r = 0; r < 6; r++) {
				for (int c = 0; c < 10; c++) {
					int cell = board.getCell(r, c);
					drawTile(g, cell, c * TILE_SIZE, r * TILE_SIZE);
				}
			}
		}

		/**
		 * Draws a single tile based on its type.
		 *
		 * @param g the graphics object used for drawing
		 * @param type the tile type
		 * @param x the x-coordinate
		 * @param y the y-coordinate
		 */
		private void drawTile(Graphics g, int type, int x, int y) {
			// Placeholder colors until you link the sprite loading logic
			switch (type) {
				case 0 -> g.setColor(Color.LIGHT_GRAY); // Floor
				case 1 -> g.setColor(Color.DARK_GRAY); // Wall
				case 2 -> g.setColor(Color.YELLOW); // Coin
				case 5 -> g.setColor(Color.MAGENTA); // Exit
				case 6 -> g.setColor(Color.BLUE); // Player
				default -> g.setColor(Color.BLACK);
			}
			g.fillRect(x, y, TILE_SIZE, TILE_SIZE);
			g.setColor(Color.WHITE);
			g.drawRect(x, y, TILE_SIZE, TILE_SIZE); // Grid lines
		}
	}

	/**
	 * Starts the maze game application.
	 *
	 * @param args command-line arguments
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new MazeGUI().setVisible(true));
	}
}
