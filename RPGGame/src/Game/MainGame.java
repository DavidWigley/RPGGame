/**
 * @author Anthony Foster + David Wigley
 * @art Therone McQueen + Alex Taylor
 * @music Jake Berlandi
 */
package Game;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import Game.ControlBase;

public class MainGame extends Canvas implements Runnable, KeyListener,MouseListener, MouseMotionListener {

	ControlBase base = new ControlBase();
	
	//pictures
	ImageIcon background = new ImageIcon(getClass().getResource("/resources/background test2.png"));
	Image picture = background.getImage();
	Image offscreen;
	ImageIcon arrowIconl = new ImageIcon(getClass().getResource("/resources/arrowl.png"));
	Image Arrowl = arrowIconl.getImage();
	ImageIcon arrowIconr = new ImageIcon(getClass().getResource("/resources/arrowr.png"));
	Image Arrowr = arrowIconr.getImage();
	ImageIcon mapIcon = new ImageIcon(getClass().getResource("/resources/map.png"));
	Image map = mapIcon.getImage();
	ImageIcon inventoryIcon = new ImageIcon(getClass().getResource("/resources/inventorypic.png"));
	Image inventory = inventoryIcon.getImage();
	ImageIcon equipmentIcon = new ImageIcon(getClass().getResource("/resources/equipment.png"));
	Image equipment = equipmentIcon.getImage();
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
	AudioInputStream audioIn;
	Clip clip;
	AudioInputStream attackIn;
	Clip attackClip;
	boolean notPlayingSound, shouldPlaySound;
	
	//player variables
	double attack, weight, defense, movement, dodgeChance;
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
	private boolean fireLeft, fireRight, shouldDrawMap, alreadyRan,
	shouldDrawInv;
	private float aX = x, aY = y, lastPressed = 0;
	private boolean isReleased, lClick, rClick, drawArrow, mouseClicked;
	
	//melee variables
	private boolean drawSword = false;
	private int swordCount = 0;
	
	//UI variables
	int healthY = 20;
	int healthX = 200;
	int invX = 600;
	int invY = 200;
	private boolean UI = true;
	private boolean escape, escapePushed, onMap, onInventory, onEquipment, AIGrounded;

	
	//AI variables
	float AIVelocityX, AIVelocityY;
	float AIX = x + 500;
	float AIY = y + 5;
	
