package src.findTheWumpus;
import java.util.Random;
import java.util.Scanner;
import java.util.InputMismatchException;
// fuck
/*
 *-------------------------------------Change Log-------------------------------------
 * -----01/12/2018-----
 * -Fixed torch exploration radius with endTurn() (JC).
 * -----01/11/2018-----
 * -Fixed random wumpus movement.
 * -DEBUG: Check endTurn() where it sets explored tiles in a radius around the player (BW).
 * -Fixed menu() when the user would complete the game (JC).
 * -Did some housekeeping stuff, annotating, etc (JC).
 * -Fixed directions for useCompass() (BW).
 * -----01/10/2018-----
 * -BUG: Wumpus doesn't move one space, it teleport to a random location
 * -Completed useCompass (BW)
 * -----01/09/2018-----
 * -Worked on making tiles near the player explored when they move, have torches, etc (JC).
 * -Got useCompass() working (BW).
 * -----01/08/2018-----
 * -Put some new stuff into useCompass (BW).
 * -IDR what I did, but I did some stuff (JC).
 * -----01/07/2018-----
 * -Added ability to have different difficulties for game boards (JC).
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
 * @version 01/12/2018
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
	 * The coordinate location of the weapon on the game board.
	 */
	static int weaponRow, weaponCol;
	
	/**
	 * Keeps track of whether or not the player has picked up any items.
	 */
	static boolean weaponFound, compassFound;
	
	/**
	 * Keeps track of how many torches the player has found.
	 */
	static int torchesFound;
	
	/**
	 * Keeps track of how many torches are on the game board.
	 */
	static int numTorches;
	
	/**
	 * True if the user fights the wumpus and the game ends.
	 */
	static boolean gameOver;

	/**
	 * The main entry point of the program.  A board with a specified difficulty is
	 * created and then the menu is run until the game is finished.
	 * 
	 * @param args - Any command line arguments.
	 */
	public static void main(String[] args) {
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
					gameBoard = makeBoard(5, 5, 3);		// 5x5 board, 3 torches.
					menu();
					break;
				}
				case "2": {		// Medium.
					gameBoard = makeBoard(10, 10, 2);	// 10x10 board, 2 torches.
					menu();
					break;
				}
				case "3": {		// Hard.
					gameBoard = makeBoard(15, 15, 1);	// 15x15 board, 1 torch.
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
		FindTheWumpus.numTorches = numTorches;
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
								break;	// Leaves game tile with no items on it.
							}
						}
						case 1: {	// Spawns player.
							if (playerPlaced) {
								continue;
							} else {
								newBoard[row][col].playerHere = true;
								newBoard[row][col].explored = true;
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
								weaponRow = row;
								weaponCol = col;
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

	/**
	 * This method displays the options available to the player and takes in their input
	 * to carry out various methods of the game.
	 */
	static void menu() {
		System.out.print("\n");
		while (true) {
			printBoard();
			System.out.println("Your Turn:\n (1) Display Board\n (2) Move");
			if (compassFound) {
				System.out.println(" (3) Use Compass");
			} else {
				System.out.println(" (3) LOCKED");
			}
			if (torchesFound > 2) {
				System.out.println(" (4) Attack Wumpus");
			} else {
				System.out.println(" (4) LOCKED");
			}
			System.out.print(" (5) Quit\nEnter an Option: ");
			try {
				userResponse = userInput.next();
			} catch (InputMismatchException e) {
				System.out.println("\nPlease choose one of the given options.\n");
				userInput.next();	// Clears the scanner.
				continue;
			}
			switch (userResponse.toLowerCase()) {
				case "1": { // Display Board.
					displayBoard();
					break;
				}
				case "2": { // Move.
					move();
					break;
				}
				case "3": { // Use Compass.
					if (compassFound) {
						useCompass();
						break;
					} else {
						System.out.println("\nThis option is locked until you find the compass.\n");
						continue;
					}
				}
				case "4": { // Attack Wumpus.
					if ((torchesFound >= 2) && (findDistance(wumpusRow, wumpusCol) <= 2)) {
						if (findDistance(wumpusRow, wumpusCol) == 2) {
							if (weaponFound) {
								attackWumpus(75);
							} else {
								attackWumpus(15);
							}
						} else if (findDistance(wumpusRow, wumpusCol) <= 1) {
							if (weaponFound) {
								attackWumpus(90);
							} else {
								attackWumpus(30);
							}
						}
					} else {
						System.out.println("\nThis option is locked until you find enough torches.\n");
						continue;
					}
					break;
				}
				case "5": { // Quit.
					gameOver = true;
					break;
				}
				default: {	// Error.
					System.out.println("\nEnter one of the given options.\n");
					continue;
				}
			}
			if (gameOver) {
				gameOver = false;	// Resets variable for next game.
				break;	// Goes back to main menu if game ends.
			} else {
				continue;	// Continues the game if it isn't over.
			}
		}
	}

	/**
	 * This method updates positions, collects items, and has the wumpus move
	 * to a new position after the end of the player's turn.
	 */
	static void endTurn() {
		// Player picks up any items on their tile.
		if (gameBoard[playerRow][playerCol].weaponHere) {
			weaponFound = true;
			gameBoard[playerRow][playerCol].weaponHere = false;
			System.out.print("You found the weapon!");
		}
		if (gameBoard[playerRow][playerCol].compassHere) {
			compassFound = true;
			gameBoard[playerRow][playerCol].compassHere = false;
			System.out.print("You found the compass!");
		}
		if (gameBoard[playerRow][playerCol].torchHere) {
			torchesFound++;
			gameBoard[playerRow][playerCol].torchHere = false;
			System.out.print("You found a torch!");
		}
		// Checks to see if user will fight the wumpus.
		if ((playerRow == wumpusRow) && (playerCol == wumpusCol)) {		// If the player bumped into the wumpus.
			System.out.print("You bumped into the wumpus!");
			if (weaponFound) {
				attackWumpus(80);	// 80% chance of winning.
			} else {
				attackWumpus(20); 	// 20% chance of winning.
			}
		} else { 	// If the player did not bump into the wumpus, it moves to a new random spot.
			while (true) {
				switch (random.nextInt(8)) {
					case 0: {
						if ((wumpusRow - 1) < 0) {	// If the wumpus would move off of the top of the board,
							continue;
						} else {
							gameBoard[wumpusRow][wumpusCol].wumpusHere = false;
							gameBoard[--wumpusRow][wumpusCol].wumpusHere = true;	// Moves wumpus one row up.
							break;
						}
					}
					case 1: {
						if ((wumpusCol + 1) > (gameBoard[wumpusRow].length - 1)) {	// If the wumpus would move off of the right of the board,
							continue;
						} else {
							gameBoard[wumpusRow][wumpusCol].wumpusHere = false;
							gameBoard[wumpusRow][++wumpusCol].wumpusHere = true;	// Moves wumpus one column right.
							break;
						}
					}
					case 2: {
						if ((wumpusRow + 1) > (gameBoard.length - 1)) {	// If the wumpus would move off of the bottom of the board,
							continue;
						} else {
							gameBoard[wumpusRow][wumpusCol].wumpusHere = false;
							gameBoard[++wumpusRow][wumpusCol].wumpusHere = true;	// Moves wumpus one row down.
							break;
						}
					}
					case 3: {
						if ((wumpusCol - 1) < 0) {	// If the wumpus would move off of the left of the board,
							continue;
						} else {
							gameBoard[wumpusRow][wumpusCol].wumpusHere = false;
							gameBoard[wumpusRow][--wumpusCol].wumpusHere = true;	// Moves wumpus one column left.
							break;
						}
					}
					case 4: {
						if (((wumpusRow - 1) < 0) || ((wumpusCol + 1) > (gameBoard[wumpusRow].length - 1))) {	// If the wumpus would move off the board,
							continue;
						} else {
							gameBoard[wumpusRow][wumpusCol].wumpusHere = false;
							gameBoard[--wumpusRow][++wumpusCol].wumpusHere = true;	// Moves wumpus diagonally up, right.
							break;
						}
					}
					case 5: {
						if (((wumpusRow + 1) > (gameBoard.length - 1)) || ((wumpusCol + 1) > (gameBoard[wumpusRow].length - 1))) {	// If the wumpus would move off the board,
							continue;
						} else {
							gameBoard[wumpusRow][wumpusCol].wumpusHere = false;
							gameBoard[++wumpusRow][++wumpusCol].wumpusHere = true;	// Moves wumpus diagonally down, right.
							break;
						}
					}
					case 6: {
						if (((wumpusRow + 1) > (gameBoard.length - 1)) || ((wumpusCol - 1) < 0)) {	// If the wumpus would move off of the board,
							continue;
						} else {
							gameBoard[wumpusRow][wumpusCol].wumpusHere = false;
							gameBoard[++wumpusRow][--wumpusCol].wumpusHere = true;	// Moves wumpus diagonally down, left.
							break;
						}
					}
					case 7: {
						if (((wumpusRow - 1) < 0) || ((wumpusCol - 1) < 0)) {	// If the wumpus would move off the board,
							continue;
						} else {
							gameBoard[wumpusRow][wumpusCol].wumpusHere = false;
							gameBoard[--wumpusRow][--wumpusCol].wumpusHere = true;	// Moves wumpus diagonally up, left.
							break;
						}
					}
				}
				break;
			}
			if ((playerRow == wumpusRow) && (playerCol == wumpusCol)) {		// If the wumpus bumped into the player.
				System.out.print("The wumpus bumped into you!");
				if (weaponFound) {
					attackWumpus(65);	// 65% chance of winning.
				} else {
					attackWumpus(5); 	// 5% chance of winning.
				}
			}
		}
		if (findDistance(wumpusRow, wumpusCol) <= torchesFound) {
			System.out.print("You have found wumpus droppings. A wumpus must be near by.");
		}
		// Sets explored tiles.
		gameBoard[playerRow][playerCol].explored = true;	// Tile player is currently on.
		for (int i = 1; i <= torchesFound; i++) {	// Tiles in the radius of the player depending on the number of torches they found.
			if ((playerRow - i) > 0) {
				gameBoard[playerRow - i][playerCol].explored = true;	// Tile to the North of the player.
			} 
			if ((playerCol + i) < (gameBoard[playerRow].length - 1)) {
				gameBoard[playerRow][playerCol + i].explored = true;	// Tile to the East of the player.
			}
			if ((playerRow + i) < (gameBoard.length - 1)) {
				gameBoard[playerRow + i][playerCol].explored = true;	// Tile to the South of the player.
			}
			if ((playerCol - i) > 0) {
				gameBoard[playerRow][playerCol - i].explored = true;	// Tile to the West of the player.
			}
			if (((playerRow - i) >= 0) && ((playerCol + i) <= (gameBoard[playerRow].length - 1))) {
				gameBoard[playerRow - i][playerCol + i].explored = true;	// Tile to the Northeast of the player.
			}
			if (((playerRow + i) <= (gameBoard.length - 1)) && ((playerCol + i) <= (gameBoard[playerRow].length - 1))) {
				gameBoard[playerRow + i][playerCol + i].explored = true;	// Tile to the Southeast of the player.
			}
			if (((playerRow + i) <= (gameBoard.length - 1)) && ((playerCol - i) >= 0)) {
				gameBoard[playerRow + i][playerCol - i].explored = true;	// Tile to the Southwest of the player.
			}
			if (((playerRow - i) >= 0) && ((playerCol - i) >= 0)) {
				gameBoard[playerRow - i][playerCol - i].explored = true;	// Tile to the Northwest of the player.
			}
		}
		System.out.print("\n");
	}

	/**
	 * This method shows the player the gameboard but only reveals tiles they have explored.
	 */
	static void displayBoard() {
		for (int row = 0; row < gameBoard.length; row++) {
			for (int col = 0; col < gameBoard[row].length; col++) {
				if (gameBoard[row][col].playerHere) {
					System.out.print("P\t");	// Player at this tile.
				} else if (gameBoard[row][col].explored) {
					if (gameBoard[row][col].wumpusHere) {
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
				} else {
					System.out.print("X\t");	// Unexplored tile.
				}
			}
			System.out.print("\n");
		}
		endTurn();
	}
	
	/**
	 * This method prints out the entire contents of the game board for debugging purposes.
	 */
	static void printBoard() {
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
	}
	
	/**
	 * This method prompts the user to move in a North, East, South, West, Northeast,
	 * Southeast, Southwest, or Northwest direction. The user can choose to cancel and 
	 * go back to the menu. If the user is at the edge of the board, they are prompted 
	 * with a message telling them that they can't move in that direction. Once the 
	 * player moves, the turn ends and endTurn() is called.
	 */
	static void move() {
		System.out.print("\n");
		while (true) {
			System.out.print("Do you want to move to the North, East, South, West,\n"
					+ "Northeast, Southeast, Southwest, Northwest, or Cancel?: ");
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
				case "northeast": {
					if (((playerRow - 1) < 0) || ((playerCol + 1) > (gameBoard[playerRow].length - 1))) {	// If the user would move off the board,
						System.out.println("\nUh oh, it looks like you can't move to the North East");
						continue;
					} else {
						gameBoard[playerRow][playerCol].playerHere = false;
						gameBoard[--playerRow][++playerCol].playerHere = true;	// Moves player diagonally up, right.
						System.out.println("\nYou moved to the North East.\n");
						endTurn();
						break;
					}
				}
				case "southeast": {
					if (((playerRow + 1) > (gameBoard.length - 1)) || ((playerCol + 1) > (gameBoard[playerRow].length - 1))) {	// If the user would move off the board,
						System.out.println("\nUh oh, it looks like you can't move to the South East.\n");
						continue;
					} else {
						gameBoard[playerRow][playerCol].playerHere = false;
						gameBoard[++playerRow][++playerCol].playerHere = true;	// Moves player diagonally down, right.
						System.out.println("\nYou moved to the South East.\n");
						endTurn();
						break;
					}
				}
				case "southwest": {
					if (((playerRow + 1) > (gameBoard.length - 1)) || ((playerCol - 1) < 0)) {	// If the user would move off of the board,
						System.out.println("\nUh oh, it looks like you can't move to the South West.\n");
						continue;
					} else {
						gameBoard[playerRow][playerCol].playerHere = false;
						gameBoard[++playerRow][--playerCol].playerHere = true;	// Moves player diagonally down, left.
						System.out.println("\nYou moved to the South West.\n");
						endTurn();
						break;
					}
				}
				case "northwest": {
					if (((playerRow - 1) < 0) || ((playerCol - 1) < 0)) {	// If the user would move off the board,
						System.out.println("\nUh oh, it looks like you can't move to the North West.\n");
						continue;
					} else {
						gameBoard[playerRow][playerCol].playerHere = false;
						gameBoard[--playerRow][--playerCol].playerHere = true;	// Moves player diagonally up, left.
						System.out.println("\nYou moved to the North West.\n");
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

		/*
		 * Location of the Item's Column
		 */
		int itemCol = 0;
		/**
		 * Location of the Item's Row
		 */
		int itemRow = 0;
		/**
		 * itemCol - playerCol = DistCol
		 */
		int distCol = 0;
		/**
		 * Location itemRow - playerRow = DistCol
		 */
		int distRow = 0;
		/**
		 * Number generated by the distance formula
		 */
		int distance = 0;

		if (compassFound) {
			// checks if you have found the item or not
			while (true) {
				System.out.println("What item would you like to search for?");
				if (weaponFound) {
					System.out.println("(1) Weapon - FOUND");
				} else {
					System.out.println("(1) Weapon");
				}
				if (torchesFound == numTorches) {
					System.out.println("(2) Torches - FOUND");
				} else if (torchesFound > 0 && torchesFound < numTorches) {
					System.out.println("(2) Torches " + torchesFound + " FOUND");
				} else {
					System.out.println("(2) Torches ");
				}
				System.out.println("(3) Wumpus");
				try {
					userResponse = userInput.next();
				} catch (InputMismatchException e) {
					System.out.println("Please enter a numerical response.");
					userInput.next();
					continue;
				}
				break;
			}
			// Case 1 + 3 competed
			switch (userResponse) {
				case "1": {
					if (weaponFound) {
						System.out.println("You already found this item");
					} else {
						for (int row = 0; row < gameBoard.length; row++) {
							for (int col = 0; col < gameBoard[0].length; col++) {
								if (gameBoard[row][col].weaponHere == true) {
									itemRow = row;
									itemCol = col;
									break;
								} else {
									continue;
								}
							}
						}
						distRow = playerRow - itemRow;
						distCol = playerCol - itemCol;
						if (distCol > 0) {
							if (distRow < 0) {
								System.out.println("Northwest");
							} else if (distRow > 0) {
								System.out.println("Southwest");
							} else {	
								System.out.println("West");
							}
						} else if (distCol < 0) {
							if (distRow > 0) {
								System.out.println("Northeast");
							} else if (distRow < 0) {
								System.out.println("Southeast");
							} else {
								System.out.println("East");
							}
						} else {
							if (distRow < 0) {
								System.out.println("North");
							} else {
								System.out.println("South");
							}
						}
					}
					break;
				}
				case "2": {
					if (torchesFound == numTorches) {
						System.out.println("You already found all of thesse itmes");
					} else {
						for (int row = 0; row < gameBoard.length; row++) {
							for (int col = 0; col < gameBoard[row].length; col++) {
								if (gameBoard[row][col].torchHere == true) {
									if(distance > (int)findDistance(row, col)){
										distance =(int)findDistance(row,col);
										itemRow = row;
										itemCol = col;
									} else {
										continue;
									}
									break;
								} else {
									continue;
								}
							}
						}
						distRow = playerRow - itemRow;
						distCol = playerCol - itemCol;
						if (distCol > 0) {
							if (distRow < 0) {
								System.out.println("Northwest");
							} else if (distRow > 0) {
								System.out.println("Southwest");
							} else {
								System.out.println("West");
							}
						} else if (distCol < 0) {
							if (distRow > 0) {
								System.out.println("Northeast");
							} else if (distRow < 0) {
								System.out.println("Southeast");
							} else {
								System.out.println("East");
							}
						}
					}
					break;
				}
				case "3": {
					itemCol = wumpusCol;
					itemRow = wumpusRow;
					distRow = playerRow - itemRow;
					distCol = playerCol - itemCol;
					if (distCol > 0) {
						if (distRow < 0) {
							System.out.println("Northwest");
						} else if (distRow > 0) {
							System.out.println("Southwest");
						} else {
							System.out.println("West");
						}
					} else if (distCol < 0) {
						if (distRow > 0) {
							System.out.println("Northeast");
						} else if (distRow < 0) {
							System.out.println("Southeast");
						} else {
							System.out.println("East");
						}
					} else {
						if (distRow < 0) {
							System.out.println("North");
						} else {
							System.out.println("South");
						}
					}
					break;
				}
				default:
					System.out.println("Incorrect response");
				}
		} else {
			System.out.println("\nYou haven't found the compass yet!");
		}
		endTurn();
	}

	/**
	 * This method randomly determines whether or not the user wins against the
	 * wumpus by using a percentage that varies off of different variables in the
	 * game.
	 * 
	 * @param oddsOfWinning - The perctentage chance the player has of winning.
	 */
	static void attackWumpus(int oddsOfWinning) {
		if (random.nextInt(100) < oddsOfWinning) { // If the user wins,
			System.out.println(" You Beat The Wumpus!\n");
		} else { // If the user loses,
			System.out.println(" The Wumpus Ate Your Fingers!\n");
		}
		gameOver = true;
	}
	
	/**
	 * This method returns the distance of two game tiles on the board using the distance formula.
	 * The equation used is sqrt((row1 - row2)^2 + (col1 - col2)^2)
	 * 
	 * @return Returns the distance between two game tiles.
	 */
	static int findDistance(int row, int col) {
		return (int)(Math.sqrt(Math.pow((playerRow - row), 2) + Math.pow((playerCol - col), 2)));
	}

}
