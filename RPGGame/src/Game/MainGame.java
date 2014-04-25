/**
 * @author Anthony Foster + David Wigley
 * @music Andrew Zucker + Jake Berlandi
 */
package Game;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
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
	ImageIcon EasyIcon = new ImageIcon(getClass().getResource("/resources/easy_new.jpg"));
	Image Easy = EasyIcon.getImage();
	ImageIcon NormalIcon = new ImageIcon(getClass().getResource("/resources/medium_new.jpg"));
	Image Normal = NormalIcon.getImage();
	ImageIcon HardIcon = new ImageIcon(getClass().getResource("/resources/hard_new.jpg"));
	Image Hard = HardIcon.getImage();
	ImageIcon ShieldIcon = new ImageIcon(getClass().getResource("/resources/shield.png"));
	Image shieldImage = ShieldIcon.getImage();


	//music variables
	AudioInputStream buttonIn;
	Clip buttonSound;
	boolean playButtonSound;
	AudioInputStream attackIn;
	Clip attackClip;
	AudioInputStream backgroundGame;
	Clip backgroundMusic;
	boolean inFlight;
	boolean notPlayingSound, shouldPlaySound;

	//player variables
	int attackStyle = 1;
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
	int gameState = 1;
	boolean canPlay = false;
	boolean canPlayOptions = false;
	boolean playOptions = false;
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
	private boolean canIncreaseAI;
	private boolean canDecreaseAI;
	private boolean increaseAI;
	private boolean decreaseAI;
	private boolean canChangePlayer;
	private boolean changePlayer;

	//play Button options
	private static final int playButtonWidth = 200;
	private static final int playButtonHeight = 80;
	private static final int playButtonX = 800;
	private static final int playButtonY = 40;
	private static final int playButtonXLeft = playButtonX;
	private static final int playButtonXRight = playButtonX + playButtonWidth;
	private static final int playButtonYUp = playButtonY;
	private static final int playButtonYDown = playButtonY + playButtonHeight;

	//play Button main menu
	private static final int playButtonWidthM = 500;
	private static final int playButtonHeightM = 200;
	private static final int playButtonXM = 40;
	private static final int playButtonYM = 40;
	private static final int playButtonXLeftM = playButtonXM;
	private static final int playButtonXRightM = playButtonXM + playButtonWidthM;
	private static final int playButtonYUpM = playButtonYM;
	private static final int playButtonYDownM = playButtonYM + playButtonHeightM;

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
	private static final int arrowY4thRow = (arrowY3rdRow - arrowHeight) - 50;
	private static final int arrowY2ndRowDown = arrowY2ndRow + arrowHeight;
	private static final int arrowY3rdRowDown = arrowY3rdRow + arrowHeight;
	private static final int arrowY4thRowDown = arrowY4thRow + arrowHeight;

	private static final int difficultyXPos = ((arrow2X-arrowX) /2) + arrowX - 105;
	private static final int difficultyWidth = (arrow2X - arrowX) - 300;

	//image constants
	private final int swordHorizontalLWidth = (int) SwordHorizontalL.getWidth(this);
	private final int swordHorizontalLHeight = (int) SwordHorizontalL.getHeight(this);
	private final int swordLWidth = (int) SwordL.getWidth(this);
	private final int swordLHeight = (int) SwordL.getHeight(this);
	private final int sword45LWidth = (int) Sword45L.getWidth(this);
	private final int sword45LHeight = (int) Sword45L.getHeight(this);
	private final int shieldWidth = (int) shieldImage.getWidth(this);
	private final int shieldHeight = (int) shieldImage.getHeight(this);
	private final double shieldScaleFactor = .75;
	private final int scaledShieldHeight = (int) (shieldHeight * shieldScaleFactor);
	//Font
	Font normal = new Font("Serif", Font.PLAIN, 12);
	Font numbers = new Font("Serif", Font.BOLD, 100);

	//AI variables
	float AIVelocityX, AIVelocityY;
	float AIX = x + 300;
	float AIY = y + 5;
	int AIHealth = 200;
	boolean allAIDead;
	private int difficulty = 1;
	int cooldown = 0;
	int currentPlayer = 1;
	AI[] AIObject;
	private int AINum = 0;
	int AIDeadCount = 0;
	Player[] playerObject;
	private static final float DAMAGE_AMOUNT = .6f;
	private double damageReduction = 1;
	Random generator = new Random();;
	private static final int AI_HEALTH_WIDTH_SCALE = 3;
	private static int AI_ORIG_HEALTH;
	public MainGame() {
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
	}

	public void run() {
		long currentTime = System.nanoTime();
		//main menu
		while (gameState == 1) {
			updateState();
		}
		//options
		while (gameState == 3) {
			options();
		}
		//playing
		while (gameState == 2) {
			try {
				startBackgroundMusic();
			} catch (Exception e) {
				e.printStackTrace();
			}
			while (isRunning) {
				if (System.nanoTime() - currentTime >= 6500000) {
					timerTask();
					currentTime = System.nanoTime();
				}
			}
		}
	}

	public void updateState() {
		if (playOptions) {
			gameState = 3;
			playOptions = false;
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
			//sets to normal
			if (getDifficulty() == 1) {
				difficulty = 2;
			}else {
				//sets to hard
				difficulty = 3;
			}
			increaseDifficulty = false;
		} else if (decreaseDifficulty) {
			//sets to normal
			if (getDifficulty() == 3) {
				difficulty = 2;
			}else {
				//sets to easy
				difficulty = 1;
			}
			decreaseDifficulty = false;
		} else if (increaseAI) {
			AINum++;
			increaseAI = false;
		} else if (decreaseAI && (AINum > 0)) {
			AINum--;
			decreaseAI = false;
		} else if(changePlayer) {
			if (currentPlayer == 1) {
				currentPlayer = 2;
			} else {
				currentPlayer = 1;
			}
			changePlayer=false;
		} else if (play) {
			initialize();
		}
		paint();
	}


	public void initialize() {
		AIObject = new AI[AINum];
		AIHealth*=difficulty;
		AI_ORIG_HEALTH = AIHealth;
		int increaseX = 0;
		for(int i=0; i < AIObject.length; i++){
			AIObject[i] = new AI(AIX + increaseX, AIY, AIHealth);
			increaseX+=generator.nextInt(500) - 249;
		}
		//not implemented yet have to redo some logic
		playerObject = new Player[currentPlayer];
		for(int i=0; i < playerObject.length; i++){
			playerObject[i] = new Player(x,y,healthX);
		}
		if(difficulty == 1) {
			damageReduction = 1;
		}else if (difficulty ==2) {
			damageReduction = .85;
		}else {
			damageReduction = .7;
		}
		gameState = 2;
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
		try {
			g = bf.getDrawGraphics();
			g.clearRect(0, 0, 1024, 768);
			g.setFont(normal);
			if (gameState == 1) {
				g.drawImage(playButton, playButtonXM, playButtonYM, playButtonWidthM, playButtonHeightM, this);				
			}else if (gameState == 3){
				int weaponPosX = ((arrow2X-arrowX) /2) + arrowX - 20;
				int weaponPosY = arrowY - 15;
				g.drawImage(Arrow, arrowX, arrowY, arrowWidth, arrowHeight, this);
				g.drawImage(Arrow, arrow2X, arrowY, -arrowWidth, arrowHeight, this);
				g.drawImage(Arrow, arrowX, arrowY2ndRow, arrowWidth, arrowHeight, this);
				g.drawImage(Arrow, arrow2X, arrowY2ndRow, -arrowWidth, arrowHeight, this);
				g.drawImage(Arrow, arrowX, arrowY3rdRow, arrowWidth, arrowHeight, this);
				g.drawImage(Arrow, arrow2X, arrowY3rdRow, -arrowWidth, arrowHeight, this);
				g.drawImage(Arrow, arrowX, arrowY4thRow, arrowWidth, arrowHeight, this);
				g.drawImage(Arrow, arrow2X, arrowY4thRow, -arrowWidth, arrowHeight, this);
				g.drawImage(playButton, playButtonX, playButtonY, playButtonWidth, playButtonHeight, this);

				//text boxes above choices
				g.setColor(Color.black);
				g.drawString("Weapon", weaponPosX, weaponPosY);
				g.drawString("Difficulty", weaponPosX, (arrowY2ndRow - 15));
				g.drawString("Number of AI", weaponPosX - 12, (arrowY3rdRow - 15));
				g.drawString("Number of players", weaponPosX - 20, (arrowY4thRow - 15));

				if (getDifficulty() == 1){
					g.drawImage(Easy, difficultyXPos, arrowY2ndRow, difficultyWidth , arrowHeight, this);
				}else if (getDifficulty() == 2) {
					g.drawImage(Normal, difficultyXPos, arrowY2ndRow, difficultyWidth, arrowHeight, this);
				}else {
					g.drawImage(Hard, difficultyXPos, arrowY2ndRow, difficultyWidth, arrowHeight, this);
				}
				if (getAttackStyle() == 2) {
					g.drawImage(Arrowr, weaponPosX -85, weaponPosY + 15, (arrow2X - arrowX) - 280, arrowHeight, this);
				}else {
					//due to the sword using all of the x positions the width is slightly different
					g.drawImage(SwordL, weaponPosX -85, weaponPosY + 15, (arrow2X - arrowX) - 300, arrowHeight, this);
				}
				int AIXPos = weaponPosX;
				if (AINum >= 10 && AINum<100) {
					AIXPos -=25;
				} else if (AINum >= 100 && AINum < 1000) {
					//the reason why this grows by 40 instead of an additional 20 is because we are not storing the variable
					//of current position anywhere so we just add another 20 onto the decrease
					AIXPos-=50;
				} else if (AINum >= 1000) {
					AIXPos-=75;
				}
				g.setFont(numbers);
				String currentNum = Integer.toString(AINum);
				String currentPlayerString = Integer.toString(currentPlayer);
				g.drawString(currentNum, AIXPos, arrowY3rdRow + 72);
				g.drawString(currentPlayerString, weaponPosX, arrowY4thRow + 72);
			}
			else {
				if ((!escape) && (!dead) &&(!allAIDead)) {
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

					if (attackStyle == 2) {
						if (drawArrow == true) {
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
					}
					else {
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
											-swordHorizontalLWidth,swordHorizontalLWidth,this);
								}
								else if (swordCount > 5 && swordCount <=15) {
									g.drawImage(Sword45L, roundedX + 80, roundedY - 30,
											-sword45LWidth, sword45LHeight, this);
								}else if (swordCount >15 && swordCount<30) {
									g.drawImage(SwordL, roundedX +90, roundedY, -swordLWidth, swordLHeight, this);	
								}
								else  if  (swordCount >30 || !drawSword){
									g.drawImage(SwordHorizontalL, roundedX + 20, roundedY - 45,
											-swordHorizontalLWidth, swordHorizontalLHeight, this);
									swordCount = 0;	
								}
							}
						} else if (shield) {
							if (mouseLeft) {
								g.drawImage(shieldImage, roundedX - 5, roundedY - 5, shieldWidth, scaledShieldHeight, this);
							} else {
								g.drawImage(shieldImage, roundedX + 5, roundedY - 5, shieldWidth, scaledShieldHeight, this);
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
		if(gameState == 2) {
			if (e.getX() > playerObject[0].getX()) {
				mouseLeft = false;
				mouseRight = true;
			} else if (e.getX() < playerObject[0].getX()) {
				mouseLeft = true;
				mouseRight = false;
			}
		}
	}

	public void mouseMoved(MouseEvent e) {
		if (gameState == 2) {
			if (e.getX() > playerObject[0].getX()) {
				mouseLeft = false;
				mouseRight = true;
			}
			else if (e.getX() < playerObject[0].getX()) {
				mouseRight = false;
				mouseLeft = true;
			}	
		}
		//main menu
		else if (gameState == 1) {
			if (e.getX() >playButtonXLeftM && e.getX() < playButtonXRightM && e.getY() > playButtonYUpM && e.getY() < playButtonYDownM) {
				canPlayOptions = true;
			}else {
				canPlayOptions = false;
			}
		}
		//options
		else if (gameState == 3) {
			//effficiency idea:
			//check left side. else if right side else clear them both. Should save some CPU cycles
			if (e.getX() >playButtonXLeft && e.getX() < playButtonXRight && e.getY() > playButtonYUp && e.getY() < playButtonYDown) {
				canPlay = true;
			}else {
				canPlay = false;
			}
			
			if (e.getX() > arrowXLeft && e.getX() < arrowXRight && (e.getY() > arrowYUp) && (e.getY() < arrowYDown)) {
				canDecreaseWeapon = true;
			}else {
				canDecreaseWeapon = false;
			}
			if (e.getX() > arrow2XLeft && e.getX() < arrow2XRight && (e.getY() > arrowYUp) && (e.getY() < arrowYDown)) {
				canIncreaseWeapon = true;
			}else {
				canIncreaseWeapon = false;
			}


			if (e.getX() > arrowXLeft && e.getX() < arrowXRight && (e.getY() > arrowY2ndRow) && (e.getY() < arrowY2ndRowDown)) {
				canDecreaseDifficulty = true;
			}else {
				canDecreaseDifficulty = false;
			}
			if (e.getX() > arrow2XLeft && e.getX() < arrow2XRight && (e.getY() > arrowY2ndRow) && (e.getY() < arrowY2ndRowDown)) {
				canIncreaseDifficulty = true;
			}else {
				canIncreaseDifficulty = false;
			}

			if (e.getX() > arrowXLeft && e.getX() < arrowXRight && e.getY() > arrowY3rdRow && e.getY() < arrowY3rdRowDown) {
				canDecreaseAI = true;
			} else {
				canDecreaseAI = false;
			}
			if (e.getX() > arrow2XLeft && e.getX() < arrow2XRight && e.getY() > arrowY3rdRow && e.getY() < arrowY3rdRowDown) {
				canIncreaseAI = true;
			} else {
				canIncreaseAI = false;
			}

			if(e.getY() > arrowY4thRow && e.getY() < arrowY4thRowDown) {
				if (e.getX() > arrowXLeft && e.getX() < arrowXRight) {
					canChangePlayer = true;
				}else if (e.getX() > arrow2XLeft && e.getX() < arrow2XRight) {
					canChangePlayer = true;
				}else {
					canChangePlayer = true;
				}
			}

		}

	}

	/**
	 * Method used to handle complete mouse clicks (click and release). For the moment it is only used while in the options and main
	 * menu so I do a conditional check to make sure you are in the to even care about running this.
	 */
	public void mouseClicked(MouseEvent click) {
		if (click.getButton() == 1 && (gameState == 3)) {
			try {
				startButtonSound();
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (canPlay) {
				play = true;
			}else if (canStart) {
				start = true;
			}else if (canIncreaseWeapon) {
				increaseWeapon = true;
			}else if (canDecreaseWeapon) {
				decreaseWeapon = true;
			}else if (canIncreaseDifficulty) {
				increaseDifficulty = true;
			}else if (canDecreaseDifficulty) {
				decreaseDifficulty = true;
			}else if (canDecreaseAI) {
				decreaseAI = true;
			}else if (canIncreaseAI) {
				increaseAI = true;
			}else if (canChangePlayer) {
				changePlayer = true;
			}
		}else if (gameState == 1) {
			if (canPlayOptions) {
				playOptions = true;
			}
			try {
				startButtonSound();
			} catch (Exception e) {
				e.printStackTrace();
			}
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
		if (gameState == 2) {
			if ((SwingUtilities.isLeftMouseButton(click))) {
				lClick = true;
				wasReleased = false;
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
			rClick=false;
		} else{
			drawSword = false;
		}

		if (rClick == true) {
			// block
			shield = true;
		}
		else {
			shield = false;
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
	public void stopButton() {
		buttonSound.stop();
	}
	public void startButtonSound() throws Exception {
		buttonIn = AudioSystem.getAudioInputStream(getClass().getResource(
				"/resources/buttonSound.wav"));
		buttonSound = AudioSystem.getClip();
		try {
			buttonSound.open(buttonIn);
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
		buttonSound.start();
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
					AIObject[i].setHealth(-25 * damageReduction);
					drawArrow = false;
					cooldown = 15;
				}
			}
		}else {
			if (mouseLeft) {
				for(int i = 0; i < AIObject.length; i++){
					if ((drawSword) && (cooldown == 0) && (playerObject[0].getX() -AIObject[i].getAIX() <85) && (playerObject[0].getX()-AIObject[i].getAIX() >-1) && (playerObject[0].getY() - AIObject[i].getAIY() < 20) && (playerObject[0].getY()-AIObject[i].getAIY()>-5)) {
						AIObject[i].setHealth(-50 * damageReduction);
						cooldown = 5;
					}
				}
			}else {
				for(int i=0; i < AIObject.length; i++){
					if ((drawSword) && (cooldown == 0) && (AIObject[i].getAIX()-playerObject[0].getX() <85) && (AIObject[i].getAIX() - playerObject[0].getX() > -1) && (playerObject[0].getY() - AIObject[i].getAIY() < 20) && (playerObject[0].getY()-AIObject[i].getAIY()>-5)) {
						AIObject[i].setHealth(-50 * damageReduction);
						cooldown = 5;
					}
				}
			}
		}
		for(int i=0; i < AIObject.length; i++){
			//checks if AI is close enough to attack with melee
			for (int j = 0; j < playerObject.length; j++) {
				if ((!shield) && (Math.abs(AIObject[i].getAIX() - playerObject[j].getX()) <= 10) && (Math.abs(AIObject[i].getAIY() - playerObject[j].getY())) <= 3) {
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
				if (!AIObject[i].isDead()){
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
}
//FIN
