package piccross;

import java.util.Arrays;

/**This class creates the game logic for the Piccross game.
 * 
 * @author Ryanh
 *
 */
public class GameLogic {
	private int[][] gameGrid;
	private int[][] checkGrid = new int[5][5];
	private String[] HHints;
	private String[] VHints;
	private int numHints;
	private int score = 0;
	private int totalChecks = 0;
	private int trueNumHints = 0;
	private boolean gameOver = false;
	private boolean mistake = false;



	/**Constructor to initialize a game instance
	 * 
	 */
	public GameLogic() {
	}
	
	/**Creates a new Piccross game.
	 * 
	 */
	public void newGame() {
		gameGrid  = new int[5][5];
		HHints = new String[5];
		VHints = new String[5];
		trueNumHints = 0;
		numHints = 1 + (int)(Math.random() * 24);
		for(int i = 0; i < numHints; i++) {
			int x = (int)(Math.random() * 25);
			if(gameGrid[x/5][x%5] == 0 ) {
				gameGrid[x/5][x%5] = 1;
				trueNumHints++;
			}
		}
		this.generateHints();
		this.resetGame();
	}
	
	/**
	 * Resets the current Piccross game.
	 */
	public void resetGame() {
		this.resetScore();
		this.resetChecks();
		this.resetCheckGrid();
		this.resetGameOver();
		this.resetMistake();
	}
	
	/**
	 * Resets the current Piccross game.
	 */
	public void resetGameFromServer() {
		trueNumHints = 0;
		for(int i = 0; i < gameGrid.length; i++) {
			for(int j = 0; j < gameGrid[i].length;j++) {
				if(gameGrid[i][j] == 1) {
					trueNumHints++;
				}
			}
		}
		this.generateHints();
		this.resetScore();
		this.resetChecks();
		this.resetCheckGrid();
		this.resetGameOver();
		this.resetMistake();
	}
	
	/**Takes a game grid and generates hints for each column.
	 * 
	 * @param gameGrid Takes the gameGrid that has been created.
	 */
	private void generateHints() {
		for(int i = 0; i < 5; i++) {
			HHints[i] = setHHints(gameGrid[i]);
		}
		for(int i = 0; i < 5; i++) {
			VHints[i] = setVHints(gameGrid,i);
		}

	}

	private String setHHints(int[] row) { 
		String x = "";
		int y = 0;
		for(int i = 0; i < 5; i++) {
			if(row[i] == 1) {
				y = y + 1;
			}
			else {
				if(y == 0) {}
				else {
					x = x + y;
					y = 0;
				}
			}
		}
		if(y !=0)
			x = x + y;
		return x;
	}

	private String setVHints(int[][] grid, int column) {
		String x = "";
		int y = 0;
		for(int i = 0; i < 5; i++) {
			if(grid[i][column] == 1) {
				y = y + 1;
			}
			else {
				if(y == 0) {}
				else {
					x = x + y;
					y = 0;
				}
			}
		}
		if(y !=0)
			x = x + y;
		return x;
	}
	/** Checks to see if a grid square is full.
	 * 
	 * @param check Square to check
	 * @return False if grid is invalid and true if valid.
	 */
	public boolean checkGrid(int check) {
		boolean answer = false;
		if(gameGrid[check%5][check/5] == 1) {
			answer = true;
			updateScore();
		}
		return answer;
	}

	/**Checks to see if a grid square is empty
	 * 
	 * @param check Square to check
	 * @return False if grid is invalid and true if valid.
	 */
	public boolean markGrid(int check) {
		boolean answer = false;
		//this.clickedGrid(gameGrid[check%5][check/5]);
		if(gameGrid[check%5][check/5] == 0) {
			answer = true;
		}
		return answer;
	}
	/** Returns the HHint at index
	 * 
	 * @param index Index of Hint
	 * @return The hint
	 */
	public String getHHints(int index) {
		return HHints[index];
	}
	
	/**Returns the VHint at index
	 * 
	 * @param index Index of Hint
	 * @return The hint
	 */
	public String getVHints(int index) {
		return VHints[index];
	}