	public MainGame() {
		

		base.frame.setVisible(true);
		base.frame.setResizable(false);
		base.frame.setMinimumSize(new Dimension(1024, 768));
		base.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		base.frame.setLocationRelativeTo(null);
		base.frame.addMouseListener(this);
		base.frame.addMouseMotionListener(this);
		base.frame.addKeyListener(this);
		//JPanel panel = new JPanel();
		//Container pane = base.frame.getContentPane();
		//pane.add(panel);
		base.frame.setVisible(true);
		base.frame.createBufferStrategy(bufNum);
		base.frame.setIconImage(frameIcon);
		try {
			startMusic();
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
	 * Paint method. Idea: make separate method to handle drawing stuff that
	 * remains constant no matter what. ie: string health bar Also main menu?
	 * 
	 * @param InvImage
	 *            is what gets passed in to draw the inventory image
	 * 
	 */
	public void paint() {
		BufferStrategy bf = base.frame.getBufferStrategy();
		// g = null;

		try {
			g = bf.getDrawGraphics();
			g.clearRect(0, 0, 1024, 768);
			if ((!escape) && (!dead)) {
				g.drawImage(picture, 0, 0, base.frame.getWidth(), base.frame.getHeight(), this);
				//g.drawImage(SwordHorizontalL, 200, 200, this);
				g.setColor(Color.red);
				g.fillOval(Math.round(x), Math.round(y), 20, 20);
				// toggles UI
				if (UI) {
					g.setColor(Color.darkGray);
					g.drawImage(map, 20, 720, this);
					g.drawImage(inventory, 120, 720, this);
					g.drawImage(equipment, 200, 720, this);
					// health bar
					g.fillRect(900, 40, 100, healthY);
					g.setColor(Color.red);
					g.fillRect(900, 40, (healthX/2), healthY);
					// draws string health bar. In future may want to make that
					// an image so it can be bigger
					g.setColor(Color.black);
					g.drawString("Health Bar", 900, 38);

				}
				// toggles Map
				if (shouldDrawMap) {
					g.drawImage(picture, 200, 100, 700, 600, this);
				}
				if (shouldDrawInv) {
					g.drawRect(invX, invY, 40, 40);
					g.drawRect(invX + 40, invY + 40, 40, 40);
					g.drawRect(invX + 80, invY + 80, 40, 40);
					g.drawRect(invX + 40, invY, 40, 40);
					g.drawRect(invX + 80, invY, 40, 40);
					g.drawRect(invX, invY + 40, 40, 40);
					g.drawRect(invX, invY + 80, 40, 40);
					g.drawRect(invX + 40, invY + 80, 40, 40);
					g.drawRect(invX + 80, invY + 40, 40, 40);
					// g.drawRect(200, 200, 40, 40);
					// g.drawRect(200, 200, 40, 40);
					// g.drawRect(200, 200, 40, 40);
					// g.drawRect(200, 200, 40, 40);
					// g.drawRect(200, 200, 40, 40);
					// g.drawRect(200, 200, 40, 40);
					// g.drawRect(200, 200, 40, 40);
				}
				
				//AI
				g.setColor(Color.blue);
				g.fillOval(Math.round(AIX), Math.round(AIY), 20, 20);

				// early access banner
				g.drawImage(earlyAccess, 0, 24, this);

				if ((drawArrow == true) && (attackStyle == 2)) {
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
					if (swordCount < 5) {
						//image flip g.drawImage(SwordHorizontalL, Math.round(x) - 2, Math.round(y) - 45, -SwordHorizontalL.getWidth(this),SwordHorizontalL.getHeight(this),this);
						g.drawImage(SwordHorizontalL, Math.round(x) - 2, Math.round(y) - 45, this);					}
					else if (swordCount > 5 && swordCount <=15) {
						g.drawImage(Sword45L,Math.round(x) - 45, Math.round(y) - 30, this);
					}else if (swordCount >15 && swordCount<30) {
						g.drawImage(SwordL, Math.round(x) - 63, Math.round(y), this);	
					}
					else  if  (swordCount >30 || !drawSword){
						g.drawImage(SwordHorizontalL, Math.round(x) - 2, Math.round(y) - 45, this);
						swordCount = 0;	
					}
				}
				else {
					g.drawImage(SwordHorizontalL, Math.round(x) - 2, Math.round(y) - 45, this);
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
		if (e.getX() > 250) {
			mouseLeft = false;
			mouseRight = true;
		}
		if (e.getX() < 250) {
			mouseRight = false;
			mouseLeft = true;
		}
		if ((e.getX() >= 20) && (e.getX() <= 65) && (e.getY() >= 720)
				&& (e.getY() <= 765)) {
			onMap = true;
		} else if ((e.getX() >= 120) && (e.getX() <= 165) && (e.getY() >= 720)
				&& (e.getY() <= 765)) {
			onInventory = true;
		} else if ((e.getX() >= 200) && (e.getX() <= 245) && (e.getY() >= 720)
				&& (e.getY() <= 765)) {
			onEquipment = true;
		}
		if ((e.getX() < 20) || (e.getX() > 65) || (e.getY() < 720)
				|| (e.getY() > 765)) {
			onMap = false;
			alreadyRan = false;
		}
		if ((e.getX() < 120) || (e.getX() > 165) || (e.getY() < 720)
				|| (e.getY() > 765)) {
			onInventory = false;
			alreadyRan = false;
		}
		if ((e.getX() < 200) || (e.getX() > 245) || (e.getY() < 720)
				|| (e.getY() > 765)) {
			onEquipment = false;
			alreadyRan = false;
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
			mouseClicked = true;
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
			// lClick = false;
			// drawingArrow = false;
			mouseClicked = false;
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
		// down arrow
//		if (e.getKeyCode() == 40 || e.getKeyCode() == 83) {
//			down = true;
//		}
		// escape key
		if ((e.getKeyCode() == 27) && (!escape) && (!escapePushed) && (!dead)) {
			escape = true;
			escapePushed = true;
			paint();
		} else if ((e.getKeyCode() == 27) && (escape) && (!escapePushed) && (!dead)) {
			escape = false;
			escapePushed = true;
		}
		//space
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			InventoryHandler.addItem(1, 11);
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
		// down arrow
//		if (e.getKeyCode() == 40 || e.getKeyCode() == 83) {
//			down = false;
//		}
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
		if (lClick == true) {
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


	// needs more math to actually move arrows and rely on feedback off the
	// mouse.
	public void arrowMech() {
		// fixes arrow flight change adds another bug
		// && drawingArrow == false
		if ((mouseLeft == true)) {
			fireLeft = true;
			fireRight = false;
		}
		// fixes arrow flight change adds another bug
		// && drawingArrow == false
		if ((mouseRight == true)) {
			fireRight = true;
			fireLeft = false;
		}
	}

	public void checkMouse() {
		if ((onMap == true) && (mouseClicked == true) && (!alreadyRan)) {
			// opens map
			System.out.println("map should open");
			alreadyRan = true;
			if (!shouldDrawMap) {
				shouldDrawMap = true;
			} else if (shouldDrawMap) {
				shouldDrawMap = false;
			}
		}
		if ((onInventory == true) && (mouseClicked == true) && (!alreadyRan)) {
			// opens inventory
			System.out.println("inventory should open");
			alreadyRan = true;
			if (!shouldDrawInv) {
				shouldDrawInv = true;
			} else if (shouldDrawInv) {
				shouldDrawInv = false;
			}
		}
		if ((onEquipment == true) && (mouseClicked == true) && (!alreadyRan)) {
			// open equipment menu
			System.out.println("equipment should open");
			alreadyRan = true;
		}

	}

	public void startMusic() throws Exception, IOException {
		audioIn = AudioSystem.getAudioInputStream(getClass().getResource(
				"/resources/intro.wav"));
		clip = AudioSystem.getClip();
		try {
			clip.open(audioIn);
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
		clip.start();
	}

	public void stopMusic() {
		clip.stop();
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

	public void timerTask() {
		if (!escape) {
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
			// moves down
//			if (down == true) {
//				y += 2;
//			}
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
			//checks if AI is close enough to attack with melee
			if ((Math.abs(AIX - x) <= 10) && Math.abs(AIY - y) <= 3) {
				//healthX -=.4;
				if (healthX <= 0) {
					dead  = true;
				}
			}
			checkMouse();
//			System.out.println("AIX "+ AIX);
//			System.out.println("AIY "+ AIY);
			
			AIMove();
			paint();
		}
	}
	
	public void AIMove() {
		AIVelocityY += (gravity * 3);
		AIX += AIVelocityX;
		AIY += AIVelocityY;
		
		if (AIY >= 590) {
			AIGrounded = true;
			AIVelocityY = 0;
			AIY = 590;
		} else {
			isGrounded = false;
		}
		if ((AIX > x) && (AIVelocityX >-4)) {
			if(Math.abs(AIX - x) <= 10) {
				AIVelocityX = 0;
			}
			else {
				AIVelocityX -= .7;
			}
		}
		else if ((AIX < x) && (AIVelocityX < 4)) {
			if(Math.abs(AIX - x) <= 10) {
				AIVelocityX = 0;
			}
			else {
				AIVelocityX += .7;
			}
		}
		
		if ((AIY > y) && (isGrounded)) {
			if (AIGrounded) {
				AIVelocityY -=5;
			}
		}
		
	}
}
