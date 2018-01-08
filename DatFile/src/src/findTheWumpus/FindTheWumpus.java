package src.findTheWumpus;
import java.util.Random;
import java.util.Scanner;
import java.util.InputMismatchException;

/*
 * -------------------------------------Change Log-------------------------------------
 * -----01/07/2018----
 * -Fixed bugs with makeBoard() and removed bias from the random spawning of items (JC).
 * -----01/06/2018-----
 * -Completely finished and debugged move() (JC).
 * -Rewrote makeBoard() so that items are generated completely randomly, but then
 * I was tweaking the code for it and now it gets stuck in an infinite loop.  I'll
 * fix that as soon as I can (JC).
 * -----01/05/2018-----
 * -BUG: There is a bias to where items are spawned with makeBoard() with the random().
 * -Did some stuff to endTurn() (BW).
 * -Started attackWumpus() (JC).
 * -Tried to fix move() cause I fucked up (JC).
 * -Started writing useCompass() (BW).
 * -----01/03/2018-----
 * -BUG: When you chose a direction with move(), it moves you in a different direction
 * than you chose.
 * -Started writing endTurn(), move(), and javadoc for state variables (JC).
 * -Started writing menu(), and displayBoard() (BW).
 * -----01/02/2018-----
 * -Started writing makeBoard() (JC).
 * -Wrote out templates for all necessary methods and state variables (BW).
 * 
 * https://stackoverflow.com/questions/11859534/how-to-calculate-the-total-time-it-takes-for-multiple-threads-to-finish-executin 
 */
/**
 * This class contains the methods to create a game board using GameTile objects
 * and have the player be able to move around the board, find items, and fight
 * the wumpus.  The game board is created by initializing GameTile objects on a 
 * rectangular board in a random order, and then randomly spawning an item on those
 * game tiles.  The player has options to move across the board to different tiles,
 * display the gameboard so they can see what items are near them, use the compass
 * to give them directions on where to find a certain item, or they can choose to
 * attack the wumpus if they are close enough.  After choosing an action, the player's
 * turn ends, and the wumpus moves to a different location.  If the wumpus and the
 * player ever end up on the same game tile, the two will attack each other and the
 * player will be given a certain probability of winning.
 * <br><br>
 * This class is abstract because it does not need to be instantiated.
 * 
 * @author Joshua Ciffer, Brian Williams
 * @version 01/07/2018
 */
abstract class FindTheWumpus {

	/**
	 * Rectangular board that contains all of the characters and items for the game.
	 */
	static GameTile[][] gameBoard;

	/**
	 * Used to generate random numbers to determine spawn points, probabilities,
	 * etc.
	 */
	static Random random = new Random();

	/**
	 * Accepts user input for menu prompts.
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
	 * Keeps track of whether or not the player has picked up any items.
	 */
	static boolean weaponFound, compassFound;
	
	/**
	 * Keeps track of how many torches the player has found.
	 */
	static int torchesFound;

	/**
	 * The main entry point of the program.  A board with a specified difficulty is
	 * created and then the menu is run until the game is finished.
	 * 
	 * @param args - Any command line arguments.
	 */
	public static void main(String[] args) {
//		gameBoard = makeBoard(5, 5, 2);
//		displayBoard();
		while (true) {
			System.out.print("--------Find The Wumpus Game--------\nBy Brian Williams, & Joshua Ciffer" + 
					"\n (1) Easy - 5x5 Board, 3 Torches\n (2) Medium - 10x10 Board, 2 Torches" +
					"\n (3) Hard - 15x15 Board, 1 Torch\n (4) Custom Difficulty\n (5) Quit\nEnter an option: ");
			try {
				userResponse = userInput.next();
			} catch (InputMismatchException e) {
				System.out.println("\nPlease enter one of the given options.\n");
				userInput.next();	// Clears the scanner.
				continue;
			}
			switch (userResponse.toLowerCase()) {
				case "1": {		// Easy.
					gameBoard = makeBoard(5, 5, 3);
					menu();
					break;
				}
				case "2": {		// Medium.
					gameBoard = makeBoard(10, 10, 2);
					menu();
					break;
				}
				case "3": {		// Hard.
					gameBoard = makeBoard(15, 15, 1);
					menu();
					break;
				}
				case "4": {		// Custom.
					int rows, cols, torches;
					while (true) {
						try {
							System.out.print("How tall will the game board be?: ");
							rows = Math.abs(userInput.nextInt());	// Stores the absolute value to prevent negative array size.
						} catch (InputMismatchException e) {
							System.out.println("\nPlease enter how tall the game board will be.\n");
							userInput.next();	// Clears the scanner.
							continue;
						}
						while (true) {
							try {
								System.out.print("How wide will the game board be?: ");
								cols = Math.abs(userInput.nextInt());	// Stores the absolute value to prevent negative array size.
							} catch (InputMismatchException e) {
								System.out.println("\nPlease enter how wide the game board will be.\n");
								userInput.next();	// Clears the scanner.
								continue;
							}
							while (true) {
								try {
									System.out.print("How many torches will there be?: ");
									torches = Math.abs(userInput.nextInt());	// Stores the absolute value to prevent negative amount of torches.
								} catch (InputMismatchException e) {
									System.out.println("\nPlease enter how many torches there will be.\n");
									userInput.next();	// Clears the scanner.
									continue;
								}
								break;
							}
							break;
						}
						break;
					}
					gameBoard = makeBoard(rows, cols, torches);
					menu();
					break;
				}
				case "5": {		// Quit.
					System.exit(0);
					break;
				}
				default: {
					System.out.println("\nPlease enter one of the given options.\n");
					continue;
				}
			}
		}
	}

