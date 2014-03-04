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
	int healthX = 200;
	int invX = 600;
	int invY = 200;
	private boolean UI = true;
	private boolean escape, escapePushed;

	
	//AI variables
	float AIVelocityX, AIVelocityY;
	float AIX = x + 300;
	float AIY = y + 5;
	int AIHealth = 200;
	boolean AI1Dead = true, AI2Dead = true, AI3Dead = true, AI4Dead = true, AI5Dead = true,AI6Dead = true,
			AI7Dead = true, AI8Dead = true, AI9Dead = true, AI10Dead = true;
	boolean allAIDead;
	String choice;
	private int difficulty = 1;
	private boolean doneEnteringValues;
	int cooldown = 0;
	int numAI;
	AI AI1,AI2,AI3,AI4,AI5,AI6,AI7,AI8,AI9,AI10;
	int currentAI=0;
	
	
	//custom colors
	Color orangeRed;
	Color lightGreen;
	Color zebPurple;
	Color cyan;
	Color pink;
	Color darkGold;
	Color steelBlue;
	Color gray;
	Color yellow;
	Random generator = new Random();
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
			choice = JOptionPane.showInputDialog(null, "How many AI do you want. Max of 10. Note may not work");
			int amount = Integer.parseInt(choice);
			if (amount < 0 || amount <= 10) {
				numAI = amount;
				doneEnteringValues = true;
			}else {
				JOptionPane.showMessageDialog(null, "Must be greater than 0 and less than 10 for this build");
			}
		}
		AIHealth*=difficulty;
		AI_ORIG_HEALTH = AIHealth;
		int increaseX = 0;
		int increaseY = 0;
		while(currentAI<numAI) {
			if (currentAI == 0) {
				AI1= new AI(AIX, AIY, AIHealth);
				AI1Dead = false;
			}else if (currentAI == 1) {
				AI2 = new AI(AIX + increaseX,AIY + increaseY,AIHealth);
				AI2Dead = false;
				orangeRed = new Color(238, 64, 0);
			}else if (currentAI ==2) {
				AI3 = new AI(AIX + increaseX,AIY + increaseY,AIHealth);
				AI3Dead = false;
				lightGreen = new Color(0,238,0);
			}else if (currentAI == 3) {
				AI4 = new AI(AIX + increaseX, AIY + increaseY, AIHealth);
				AI4Dead = false;
				zebPurple = new Color(47,11,37);
			}else if (currentAI == 4) {
				AI5 = new AI(AIX + increaseX, AIY + increaseY, AIHealth);
				AI5Dead = false;
				cyan = new Color(0,205,205);
			}else if (currentAI == 5) {
				AI6 = new AI(AIX + increaseX, AIY + increaseY, AIHealth);
				AI6Dead = false;
				pink = new Color(238,18,137);
			}else if (currentAI == 6) {
				AI7 = new AI(AIX + increaseX, AIY + increaseY, AIHealth);
				AI7Dead = false;
				darkGold = new Color(205,149,12);
			}else if (currentAI == 7) {
				AI8 = new AI(AIX + increaseX, AIY + increaseY, AIHealth);
				AI8Dead = false;
				steelBlue = new Color(35,107,142);
			}else if (currentAI == 8) {
				AI9 = new AI(AIX + increaseX, AIY + increaseY, AIHealth);
				AI9Dead = false;
				gray = new Color(128,128,128);
			}else if (currentAI == 9) {
				AI10 = new AI(AIX + increaseX, AIY + increaseY, AIHealth);
				AI10Dead = false;
				yellow = new Color(255,255,0);
			}
			currentAI++;
			increaseX+=generator.nextInt(500) - 249;
			increaseY-=5;
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
			startBackgroundMusic();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void run() {
		long currentTime = System.nanoTime();
		// setup timer task

		while (isRunning) {
			if (System.nanoTime() - currentTime >= 6500000) {
				timerTask();
				currentTime = System.nanoTime();
			}
		}
	}

	/**
	 * Paint method. Handles drawing for the game.
	 */
	public void paint() {
		BufferStrategy bf = base.frame.getBufferStrategy();
		// g = null;

		try {
			g = bf.getDrawGraphics();
			g.clearRect(0, 0, 1024, 768);
			if ((!escape) && (!dead) &&(!allAIDead)) {
				g.drawImage(picture, 0, 0, base.frame.getWidth(), base.frame.getHeight(), this);
				g.setColor(Color.red);
				g.fillOval(Math.round(x), Math.round(y), 20, 20);
				// toggles UI
				if (UI) {
					g.setColor(Color.darkGray);
					// health bar
					g.fillRect(900, 40, 100, healthY);
					g.setColor(Color.red);
					g.fillRect(900, 40, (healthX/2), healthY);
					// draws string health bar. In future may want to make that
					// an image so it can be bigger
					g.setColor(Color.black);
					g.drawString("Health Bar", 900, 38);
				}
				
				//AI
				if (currentAI == 1) {
					if (!AI1Dead){
						g.setColor(Color.blue);
						g.fillOval(Math.round(AI1.getAIX()), Math.round(AI1.getAIY()), 20, 20);
						int AI1X = (int) AI1.getAIX();
						int AI1Y = (int) AI1.getAIY() - 30;
						g.setColor(Color.darkGray);
						g.fillRect(AI1X, AI1Y, AI_ORIG_HEALTH/AI_HEALTH_WIDTH_SCALE, healthY);
						g.setColor(Color.blue);
						g.fillRect(AI1X, AI1Y, (int) AI1.getAIHealth()/AI_HEALTH_WIDTH_SCALE, healthY);
					}
				} else if (currentAI == 2) {
					if (!AI1Dead) {
						g.setColor(Color.blue);
						g.fillOval(Math.round(AI1.getAIX()), Math.round(AI1.getAIY()), 20, 20);
						int AI1X = (int) AI1.getAIX();
						int AI1Y = (int) AI1.getAIY() - 30;
						g.setColor(Color.darkGray);
						g.fillRect(AI1X, AI1Y, AI_ORIG_HEALTH/AI_HEALTH_WIDTH_SCALE, healthY);
						g.setColor(Color.blue);
						g.fillRect(AI1X, AI1Y, (int) AI1.getAIHealth()/AI_HEALTH_WIDTH_SCALE, healthY);
					}
					if (!AI2Dead) {
						g.setColor(orangeRed);
						g.fillOval(Math.round(AI2.getAIX()), Math.round(AI2.getAIY()), 20, 20);
						int AI2X = (int) AI2.getAIX();
						int AI2Y = (int) AI2.getAIY() - 30;
						g.setColor(Color.darkGray);
						g.fillRect(AI2X, AI2Y, AI_ORIG_HEALTH/AI_HEALTH_WIDTH_SCALE, healthY);
						g.setColor(orangeRed);
						g.fillRect(AI2X, AI2Y, (int) AI2.getAIHealth()/AI_HEALTH_WIDTH_SCALE, healthY);
					}
				} else if (currentAI == 3) {
					if (!AI1Dead) {
						g.setColor(Color.blue);
						g.fillOval(Math.round(AI1.getAIX()), Math.round(AI1.getAIY()), 20, 20);
						int AI1X = (int) AI1.getAIX();
						int AI1Y = (int) AI1.getAIY() - 30;
						g.setColor(Color.darkGray);
						g.fillRect(AI1X, AI1Y, AI_ORIG_HEALTH/AI_HEALTH_WIDTH_SCALE, healthY);
						g.setColor(Color.blue);
						g.fillRect(AI1X, AI1Y, (int) AI1.getAIHealth()/AI_HEALTH_WIDTH_SCALE, healthY);
					}
					if (!AI2Dead) {
						g.setColor(orangeRed);
						g.fillOval(Math.round(AI2.getAIX()), Math.round(AI2.getAIY()), 20, 20);
						int AI2X = (int) AI2.getAIX();
						int AI2Y = (int) AI2.getAIY() - 30;
						g.setColor(Color.darkGray);
						g.fillRect(AI2X, AI2Y, AI_ORIG_HEALTH/AI_HEALTH_WIDTH_SCALE, healthY);
						g.setColor(orangeRed);
						g.fillRect(AI2X, AI2Y, (int) AI2.getAIHealth()/AI_HEALTH_WIDTH_SCALE, healthY);
					}
					if (!AI3Dead) {
						g.setColor(cyan);
						g.fillOval(Math.round(AI3.getAIX()), Math.round(AI3.getAIY()), 20, 20);
						int AI3X = (int) AI3.getAIX();
						int AI3Y = (int) AI3.getAIY() - 30;
						g.setColor(Color.darkGray);
						g.fillRect(AI3X, AI3Y, AI_ORIG_HEALTH/AI_HEALTH_WIDTH_SCALE, healthY);
						g.setColor(cyan);
						g.fillRect(AI3X, AI3Y, (int) AI3.getAIHealth()/AI_HEALTH_WIDTH_SCALE, healthY);
					}
				} else if (currentAI == 4) {
					if (!AI1Dead) {
						g.setColor(Color.blue);
						g.fillOval(Math.round(AI1.getAIX()), Math.round(AI1.getAIY()), 20, 20);
						int AI1X = (int) AI1.getAIX();
						int AI1Y = (int) AI1.getAIY() - 30;
						g.setColor(Color.darkGray);
						g.fillRect(AI1X, AI1Y, AI_ORIG_HEALTH/AI_HEALTH_WIDTH_SCALE, healthY);
						g.setColor(Color.blue);
						g.fillRect(AI1X, AI1Y, (int) AI1.getAIHealth()/AI_HEALTH_WIDTH_SCALE, healthY);
					}
					if(!AI2Dead) {
						g.setColor(orangeRed);
						g.fillOval(Math.round(AI2.getAIX()), Math.round(AI2.getAIY()), 20, 20);
						int AI2X = (int) AI2.getAIX();
						int AI2Y = (int) AI2.getAIY() - 30;
						g.setColor(Color.darkGray);
						g.fillRect(AI2X, AI2Y, AI_ORIG_HEALTH/AI_HEALTH_WIDTH_SCALE, healthY);
						g.setColor(orangeRed);
						g.fillRect(AI2X, AI2Y, (int) AI2.getAIHealth()/AI_HEALTH_WIDTH_SCALE, healthY);
					}
					if (!AI3Dead) {
						g.setColor(cyan);
						g.fillOval(Math.round(AI3.getAIX()), Math.round(AI3.getAIY()), 20, 20);
						int AI3X = (int) AI3.getAIX();
						int AI3Y = (int) AI3.getAIY() - 30;
						g.setColor(Color.darkGray);
						g.fillRect(AI3X, AI3Y, AI_ORIG_HEALTH/AI_HEALTH_WIDTH_SCALE, healthY);
						g.setColor(cyan);
						g.fillRect(AI3X, AI3Y, (int) AI3.getAIHealth()/AI_HEALTH_WIDTH_SCALE, healthY);
					}
					if (!AI4Dead) {
						g.setColor(darkGold);
						g.fillOval(Math.round(AI4.getAIX()), Math.round(AI4.getAIY()), 20, 20);
						int AI4X = (int) AI4.getAIX();
						int AI4Y = (int) AI4.getAIY() - 30;
						g.setColor(Color.darkGray);
						g.fillRect(AI4X, AI4Y, AI_ORIG_HEALTH/AI_HEALTH_WIDTH_SCALE, healthY);
						g.setColor(darkGold);
						g.fillRect(AI4X, AI4Y, (int) AI4.getAIHealth()/AI_HEALTH_WIDTH_SCALE, healthY);
					}
				} else if(currentAI ==5) {
					if (!AI1Dead) {
						g.setColor(Color.blue);
						g.fillOval(Math.round(AI1.getAIX()), Math.round(AI1.getAIY()), 20, 20);
						int AI1X = (int) AI1.getAIX();
						int AI1Y = (int) AI1.getAIY() - 30;
						g.setColor(Color.darkGray);
						g.fillRect(AI1X, AI1Y, AI_ORIG_HEALTH/AI_HEALTH_WIDTH_SCALE, healthY);
						g.setColor(Color.blue);
						g.fillRect(AI1X, AI1Y, (int) AI1.getAIHealth()/AI_HEALTH_WIDTH_SCALE, healthY);
					}
					if(!AI2Dead) {
						g.setColor(orangeRed);
						g.fillOval(Math.round(AI2.getAIX()), Math.round(AI2.getAIY()), 20, 20);
					}
					if (!AI3Dead) {
						g.setColor(cyan);
						g.fillOval(Math.round(AI3.getAIX()), Math.round(AI3.getAIY()), 20, 20);
					}
					if (!AI4Dead) {
						g.setColor(darkGold);
						g.fillOval(Math.round(AI4.getAIX()), Math.round(AI4.getAIY()), 20, 20);
					}
					if (!AI5Dead) {
						g.setColor(gray);
						g.fillOval(Math.round(AI5.getAIX()), Math.round(AI5.getAIY()), 20, 20);
					}
				} else if (currentAI == 6) {
					if (!AI1Dead) {
						g.setColor(Color.blue);
						g.fillOval(Math.round(AI1.getAIX()), Math.round(AI1.getAIY()), 20, 20);
					}
					if(!AI2Dead) {
						g.setColor(orangeRed);
						g.fillOval(Math.round(AI2.getAIX()), Math.round(AI2.getAIY()), 20, 20);
					}
					if (!AI3Dead) {
						g.setColor(cyan);
						g.fillOval(Math.round(AI3.getAIX()), Math.round(AI3.getAIY()), 20, 20);
					}
					if (!AI4Dead) {
						g.setColor(darkGold);
						g.fillOval(Math.round(AI4.getAIX()), Math.round(AI4.getAIY()), 20, 20);
					}
					if (!AI5Dead) {
						g.setColor(gray);
						g.fillOval(Math.round(AI5.getAIX()), Math.round(AI5.getAIY()), 20, 20);
					}
					if(!AI6Dead) {
						g.setColor(lightGreen);
						g.fillOval(Math.round(AI6.getAIX()), Math.round(AI6.getAIY()), 20, 20);
					}
				} else if(currentAI == 7) {
					if (!AI1Dead) {
						g.setColor(Color.blue);
						g.fillOval(Math.round(AI1.getAIX()), Math.round(AI1.getAIY()), 20, 20);
					}
					if(!AI2Dead) {
						g.setColor(orangeRed);
						g.fillOval(Math.round(AI2.getAIX()), Math.round(AI2.getAIY()), 20, 20);
					}
					if (!AI3Dead) {
						g.setColor(cyan);
						g.fillOval(Math.round(AI3.getAIX()), Math.round(AI3.getAIY()), 20, 20);
					}
					if (!AI4Dead) {
						g.setColor(darkGold);
						g.fillOval(Math.round(AI4.getAIX()), Math.round(AI4.getAIY()), 20, 20);
					}
					if (!AI5Dead) {
						g.setColor(gray);
						g.fillOval(Math.round(AI5.getAIX()), Math.round(AI5.getAIY()), 20, 20);
					}
					if(!AI6Dead) {
						g.setColor(lightGreen);
						g.fillOval(Math.round(AI6.getAIX()), Math.round(AI6.getAIY()), 20, 20);
					}
					if(!AI7Dead) {
						g.setColor(zebPurple);
						g.fillOval(Math.round(AI7.getAIX()), Math.round(AI7.getAIY()), 20, 20);
					}
				} else if (currentAI == 8) {
					if (!AI1Dead) {
						g.setColor(Color.blue);
						g.fillOval(Math.round(AI1.getAIX()), Math.round(AI1.getAIY()), 20, 20);
					}
					if(!AI2Dead) {
						g.setColor(orangeRed);
						g.fillOval(Math.round(AI2.getAIX()), Math.round(AI2.getAIY()), 20, 20);
					}
					if (!AI3Dead) {
						g.setColor(cyan);
						g.fillOval(Math.round(AI3.getAIX()), Math.round(AI3.getAIY()), 20, 20);
					}
					if (!AI4Dead) {
						g.setColor(darkGold);
						g.fillOval(Math.round(AI4.getAIX()), Math.round(AI4.getAIY()), 20, 20);
					}
					if (!AI5Dead) {
						g.setColor(gray);
						g.fillOval(Math.round(AI5.getAIX()), Math.round(AI5.getAIY()), 20, 20);
					}
					if(!AI6Dead) {
						g.setColor(lightGreen);
						g.fillOval(Math.round(AI6.getAIX()), Math.round(AI6.getAIY()), 20, 20);
					}
					if(!AI7Dead) {
						g.setColor(zebPurple);
						g.fillOval(Math.round(AI7.getAIX()), Math.round(AI7.getAIY()), 20, 20);
					}
					if (!AI8Dead) {
						g.setColor(pink);
						g.fillOval(Math.round(AI8.getAIX()), Math.round(AI8.getAIY()), 20, 20);
					}
				} else if (currentAI == 9) {
					if (!AI1Dead) {
						g.setColor(Color.blue);
						g.fillOval(Math.round(AI1.getAIX()), Math.round(AI1.getAIY()), 20, 20);
					}
					if(!AI2Dead) {
						g.setColor(orangeRed);
						g.fillOval(Math.round(AI2.getAIX()), Math.round(AI2.getAIY()), 20, 20);
					}
					if (!AI3Dead) {
						g.setColor(cyan);
						g.fillOval(Math.round(AI3.getAIX()), Math.round(AI3.getAIY()), 20, 20);
					}
					if (!AI4Dead) {
						g.setColor(darkGold);
						g.fillOval(Math.round(AI4.getAIX()), Math.round(AI4.getAIY()), 20, 20);
					}
					if (!AI5Dead) {
						g.setColor(gray);
						g.fillOval(Math.round(AI5.getAIX()), Math.round(AI5.getAIY()), 20, 20);
					}
					if(!AI6Dead) {
						g.setColor(lightGreen);
						g.fillOval(Math.round(AI6.getAIX()), Math.round(AI6.getAIY()), 20, 20);
					}
					if(!AI7Dead) {
						g.setColor(zebPurple);
						g.fillOval(Math.round(AI7.getAIX()), Math.round(AI7.getAIY()), 20, 20);
					}
					if (!AI8Dead) {
						g.setColor(pink);
						g.fillOval(Math.round(AI8.getAIX()), Math.round(AI8.getAIY()), 20, 20);
					}
					if (!AI9Dead) {
						g.setColor(steelBlue);
						g.fillOval(Math.round(AI9.getAIX()), Math.round(AI9.getAIY()), 20, 20);
					}
				} else if(currentAI == 10) {
					if (!AI1Dead) {
						g.setColor(Color.blue);
						g.fillOval(Math.round(AI1.getAIX()), Math.round(AI1.getAIY()), 20, 20);
					}
					if(!AI2Dead) {
						g.setColor(orangeRed);
						g.fillOval(Math.round(AI2.getAIX()), Math.round(AI2.getAIY()), 20, 20);
					}
					if (!AI3Dead) {
						g.setColor(cyan);
						g.fillOval(Math.round(AI3.getAIX()), Math.round(AI3.getAIY()), 20, 20);
					}
					if (!AI4Dead) {
						g.setColor(darkGold);
						g.fillOval(Math.round(AI4.getAIX()), Math.round(AI4.getAIY()), 20, 20);
					}
					if (!AI5Dead) {
						g.setColor(gray);
						g.fillOval(Math.round(AI5.getAIX()), Math.round(AI5.getAIY()), 20, 20);
					}
					if(!AI6Dead) {
						g.setColor(lightGreen);
						g.fillOval(Math.round(AI6.getAIX()), Math.round(AI6.getAIY()), 20, 20);
					}
					if(!AI7Dead) {
						g.setColor(zebPurple);
						g.fillOval(Math.round(AI7.getAIX()), Math.round(AI7.getAIY()), 20, 20);
					}
					if (!AI8Dead) {
						g.setColor(pink);
						g.fillOval(Math.round(AI8.getAIX()), Math.round(AI8.getAIY()), 20, 20);
					}
					if (!AI9Dead) {
						g.setColor(steelBlue);
						g.fillOval(Math.round(AI9.getAIX()), Math.round(AI9.getAIY()), 20, 20);
					}
					if (!AI10Dead) {
						g.setColor(yellow);
						g.fillOval(Math.round(AI10.getAIX()), Math.round(AI10.getAIY()), 20, 20);
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
					aX = x;
					aY = y;
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
				if (drawSword) {
					swordCount++;
					if (mouseLeft) {
						if (swordCount < 5) {
							g.drawImage(SwordHorizontalL, Math.round(x) - 2, Math.round(y) - 45, this);
						}
						else if (swordCount > 5 && swordCount <=15) {
							g.drawImage(Sword45L,Math.round(x) - 45, Math.round(y) - 30, this);
						}else if (swordCount >15 && swordCount<30) {
							g.drawImage(SwordL, Math.round(x) - 63, Math.round(y), this);	
						}
						else  if  (swordCount >30 || !drawSword){
							g.drawImage(SwordHorizontalL, Math.round(x) - 2, Math.round(y) - 45, this);
							swordCount = 0;	
						}
					}else {
						if (swordCount < 5) {
							//image flip g.drawImage(SwordHorizontalL, Math.round(x) - 2, Math.round(y) - 45, -SwordHorizontalL.getWidth(this),SwordHorizontalL.getHeight(this),this);
							g.drawImage(SwordHorizontalL, Math.round(x) + 20, Math.round(y) - 45,
									-SwordHorizontalL.getWidth(this),SwordHorizontalL.getHeight(this),this);
						}
						else if (swordCount > 5 && swordCount <=15) {
							g.drawImage(Sword45L,Math.round(x) + 80, Math.round(y) - 30,
									-Sword45L.getWidth(this), Sword45L.getHeight(this), this);
						}else if (swordCount >15 && swordCount<30) {
							g.drawImage(SwordL, Math.round(x) +90, Math.round(y), -SwordL.getWidth(this), SwordL.getHeight(this), this);	
						}
						else  if  (swordCount >30 || !drawSword){
							g.drawImage(SwordHorizontalL, Math.round(x) + 20, Math.round(y) - 45,
									-SwordHorizontalL.getWidth(this), SwordHorizontalL.getHeight(this), this);
							swordCount = 0;	
						}
					}
				}
				else {
					if(mouseLeft) {
						g.drawImage(SwordHorizontalL, Math.round(x) - 2, Math.round(y) - 45, this);
					}else{
						g.drawImage(SwordHorizontalL, Math.round(x) + 20, Math.round(y) - 45,
								-SwordHorizontalL.getWidth(this), SwordHorizontalL.getHeight(this), this);
					}
					swordCount = 0;
				}
				if (wasReleased) {
					lClick = false;
				}
			}
			}
			if (escape) {
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
		if (e.getX() > x) {
			mouseLeft = false;
			mouseRight = true;
		}
		if (e.getX() < x) {
			mouseRight = false;
			mouseLeft = true;
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
			if (!alreadyShot) {
				arrowMech();
				aY = y;
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
			//up = true;
			//upPressed = true;
			//down = false;
			if (isGrounded) {
				velocityY = -8;
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
			//up = false;
			//upPressed = false;
		}
		// right arrow
		if (e.getKeyCode() == 39 || e.getKeyCode() == 68) {
			right = false;
			if (left) {
				lastPressed = 2;
			}
		}

		// toggle UI
		if (e.getKeyCode() == 112) {
			if (UI) {
				UI = false;
			} else if (!UI) {
				UI = true;
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
		if ((!escape) && (!dead) && (!allAIDead)) {
			velocityY += gravity;
			x += velocityX;
			y += velocityY;
			
			// moves left
			if ((left == true) && (velocityX > -5) && (lastPressed == 2)) {
				velocityX -= 1;
			}
			// moves right
			if ((right == true) && (velocityX < 5) && (lastPressed == 1)) {
				velocityX += 1;
			}
			if ((!right) && (!left) && (isGrounded) && (velocityX > 0)) {
				velocityX-=.3;
				if (Math.abs(velocityX) < .29) {
					velocityX = 0;
				}
			} else if ((!right) && (!left) && (isGrounded) && (velocityX < 0)) {
				velocityX+=.3;
				if (Math.abs(velocityX) < .29) {
					velocityX = 0;
				}
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
			if ((AI1Dead) && (AI2Dead) && (AI3Dead) && (AI4Dead) && (AI5Dead) && (AI6Dead) && (AI7Dead) && (AI8Dead) && (AI9Dead) && (AI10Dead)) {
				allAIDead = true;
			}
			paint();
		}
	}
	
	/**
	 * Method that handles damage to AI objects. Fairly long and only bow portion works.
	 */
	public void AIDamage() {
		if (attackStyle == 2) {
			if (currentAI == 1) {
				if ((inFlight) && (Math.abs(aX-AI1.getAIX()) < 5) && (Math.abs(aY-AI1.getAIY())) < 4 ) {
					AI1.setHealth(-25);
					drawArrow = false;
					cooldown = 15;
					AI1Dead = AI1.isDead();
				}
			}
			
			else if (currentAI == 2) {
				if ((inFlight) && (Math.abs(aX-AI1.getAIX()) < 5) && (Math.abs(aY-AI1.getAIY())) < 4 ) {
					AIHealth-=25;
					drawArrow = false;
					cooldown = 15;
					AI1Dead = AI1.isDead();
				}
				if ((inFlight) && (Math.abs(aX-AI2.getAIX()) < 5) && (Math.abs(aY-AI2.getAIY())) < 4 ) {
					AIHealth-=25;
					drawArrow = false;
					cooldown = 15;
					AI2Dead = AI2.isDead();
				}
			}
			
			else if (currentAI == 3) {
				if ((inFlight) && (Math.abs(aX-AI1.getAIX()) < 5) && (Math.abs(aY-AI1.getAIY())) < 4 ) {
					AIHealth-=25;
					drawArrow = false;
					cooldown = 15;
					AI1Dead = AI1.isDead();
				}
				if ((inFlight) && (Math.abs(aX-AI2.getAIX()) < 5) && (Math.abs(aY-AI2.getAIY())) < 4 ) {
					AIHealth-=25;
					drawArrow = false;
					cooldown = 15;
					AI2Dead = AI2.isDead();
				}
				if ((inFlight) && (Math.abs(aX-AI3.getAIX()) < 5) && (Math.abs(aY-AI3.getAIY())) < 4 ) {
					AIHealth-=25;
					drawArrow = false;
					cooldown = 15;
					AI3Dead = AI3.isDead();
				}
			}
			
			else if (currentAI == 4) {
				if ((inFlight) && (Math.abs(aX-AI1.getAIX()) < 5) && (Math.abs(aY-AI1.getAIY())) < 4 ) {
					AIHealth-=25;
					drawArrow = false;
					cooldown = 15;
					AI1Dead = AI1.isDead();
				}
				if ((inFlight) && (Math.abs(aX-AI2.getAIX()) < 5) && (Math.abs(aY-AI2.getAIY())) < 4 ) {
					AIHealth-=25;
					drawArrow = false;
					cooldown = 15;
					AI2Dead = AI2.isDead();
				}
				if ((inFlight) && (Math.abs(aX-AI3.getAIX()) < 5) && (Math.abs(aY-AI3.getAIY())) < 4 ) {
					AIHealth-=25;
					drawArrow = false;
					cooldown = 15;
					AI3Dead = AI3.isDead();
				}
				if ((inFlight) && (Math.abs(aX-AI4.getAIX()) < 5) && (Math.abs(aY-AI4.getAIY())) < 4 ) {
					AIHealth-=25;
					drawArrow = false;
					cooldown = 15;
					AI4Dead = AI4.isDead();
				}
			}
			
			else if (currentAI == 5) {
				if ((inFlight) && (Math.abs(aX-AI1.getAIX()) < 5) && (Math.abs(aY-AI1.getAIY())) < 4 ) {
					AIHealth-=25;
					drawArrow = false;
					cooldown = 15;
					AI1Dead = AI1.isDead();
				}
				if ((inFlight) && (Math.abs(aX-AI2.getAIX()) < 5) && (Math.abs(aY-AI2.getAIY())) < 4 ) {
					AIHealth-=25;
					drawArrow = false;
					cooldown = 15;
					AI2Dead = AI2.isDead();
				}
				if ((inFlight) && (Math.abs(aX-AI3.getAIX()) < 5) && (Math.abs(aY-AI3.getAIY())) < 4 ) {
					AIHealth-=25;
					drawArrow = false;
					cooldown = 15;
					AI3Dead = AI3.isDead();
				}
				if ((inFlight) && (Math.abs(aX-AI4.getAIX()) < 5) && (Math.abs(aY-AI4.getAIY())) < 4 ) {
					AIHealth-=25;
					drawArrow = false;
					cooldown = 15;
					AI4Dead = AI4.isDead();
				}
				if ((inFlight) && (Math.abs(aX-AI5.getAIX()) < 5) && (Math.abs(aY-AI5.getAIY())) < 4 ) {
					AIHealth-=25;
					drawArrow = false;
					cooldown = 15;
					AI5Dead = AI5.isDead();
				}
			}
			
			else if (currentAI == 6) {
				if ((inFlight) && (Math.abs(aX-AI1.getAIX()) < 5) && (Math.abs(aY-AI1.getAIY())) < 4 ) {
					AIHealth-=25;
					drawArrow = false;
					cooldown = 15;
					AI1Dead = AI1.isDead();
				}
				if ((inFlight) && (Math.abs(aX-AI2.getAIX()) < 5) && (Math.abs(aY-AI2.getAIY())) < 4 ) {
					AIHealth-=25;
					drawArrow = false;
					cooldown = 15;
					AI2Dead = AI2.isDead();
				}
				if ((inFlight) && (Math.abs(aX-AI3.getAIX()) < 5) && (Math.abs(aY-AI3.getAIY())) < 4 ) {
					AIHealth-=25;
					drawArrow = false;
					cooldown = 15;
					AI3Dead = AI3.isDead();
				}
				if ((inFlight) && (Math.abs(aX-AI4.getAIX()) < 5) && (Math.abs(aY-AI4.getAIY())) < 4 ) {
					AIHealth-=25;
					drawArrow = false;
					cooldown = 15;
					AI4Dead = AI4.isDead();
				}
				if ((inFlight) && (Math.abs(aX-AI5.getAIX()) < 5) && (Math.abs(aY-AI5.getAIY())) < 4 ) {
					AIHealth-=25;
					drawArrow = false;
					cooldown = 15;
					AI5Dead = AI5.isDead();
				}
				if ((inFlight) && (Math.abs(aX-AI6.getAIX()) < 5) && (Math.abs(aY-AI6.getAIY())) < 4 ) {
					AIHealth-=25;
					drawArrow = false;
					cooldown = 15;
					AI6Dead = AI6.isDead();
				}
			}
			
			else if (currentAI == 7) {
				if ((inFlight) && (Math.abs(aX-AI1.getAIX()) < 5) && (Math.abs(aY-AI1.getAIY())) < 4 ) {
					AIHealth-=25;
					drawArrow = false;
					cooldown = 15;
					AI1Dead = AI1.isDead();
				}
				if ((inFlight) && (Math.abs(aX-AI2.getAIX()) < 5) && (Math.abs(aY-AI2.getAIY())) < 4 ) {
					AIHealth-=25;
					drawArrow = false;
					cooldown = 15;
					AI2Dead = AI2.isDead();
				}
				if ((inFlight) && (Math.abs(aX-AI3.getAIX()) < 5) && (Math.abs(aY-AI3.getAIY())) < 4 ) {
					AIHealth-=25;
					drawArrow = false;
					cooldown = 15;
					AI3Dead = AI3.isDead();
				}
				if ((inFlight) && (Math.abs(aX-AI4.getAIX()) < 5) && (Math.abs(aY-AI4.getAIY())) < 4 ) {
					AIHealth-=25;
					drawArrow = false;
					cooldown = 15;
					AI4Dead = AI4.isDead();
				}
				if ((inFlight) && (Math.abs(aX-AI5.getAIX()) < 5) && (Math.abs(aY-AI5.getAIY())) < 4 ) {
					AIHealth-=25;
					drawArrow = false;
					cooldown = 15;
					AI5Dead = AI5.isDead();
				}
				if ((inFlight) && (Math.abs(aX-AI6.getAIX()) < 5) && (Math.abs(aY-AI6.getAIY())) < 4 ) {
					AIHealth-=25;
					drawArrow = false;
					cooldown = 15;
					AI6Dead = AI6.isDead();
				}
				if ((inFlight) && (Math.abs(aX-AI7.getAIX()) < 5) && (Math.abs(aY-AI7.getAIY())) < 4 ) {
					AIHealth-=25;
					drawArrow = false;
					cooldown = 15;
					AI7Dead = AI7.isDead();
				}
			}
			
			else if (currentAI == 8) {
				if ((inFlight) && (Math.abs(aX-AI1.getAIX()) < 5) && (Math.abs(aY-AI1.getAIY())) < 4 ) {
					AIHealth-=25;
					drawArrow = false;
					cooldown = 15;
					AI1Dead = AI1.isDead();
				}
				if ((inFlight) && (Math.abs(aX-AI2.getAIX()) < 5) && (Math.abs(aY-AI2.getAIY())) < 4 ) {
					AIHealth-=25;
					drawArrow = false;
					cooldown = 15;
					AI2Dead = AI2.isDead();
				}
				if ((inFlight) && (Math.abs(aX-AI3.getAIX()) < 5) && (Math.abs(aY-AI3.getAIY())) < 4 ) {
					AIHealth-=25;
					drawArrow = false;
					cooldown = 15;
					AI3Dead = AI3.isDead();
				}
				if ((inFlight) && (Math.abs(aX-AI4.getAIX()) < 5) && (Math.abs(aY-AI4.getAIY())) < 4 ) {
					AIHealth-=25;
					drawArrow = false;
					cooldown = 15;
					AI4Dead = AI4.isDead();
				}
				if ((inFlight) && (Math.abs(aX-AI5.getAIX()) < 5) && (Math.abs(aY-AI5.getAIY())) < 4 ) {
					AIHealth-=25;
					drawArrow = false;
					cooldown = 15;
					AI5Dead = AI5.isDead();
				}
				if ((inFlight) && (Math.abs(aX-AI6.getAIX()) < 5) && (Math.abs(aY-AI6.getAIY())) < 4 ) {
					AIHealth-=25;
					drawArrow = false;
					cooldown = 15;
					AI6Dead = AI6.isDead();
				}
				if ((inFlight) && (Math.abs(aX-AI7.getAIX()) < 5) && (Math.abs(aY-AI7.getAIY())) < 4 ) {
					AIHealth-=25;
					drawArrow = false;
					cooldown = 15;
					AI7Dead = AI7.isDead();
				}
				if ((inFlight) && (Math.abs(aX-AI8.getAIX()) < 5) && (Math.abs(aY-AI8.getAIY())) < 4 ) {
					AIHealth-=25;
					drawArrow = false;
					cooldown = 15;
					AI8Dead = AI8.isDead();
				}
			}
			
			else if (currentAI == 9) {
				if ((inFlight) && (Math.abs(aX-AI1.getAIX()) < 5) && (Math.abs(aY-AI1.getAIY())) < 4 ) {
					AIHealth-=25;
					drawArrow = false;
					cooldown = 15;
					AI1Dead = AI1.isDead();
				}
				if ((inFlight) && (Math.abs(aX-AI2.getAIX()) < 5) && (Math.abs(aY-AI2.getAIY())) < 4 ) {
					AIHealth-=25;
					drawArrow = false;
					cooldown = 15;
					AI2Dead = AI2.isDead();
				}
				if ((inFlight) && (Math.abs(aX-AI3.getAIX()) < 5) && (Math.abs(aY-AI3.getAIY())) < 4 ) {
					AIHealth-=25;
					drawArrow = false;
					cooldown = 15;
					AI3Dead = AI3.isDead();
				}
				if ((inFlight) && (Math.abs(aX-AI4.getAIX()) < 5) && (Math.abs(aY-AI4.getAIY())) < 4 ) {
					AIHealth-=25;
					drawArrow = false;
					cooldown = 15;
					AI4Dead = AI4.isDead();
				}
				if ((inFlight) && (Math.abs(aX-AI5.getAIX()) < 5) && (Math.abs(aY-AI5.getAIY())) < 4 ) {
					AIHealth-=25;
					drawArrow = false;
					cooldown = 15;
					AI5Dead = AI5.isDead();
				}
				if ((inFlight) && (Math.abs(aX-AI6.getAIX()) < 5) && (Math.abs(aY-AI6.getAIY())) < 4 ) {
					AIHealth-=25;
					drawArrow = false;
					cooldown = 15;
					AI6Dead = AI6.isDead();
				}
				if ((inFlight) && (Math.abs(aX-AI7.getAIX()) < 5) && (Math.abs(aY-AI7.getAIY())) < 4 ) {
					AIHealth-=25;
					drawArrow = false;
					cooldown = 15;
					AI7Dead = AI7.isDead();
				}
				if ((inFlight) && (Math.abs(aX-AI8.getAIX()) < 5) && (Math.abs(aY-AI8.getAIY())) < 4 ) {
					AIHealth-=25;
					drawArrow = false;
					cooldown = 15;
					AI8Dead = AI8.isDead();
				}
				if ((inFlight) && (Math.abs(aX-AI9.getAIX()) < 5) && (Math.abs(aY-AI9.getAIY())) < 4 ) {
					AIHealth-=25;
					drawArrow = false;
					cooldown = 15;
					AI9Dead = AI9.isDead();
				}
			}
			
			else if(currentAI == 10) {
				if ((inFlight) && (Math.abs(aX-AI1.getAIX()) < 5) && (Math.abs(aY-AI1.getAIY())) < 4 ) {
					AIHealth-=25;
					drawArrow = false;
					cooldown = 15;
					AI1Dead = AI1.isDead();
				}
				if ((inFlight) && (Math.abs(aX-AI2.getAIX()) < 5) && (Math.abs(aY-AI2.getAIY())) < 4 ) {
					AIHealth-=25;
					drawArrow = false;
					cooldown = 15;
					AI2Dead = AI2.isDead();
				}
				if ((inFlight) && (Math.abs(aX-AI3.getAIX()) < 5) && (Math.abs(aY-AI3.getAIY())) < 4 ) {
					AIHealth-=25;
					drawArrow = false;
					cooldown = 15;
					AI3Dead = AI3.isDead();
				}
				if ((inFlight) && (Math.abs(aX-AI4.getAIX()) < 5) && (Math.abs(aY-AI4.getAIY())) < 4 ) {
					AIHealth-=25;
					drawArrow = false;
					cooldown = 15;
					AI4Dead = AI4.isDead();
				}
				if ((inFlight) && (Math.abs(aX-AI5.getAIX()) < 5) && (Math.abs(aY-AI5.getAIY())) < 4 ) {
					AIHealth-=25;
					drawArrow = false;
					cooldown = 15;
					AI5Dead = AI5.isDead();
				}
				if ((inFlight) && (Math.abs(aX-AI6.getAIX()) < 5) && (Math.abs(aY-AI6.getAIY())) < 4 ) {
					AIHealth-=25;
					drawArrow = false;
					cooldown = 15;
					AI6Dead = AI6.isDead();
				}
				if ((inFlight) && (Math.abs(aX-AI7.getAIX()) < 5) && (Math.abs(aY-AI7.getAIY())) < 4 ) {
					AIHealth-=25;
					drawArrow = false;
					cooldown = 15;
					AI7Dead = AI7.isDead();
				}
				if ((inFlight) && (Math.abs(aX-AI8.getAIX()) < 5) && (Math.abs(aY-AI8.getAIY())) < 4 ) {
					AIHealth-=25;
					drawArrow = false;
					cooldown = 15;
					AI8Dead = AI8.isDead();
				}
				if ((inFlight) && (Math.abs(aX-AI9.getAIX()) < 5) && (Math.abs(aY-AI9.getAIY())) < 4 ) {
					AIHealth-=25;
					drawArrow = false;
					cooldown = 15;
					AI9Dead = AI9.isDead();
				}
				if ((inFlight) && (Math.abs(aX-AI10.getAIX()) < 5) && (Math.abs(aY-AI10.getAIY())) < 4 ) {
					AIHealth-=25;
					drawArrow = false;
					cooldown = 15;
					AI10Dead = AI10.isDead();
				}
			}
		}else {

			if (mouseLeft) {
				if ((drawSword) && (cooldown ==0) && (x -AI1.getAIX() <85) && (x-AI1.getAIX() >-1) && (y - AI1.getAIY() < 20) && (y-AI1.getAIY()>-5)) {
					AIHealth-=50;
					cooldown = 5;
					AI1Dead = AI1.isDead();
				}
			}else {
				if ((drawSword) && (cooldown == 0) && (AI1.getAIX()-x <85) && (AI1.getAIX() - x > -1) && (y - AI1.getAIY() < 20) && (y-AI1.getAIY()>-5)) {
					AIHealth-=50;
					cooldown = 5;
					AI1Dead = AI1.isDead();
				}
			}
		}
		//checks if AI is close enough to attack with melee
		if ((Math.abs(AIX - x) <= 10) && Math.abs(AIY - y) <= 3) {
			healthX -=(.5 * difficulty);
			if (healthX <= 0) {
				dead  = true;
			}
		}
	}
	public void AIMove() {
		if (currentAI == 1) {
			if ((AI1.getAIX() > x) && (AI1.getAIVelocityX() >-4)) {
				if(Math.abs(AI1.getAIX() - x) <= 10) {
					AI1.setAIVelocityX(0);
				}
				else {
					AI1.setAIVelocityX(-.7f);
				}
			}else if ((AI1.getAIX() < x) && (AI1.getAIVelocityX() < 4)) {
				if(Math.abs(AI1.getAIX() - x) <= 10) {
					AI1.setAIVelocityX(0);
				}
				else {
					AI1.setAIVelocityX(.7f);
				}
			}
			if ((AI1.getAIY() > y) && (isGrounded)) {
				if (AI1.isAIGrounded()) {
					System.out.println("trying to move AI1YV");
					AI1.setAIVelocityY(-.5f);
				}
			}
			AI1.move();
		} 
		
		else if (currentAI == 2) {
			if ((AI1.getAIX() > x) && (AI1.getAIVelocityX() >-4)) {
				if(Math.abs(AI1.getAIX() - x) <= 10) {
					AI1.setAIVelocityX(0);
				}
				else {
					AI1.setAIVelocityX(-.7f);
				}
			}else if ((AI1.getAIX() < x) && (AI1.getAIVelocityX() < 4)) {
				if(Math.abs(AI1.getAIX() - x) <= 10) {
					AI1.setAIVelocityX(0);
				}
				else {
					AI1.setAIVelocityX(.7f);
				}
			}
			if ((AI1.getAIY() > y) && (isGrounded)) {
				if (AI1.isAIGrounded()) {
					AI1.setAIVelocityY(-.5f);
				}
			}
			AI1.move();
			
			if ((AI2.getAIX() > x) && (AI2.getAIVelocityX() >-4)) {
				if(Math.abs(AI2.getAIX() - x) <= 10) {
					AI2.setAIVelocityX(0);
				}
				else {
					AI2.setAIVelocityX(-.7f);
				}
			}else if ((AI2.getAIX() < x) && (AI2.getAIVelocityX() < 4)) {
				if(Math.abs(AI2.getAIX() - x) <= 10) {
					AI2.setAIVelocityX(0);
				}
				else {
					AI2.setAIVelocityX(.7f);
				}
			}
			if ((AI2.getAIY() > y) && (isGrounded)) {
				if (AI2.isAIGrounded()) {
					AI2.setAIVelocityY(-.5f);
				}
			}
			AI2.move();

		}
		
		else if (currentAI == 3) {
			if ((AI1.getAIX() > x) && (AI1.getAIVelocityX() >-4)) {
				if(Math.abs(AI1.getAIX() - x) <= 10) {
					AI1.setAIVelocityX(0);
				}
				else {
					AI1.setAIVelocityX(-.7f);
				}
			}else if ((AI1.getAIX() < x) && (AI1.getAIVelocityX() < 4)) {
				if(Math.abs(AI1.getAIX() - x) <= 10) {
					AI1.setAIVelocityX(0);
				}
				else {
					AI1.setAIVelocityX(.7f);
				}
			}
			if ((AI1.getAIY() > y) && (isGrounded)) {
				if (AI1.isAIGrounded()) {
					AI1.setAIVelocityY(-.5f);
				}
			}
			AI1.move();
			
			if ((AI2.getAIX() > x) && (AI2.getAIVelocityX() >-4)) {
				if(Math.abs(AI2.getAIX() - x) <= 10) {
					AI2.setAIVelocityX(0);
				}
				else {
					AI2.setAIVelocityX(-.7f);
				}
			}else if ((AI2.getAIX() < x) && (AI2.getAIVelocityX() < 4)) {
				if(Math.abs(AI2.getAIX() - x) <= 10) {
					AI2.setAIVelocityX(0);
				}
				else {
					AI2.setAIVelocityX(.7f);
				}
			}
			if ((AI2.getAIY() > y) && (isGrounded)) {
				if (AI2.isAIGrounded()) {
					AI2.setAIVelocityY(-.5f);
				}
			}
			AI2.move();
			

			if ((AI3.getAIX() > x) && (AI3.getAIVelocityX() >-4)) {
				if(Math.abs(AI3.getAIX() - x) <= 10) {
					AI3.setAIVelocityX(0);
				}
				else {
					AI3.setAIVelocityX(-.7f);
				}
			}else if ((AI3.getAIX() < x) && (AI3.getAIVelocityX() < 4)) {
				if(Math.abs(AI3.getAIX() - x) <= 10) {
					AI3.setAIVelocityX(0);
				}
				else {
					AI3.setAIVelocityX(.7f);
				}
			}
			if ((AI3.getAIY() > y) && (isGrounded)) {
				if (AI3.isAIGrounded()) {
					AI3.setAIVelocityY(-.5f);
				}
			}
			AI3.move();
		}
		
		else if(currentAI == 4){
			if ((AI1.getAIX() > x) && (AI1.getAIVelocityX() >-4)) {
				if(Math.abs(AI1.getAIX() - x) <= 10) {
					AI1.setAIVelocityX(0);
				}
				else {
					AI1.setAIVelocityX(-.7f);
				}
			}else if ((AI1.getAIX() < x) && (AI1.getAIVelocityX() < 4)) {
				if(Math.abs(AI1.getAIX() - x) <= 10) {
					AI1.setAIVelocityX(0);
				}
				else {
					AI1.setAIVelocityX(.7f);
				}
			}
			if ((AI1.getAIY() > y) && (isGrounded)) {
				if (AI1.isAIGrounded()) {
					AI1.setAIVelocityY(-.5f);
				}
			}
			AI1.move();
			
			if ((AI2.getAIX() > x) && (AI2.getAIVelocityX() >-4)) {
				if(Math.abs(AI2.getAIX() - x) <= 10) {
					AI2.setAIVelocityX(0);
				}
				else {
					AI2.setAIVelocityX(-.7f);
				}
			}else if ((AI2.getAIX() < x) && (AI2.getAIVelocityX() < 4)) {
				if(Math.abs(AI2.getAIX() - x) <= 10) {
					AI2.setAIVelocityX(0);
				}
				else {
					AI2.setAIVelocityX(.7f);
				}
			}
			if ((AI2.getAIY() > y) && (isGrounded)) {
				if (AI2.isAIGrounded()) {
					AI2.setAIVelocityY(-.5f);
				}
			}
			AI2.move();
			

			if ((AI3.getAIX() > x) && (AI3.getAIVelocityX() >-4)) {
				if(Math.abs(AI3.getAIX() - x) <= 10) {
					AI3.setAIVelocityX(0);
				}
				else {
					AI3.setAIVelocityX(-.7f);
				}
			}else if ((AI3.getAIX() < x) && (AI3.getAIVelocityX() < 4)) {
				if(Math.abs(AI3.getAIX() - x) <= 10) {
					AI3.setAIVelocityX(0);
				}
				else {
					AI3.setAIVelocityX(.7f);
				}
			}
			if ((AI3.getAIY() > y) && (isGrounded)) {
				if (AI3.isAIGrounded()) {
					AI3.setAIVelocityY(-.5f);
				}
			}
			AI3.move();
			
			if ((AI4.getAIX() > x) && (AI4.getAIVelocityX() >-4)) {
				if(Math.abs(AI4.getAIX() - x) <= 10) {
					AI4.setAIVelocityX(0);
				}
				else {
					AI4.setAIVelocityX(-.7f);
				}
			}else if ((AI4.getAIX() < x) && (AI4.getAIVelocityX() < 4)) {
				if(Math.abs(AI4.getAIX() - x) <= 10) {
					AI4.setAIVelocityX(0);
				}
				else {
					AI4.setAIVelocityX(.7f);
				}
			}
			if ((AI4.getAIY() > y) && (isGrounded)) {
				if (AI4.isAIGrounded()) {
					AI4.setAIVelocityY(-.5f);
				}
			}
			AI4.move();
		}
		
		else if (currentAI == 5) {
			if ((AI1.getAIX() > x) && (AI1.getAIVelocityX() >-4)) {
				if(Math.abs(AI1.getAIX() - x) <= 10) {
					AI1.setAIVelocityX(0);
				}
				else {
					AI1.setAIVelocityX(-.7f);
				}
			}else if ((AI1.getAIX() < x) && (AI1.getAIVelocityX() < 4)) {
				if(Math.abs(AI1.getAIX() - x) <= 10) {
					AI1.setAIVelocityX(0);
				}
				else {
					AI1.setAIVelocityX(.7f);
				}
			}
			if ((AI1.getAIY() > y) && (isGrounded)) {
				if (AI1.isAIGrounded()) {
					AI1.setAIVelocityY(-.5f);
				}
			}
			AI1.move();
			
			if ((AI2.getAIX() > x) && (AI2.getAIVelocityX() >-4)) {
				if(Math.abs(AI2.getAIX() - x) <= 10) {
					AI2.setAIVelocityX(0);
				}
				else {
					AI2.setAIVelocityX(-.7f);
				}
			}else if ((AI2.getAIX() < x) && (AI2.getAIVelocityX() < 4)) {
				if(Math.abs(AI2.getAIX() - x) <= 10) {
					AI2.setAIVelocityX(0);
				}
				else {
					AI2.setAIVelocityX(.7f);
				}
			}
			if ((AI2.getAIY() > y) && (isGrounded)) {
				if (AI2.isAIGrounded()) {
					AI2.setAIVelocityY(-.5f);
				}
			}
			AI2.move();
			

			if ((AI3.getAIX() > x) && (AI3.getAIVelocityX() >-4)) {
				if(Math.abs(AI3.getAIX() - x) <= 10) {
					AI3.setAIVelocityX(0);
				}
				else {
					AI3.setAIVelocityX(-.7f);
				}
			}else if ((AI3.getAIX() < x) && (AI3.getAIVelocityX() < 4)) {
				if(Math.abs(AI3.getAIX() - x) <= 10) {
					AI3.setAIVelocityX(0);
				}
				else {
					AI3.setAIVelocityX(.7f);
				}
			}
			if ((AI3.getAIY() > y) && (isGrounded)) {
				if (AI3.isAIGrounded()) {
					AI3.setAIVelocityY(-.5f);
				}
			}
			AI3.move();
			
			if ((AI4.getAIX() > x) && (AI4.getAIVelocityX() >-4)) {
				if(Math.abs(AI4.getAIX() - x) <= 10) {
					AI4.setAIVelocityX(0);
				}
				else {
					AI4.setAIVelocityX(-.7f);
				}
			}else if ((AI4.getAIX() < x) && (AI4.getAIVelocityX() < 4)) {
				if(Math.abs(AI4.getAIX() - x) <= 10) {
					AI4.setAIVelocityX(0);
				}
				else {
					AI4.setAIVelocityX(.7f);
				}
			}
			if ((AI4.getAIY() > y) && (isGrounded)) {
				if (AI4.isAIGrounded()) {
					AI4.setAIVelocityY(-.5f);
				}
			}
			AI4.move();
			
			if ((AI5.getAIX() > x) && (AI5.getAIVelocityX() >-4)) {
				if(Math.abs(AI5.getAIX() - x) <= 10) {
					AI5.setAIVelocityX(0);
				}
				else {
					AI5.setAIVelocityX(-.7f);
				}
			}else if ((AI5.getAIX() < x) && (AI5.getAIVelocityX() < 4)) {
				if(Math.abs(AI5.getAIX() - x) <= 10) {
					AI5.setAIVelocityX(0);
				}
				else {
					AI5.setAIVelocityX(.7f);
				}
			}
			if ((AI5.getAIY() > y) && (isGrounded)) {
				if (AI5.isAIGrounded()) {
					AI5.setAIVelocityY(-.5f);
				}
			}
			AI5.move();
		}
		
		else if (currentAI == 6) {
			if ((AI1.getAIX() > x) && (AI1.getAIVelocityX() >-4)) {
				if(Math.abs(AI1.getAIX() - x) <= 10) {
					AI1.setAIVelocityX(0);
				}
				else {
					AI1.setAIVelocityX(-.7f);
				}
			}else if ((AI1.getAIX() < x) && (AI1.getAIVelocityX() < 4)) {
				if(Math.abs(AI1.getAIX() - x) <= 10) {
					AI1.setAIVelocityX(0);
				}
				else {
					AI1.setAIVelocityX(.7f);
				}
			}
			if ((AI1.getAIY() > y) && (isGrounded)) {
				if (AI1.isAIGrounded()) {
					AI1.setAIVelocityY(-.5f);
				}
			}
			AI1.move();
			
			if ((AI2.getAIX() > x) && (AI2.getAIVelocityX() >-4)) {
				if(Math.abs(AI2.getAIX() - x) <= 10) {
					AI2.setAIVelocityX(0);
				}
				else {
					AI2.setAIVelocityX(-.7f);
				}
			}else if ((AI2.getAIX() < x) && (AI2.getAIVelocityX() < 4)) {
				if(Math.abs(AI2.getAIX() - x) <= 10) {
					AI2.setAIVelocityX(0);
				}
				else {
					AI2.setAIVelocityX(.7f);
				}
			}
			if ((AI2.getAIY() > y) && (isGrounded)) {
				if (AI2.isAIGrounded()) {
					AI2.setAIVelocityY(-.5f);
				}
			}
			AI2.move();
			

			if ((AI3.getAIX() > x) && (AI3.getAIVelocityX() >-4)) {
				if(Math.abs(AI3.getAIX() - x) <= 10) {
					AI3.setAIVelocityX(0);
				}
				else {
					AI3.setAIVelocityX(-.7f);
				}
			}else if ((AI3.getAIX() < x) && (AI3.getAIVelocityX() < 4)) {
				if(Math.abs(AI3.getAIX() - x) <= 10) {
					AI3.setAIVelocityX(0);
				}
				else {
					AI3.setAIVelocityX(.7f);
				}
			}
			if ((AI3.getAIY() > y) && (isGrounded)) {
				if (AI3.isAIGrounded()) {
					AI3.setAIVelocityY(-.5f);
				}
			}
			AI3.move();
			
			if ((AI4.getAIX() > x) && (AI4.getAIVelocityX() >-4)) {
				if(Math.abs(AI4.getAIX() - x) <= 10) {
					AI4.setAIVelocityX(0);
				}
				else {
					AI4.setAIVelocityX(-.7f);
				}
			}else if ((AI4.getAIX() < x) && (AI4.getAIVelocityX() < 4)) {
				if(Math.abs(AI4.getAIX() - x) <= 10) {
					AI4.setAIVelocityX(0);
				}
				else {
					AI4.setAIVelocityX(.7f);
				}
			}
			if ((AI4.getAIY() > y) && (isGrounded)) {
				if (AI4.isAIGrounded()) {
					AI4.setAIVelocityY(-.5f);
				}
			}
			AI4.move();
			
			if ((AI5.getAIX() > x) && (AI5.getAIVelocityX() >-4)) {
				if(Math.abs(AI5.getAIX() - x) <= 10) {
					AI5.setAIVelocityX(0);
				}
				else {
					AI5.setAIVelocityX(-.7f);
				}
			}else if ((AI5.getAIX() < x) && (AI5.getAIVelocityX() < 4)) {
				if(Math.abs(AI5.getAIX() - x) <= 10) {
					AI5.setAIVelocityX(0);
				}
				else {
					AI5.setAIVelocityX(.7f);
				}
			}
			if ((AI5.getAIY() > y) && (isGrounded)) {
				if (AI5.isAIGrounded()) {
					AI5.setAIVelocityY(-.5f);
				}
			}
			AI5.move();
			
			if ((AI6.getAIX() > x) && (AI6.getAIVelocityX() >-4)) {
				if(Math.abs(AI6.getAIX() - x) <= 10) {
					AI6.setAIVelocityX(0);
				}
				else {
					AI6.setAIVelocityX(-.7f);
				}
			}else if ((AI6.getAIX() < x) && (AI6.getAIVelocityX() < 4)) {
				if(Math.abs(AI6.getAIX() - x) <= 10) {
					AI6.setAIVelocityX(0);
				}
				else {
					AI6.setAIVelocityX(.7f);
				}
			}
			if ((AI6.getAIY() > y) && (isGrounded)) {
				if (AI6.isAIGrounded()) {
					AI6.setAIVelocityY(-.5f);
				}
			}
			AI6.move();
		}
		
		else if (currentAI == 7) {
			if ((AI1.getAIX() > x) && (AI1.getAIVelocityX() >-4)) {
				if(Math.abs(AI1.getAIX() - x) <= 10) {
					AI1.setAIVelocityX(0);
				}
				else {
					AI1.setAIVelocityX(-.7f);
				}
			}else if ((AI1.getAIX() < x) && (AI1.getAIVelocityX() < 4)) {
				if(Math.abs(AI1.getAIX() - x) <= 10) {
					AI1.setAIVelocityX(0);
				}
				else {
					AI1.setAIVelocityX(.7f);
				}
			}
			if ((AI1.getAIY() > y) && (isGrounded)) {
				if (AI1.isAIGrounded()) {
					AI1.setAIVelocityY(-.5f);
				}
			}
			AI1.move();
			
			if ((AI2.getAIX() > x) && (AI2.getAIVelocityX() >-4)) {
				if(Math.abs(AI2.getAIX() - x) <= 10) {
					AI2.setAIVelocityX(0);
				}
				else {
					AI2.setAIVelocityX(-.7f);
				}
			}else if ((AI2.getAIX() < x) && (AI2.getAIVelocityX() < 4)) {
				if(Math.abs(AI2.getAIX() - x) <= 10) {
					AI2.setAIVelocityX(0);
				}
				else {
					AI2.setAIVelocityX(.7f);
				}
			}
			if ((AI2.getAIY() > y) && (isGrounded)) {
				if (AI2.isAIGrounded()) {
					AI2.setAIVelocityY(-.5f);
				}
			}
			AI2.move();
			

			if ((AI3.getAIX() > x) && (AI3.getAIVelocityX() >-4)) {
				if(Math.abs(AI3.getAIX() - x) <= 10) {
					AI3.setAIVelocityX(0);
				}
				else {
					AI3.setAIVelocityX(-.7f);
				}
			}else if ((AI3.getAIX() < x) && (AI3.getAIVelocityX() < 4)) {
				if(Math.abs(AI3.getAIX() - x) <= 10) {
					AI3.setAIVelocityX(0);
				}
				else {
					AI3.setAIVelocityX(.7f);
				}
			}
			if ((AI3.getAIY() > y) && (isGrounded)) {
				if (AI3.isAIGrounded()) {
					AI3.setAIVelocityY(-.5f);
				}
			}
			AI3.move();
			
			if ((AI4.getAIX() > x) && (AI4.getAIVelocityX() >-4)) {
				if(Math.abs(AI4.getAIX() - x) <= 10) {
					AI4.setAIVelocityX(0);
				}
				else {
					AI4.setAIVelocityX(-.7f);
				}
			}else if ((AI4.getAIX() < x) && (AI4.getAIVelocityX() < 4)) {
				if(Math.abs(AI4.getAIX() - x) <= 10) {
					AI4.setAIVelocityX(0);
				}
				else {
					AI4.setAIVelocityX(.7f);
				}
			}
			if ((AI4.getAIY() > y) && (isGrounded)) {
				if (AI4.isAIGrounded()) {
					AI4.setAIVelocityY(-.5f);
				}
			}
			AI4.move();
			
			if ((AI5.getAIX() > x) && (AI5.getAIVelocityX() >-4)) {
				if(Math.abs(AI5.getAIX() - x) <= 10) {
					AI5.setAIVelocityX(0);
				}
				else {
					AI5.setAIVelocityX(-.7f);
				}
			}else if ((AI5.getAIX() < x) && (AI5.getAIVelocityX() < 4)) {
				if(Math.abs(AI5.getAIX() - x) <= 10) {
					AI5.setAIVelocityX(0);
				}
				else {
					AI5.setAIVelocityX(.7f);
				}
			}
			if ((AI5.getAIY() > y) && (isGrounded)) {
				if (AI5.isAIGrounded()) {
					AI5.setAIVelocityY(-.5f);
				}
			}
			AI5.move();
			
			if ((AI6.getAIX() > x) && (AI6.getAIVelocityX() >-4)) {
				if(Math.abs(AI6.getAIX() - x) <= 10) {
					AI6.setAIVelocityX(0);
				}
				else {
					AI6.setAIVelocityX(-.7f);
				}
			}else if ((AI6.getAIX() < x) && (AI6.getAIVelocityX() < 4)) {
				if(Math.abs(AI6.getAIX() - x) <= 10) {
					AI6.setAIVelocityX(0);
				}
				else {
					AI6.setAIVelocityX(.7f);
				}
			}
			if ((AI6.getAIY() > y) && (isGrounded)) {
				if (AI6.isAIGrounded()) {
					AI6.setAIVelocityY(-.5f);
				}
			}
			AI6.move();
			
			if ((AI7.getAIX() > x) && (AI7.getAIVelocityX() >-4)) {
				if(Math.abs(AI7.getAIX() - x) <= 10) {
					AI7.setAIVelocityX(0);
				}
				else {
					AI7.setAIVelocityX(-.7f);
				}
			}else if ((AI7.getAIX() < x) && (AI7.getAIVelocityX() < 4)) {
				if(Math.abs(AI7.getAIX() - x) <= 10) {
					AI7.setAIVelocityX(0);
				}
				else {
					AI7.setAIVelocityX(.7f);
				}
			}
			if ((AI7.getAIY() > y) && (isGrounded)) {
				if (AI7.isAIGrounded()) {
					AI7.setAIVelocityY(-.5f);
				}
			}
			AI7.move();
		}
		
		else if (currentAI == 8) {
			if ((AI1.getAIX() > x) && (AI1.getAIVelocityX() >-4)) {
				if(Math.abs(AI1.getAIX() - x) <= 10) {
					AI1.setAIVelocityX(0);
				}
				else {
					AI1.setAIVelocityX(-.7f);
				}
			}else if ((AI1.getAIX() < x) && (AI1.getAIVelocityX() < 4)) {
				if(Math.abs(AI1.getAIX() - x) <= 10) {
					AI1.setAIVelocityX(0);
				}
				else {
					AI1.setAIVelocityX(.7f);
				}
			}
			if ((AI1.getAIY() > y) && (isGrounded)) {
				if (AI1.isAIGrounded()) {
					AI1.setAIVelocityY(-.5f);
				}
			}
			AI1.move();
			
			if ((AI2.getAIX() > x) && (AI2.getAIVelocityX() >-4)) {
				if(Math.abs(AI2.getAIX() - x) <= 10) {
					AI2.setAIVelocityX(0);
				}
				else {
					AI2.setAIVelocityX(-.7f);
				}
			}else if ((AI2.getAIX() < x) && (AI2.getAIVelocityX() < 4)) {
				if(Math.abs(AI2.getAIX() - x) <= 10) {
					AI2.setAIVelocityX(0);
				}
				else {
					AI2.setAIVelocityX(.7f);
				}
			}
			if ((AI2.getAIY() > y) && (isGrounded)) {
				if (AI2.isAIGrounded()) {
					AI2.setAIVelocityY(-.5f);
				}
			}
			AI2.move();
			

			if ((AI3.getAIX() > x) && (AI3.getAIVelocityX() >-4)) {
				if(Math.abs(AI3.getAIX() - x) <= 10) {
					AI3.setAIVelocityX(0);
				}
				else {
					AI3.setAIVelocityX(-.7f);
				}
			}else if ((AI3.getAIX() < x) && (AI3.getAIVelocityX() < 4)) {
				if(Math.abs(AI3.getAIX() - x) <= 10) {
					AI3.setAIVelocityX(0);
				}
				else {
					AI3.setAIVelocityX(.7f);
				}
			}
			if ((AI3.getAIY() > y) && (isGrounded)) {
				if (AI3.isAIGrounded()) {
					AI3.setAIVelocityY(-.5f);
				}
			}
			AI3.move();
			
			if ((AI4.getAIX() > x) && (AI4.getAIVelocityX() >-4)) {
				if(Math.abs(AI4.getAIX() - x) <= 10) {
					AI4.setAIVelocityX(0);
				}
				else {
					AI4.setAIVelocityX(-.7f);
				}
			}else if ((AI4.getAIX() < x) && (AI4.getAIVelocityX() < 4)) {
				if(Math.abs(AI4.getAIX() - x) <= 10) {
					AI4.setAIVelocityX(0);
				}
				else {
					AI4.setAIVelocityX(.7f);
				}
			}
			if ((AI4.getAIY() > y) && (isGrounded)) {
				if (AI4.isAIGrounded()) {
					AI4.setAIVelocityY(-.5f);
				}
			}
			AI4.move();
			
			if ((AI5.getAIX() > x) && (AI5.getAIVelocityX() >-4)) {
				if(Math.abs(AI5.getAIX() - x) <= 10) {
					AI5.setAIVelocityX(0);
				}
				else {
					AI5.setAIVelocityX(-.7f);
				}
			}else if ((AI5.getAIX() < x) && (AI5.getAIVelocityX() < 4)) {
				if(Math.abs(AI5.getAIX() - x) <= 10) {
					AI5.setAIVelocityX(0);
				}
				else {
					AI5.setAIVelocityX(.7f);
				}
			}
			if ((AI5.getAIY() > y) && (isGrounded)) {
				if (AI5.isAIGrounded()) {
					AI5.setAIVelocityY(-.5f);
				}
			}
			AI5.move();
			
			if ((AI6.getAIX() > x) && (AI6.getAIVelocityX() >-4)) {
				if(Math.abs(AI6.getAIX() - x) <= 10) {
					AI6.setAIVelocityX(0);
				}
				else {
					AI6.setAIVelocityX(-.7f);
				}
			}else if ((AI6.getAIX() < x) && (AI6.getAIVelocityX() < 4)) {
				if(Math.abs(AI6.getAIX() - x) <= 10) {
					AI6.setAIVelocityX(0);
				}
				else {
					AI6.setAIVelocityX(.7f);
				}
			}
			if ((AI6.getAIY() > y) && (isGrounded)) {
				if (AI6.isAIGrounded()) {
					AI6.setAIVelocityY(-.5f);
				}
			}
			AI6.move();
			
			if ((AI7.getAIX() > x) && (AI7.getAIVelocityX() >-4)) {
				if(Math.abs(AI7.getAIX() - x) <= 10) {
					AI7.setAIVelocityX(0);
				}
				else {
					AI7.setAIVelocityX(-.7f);
				}
			}else if ((AI7.getAIX() < x) && (AI7.getAIVelocityX() < 4)) {
				if(Math.abs(AI7.getAIX() - x) <= 10) {
					AI7.setAIVelocityX(0);
				}
				else {
					AI7.setAIVelocityX(.7f);
				}
			}
			if ((AI7.getAIY() > y) && (isGrounded)) {
				if (AI7.isAIGrounded()) {
					AI7.setAIVelocityY(-.5f);
				}
			}
			AI7.move();
			
			if ((AI8.getAIX() > x) && (AI8.getAIVelocityX() >-4)) {
				if(Math.abs(AI8.getAIX() - x) <= 10) {
					AI8.setAIVelocityX(0);
				}
				else {
					AI8.setAIVelocityX(-.7f);
				}
			}else if ((AI8.getAIX() < x) && (AI8.getAIVelocityX() < 4)) {
				if(Math.abs(AI8.getAIX() - x) <= 10) {
					AI8.setAIVelocityX(0);
				}
				else {
					AI8.setAIVelocityX(.7f);
				}
			}
			if ((AI8.getAIY() > y) && (isGrounded)) {
				if (AI8.isAIGrounded()) {
					AI8.setAIVelocityY(-.5f);
				}
			}
			AI8.move();
		}
		
		else if (currentAI == 9) {
			if ((AI1.getAIX() > x) && (AI1.getAIVelocityX() >-4)) {
				if(Math.abs(AI1.getAIX() - x) <= 10) {
					AI1.setAIVelocityX(0);
				}
				else {
					AI1.setAIVelocityX(-.7f);
				}
			}else if ((AI1.getAIX() < x) && (AI1.getAIVelocityX() < 4)) {
				if(Math.abs(AI1.getAIX() - x) <= 10) {
					AI1.setAIVelocityX(0);
				}
				else {
					AI1.setAIVelocityX(.7f);
				}
			}
			if ((AI1.getAIY() > y) && (isGrounded)) {
				if (AI1.isAIGrounded()) {
					AI1.setAIVelocityY(-.5f);
				}
			}
			AI1.move();
			
			if ((AI2.getAIX() > x) && (AI2.getAIVelocityX() >-4)) {
				if(Math.abs(AI2.getAIX() - x) <= 10) {
					AI2.setAIVelocityX(0);
				}
				else {
					AI2.setAIVelocityX(-.7f);
				}
			}else if ((AI2.getAIX() < x) && (AI2.getAIVelocityX() < 4)) {
				if(Math.abs(AI2.getAIX() - x) <= 10) {
					AI2.setAIVelocityX(0);
				}
				else {
					AI2.setAIVelocityX(.7f);
				}
			}
			if ((AI2.getAIY() > y) && (isGrounded)) {
				if (AI2.isAIGrounded()) {
					AI2.setAIVelocityY(-.5f);
				}
			}
			AI2.move();
			

			if ((AI3.getAIX() > x) && (AI3.getAIVelocityX() >-4)) {
				if(Math.abs(AI3.getAIX() - x) <= 10) {
					AI3.setAIVelocityX(0);
				}
				else {
					AI3.setAIVelocityX(-.7f);
				}
			}else if ((AI3.getAIX() < x) && (AI3.getAIVelocityX() < 4)) {
				if(Math.abs(AI3.getAIX() - x) <= 10) {
					AI3.setAIVelocityX(0);
				}
				else {
					AI3.setAIVelocityX(.7f);
				}
			}
			if ((AI3.getAIY() > y) && (isGrounded)) {
				if (AI3.isAIGrounded()) {
					AI3.setAIVelocityY(-.5f);
				}
			}
			AI3.move();
			
			if ((AI4.getAIX() > x) && (AI4.getAIVelocityX() >-4)) {
				if(Math.abs(AI4.getAIX() - x) <= 10) {
					AI4.setAIVelocityX(0);
				}
				else {
					AI4.setAIVelocityX(-.7f);
				}
			}else if ((AI4.getAIX() < x) && (AI4.getAIVelocityX() < 4)) {
				if(Math.abs(AI4.getAIX() - x) <= 10) {
					AI4.setAIVelocityX(0);
				}
				else {
					AI4.setAIVelocityX(.7f);
				}
			}
			if ((AI4.getAIY() > y) && (isGrounded)) {
				if (AI4.isAIGrounded()) {
					AI4.setAIVelocityY(-.5f);
				}
			}
			AI4.move();
			
			if ((AI5.getAIX() > x) && (AI5.getAIVelocityX() >-4)) {
				if(Math.abs(AI5.getAIX() - x) <= 10) {
					AI5.setAIVelocityX(0);
				}
				else {
					AI5.setAIVelocityX(-.7f);
				}
			}else if ((AI5.getAIX() < x) && (AI5.getAIVelocityX() < 4)) {
				if(Math.abs(AI5.getAIX() - x) <= 10) {
					AI5.setAIVelocityX(0);
				}
				else {
					AI5.setAIVelocityX(.7f);
				}
			}
			if ((AI5.getAIY() > y) && (isGrounded)) {
				if (AI5.isAIGrounded()) {
					AI5.setAIVelocityY(-.5f);
				}
			}
			AI5.move();
			
			if ((AI6.getAIX() > x) && (AI6.getAIVelocityX() >-4)) {
				if(Math.abs(AI6.getAIX() - x) <= 10) {
					AI6.setAIVelocityX(0);
				}
				else {
					AI6.setAIVelocityX(-.7f);
				}
			}else if ((AI6.getAIX() < x) && (AI6.getAIVelocityX() < 4)) {
				if(Math.abs(AI6.getAIX() - x) <= 10) {
					AI6.setAIVelocityX(0);
				}
				else {
					AI6.setAIVelocityX(.7f);
				}
			}
			if ((AI6.getAIY() > y) && (isGrounded)) {
				if (AI6.isAIGrounded()) {
					AI6.setAIVelocityY(-.5f);
				}
			}
			AI6.move();
			
			if ((AI7.getAIX() > x) && (AI7.getAIVelocityX() >-4)) {
				if(Math.abs(AI7.getAIX() - x) <= 10) {
					AI7.setAIVelocityX(0);
				}
				else {
					AI7.setAIVelocityX(-.7f);
				}
			}else if ((AI7.getAIX() < x) && (AI7.getAIVelocityX() < 4)) {
				if(Math.abs(AI7.getAIX() - x) <= 10) {
					AI7.setAIVelocityX(0);
				}
				else {
					AI7.setAIVelocityX(.7f);
				}
			}
			if ((AI7.getAIY() > y) && (isGrounded)) {
				if (AI7.isAIGrounded()) {
					AI7.setAIVelocityY(-.5f);
				}
			}
			AI7.move();
			
			if ((AI8.getAIX() > x) && (AI8.getAIVelocityX() >-4)) {
				if(Math.abs(AI8.getAIX() - x) <= 10) {
					AI8.setAIVelocityX(0);
				}
				else {
					AI8.setAIVelocityX(-.7f);
				}
			}else if ((AI8.getAIX() < x) && (AI8.getAIVelocityX() < 4)) {
				if(Math.abs(AI8.getAIX() - x) <= 10) {
					AI8.setAIVelocityX(0);
				}
				else {
					AI8.setAIVelocityX(.7f);
				}
			}
			if ((AI8.getAIY() > y) && (isGrounded)) {
				if (AI8.isAIGrounded()) {
					AI8.setAIVelocityY(-.5f);
				}
			}
			AI8.move();
			
			if ((AI9.getAIX() > x) && (AI9.getAIVelocityX() >-4)) {
				if(Math.abs(AI9.getAIX() - x) <= 10) {
					AI9.setAIVelocityX(0);
				}
				else {
					AI9.setAIVelocityX(-.7f);
				}
			}else if ((AI9.getAIX() < x) && (AI9.getAIVelocityX() < 4)) {
				if(Math.abs(AI9.getAIX() - x) <= 10) {
					AI9.setAIVelocityX(0);
				}
				else {
					AI9.setAIVelocityX(.7f);
				}
			}
			if ((AI9.getAIY() > y) && (isGrounded)) {
				if (AI9.isAIGrounded()) {
					AI9.setAIVelocityY(-.5f);
				}
			}
			AI9.move();
		}
		
		else if (currentAI == 10) {
			if ((AI1.getAIX() > x) && (AI1.getAIVelocityX() >-4)) {
				if(Math.abs(AI1.getAIX() - x) <= 10) {
					AI1.setAIVelocityX(0);
				}
				else {
					AI1.setAIVelocityX(-.7f);
				}
			}else if ((AI1.getAIX() < x) && (AI1.getAIVelocityX() < 4)) {
				if(Math.abs(AI1.getAIX() - x) <= 10) {
					AI1.setAIVelocityX(0);
				}
				else {
					AI1.setAIVelocityX(.7f);
				}
			}
			if ((AI1.getAIY() > y) && (isGrounded)) {
				if (AI1.isAIGrounded()) {
					AI1.setAIVelocityY(-.5f);
				}
			}
			AI1.move();
			
			if ((AI2.getAIX() > x) && (AI2.getAIVelocityX() >-4)) {
				if(Math.abs(AI2.getAIX() - x) <= 10) {
					AI2.setAIVelocityX(0);
				}
				else {
					AI2.setAIVelocityX(-.7f);
				}
			}else if ((AI2.getAIX() < x) && (AI2.getAIVelocityX() < 4)) {
				if(Math.abs(AI2.getAIX() - x) <= 10) {
					AI2.setAIVelocityX(0);
				}
				else {
					AI2.setAIVelocityX(.7f);
				}
			}
			if ((AI2.getAIY() > y) && (isGrounded)) {
				if (AI2.isAIGrounded()) {
					AI2.setAIVelocityY(-.5f);
				}
			}
			AI2.move();
			

			if ((AI3.getAIX() > x) && (AI3.getAIVelocityX() >-4)) {
				if(Math.abs(AI3.getAIX() - x) <= 10) {
					AI3.setAIVelocityX(0);
				}
				else {
					AI3.setAIVelocityX(-.7f);
				}
			}else if ((AI3.getAIX() < x) && (AI3.getAIVelocityX() < 4)) {
				if(Math.abs(AI3.getAIX() - x) <= 10) {
					AI3.setAIVelocityX(0);
				}
				else {
					AI3.setAIVelocityX(.7f);
				}
			}
			if ((AI3.getAIY() > y) && (isGrounded)) {
				if (AI3.isAIGrounded()) {
					AI3.setAIVelocityY(-.5f);
				}
			}
			AI3.move();
			
			if ((AI4.getAIX() > x) && (AI4.getAIVelocityX() >-4)) {
				if(Math.abs(AI4.getAIX() - x) <= 10) {
					AI4.setAIVelocityX(0);
				}
				else {
					AI4.setAIVelocityX(-.7f);
				}
			}else if ((AI4.getAIX() < x) && (AI4.getAIVelocityX() < 4)) {
				if(Math.abs(AI4.getAIX() - x) <= 10) {
					AI4.setAIVelocityX(0);
				}
				else {
					AI4.setAIVelocityX(.7f);
				}
			}
			if ((AI4.getAIY() > y) && (isGrounded)) {
				if (AI4.isAIGrounded()) {
					AI4.setAIVelocityY(-.5f);
				}
			}
			AI4.move();
			
			if ((AI5.getAIX() > x) && (AI5.getAIVelocityX() >-4)) {
				if(Math.abs(AI5.getAIX() - x) <= 10) {
					AI5.setAIVelocityX(0);
				}
				else {
					AI5.setAIVelocityX(-.7f);
				}
			}else if ((AI5.getAIX() < x) && (AI5.getAIVelocityX() < 4)) {
				if(Math.abs(AI5.getAIX() - x) <= 10) {
					AI5.setAIVelocityX(0);
				}
				else {
					AI5.setAIVelocityX(.7f);
				}
			}
			if ((AI5.getAIY() > y) && (isGrounded)) {
				if (AI5.isAIGrounded()) {
					AI5.setAIVelocityY(-.5f);
				}
			}
			AI5.move();
			
			if ((AI6.getAIX() > x) && (AI6.getAIVelocityX() >-4)) {
				if(Math.abs(AI6.getAIX() - x) <= 10) {
					AI6.setAIVelocityX(0);
				}
				else {
					AI6.setAIVelocityX(-.7f);
				}
			}else if ((AI6.getAIX() < x) && (AI6.getAIVelocityX() < 4)) {
				if(Math.abs(AI6.getAIX() - x) <= 10) {
					AI6.setAIVelocityX(0);
				}
				else {
					AI6.setAIVelocityX(.7f);
				}
			}
			if ((AI6.getAIY() > y) && (isGrounded)) {
				if (AI6.isAIGrounded()) {
					AI6.setAIVelocityY(-.5f);
				}
			}
			AI6.move();
			
			if ((AI7.getAIX() > x) && (AI7.getAIVelocityX() >-4)) {
				if(Math.abs(AI7.getAIX() - x) <= 10) {
					AI7.setAIVelocityX(0);
				}
				else {
					AI7.setAIVelocityX(-.7f);
				}
			}else if ((AI7.getAIX() < x) && (AI7.getAIVelocityX() < 4)) {
				if(Math.abs(AI7.getAIX() - x) <= 10) {
					AI7.setAIVelocityX(0);
				}
				else {
					AI7.setAIVelocityX(.7f);
				}
			}
			if ((AI7.getAIY() > y) && (isGrounded)) {
				if (AI7.isAIGrounded()) {
					AI7.setAIVelocityY(-.5f);
				}
			}
			AI7.move();
			
			if ((AI8.getAIX() > x) && (AI8.getAIVelocityX() >-4)) {
				if(Math.abs(AI8.getAIX() - x) <= 10) {
					AI8.setAIVelocityX(0);
				}
				else {
					AI8.setAIVelocityX(-.7f);
				}
			}else if ((AI8.getAIX() < x) && (AI8.getAIVelocityX() < 4)) {
				if(Math.abs(AI8.getAIX() - x) <= 10) {
					AI8.setAIVelocityX(0);
				}
				else {
					AI8.setAIVelocityX(.7f);
				}
			}
			if ((AI8.getAIY() > y) && (isGrounded)) {
				if (AI8.isAIGrounded()) {
					AI8.setAIVelocityY(-.5f);
				}
			}
			AI8.move();
			
			if ((AI9.getAIX() > x) && (AI9.getAIVelocityX() >-4)) {
				if(Math.abs(AI9.getAIX() - x) <= 10) {
					AI9.setAIVelocityX(0);
				}
				else {
					AI9.setAIVelocityX(-.7f);
				}
			}else if ((AI9.getAIX() < x) && (AI9.getAIVelocityX() < 4)) {
				if(Math.abs(AI9.getAIX() - x) <= 10) {
					AI9.setAIVelocityX(0);
				}
				else {
					AI9.setAIVelocityX(.7f);
				}
			}
			if ((AI9.getAIY() > y) && (isGrounded)) {
				if (AI9.isAIGrounded()) {
					AI9.setAIVelocityY(-.5f);
				}
			}
			AI9.move();
			
			if ((AI10.getAIX() > x) && (AI10.getAIVelocityX() >-4)) {
				if(Math.abs(AI10.getAIX() - x) <= 10) {
					AI10.setAIVelocityX(0);
				}
				else {
					AI10.setAIVelocityX(-.7f);
				}
			}else if ((AI10.getAIX() < x) && (AI10.getAIVelocityX() < 4)) {
				if(Math.abs(AI10.getAIX() - x) <= 10) {
					AI10.setAIVelocityX(0);
				}
				else {
					AI10.setAIVelocityX(.7f);
				}
			}
			if ((AI10.getAIY() > y) && (isGrounded)) {
				if (AI10.isAIGrounded()) {
					AI10.setAIVelocityY(-.5f);
				}
			}
			AI10.move();
		}
	}
}
//FIN