/**
 * @author Anthony Foster + David Wigley
 * @music Andrew Zucker + Jake Berlandi
 */
package Game;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.util.Random;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import Game.StartUp;
@SuppressWarnings("serial")
public class MainGame extends Canvas implements Runnable, KeyListener,MouseListener, MouseMotionListener {
	StartUp base = new StartUp();
	
	//pictures
	ImageIcon background = new ImageIcon(getClass().getResource("/resources/background test2.png"));
	Image picture = background.getImage();
	Image offscreen;
	ImageIcon arrowIconl = new ImageIcon(getClass().getResource("/resources/arrowl.png"));
	Image Arrowl = arrowIconl.getImage();
	ImageIcon arrowIconr = new ImageIcon(getClass().getResource("/resources/arrowr.png"));
	Image Arrowr = arrowIconr.getImage();
	ImageIcon frameIconI = new ImageIcon(getClass().getResource("/resources/nuke.png"));
	Image frameIcon = frameIconI.getImage();
	ImageIcon earlyAccessIcon = new ImageIcon(getClass().getResource("/resources/early_access.jpg"));
	Image earlyAccess = earlyAccessIcon.getImage();
	ImageIcon swordHorizontalLIcon = new ImageIcon(getClass().getResource("/resources/sword horizontal L.png"));
	Image SwordHorizontalL = swordHorizontalLIcon.getImage();
	ImageIcon sword45LIcon = new ImageIcon(getClass().getResource("/resources/sword 45 l.png"));
	Image Sword45L = sword45LIcon.getImage();
	ImageIcon swordLIcon = new ImageIcon(getClass().getResource("/resources/sword l.png"));
	Image SwordL = swordLIcon.getImage();
	ImageIcon playButtonIcon = new ImageIcon(getClass().getResource("/resources/play_button.png"));
	Image playButton = playButtonIcon.getImage();
	ImageIcon ArrowIcon = new ImageIcon(getClass().getResource("/resources/leftarrow.png"));
	Image Arrow = ArrowIcon.getImage();
	ImageIcon EasyIcon = new ImageIcon(getClass().getResource("/resources/easy.png"));
	Image Easy = EasyIcon.getImage();
	ImageIcon NormalIcon = new ImageIcon(getClass().getResource("/resources/normal.png"));
	Image Normal = NormalIcon.getImage();
	ImageIcon HardIcon = new ImageIcon(getClass().getResource("/resources/hard.png"));
	Image Hard = HardIcon.getImage();
	
	
	//music variables
	AudioInputStream attackIn;
	Clip attackClip;
	AudioInputStream backgroundGame;
	Clip backgroundMusic;
	boolean inFlight;
	boolean notPlayingSound, shouldPlaySound;
	
	//player variables
	int attackStyle;
	float gravity = 0.2f, x = 350, y = 590, velocityX, velocityY;
	boolean isGrounded, right, left, shield, dead;
	
	//frame variables
	private int bufNum = 2;
	public Graphics g;
	boolean isRunning = false;
	
	//arrow variables
	public boolean mouseLeft, mouseRight, goingLeft, goingRight;
	//1 is left 2 is right
	int currentlyDrawingArrow = 0;
	boolean wasReleased = true;
	private boolean alreadyShot = false;
	private boolean fireLeft, fireRight;
	private float aX = x, aY = y, lastPressed = 0;
	private boolean lClick, rClick, drawArrow;
	
	//melee variables
	private boolean drawSword = false;
	private int swordCount = 0;
	
	//UI variables
	int healthY = 20;
	double healthX = 200;
	int invX = 600;
	int invY = 200;
	private boolean escape, escapePushed;
	
	//variables incorporating main menu
	int gameState = 3;
	boolean canPlay = false;
	boolean play = false;
	boolean canStart;
	boolean start;
	