	/**
	 * This method creates a new GameTile[][] with the given parameters, and with
	 * all of the game items spawned in random positions. The game board will always
	 * be a rectangle, the array will never be ragged.
	 * 
	 * @param numRows - The number of rows in the game board.
	 * @param numCols - The number of columns in the game board.
	 * @param numTorches - The number of torches to be spawned.
	 * @return Returns a new GameTile[][] with the specified size, number of
	 * torches, and with all of the game items spawned.
	 */
	static GameTile[][] makeBoard(int numRows, int numCols, int numTorches) {
		GameTile[][] newBoard = new GameTile[numRows][numCols];
		boolean playerPlaced = false, wumpusPlaced = false, weaponPlaced = false, compassPlaced = false;
		int torchesPlaced = 0;
		for (int gameTilesPlaced = 0; gameTilesPlaced < (numRows * numCols); gameTilesPlaced++) {
			int row = random.nextInt(newBoard.length);	// The coordinates of the next tiles to be created are generated randomly, and the game board
			int col = random.nextInt(newBoard[row].length);// is filled in a random order.
			if (newBoard[row][col] == null) {	// If this spot does not have a tile placed, create one.
				newBoard[row][col] = new GameTile();
				while (true) {
					switch (random.nextInt(6)) {	// Randomly picks the item to spawn on the game tile.
						case 0: {	// Spawns empty tile.
							if (!(playerPlaced && wumpusPlaced && weaponPlaced && compassPlaced && (torchesPlaced == numTorches))) {
								continue;	// If the other items haven't been spawned yet, continue and spawn the items before spawning empty tiles.
							} else {
								break;	// Spawns no item on this tile.
							}
						}
						case 1: {	// Spawns player.
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
						case 2: {	// Spawns wumpus.
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
						case 3: {	// Spawns weapon.
							if (weaponPlaced) {
								continue;
							} else {
								newBoard[row][col].weaponHere = true;
								weaponPlaced = true;
								break;
							}
						}		
						case 4: {	// Spawns compass.
							if (compassPlaced) {
								continue;
							} else {
								newBoard[row][col].compassHere = true;
								compassPlaced = true;
								break;
							}
						}
						case 5: {	// Spawns torch.
							if (torchesPlaced == numTorches) {
								continue;
							} else {
								newBoard[row][col].torchHere = true;
								torchesPlaced++;
								break;
							}
						}
					}
					break;
				}
			} else {	// If a tile was already placed at this spot, do nothing.
				gameTilesPlaced--;	// The loop incrementer is decremented if a tile was already spawned at these coordinates.
			}                    // This is so the loop actually spawns the correct amount of tiles and makes sure it isn't
		}                        // counting the same tile more than once.
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
				case "5": { // Exit
					System.exit(0);
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

	static void displayBoard() {
		for (int row = 0; row < gameBoard.length; row++) {
			for (int col = 0; col < gameBoard[row].length; col++) {
				if (gameBoard[row][col].playerHere) {
					System.out.print("P\t");	// Player at this tile.
				} else if (gameBoard[row][col].wumpusHere) {
					System.out.print("W\t");	// Wumpus at this tile.
				} else if (gameBoard[row][col].weaponHere) {
					System.out.print("A\t");	// Weapon at this tile.
				} else if (gameBoard[row][col].compassHere) {
					System.out.print("C\t");	// Compass at this tile.
				} else if (gameBoard[row][col].torchHere) {
					System.out.print("T\t");	// Torch at this tile.
				} else if (gameBoard[row][col].explored) {
					System.out.print("O\t");	// Explored tile.
				} else {
					System.out.print("X\t");	// Unexplored tile.
				}
			}
			System.out.print("\n");
		}
		endTurn();
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
					if ((playerRow - 1) < 0) {	// If the player would move off of the top of the board,
						System.out.println("\nUh oh, it looks like you can't move to the North. Try a different direction.");
						continue;
					} else {
						gameBoard[playerRow][playerCol].playerHere = false;
						gameBoard[--playerRow][playerCol].playerHere = true;	// Moves player one row up.
						System.out.println("\nYou moved to the North.\n");
						endTurn();
						break;
					}
				}
				case "east": {
					if ((playerCol + 1) > (gameBoard[playerRow].length - 1)) {	// If the user would move off of the right of the board,
						System.out.println("\nUh oh, it looks like you can't move to the East. Try a different direction.");
						continue;
					} else {
						gameBoard[playerRow][playerCol].playerHere = false;
						gameBoard[playerRow][++playerCol].playerHere = true;	// Moves player one column right.
						System.out.println("\nYou moved to the East.\n");
						endTurn();
						break;
					}
				}
				case "south": {
					if ((playerRow + 1) > (gameBoard.length - 1)) {	// If the user would move off of the bottom of the board,
						System.out.println("\nUh oh, it looks like you can't move to the South. Try a different direction.");
						continue;
					} else {
						gameBoard[playerRow][playerCol].playerHere = false;
						gameBoard[++playerRow][playerCol].playerHere = true;	// Moves player one row down.
						System.out.println("\nYou moved to the South.\n");
						endTurn();
						break;
					}
				}
				case "west": {
					if ((playerCol - 1) < 0) {	// If the user would move off of the left of the board,
						System.out.println("\nUh oh, it looks like you can't move to the West. Try a different direction.");
						continue;
					} else {
						gameBoard[playerRow][playerCol].playerHere = false;
						gameBoard[playerRow][--playerCol].playerHere = true;	// Moves player one column left.
						System.out.println("\nYou moved to the West.\n");
						endTurn();
						break;
					}
				}
				case "cancel": {
					System.out.print("\n");
					break;	// Goes back to the menu without ending the turn.
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