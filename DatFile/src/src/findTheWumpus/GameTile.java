package src.findTheWumpus;

/**
 * This class represents one tile of the game board. It contains variables that
 * keep track of its position on the board and what items are stored at that
 * tile.
 * 
 * @author Joshua Ciffer, Brian Williams
 * @version 01/05/2018
 */
class GameTile {

	public static void main(String[] args) {
		System.out.println(new GameTile(2, 3).getClass().getName());
	}
	
	/**
	 * This object's position on the game board.
	 */
	int row, col;

	/**
	 * True if the player has explored this game tile, false if it is undiscovered.
	 */
	boolean explored;

	/**
	 * True if the specific item is stored at this game tile, false if not.
	 */
	boolean playerHere, wumpusHere, weaponHere, compassHere, torchHere;

	/**
	 * Constructs a game tile object with its coordinates on the game board.
	 * 
	 * @param row - The game tile's row in the game board array.
	 * @param col - The game tile's column in the game board array.
	 */
	GameTile(int row, int col) {
		this.row = row;
		this.col = col;
	}

}