	//options logic choices
	private boolean canIncreaseWeapon;
	private boolean canDecreaseWeapon;
	private boolean increaseWeapon;
	private boolean decreaseWeapon;
	private boolean canIncreaseDifficulty;
	private boolean canDecreaseDifficulty;
	private boolean increaseDifficulty;
	private boolean decreaseDifficulty;
	
	//play Button
	private static final int playButtonWidth = 200;
	private static final int playButtonHeight = 80;
	private static final int playButtonX = 150;
	private static final int playButtonY = 100;
	private static final int playButtonXLeft = playButtonX;
	private static final int playButtonXRight = playButtonX + playButtonWidth;
	private static final int playButtonYUp = playButtonY;
	private static final int playButtonYDown = playButtonY + playButtonHeight;

	//back Button
//	private static final int backButtonWidth;
//	private static final int backButtonLength;
//	private static final int backButtonX;
//	private static final int backButtonY;
//	private static final int backButtonXLeft;
//	private static final int backButtonXRight;
//	private static final int backButtonYUp;
//	private static final int backButtonYDown;
	
	//arrows
	private static final int arrowWidth = 150;
	private static final int arrowHeight = 80;
	private static final int arrowX = 256;
	private static final int arrow2X = 768;
	private static final int arrowY = 600;
	private static final int arrowXLeft = arrowX;
	private static final int arrowXRight = arrowX + arrowWidth;
	private static final int arrow2XLeft = arrow2X - arrowWidth;
	private static final int arrow2XRight = arrow2X;
	private static final int arrowYUp = arrowY;
	private static final int arrowYDown = arrowY + arrowHeight;
	private static final int arrowY2ndRow = (arrowY - arrowHeight) - 50;
	private static final int arrowY3rdRow = (arrowY2ndRow - arrowHeight) - 50;
	private static final int arrowY2ndRowDown = arrowY2ndRow + arrowHeight;
	private static final int arrowY3rdRowDown = arrowY3rdRow + arrowHeight;
	
	//start Button
//	private static final int startButtonWidth;
//	private static final int startButtonLength;
//	private static final int startButtonX;
//	private static final int startButtonY;
//	private static final int startButtonXLeft;
//	private static final int startButtonXRight;
//	private static final int startButtonYUp;
//	private static final int startButtonYDown;
	
	//AI variables
	float AIVelocityX, AIVelocityY;
	float AIX = x + 300;
	float AIY = y + 5;
	int AIHealth = 200;
	boolean allAIDead;
	String choice;
	private int difficulty = 1;
	private boolean doneEnteringValues;
	int cooldown = 0;
	int currentAI=0;
	int numPlayer;
	int currentPlayer;
	AI[] AIObject;
	int AIDeadCount = 0;
	Player[] playerObject;
	private static final float DAMAGE_AMOUNT = .6f;
	
