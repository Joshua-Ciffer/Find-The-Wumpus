
package src.findTheWumpus;

import java.util.Random;
import java.util.Scanner;
import java.util.InputMismatchException;

/**
 * https://stackoverflow.com/questions/11859534/how-to-calculate-the-total-time-
 * it-takes-for-multiple-threads-to-finish-executin <br>
 * <br>
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
	 * Used to generate random numbers to determine spawn points, probabilities,
	 * etc.
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
			switch (userResponse) {
				case "1": { // Display Board
					displayBoard();
					endTurn();
					break;
				}
				case "2": { // Move
					move();
					break;
				}
				case "3": { // Use Compass
					if (compassFound) {
						useCompass();
						endTurn();
						break;
					} else {
						System.out.println("You have not found the compass yet.");
						continue;
					}
				}
				case "4": { // Attack Wumpus
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
		if ((playerRow == wumpusRow) && (playerCol == wumpusCol)) {
			if (weaponFound) {
				attackWumpus(80);
			} else {
				attackWumpus(20); // 20% chance of winning.
			}
		} else { // If the player did not find the wumpus, it moves to a new random spot.
			gameBoard[wumpusRow][wumpusCol].wumpusHere = false;
			wumpusRow = random.nextInt(gameBoard.length);
			wumpusCol = random.nextInt(gameBoard[wumpusRow].length);
			gameBoard[wumpusRow][wumpusCol].wumpusHere = true;
			if ((playerRow == wumpusRow) && (playerCol == wumpusCol)) {
				if (weaponFound) {
					attackWumpus(65);
				} else {
					attackWumpus(5); // 5% chance of winning.
				}
			}
		}
		//
	}

	// methods the menu will call
	static void displayBoard() {
		for (int row = 0 ; row < gameBoard.length ; row++) {
			for (int col = 0 ; col < gameBoard[row].length ; col++) {
				if (gameBoard[row][col].playerHere) {
					System.out.print("(" + row + ", " + col + ") " + "P\t");
				} else if (gameBoard[row][col].wumpusHere) {
					System.out.print("(" + row + ", " + col + ") " + "W\t");
				} else if (gameBoard[row][col].weaponHere) {
					System.out.print("(" + row + ", " + col + ") " + "A\t");
				} else if (gameBoard[row][col].compassHere) {
					System.out.print("(" + row + ", " + col + ") " + "C\t");
				} else if (gameBoard[row][col].torchHere) {
					System.out.print("(" + row + ", " + col + ") " + "T\t");
				} else {
					System.out.print("(" + row + ", " + col + ") " + "X\t");
				}
			}
			System.out.print("\n");
		}
		// endTurn();
	}

	/**
	 * This method prompts the user to move in a North, East, South, or West
	 * direction. The user can choose to cancel and go back to the menu. If the user
	 * is at the edge of the board, they are prompted with a message telling them
	 * that they can't move in that direction. Once the player moves, the turn ends
	 * and endTurn() is called.
	 */
	static void move() {
		while (true) {
			System.out.print("\nDo you want to move to the North, East, South, West, or Cancel?: ");
			try {
				userResponse = userInput.next();
			} catch (InputMismatchException e) {
				System.out.println("\nPlease choose a direction to move.\n");
				userInput.next(); // Clears the Scanner.
				continue;
			}
			switch (userResponse.toLowerCase()) {
				case "north": {
					if ((playerRow - 1) < 0) {
						System.out.println("\nUh oh, it looks like you can't move to the North. Try a different direction.");
						continue;
					} else {
						gameBoard[playerRow][playerCol].playerHere = false;
						gameBoard[--playerRow][playerCol].playerHere = true;
						System.out.println("\nYou moved to the North.\n");
						endTurn();
						break;
					}
				}
				case "east": {
					if ((playerCol + 1) > (gameBoard[playerRow].length - 1)) {
						System.out.println("\nUh oh, it looks like you can't move to the East. Try a different direction.");
						continue;
					} else {
						gameBoard[playerRow][playerCol].playerHere = false;
						gameBoard[playerRow][++playerCol].playerHere = true;
						System.out.println("\nYou moved to the East.\n");
						endTurn();
						break;
					}
				}
				case "south": {
					if ((playerRow + 1) > (gameBoard.length - 1)) {
						System.out.println("\nUh oh, it looks like you can't move to the South. Try a different direction.");
						continue;
					} else {
						gameBoard[playerRow][playerCol].playerHere = false;
						gameBoard[++playerRow][playerCol].playerHere = true;
						System.out.println("\nYou moved to the South.\n");
						endTurn();
						break;
					}
				}
				case "west": {
					if ((playerCol - 1) < 0) {
						System.out.println("\nUh oh, it looks like you can't move to the West. Try a different direction.");
						continue;
					} else {
						gameBoard[playerRow][playerCol].playerHere = false;
						gameBoard[playerRow][--playerCol].playerHere = true;
						System.out.println("\nYou moved to the West.\n");
						endTurn();
						break;
					}
				}
				case "cancel": {
					System.out.print("\n");
					break;
				}
				default: {
					System.out.println("\nPlease choose a direction to move.\n");
					continue;
				}
			}
			break;
		}
	}

	static void useCompass() {

		if (compassFound) {
			// checks if you have found the item or not
			while (true) {
				System.out.println("What item would you like to search for?");
				if (weaponFound == false) {
					System.out.println("(1) Weapon - FOUND");
				} else if (weaponFound == true) {
					System.out.println("(1) Weapon");
				}
				if (torchesFound == 2) {
					System.out.println("(2) Torches - FOUND");
				} else if (torchesFound == 2) {
					System.out.println("(2) Torches - 1 FOUND");
				} else {
					System.out.println("(2) Torches ");
				}
				try {
					userResponse = userInput.next();
				} catch (InputMismatchException e) {
					System.out.println("Please enter a numerical response.");
					break;
				}
			}
			// Yeah I'm learning
			switch (userResponse) {
				case "1":

				case "2":
			}
		} else {
			System.out.println("You have not found the compass yet.");
			endTurn();
		}
	}

	static void attackWumpus(int oddsOfWinning) {
		if (random.nextInt(100) < oddsOfWinning) { // If the user wins,
			System.out.println("You Beat The Wumpus!");
		} else { // If the user loses,
			System.out.println("The Wumpus Ate Your Fingers!");
			System.exit(0);
		}
	}

}