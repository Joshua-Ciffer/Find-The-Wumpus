
package src.findTheWumpus;

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

	// also made the methods, but didn't fill it in
	public static void main(String[] args) {
		menu();
		endTurn();
	}

	public static GameTile[][] makeBoard(int numRows,int numCols) {
		GameTile[][] newBoard = new GameTile[numRows][numCols];
		for (int row = 0; row < newBoard.length; row++) {
			for (int col = 0; col < newBoard[row].length; col++) {
				
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