	Random generator = new Random();;
	private static final int AI_HEALTH_WIDTH_SCALE = 3;
	private static int AI_ORIG_HEALTH;
	public MainGame() {
		while(!doneEnteringValues) {
			choice = JOptionPane.showInputDialog(null, "Would you like a sword or bow?");
			if (choice.equalsIgnoreCase("bow")) {
				attackStyle = 2;
				doneEnteringValues = true;
			} else if (choice.equalsIgnoreCase("sword")){
				attackStyle = 1;
				doneEnteringValues = true;
			}else {
				JOptionPane.showMessageDialog(null, "You did not enter bow or sword");
			}
		}
		
		doneEnteringValues = false;
		while (!doneEnteringValues) {
			choice = JOptionPane.showInputDialog(null, "Easy, medium, or hard?");
			if (choice.equalsIgnoreCase("easy")) {
				difficulty = 1;
				doneEnteringValues = true;
			}else if (choice.equalsIgnoreCase("medium")) {
				difficulty = 2;
				doneEnteringValues = true;
			} else if (choice.equalsIgnoreCase("hard")){
				difficulty = 3;
				doneEnteringValues = true;
			}else {
				JOptionPane.showMessageDialog(null, "You did not enter easy, medium, or hard");
			}
		}
		doneEnteringValues=false;
		while(!doneEnteringValues){
			choice = JOptionPane.showInputDialog(null, "How many AI do you want.");
			int amount = Integer.parseInt(choice);
			if (amount > 0) {
				AIObject = new AI[amount];
				doneEnteringValues = true;
			}else {
				JOptionPane.showMessageDialog(null, "Must be greater than 0.");
			}
		}
		AIHealth*=difficulty;
		AI_ORIG_HEALTH = AIHealth;
		int increaseX = 0;
		int increaseY = 0;
		for(int i=0; i < AIObject.length; i++){
			AIObject[i] = new AI(AIX + increaseX, AIY + increaseY, AIHealth);
			increaseX+=generator.nextInt(500) - 249;
			increaseY-=5;
		}
		//not implemented yet have to incorporate link list
		doneEnteringValues=false;
		while(!doneEnteringValues){
			choice = JOptionPane.showInputDialog(null, "1 player or 2 player. 2 Player does not work as of 3/7/14.");
			int amount = Integer.parseInt(choice);
			if (amount < 0 || amount <= 2) {
				numPlayer = amount;
				playerObject = new Player[amount];
				doneEnteringValues = true;
			}else {
				JOptionPane.showMessageDialog(null, "Must be greater than 0 and less than 10 for this build");
			}
		}
		for(int i=0; i < playerObject.length; i++){
			playerObject[i] = new Player(x,y,healthX);
		}

		base.frame.setVisible(true);
		base.frame.setResizable(false);
		base.frame.setMinimumSize(new Dimension(1024, 768));
		base.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		base.frame.setLocationRelativeTo(null);
		base.frame.addMouseListener(this);
		base.frame.addMouseMotionListener(this);
		base.frame.addKeyListener(this);
		base.frame.setVisible(true);
		base.frame.createBufferStrategy(bufNum);
		base.frame.setIconImage(frameIcon);
		try {
			//startBackgroundMusic();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void run() {
		long currentTime = System.nanoTime();
		//main menu
		while (gameState == 1) {
			updateState();
		}
		//playing
		while (gameState == 2) {
			while (isRunning) {
				if (System.nanoTime() - currentTime >= 6500000) {
					timerTask();
					currentTime = System.nanoTime();
				}
			}
		}
		//options
		while (gameState == 3) {
			options();
		}
	}
	
	public void updateState() {

		if (gameState == 1) {
			if (play) {
				gameState =2;
			}
		}
		paint();
	}
	
	public void options() {
		if (decreaseWeapon || increaseWeapon) {
			if (getAttackStyle() == 1) {
				attackStyle = 2;
			}else{
				attackStyle = 1;
			}
			decreaseWeapon = false;
			increaseWeapon = false;
		} else if (increaseDifficulty) {
			//easy
			if (getDifficulty() == 1) {
				difficulty = 2;
			}else {
				difficulty = 3;
			}
		} else if (decreaseDifficulty) {
			if (getDifficulty() == 3) {
				difficulty = 2;
			}else {
				difficulty = 1;
			}
		}
		paint();
	}
	public int getDifficulty() {
		return difficulty;
	}
	
	public int getAttackStyle() {
		return attackStyle;
	}

	/**
	 * Paint method. Handles all drawing functions
	 * 
	 */
	public void paint() {
		BufferStrategy bf = base.frame.getBufferStrategy();
		// g = null;

		try {
			g = bf.getDrawGraphics();
			g.clearRect(0, 0, 1024, 768);
			if (gameState == 1) {
				g.drawImage(playButton, playButtonX, playButtonY, playButtonWidth, playButtonHeight, this);				
			}else if (gameState == 3){
				int weaponPosX = ((arrow2X-arrowX) /2) + arrowX - 20;
				int weaponPosY = arrowY - 15;
				g.drawImage(Arrow, arrowX, arrowY, arrowWidth, arrowHeight, this);
				g.drawImage(Arrow, arrow2X, arrowY, -arrowWidth, arrowHeight, this);
				g.drawImage(Arrow, arrowX, arrowY2ndRow, arrowWidth, arrowHeight, this);
				g.drawImage(Arrow, arrow2X, arrowY2ndRow, -arrowWidth, arrowHeight, this);
				
				g.setColor(Color.black);
				g.drawString("Weapon", weaponPosX, weaponPosY);
				g.drawString("Difficulty", weaponPosX, (arrowY2ndRow - 15));
				if (getDifficulty() == 1){
					g.drawImage(Easy, weaponPosX, (arrowY2ndRow - 15), (arrow2X - arrowX) - 280, arrowHeight, this);
				}else if (getDifficulty() == 2) {
					g.drawImage(Normal, weaponPosX, (arrowY2ndRow - 15), (arrow2X - arrowX) - 280, arrowHeight, this);
				}else {
					g.drawImage(Hard, weaponPosX, (arrowY2ndRow - 15), (arrow2X - arrowX) - 280, arrowHeight, this);
				}
				if (getAttackStyle() == 2) {
					g.drawImage(Arrowr, weaponPosX -85, weaponPosY + 15, (arrow2X - arrowX) - 280, arrowHeight, this);
				}else {
					//due to the sword using all of the x positions the width is slightly different
					g.drawImage(SwordL, weaponPosX -85, weaponPosY + 15, (arrow2X - arrowX) - 300, arrowHeight, this);
				}
				
			}
			else if ((!escape) && (!dead) &&(!allAIDead)) {
				g.drawImage(picture, 0, 0, base.frame.getWidth(), base.frame.getHeight(), this);
				for (int i = 0; i < playerObject.length; i++) {
					int playerX = (int) playerObject[i].getX();
					int playerY = (int) playerObject[i].getY();
					g.setColor(Color.darkGray);
					// health bar
					g.fillRect(playerX, playerY - 30, 100, healthY);
					g.setColor(Color.red);
					g.fillOval(playerX, playerY, 20, 20);
					g.fillRect(playerX, playerY - 30, (int) (playerObject[i].getHealth()/2) , healthY);
				}
				//AI
				for (int i = 0; i < AIObject.length; i++) {
					if (!AIObject[i].isDead()) {
						int AIRoundedX = (int) AIObject[i].getAIX();
						int AIRoundedY = (int) AIObject[i].getAIY();
						if (AIObject[i].getColor() == null) {
							int red = generator.nextInt(256);
							int green = generator.nextInt(256);
							int blue = generator.nextInt(256);
							Color newColor = new Color(red,green,blue);
							g.setColor(newColor);
							AIObject[i].setColor(newColor);
						} else {
							g.setColor(AIObject[i].getColor());
						}
						g.fillOval(AIRoundedX, AIRoundedY, 20, 20);
						AIDeadCount = 0;
						g.setColor(Color.darkGray);
						g.fillRect(AIRoundedX, AIRoundedY - 40, AI_ORIG_HEALTH/AI_HEALTH_WIDTH_SCALE, healthY);
						g.setColor(AIObject[i].getColor());
						g.fillRect(AIRoundedX, AIRoundedY - 40, (int) AIObject[i].getAIHealth()/AI_HEALTH_WIDTH_SCALE, healthY);
					}else{
						AIDeadCount++;
					}
				}
				// early access banner
				g.drawImage(earlyAccess, 0, 24, this);

				if ((drawArrow == true) && (attackStyle == 2)) {
					inFlight = true;
					shouldPlaySound = true;
					if ((fireLeft) && (currentlyDrawingArrow != 2)) {
						goingLeft = true;
						goingRight = false;
						currentlyDrawingArrow = 1;
						g.drawImage(Arrowl, Math.round(aX), Math.round(aY), this);

					} else if ((fireRight) && (currentlyDrawingArrow != 1)) {
						goingRight = true;
						goingLeft = false;
						currentlyDrawingArrow = 2;
						g.drawImage(Arrowr, Math.round(aX), Math.round(aY), this);
					}
				}
				if ((aX >= 1024) || (!drawArrow) || (aX <= 0)) {
					inFlight = false;
					aX = playerObject[0].getX();
					aY = playerObject[0].getY();
					currentlyDrawingArrow = 0;
					drawArrow = false;
					shouldPlaySound = false;
					notPlayingSound = false;
					alreadyShot = false;
					if (wasReleased) {
						lClick = false;
					}
				}
			if (attackStyle == 1){
				int roundedX = Math.round(playerObject[0].getX());
				int roundedY = Math.round(playerObject[0].getY());
				if (drawSword) {
					swordCount++;
					if (mouseLeft) {
						if (swordCount < 5) {
							g.drawImage(SwordHorizontalL, roundedX - 2, roundedY - 45, this);
						}
						else if (swordCount > 5 && swordCount <=15) {
							g.drawImage(Sword45L, roundedX - 45, roundedY - 30, this);
						}else if (swordCount >15 && swordCount<30) {
							g.drawImage(SwordL, roundedX - 63, roundedY, this);	
						}
						else  if  (swordCount >30 || !drawSword){
							g.drawImage(SwordHorizontalL, roundedX - 2, roundedY - 45, this);
							swordCount = 0;	
						}
					}else {
						if (swordCount < 5) {
							//image flip g.drawImage(SwordHorizontalL, Math.round(x) - 2, Math.round(y) - 45, -SwordHorizontalL.getWidth(this),SwordHorizontalL.getHeight(this),this);
							g.drawImage(SwordHorizontalL, roundedX + 20, roundedY - 45,
									-SwordHorizontalL.getWidth(this),SwordHorizontalL.getHeight(this),this);
						}
						else if (swordCount > 5 && swordCount <=15) {
							g.drawImage(Sword45L, roundedX + 80, roundedY - 30,
									-Sword45L.getWidth(this), Sword45L.getHeight(this), this);
						}else if (swordCount >15 && swordCount<30) {
							g.drawImage(SwordL, roundedX +90, roundedY, -SwordL.getWidth(this), SwordL.getHeight(this), this);	
						}
						else  if  (swordCount >30 || !drawSword){
							g.drawImage(SwordHorizontalL, roundedX + 20, roundedY - 45,
									-SwordHorizontalL.getWidth(this), SwordHorizontalL.getHeight(this), this);
							swordCount = 0;	
						}
					}
				}
				else {
					if(mouseLeft) {
						g.drawImage(SwordHorizontalL, roundedX - 2, roundedY - 45, this);
					}else{
						g.drawImage(SwordHorizontalL, roundedX + 20, roundedY - 45,
								-SwordHorizontalL.getWidth(this), SwordHorizontalL.getHeight(this), this);
					}
					swordCount = 0;
				}
				if (wasReleased) {
					lClick = false;
				}
			}
			}else if (escape) {
				g.setColor(Color.white);
				g.drawRect(0, 0, 1024, 768);
				g.setColor(Color.black);
				g.drawString("PAUSED", 512, 389);
			}
			else if (dead) {
				g.setColor(Color.black);
				g.drawRect(0, 0, 1024, 768);
				g.drawString("Dead", 512, 389);
			} else if(allAIDead) {
				g.setColor(Color.black);
				g.drawRect(0, 0, 1024, 768);
				g.drawString("You win", 512, 389);
			}

		} finally {
			g.dispose();
		}
		// Shows the contents of the backbuffer on the screen.
		bf.show();
	}


	/**
	 * Starts up the game
	 */
	public void start() {
		isRunning = true;
		new Thread(this).start();
	}

	public void stop() {
		isRunning = false;
	}

	public void mouseDragged(MouseEvent e) {

	}

	public void mouseMoved(MouseEvent e) {
		if (e.getX() > playerObject[0].getX()) {
			mouseLeft = false;
			mouseRight = true;
		}
		if (e.getX() < playerObject[0].getX()) {
			mouseRight = false;
			mouseLeft = true;
		}
		if (gameState == 2) {
			
		}
		//main menu
		else if (gameState == 1) {
			if (e.getX() >playButtonXLeft && e.getX() < playButtonXRight && e.getY() > playButtonYUp && e.getY() < playButtonYDown) {
				canPlay = true;
			}else {
				canPlay = false;
			}
		}
		//options
		else if (gameState == 3) {
			if (e.getX() > arrowXLeft && e.getX() < arrowXRight && e.getY() > arrowYUp && e.getY() < arrowYDown) {
				canDecreaseWeapon = true;
			}else {
				canDecreaseWeapon = false;
			}
			if (e.getX() > arrow2XLeft && e.getX() < arrow2XRight && e.getY() > arrowYUp && e.getY() < arrowYDown) {
				canIncreaseWeapon = true;
			}else {
				canIncreaseWeapon = false;
			}
			
			
			if (e.getX() > arrowXLeft && e.getX() < arrowXRight && e.getY() > arrowY2ndRow && e.getY() < arrowY2ndRowDown) {
				canDecreaseDifficulty = true;
			}else {
				canDecreaseDifficulty = false;
			}
			if (e.getX() > arrow2XLeft && e.getX() < arrow2XRight && e.getY() > arrowY2ndRow && e.getY() < arrowY2ndRowDown) {
				canIncreaseDifficulty = true;
			}else {
				canIncreaseDifficulty = false;
			}
		}

	}

	public void mouseClicked(MouseEvent click) {
		if (click.getButton() == 1) {

		}

	}

	public void mouseEntered(MouseEvent e) {

	}

	public void mouseExited(MouseEvent e) {

	}

	/**
	 * This method handles mouse clicks
	 */
	public void mousePressed(MouseEvent click) {
		if ((SwingUtilities.isLeftMouseButton(click))) {
			lClick = true;
			wasReleased = false;
			if(gameState == 2){
				
			}
			else if ((gameState == 1) && canPlay) {
				play = true;
			}else if (gameState == 3 && (canStart)) {
				start = true;
			}else if (gameState ==3 && (canIncreaseWeapon)) {
				increaseWeapon = true;
			}else if (gameState == 3 && (canDecreaseWeapon)) {
				decreaseWeapon = true;
			}else if (gameState == 3 && canIncreaseDifficulty) {
				increaseDifficulty = true;
			}else if (gameState == 3 && canDecreaseDifficulty) {
				decreaseDifficulty = true;
			}
			if (!alreadyShot) {
				arrowMech();
				aY = playerObject[0].getY();
			}
			alreadyShot = true;
		}
		else if (SwingUtilities.isRightMouseButton(click)) {
			rClick = true;
		}
	}

	/**
	 * This method handles the release of mouse clicks
	 */
	public void mouseReleased(MouseEvent release) {
		if (SwingUtilities.isLeftMouseButton(release)) {
			wasReleased = true;
		}

		else if (SwingUtilities.isRightMouseButton(release)) {
			rClick = false;
		}
	}

	/**
	 * This method handle the movement for the character and all other key binds
	 */
	public void keyPressed(KeyEvent e) {
		// left arrow
		if (e.getKeyCode() == 37 || e.getKeyCode() == 65) {
			left = true;
			lastPressed = 2;
		}
		// up arrow
		if (e.getKeyCode() == 38 || e.getKeyCode() == 87) {
			//checks if grounded if it is then it allows you to jump
			if (playerObject[0].isGrounded()) {
				playerObject[0].setVelocityY(-12);
			}
			
		}
		// right arrow
		if (e.getKeyCode() == 39 || e.getKeyCode() == 68) {
			right = true;
			lastPressed = 1;
		}
		// escape key
		if ((e.getKeyCode() == 27) && (!escape) && (!escapePushed) && (!dead)) {
			escape = true;
			escapePushed = true;
			paint();
		} else if ((e.getKeyCode() == 27) && (escape) && (!escapePushed) && (!dead)) {
			escape = false;
			escapePushed = true;
		}
		
	}

	/**
	 * This method handles the stopping of movement for the character and
	 * stoping all other key binds
	 */
	public void keyReleased(KeyEvent e) {
		// left arrow
		if (e.getKeyCode() == 37 || e.getKeyCode() == 65) {
			left = false;
			if (right) {
				lastPressed = 1;
			}

		}
		// up arrow
		if (e.getKeyCode() == 38 || e.getKeyCode() == 87) {
			
		}
		// right arrow
		if (e.getKeyCode() == 39 || e.getKeyCode() == 68) {
			right = false;
			if (left) {
				lastPressed = 2;
			}
		}
		if ((e.getKeyCode() == 27)  && (escapePushed)) {
			escapePushed = false;
		}
	}

	public void keyTyped(KeyEvent e) {

	}

	/**
	 * Melee attack style
	 */
	public void melee() {
		// swing
		//cooldown doesnt work with melee. Way it works is it 
		//effectively stops render with melee there is animation 
		//so doesnt work
		if (lClick == true) {
			drawSword = true;
		} else{
			drawSword = false;
		}

		if (rClick == true) {
			// block
			if (shield == true) {

			}
			// reg attack will be copy pasted in here
			else {

			}
		}

	}

	/**
	 * Range attack style
	 */
	public void range() {
		// shoot
		if (lClick == true && cooldown == 0) {
			drawArrow = true;
		} else {
			drawArrow = false;
		}
	}

	/**
	 * Magic attack style
	 */
	public void magic() {
		// mage primary hand
		if (lClick == true) {

		}
		// mage offhand
		if (rClick == true) {

		}
	}
	public void arrowMech() {
		if ((mouseLeft == true)) {
			fireLeft = true;
			fireRight = false;
		}
		if ((mouseRight == true)) {
			fireRight = true;
			fireLeft = false;
		}
	}

	public void stopMusic() {
		backgroundMusic.stop();
	}

	public void attackSound() throws Exception {
		if (attackStyle == 1) {
			//play attack sound
		} else if (attackStyle == 2) {
			attackIn = AudioSystem.getAudioInputStream(getClass().getResource(
					"/resources/bowsound2.wav"));
			attackClip = AudioSystem.getClip();
			try {
				attackClip.open(attackIn);
			} catch (LineUnavailableException e) {
				e.printStackTrace();
			}
		} else {

		}
		attackClip.start();
		notPlayingSound = true;
	}
	public void startBackgroundMusic() throws Exception {
		backgroundGame = AudioSystem.getAudioInputStream(getClass().getResource(
				"/resources/background music.wav"));
		backgroundMusic = AudioSystem.getClip();
		try {
			backgroundMusic.open(backgroundGame);
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
		backgroundMusic.loop(278);
	}

	public void timerTask() {
		if (AIDeadCount >= AIObject.length) {
			allAIDead = true;
			paint();
		} else if (dead) {
			paint();
		} else if(escape) {
			paint();
		}
		//if ((!escape) && (!dead) && (!allAIDead))
		else {
			//player1.setVelocityY(gravity);
			for (int i=0; i < playerObject.length; i++) {	
				// moves left
				if ((left == true) && (playerObject[i].getVelocityX() > -5) && (lastPressed == 2)) {
					playerObject[i].setVelocityX(-1);
				}
				// moves right
				if ((right == true) && (playerObject[i].getVelocityX() < 5) && (lastPressed == 1)) {
					playerObject[i].setVelocityX(1);
				}
				if ((!right) && (!left) && (playerObject[i].isGrounded()) && (playerObject[i].getVelocityX() > 0)) {
					playerObject[i].setVelocityX(-.3f);
					if (Math.abs(playerObject[i].getVelocityX()) < .29) {
						playerObject[i].setVelocityX(0);
					}
				} else if ((!right) && (!left) && (playerObject[i].isGrounded()) && (playerObject[i].getVelocityX() < 0)) {
					playerObject[i].setVelocityX(.3f);
					if (Math.abs(playerObject[i].getVelocityX()) < .29) {
						playerObject[i].setVelocityX(0);
					}
				}
				playerObject[i].move();
			}
			if (y >= 590) {
				isGrounded = true;
				velocityY = 0;
				y = 590;
			} else {
				isGrounded = false;
			}
			if (goingLeft == true) {
				aX-=7;
			}
			if (goingRight == true) {
				aX+=7;
			}
			

			if (attackStyle == 1) {
				melee();
			} else if (attackStyle == 2) {
				range();
			} else {
				magic();
			}
			if ((shouldPlaySound) && (!notPlayingSound)) {
				try {
					attackSound();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (cooldown>0) {
				cooldown--;
			}	
			AIMove();
			AIDamage();
			paint();
		}
	}
	
	/**
	 * Method that handles damage to AI objects. Fairly long and only bow portion works.
	 */
	public void AIDamage() {
		if (attackStyle == 2) {
			for(int i = 0; i < AIObject.length; i++) {
				if ((inFlight) && (Math.abs(aX-AIObject[i].getAIX()) < 5) && (Math.abs(aY-AIObject[i].getAIY())) < 4 ) {
					AIObject[i].setHealth(-25);
					drawArrow = false;
					cooldown = 15;
				}
			}
		}else {
			if (mouseLeft) {
				for(int i = 0; i < AIObject.length; i++){
					if ((drawSword) && (cooldown ==0) && (x -AIObject[i].getAIX() <85) && (x-AIObject[i].getAIX() >-1) && (y - AIObject[i].getAIY() < 20) && (y-AIObject[i].getAIY()>-5)) {
						AIObject[i].setHealth(-50);
						cooldown = 5;
					}
				}
			}else {
				for(int i=0; i < AIObject.length; i++){
					if ((drawSword) && (cooldown == 0) && (AIObject[i].getAIX()-x <85) && (AIObject[i].getAIX() - x > -1) && (y - AIObject[i].getAIY() < 20) && (y-AIObject[i].getAIY()>-5)) {
						AIObject[i].setHealth(-50);
						cooldown = 5;
					}
				}
			}
		}
		for(int i=0; i < AIObject.length; i++){
			//checks if AI is close enough to attack with melee
			for (int j = 0; j < playerObject.length; j++) {
				if ((Math.abs(AIObject[i].getAIX() - playerObject[j].getX()) <= 10) && (Math.abs(AIObject[i].getAIY() - playerObject[j].getY())) <= 3) {
					playerObject[j].setHealth(DAMAGE_AMOUNT * difficulty);
					if (playerObject[j].getHealth() <= 0) {
						dead  = true;
					}
				}
			}
		}
	}
	public void AIMove() {
		for(int i = 0; i < AIObject.length; i ++){
			for(int j = 0; j < playerObject.length; j++) {
				if ((AIObject[i].getAIX() > playerObject[j].getX()) && (AIObject[i].getAIVelocityX() >-4)) {
					if(Math.abs(AIObject[i].getAIX() - playerObject[j].getX()) <= 10) {
						AIObject[i].setAIVelocityX(0);
					}
					else {
						AIObject[i].setAIVelocityX(-.7f);
					}
				}else if ((AIObject[i].getAIX() < playerObject[j].getX()) && (AIObject[i].getAIVelocityX() < 4)) {
					if(Math.abs(AIObject[i].getAIX() - playerObject[j].getX()) <= 10) {
						AIObject[i].setAIVelocityX(0);
					}
					else {
						AIObject[i].setAIVelocityX(.7f);
					}
				}
				if ((AIObject[i].getAIY() > playerObject[j].getX()) && (playerObject[j].isGrounded())) {
					if (AIObject[i].isAIGrounded()) {
						AIObject[i].setAIVelocityY(-.5f);
					}
				}
				AIObject[i].move();
			}
		}
	}
}
//FIN
