package src.findTheWumpus;

/**
 * This class represents one tile of the game board. It contains variables that
 * keep track of what items are stored at the tile.
 * 
 * @author Joshua Ciffer, Brian Williams
 * @version 01/05/2018
 */
class GameTile {

	/**
	 * True if the player has explored this game tile, false if it is undiscovered.
	 */
	boolean explored;

	/**
	 * True if the specific item is stored at this game tile, false if not.
	 */
	boolean playerHere, wumpusHere, weaponHere, compassHere, torchHere;

	/**
	 * Constructs a game tile object with nothing on it.
	 */
	GameTile() {}

}