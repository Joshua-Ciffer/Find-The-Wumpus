package src.findTheWumpus;
import java.util.Random;

/**
 * https://stackoverflow.com/questions/11859534/how-to-calculate-the-total-time-it-takes-for-multiple-threads-to-finish-executin
 * 
 * @author Joshua Ciffer, Brian Williams
 * @version 01/02/2018
 */
public class FindTheWumpus {
	// Thanks for getting ur shit done bud.

	// made dem variables
	boolean foundWeapon = false;
	int torchesFound = 0;
	boolean compassFound = false;
	GameTile[][] board = new GameTile[10][10];
	static Random randoms = new Random();

	// also made the methods, but didn't fill it in
	public static void main(String[] args) {
		menu();
		endTurn();
	}

	public static GameTile[][] makeBoard(int numRows,int numCols) {
		GameTile[][] newBoard = new GameTile[numRows][numCols];
		for (int row = 0; row < newBoard.length; row++) {
			for (int col = 0; col < newBoard[row].length; col++) {
				newBoard[row][col] = new GameTile();
				do {
					switch (randoms.nextInt(7) + 1) {
						case 1: {
							newBoard[row][col].wumpusHere = true;
						}
						case 2: {
							newBoard[row][col].playerHere = true;
						}
						case 3: {
							
						}
						case 4: {
							
						}
						case 5: {
							
						}
						case 6: {
							
						}
					}
				} while (true);
			}
		}
		return newBoard;
	}

	public static void menu() {
		// fill in the blank
	}

	public static void endTurn() {
		// fill in the blank
	}

	// methods the menu will call
	public static void displayBoard() {
		// fill in the blank
	}

	public static void move() {
		// fill in the blank
	}

	public static void useCompass() {
		// fill in the blank
	}

	public static void attacWumpus() {
		// fill in the blank
	}
}
