package piccross;

import java.util.Arrays;

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
	/*	gameGrid  = new int[5][5];
		HHints = new String[5];
		VHints = new String[5];
		numHints = (int)(Math.random() * 25);
		for(int i = 0; i < numHints;i++) {
			int x = (int)(Math.random() * 25);
			if(gameGrid[x/5][x%5] == 0 ) {
				gameGrid[x/5][x%5] = 1;
			}
			
		}	*/
	}
	
	public void newGame() {
		gameGrid  = new int[5][5];
		HHints = new String[5];
		VHints = new String[5];
		numHints = (int)(Math.random() * 25);
		for(int i = 0; i < numHints;i++) {
			int x = (int)(Math.random() * 25);
			if(gameGrid[x/5][x%5] == 0 ) {
				gameGrid[x/5][x%5] = 1;
				trueNumHints++;
			}
			
		}
		this.generateHints();
		this.resetScore();
		this.resetChecks();
	}
	
	public void resetGame() {
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
		System.out.println("HHints");
		for(int i = 0; i < 5; i++) {
			HHints[i] = setHHints(gameGrid[i]);
			System.out.print(HHints[i]);
			System.out.println();
		}
		System.out.println("VHints");
		for(int i = 0; i < 5; i++) {
			VHints[i] = setVHints(gameGrid,i);
			System.out.print(VHints[i]);
			System.out.println();
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
	 * @param check
	 * @return
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
	 * @param check
	 * @return
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
	 * @param index
	 * @return
	 */
	public String getHHints(int index) {
		return HHints[index];
	}
	
	/**
	 * 
	 */
	public void resetCheckGrid() {
		checkGrid = new int[5][5];
	}
	
	/**
	 * 
	 */
	public void clickedGrid(int index) {
		checkGrid[index%5][index/5] = 1;
	}
	
	/**
	 * 
	 */
	public int isClicked(int index) {
		return checkGrid[index%5][index/5];	
	}
	/**Returns the VHint at index
	 * 
	 * @param index
	 * @return
	 */
	public String getVHints(int index) {
		return VHints[index];
	}
	/** Prints out the game grid 
	 * 
	 */
	public void printGrid() {
		System.out.println(Arrays.deepToString(gameGrid));
	}
	
	private void updateScore() {
		score++;
	}
	
	public void updateCheck() {
		totalChecks++;
		System.out.println(totalChecks);
	}
	
	public int getScore() {
		return score;
	}
	public void resetScore() {
		score = 0;
	}
	
	public void resetChecks() {
		totalChecks = 0;
	}
	
	public boolean checkOver() {
		boolean check = false;
		if(totalChecks == 25) {
			check = true;
		}
		return check;
	}
	
	public boolean checkWin() {
		boolean check = false;
		if(score == trueNumHints) {
			check = true;
		}
		return check;
	}
	
	public boolean getGameOver() {
		return gameOver;
	}
	public void setGameOver() {
		gameOver = true;
	}
	public void resetGameOver() {
		gameOver = false;
	}
	public boolean getMistake() {
		return mistake;
	}
	public void setMistake() {
		mistake = true;
	}
	public void resetMistake() {
		mistake = false;
	}
	
	public void debug1() {
		gameGrid = new int[5][5];
		this.resetGame();
		this.generateHints();
		
	}
	public void debug2() {
		numHints = 25;
		for(int i = 0; i < numHints;i++) {
			gameGrid[i/5][i%5] = 1;
		}
		this.resetGame();
		this.generateHints();
	}
	public void debug3() {
		this.resetGame();	
		this.generateHints();
	}		
}
