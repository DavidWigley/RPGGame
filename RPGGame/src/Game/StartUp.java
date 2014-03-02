/**
 * @author David Wigley + Anthony Foster
 * This controls that start up of the game.
 */

package Game;

import javax.swing.JFrame;

public class StartUp {
	//setting up object
	private static MainGame game = new MainGame();
	public JFrame frame = new JFrame("Game");
	
	
	public static void main(String[] args) {
		
		game.start();
	}
	
		
}
