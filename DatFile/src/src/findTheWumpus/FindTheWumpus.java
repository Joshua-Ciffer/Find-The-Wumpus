package src.findTheWumpus;
import java.util.Random;
import java.util.Scanner;
import java.util.InputMismatchException;

/**
 * https://stackoverflow.com/questions/11859534/how-to-calculate-the-total-time-it-takes-for-multiple-threads-to-finish-executin
 * 
 * @author Joshua Ciffer, Brian Williams
 * @version 01/02/2018
 */
public class FindTheWumpus {
	// Thanks for getting ur shit done bud.

	// made dem variables
	static boolean foundWeapon = false;
	static int torchesFound = 0;
	static boolean compassFound = false;
	static GameTile[][] board = new GameTile[10][10];
	static Random randoms = new Random();
	static int intResponse;
	static String response;
	static int playerRow;
	static int playerCol;

	// also made the methods, but didn't fill it in
	public static void main(String[] args) {
		board = makeBoard(5, 50, 5);
		
//		menu();
//		endTurn();
	}

		/**
	 * This method creates a new GameTile[][] with the given parameters, and with all of the game items spawned in random positions.  The game board will always be a rectangle, the array will never be ragged. 
	 * 
	 * @param numRows - The number of rows in the game board.
	 * @param numCols - The number of columns in the game board.
	 * @param numTorches - The number of torches to be spawned.
	 * @return Returns a new GameTile[][] with the specified size, number of torches, and with all of the game items spawned.
	 */
	public static GameTile[][] makeBoard(int numRows, int numCols, int numTorches) {
		GameTile[][] newBoard = new GameTile[numRows][numCols];
		boolean wumpusPlaced = false, weaponPlaced = false, playerPlaced = false, compassPlaced = false;
		int torchesPlaced = 0;
		for (int row = 0 ; row < newBoard.length ; row++) {
			for (int col = 0 ; col < newBoard[row].length ; col++) {
				newBoard[row][col] = new GameTile(col, row);
				do {
					switch (randoms.nextInt(6) + 1) {
						case 1: { // Generates Wumpus
							if (wumpusPlaced) {
								continue;
							} else {
								newBoard[row][col].wumpusHere = true;
								wumpusPlaced = true;
								break;
							}
						}
						case 2: { // Generates Weapon
							if (weaponPlaced) {
								continue;
							} else {
								newBoard[row][col].weaponHere = true;
								weaponPlaced = true;
								break;
							}
						}
						case 3: { // Generates Player
							if (playerPlaced) {
								continue;
							} else {
								newBoard[row][col].playerHere = true;
								playerPlaced = true;
								playerRow = row;
								playerCol = col;
								break;
							}
						}
						case 4: { // Generates Torch
							if (torchesPlaced == numTorches) {
								continue;
							} else {
								newBoard[row][col].torchHere = true;
								torchesPlaced++;
								break;
							}
						}
						case 5: { // Generates Compass
							if (compassPlaced) {
								continue;
							} else {
								newBoard[row][col].compassHere = true;
								compassPlaced = true;
								break;
							}
						}
					}
					break;
				} while (true);
			}
		}
		return newBoard;
	}

	public static void menu() {
			while (true) {
			// Display User Options
			System.out.println("Your Turn");
			System.out.println("1. Display Board\n2. Move");
			if (compassFound == false) {
				System.out.println("3. LOCKED");
			} else {
				System.out.println("3. Use Compass");
			}
			if (torchesFound < 2) {
				System.out.println("4. LOCKED");
			} else {
				System.out.println("4. Attack Wumpus");
			}
			// Selects appropiate choice
			try {
				intResponse = myScan.nextInt();
			} catch (InputMismatchException e) {
				System.out.println("Please choose one of the available numerical responses1");
			}
			if(intResponse == 1){
				displayBoard();
			}else if(intResponse == 2){
				System.out.println("Which Direction would you like to move?");
				response = myScan.nextLine();
				switch (response.toLowerCase()) {
					case "NORTH":
						move("north");
						myScan.nextLine();
					case "SOUTH":
						move("sout");
						myScan.nextLine();
					case "EAST":
						move("east");
						myScan.nextLine();
					case "WEST":
						move("west");
						myScan.nextLine();
				}
			}else if(intResponse == 3){
				if(compassFound == false){
					System.out.println("This response is locked");
				}
			}else if(intResponse == 4){
			}else{
			
				System.out.println("Please choose one of the available numerical responses3");
			}
		}
	}

	public static void endTurn() {
		// fill in the blank
	}

	// methods the menu will call
	public static void displayBoard() {
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[row].length; col++) {
				if (board[row][col].wumpusHere) {
					System.out.print("W\t");
				} else if (board[row][col].weaponHere) {
					System.out.print("A\t");
				} else if (board[row][col].playerHere) {
					System.out.print("M\t");
				} else if (board[row][col].torchHere) {
					System.out.print("T\t");
				} else if (board[row][col].compassHere) {
					System.out.print("C\t");
				} else {
					System.out.print("X\t");
				}
			}
			System.out.print("\n");
		}
	}

	public static void move(String direction) {
		switch (direction.toLowerCase()) {
			case "north": {
				
				endTurn();
				break;
			}
			case "east": {
				
				endTurn();
				break;
			}
			case "south": {
				
				endTurn();
				break;
			}
			case "west": {
				
				endTurn();
				break;
			}
			default: {
				System.out.println("Error! Choose a direction you idiot.");
				endTurn();
				break;
			}
		}
	}

	public static void useCompass() {
		// fill in the blank
	}

	public static void attacWumpus() {
		// fill in the blank
	}
}
