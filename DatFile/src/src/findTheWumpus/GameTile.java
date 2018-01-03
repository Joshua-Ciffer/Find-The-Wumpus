package src.findTheWumpus;

/**
 * 
 * @author Joshua Ciffer, Brian Williams
 * @version 01/02/2018
 */
public class GameTile {
	
	int row;
	int col;
	boolean explored;
	boolean playerHere;
	boolean wumpusHere;
	boolean weaponHere;
	boolean compassHere;
	boolean torchHere;
	
	GameTile(int row, int col) {
		this.row = row;
		this.col = col;
	}
	
}