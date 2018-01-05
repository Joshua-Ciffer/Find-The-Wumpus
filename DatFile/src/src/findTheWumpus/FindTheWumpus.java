package src.findTheWumpus;
import java.util.Random;
import java.util.Scanner;
import java.util.InputMismatchException;

/**
 * https://stackoverflow.com/questions/11859534/how-to-calculate-the-total-time-it-takes-for-multiple-threads-to-finish-executin
 * <br><br>
 * This class is abstract because it does not need to be instantiated.
 * 
 * @author Joshua Ciffer, Brian Williams
 * @version 01/05/2018
 */
abstract class FindTheWumpus {

	/**
	 * Board that contains all of the characters and items for the game.
	 */
	static GameTile[][] gameBoard;
	
	/**
	 * Used to generate random numbers to determine spawn points, probabilities, etc.
	 */
	static Random random = new Random();
	
	/**
	 * Accepts user input for menus.
	 */
	static Scanner userInput = new Scanner(System.in);
	
	/**
	 * Stores the user's response to a menu prompt.
	 */
	static String userResponse;
	
	/**
	 * The coordinate location of the player on the game board.
	 */
	static int playerRow, playerCol;
	
	/**
	 * The coordinate location of the wumpus on the game board.
	 */
	static int wumpusRow, wumpusCol;
	
	/**
	 * The number of torches the player has found.
	 */
	static int torchesFound;
	
	/**
	 * Keeps track of whether or not the player has picked up any items.
	 */
	static boolean weaponFound, compassFound;

	public static void main(String[] args) {
		gameBoard = makeBoard(5, 5, 5);
		menu();
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
	static GameTile[][] makeBoard(int numRows, int numCols, int numTorches) {
		GameTile[][] newBoard = new GameTile[numRows][numCols];
		boolean wumpusPlaced = false, weaponPlaced = false, playerPlaced = false, compassPlaced = false;
		int torchesPlaced = 0;
		for (int row = 0 ; row < newBoard.length ; row++) {
			for (int col = 0 ; col < newBoard[row].length ; col++) {
				newBoard[row][col] = new GameTile(row, col);
				while (true) {
					switch (random.nextInt(6) + 1) {
						case 1: { // Generates Wumpus
							if (wumpusPlaced) {
								continue;
							} else {
								newBoard[row][col].wumpusHere = true;
								wumpusPlaced = true;
								wumpusRow = row;
								wumpusCol = col;
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
				}
			}
		}
		return newBoard;
	}
	
	static void menu() {
		while (true) {
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
			displayBoard();
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
						attackWumpus(5);
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
		}
	}
	
	static void endTurn() {
		if ((playerRow == wumpusRow) && (playerCol == wumpusCol)) {		// If player is on the same spot as the wumpus.
			if (weaponFound) {
				attackWumpus(80);	// 80% chance of winning if the player has the weapon.
			} else {
				attackWumpus(20);	// 20% chance of winning.
			}
		} else {	// If the player did not find the wumpus, it moves to a new random spot.
			gameBoard[wumpusRow][wumpusCol].wumpusHere = false;
			wumpusRow = random.nextInt(gameBoard.length + 1);
			wumpusCol = random.nextInt(gameBoard[wumpusRow].length + 1);
			gameBoard[wumpusRow][wumpusCol].wumpusHere = true;
			if ((playerRow == wumpusRow) && (playerCol == wumpusCol)) {		// If the wumpus moved to the same spot as the player.
				if (weaponFound) {
					attackWumpus(65);	// 65% chance of winning if the player has the weapon.
				} else {
					attackWumpus(5);	// 5% chance of winning.
				}
			}
		}
		//
	}

	// methods the menu will call
	static void displayBoard() {
		for (int row = 0 ; row < gameBoard.length ; row++) {
			for (int col = 0 ; col < gameBoard[row].length ; col++) {
				if (gameBoard[row][col].wumpusHere) {
					System.out.print("W\t");
				} else if (gameBoard[row][col].weaponHere) {
					System.out.print("A\t");
				} else if (gameBoard[row][col].playerHere) {
					System.out.print("P\t");
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

	static void move() {
		while (true) {
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
		}
		endTurn();
	}

	static void useCompass() {
		if (compassFound) {
			System.out.println("What item would you like to search for?");
			if(weaponFound == false){
				continue;
			}else{
				System.out.println("");
			}
		} else {
			System.out.println("You have not found the compass yet.");
			endTurn();
		}
	}

	static void attackWumpus(int oddsOfWinning) {
		if (random.nextInt(100) < oddsOfWinning) {	// If the user wins,
			System.out.println("You Beat The Wumpus!");
		} else {	// If the user loses,
			System.out.println("The Wumpus Ate Your Fingers!"); 
			System.exit(0);
		}
	}
	
}