	/** Resets the check grid.
	 * 
	 */
	public void resetCheckGrid() {
		checkGrid = new int[5][5];
	}

	/**Changes a square in the checked grid to checked. 
	 * 
	 * @param index Index of the click square.
	 */
	public void clickedGrid(int index) {
		checkGrid[index%5][index/5] = 1;
	}

	/**Checks to see if the square being click is already checked.
	 * @param index Index of the click square.
	 * @return The value of the clicked squared
	 */
	public int isClicked(int index) {
		return checkGrid[index%5][index/5];	
	}
	
	private void updateScore() {
		score++;
	}
	
	/**
	 * Updates the number of checks done.
	 */
	public void updateCheck() {
		totalChecks++;
	}
	
	/** Gets current score of the game.
	 * 
	 * @return The current score.
	 */
	public int getScore() {
		return score;
	}
	
	/**
	 * resets the current score to 0.
	 */
	public void resetScore() {
		score = 0;
	}
	
	/**
	 * resets the current number of checks to 0.
	 */
	public void resetChecks() {
		totalChecks = 0;
	}
	
	/** Checks to see if the game is over.
	 * 
	 * @return returns true if the game is over, and false if not.
	 */
	public boolean checkOver() {
		boolean check = false;
		if(totalChecks == 25) {
			check = true;
		}
		return check;
	}
	
	/**Checks to see if the game has been won.
	 * 
	 * @return returns true if the game is won, and false if not.
	 */
	public boolean checkWin() {
		boolean check = false;
		if(score == trueNumHints) {
			check = true;
		}
		return check;
	}
	
	/**Returns game over status.
	 * 
	 * @return game over status.
	 */
	public boolean getGameOver() {
		return gameOver;
	}
	
	/**
	 * Sets game over status to true.
	 */
	public void setGameOver() {
		gameOver = true;
	}
	
	/**Resets the game over status to false.
	 * 
	 */
	public void resetGameOver() {
		gameOver = false;
	}
	
	/**Returns whether the user has made a mistake.
	 * 
	 * @return true if the user has made a mistake and false if they have not.
	 */
	public boolean getMistake() {
		return mistake;
	}
	
	/**
	 * Sets the fact that the user has made a mistake.
	 */
	public void setMistake() {
		mistake = true;
	}
	
	/**
	 * Resets the mistake flag.
	 */
	public void resetMistake() {
		mistake = false;
	}
	/**The smallest game. 1 Hint.
	 * 
	 */
	public void debug1() {
		int x =  1 + (int)(Math.random() * 24);
		trueNumHints = 1;
		gameGrid = new int[5][5];
		gameGrid[x/5][x%5] = 1;

		this.resetGame();
		this.generateHints();

	}

	/**The largest game, all squares are valid
	 * 
	 */
	public void debug2() {
		numHints = 25;
		trueNumHints = 25;
		for(int i = 0; i < numHints;i++) {
			gameGrid[i/5][i%5] = 1;
		}
		this.resetGame();
		this.generateHints();
	}

	/**Every Other square is valid
	 * 
	 */
	public void debug3() {
		trueNumHints = 13;
		gameGrid = new int[5][5];
		gameGrid[0][0] = 1;
		gameGrid[0][2] = 1;
		gameGrid[0][4] = 1;
		gameGrid[4][0] = 1;
		gameGrid[4][2] = 1;
		gameGrid[4][4] = 1;
		gameGrid[2][0] = 1;
		gameGrid[2][4] = 1;
		gameGrid[1][1] = 1;
		gameGrid[1][3] = 1;
		gameGrid[3][1] = 1;
		gameGrid[3][3] = 1;
		gameGrid[2][2] = 1;
		this.resetGame();	
		this.generateHints();
	}		
	
	public int[][] getGame(){
		return Arrays.copyOf(gameGrid,gameGrid.length);
	}
	
	public void setGame(int[][] game) {
		gameGrid = game;
	}
}
