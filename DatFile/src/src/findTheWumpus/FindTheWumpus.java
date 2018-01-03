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

	private static GameTile[][] gameBoard;
	private static Random random = new Random();
	private static Scanner userInput = new Scanner(System.in);
	private static String userResponse;
	private static int playerRow, playerCol, torchesFound;
	private static boolean weaponFound, compassFound;

	// also made the methods, but didn't fill it in
	public static void main(String[] args) {
		gameBoard = makeBoard(5, 50, 5);

		// menu();
		// endTurn();
	}

	/**
	 * This method creates a new GameTile[][] with the given parameters, and with
	 * all of the game items spawned in random positions. The game board will always
	 * be a rectangle, the array will never be ragged.
	 * 
	 * @param numRows
	 *            - The number of rows in the game board.
	 * @param numCols
	 *            - The number of columns in the game board.
	 * @param numTorches
	 *            - The number of torches to be spawned.
	 * @return Returns a new GameTile[][] with the specified size, number of
	 *         torches, and with all of the game items spawned.
	 */
	public static GameTile[][] makeBoard(int numRows, int numCols, int numTorches) {
		GameTile[][] newBoard = new GameTile[numRows][numCols];
		boolean wumpusPlaced = false, weaponPlaced = false, playerPlaced = false, compassPlaced = false;
		int torchesPlaced = 0;
		for (int row = 0 ; row < newBoard.length ; row++) {
			for (int col = 0 ; col < newBoard[row].length ; col++) {
				newBoard[row][col] = new GameTile(col, row);
				do {
					switch (random.nextInt(6) + 1) {
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
		do {
			// Display User Options
			System.out.println("Your Turn\n(1) Display Board\n(2) Move");
			if (compassFound) {
				System.out.println("(3) Use Compass");
			} else {
				System.out.println("(3) LOCKED.");
			}
			if (torchesFound > 2) {
				System.out.println("(4) Attack Wumpus");
			} else {
				System.out.println("(4) LOCKED.");
			}
			System.out.print("(5) Exit\nEnter an Option: ");
			// Selects appropriate choice
			try {
				userResponse = userInput.next();
			} catch (InputMismatchException e) {
				System.out.println("Please choose one of the available numerical responses!");
				userInput.next();
				continue;
			}
			switch (userResponse) {
				case "1": {	// Display Board
					displayBoard();
					endTurn();
					break;
				}
				case "2": {	// Move
					move();
					endTurn();
					break;
				}
				case "3": {	// Use Compass
					if (compassFound) {
						useCompass();
						endTurn();
						break;
					} else {
						System.out.println("You have not found the compass yet.");
						continue;
					}
				}
				case "4": {	// Attack Wumpus
					if (torchesFound > 2) {
						attackWumpus();
						endTurn();
						break;
					} else {
						System.out.println("You have not found enough torches yet.");
						continue;
					}
				}
				default: {
					System.out.println("Enter one of the options.");
					continue;
				}
			}
			continue; // Breakpoint
		} while (true);
	}

	public static void endTurn() {
		// fill in the blank
	}

	// methods the menu will call
	public static void displayBoard() {
		for (int row = 0 ; row < gameBoard.length ; row++) {
			for (int col = 0 ; col < gameBoard[row].length ; col++) {
				if (gameBoard[row][col].wumpusHere) {
					System.out.print("W\t");
				} else if (gameBoard[row][col].weaponHere) {
					System.out.print("A\t");
				} else if (gameBoard[row][col].playerHere) {
					System.out.print("M\t");
				} else if (gameBoard[row][col].torchHere) {
					System.out.print("T\t");
				} else if (gameBoard[row][col].compassHere) {
					System.out.print("C\t");
				} else {
					System.out.print("X\t");
				}
			}
			System.out.print("\n");
		}
	}

	public static void move() {
		do {
			System.out.print("Which direction do you want to move?\nNorth, East, South, or West?: ");
			try {
				userResponse = userInput.next();
			} catch (InputMismatchException e) {
				System.out.println("Mismatch Exception");
				userInput.next();
				continue;
			}
			switch (userResponse) {
				case "north": {
					gameBoard[playerRow][playerCol].playerHere = false;
					gameBoard[playerRow][--playerCol].playerHere = true;
					endTurn();
					break;
				}
				case "east": {
					gameBoard[playerRow][playerCol].playerHere = false;
					gameBoard[++playerRow][playerCol].playerHere = true;
					endTurn();
					break;
				}
				case "south": {
					gameBoard[playerRow][playerCol].playerHere = false;
					gameBoard[playerRow][++playerCol].playerHere = true;
					endTurn();
					break;
				}
				case "west": {
					gameBoard[playerRow][playerCol].playerHere = false;
					gameBoard[--playerRow][playerCol].playerHere = true;
					endTurn();
					break;
				}
				default: {
					System.out.println("Error! Choose a direction you idiot.");
					endTurn();
					continue;
				}
			}
			break;
		} while (true);
		endTurn();
	}

	public static void useCompass() {
		// fill in the blank
	}

	public static void attackWumpus() {
		// fill in the blank
	}
}
