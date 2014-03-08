package Game;

public class Player {
	private float x;
	private float y;
	private double health;
	private static final float DEFAULT_X = 350;
	private static final float DEFAULT_Y = 590;
	private static final double DEFAULT_HEALTH = 200.0;
	private float velocityX = 0;
	private float velocityY = 0;
	private boolean canMove = true;
	private static final float gravity = 0.2f;
	private boolean grounded;
	
	//anthony is a noobhead
	
	public Player() {
		x = DEFAULT_X;
		y = DEFAULT_Y;
		health = DEFAULT_HEALTH;
	}
	
	
	public Player(float x, float y, double health) {
		this.x = x;
		this.y = y;
		this.health = health;
	}
	
	
	public void move() {
		if (canMove) {
			velocityY+= (gravity * 3);
			x+=velocityX;
			y+=velocityY;
			if (y >= 590) {
				grounded = true;
				velocityY = 0;
				y = 590;
			} else {
				grounded = false;
			}
		}
	}
	
	public void setVelocityX(float amount) {
		if (amount !=0) {
			velocityX+=amount;
		}else{
			velocityX=0;
		}
	}
	public void setVelocityY(float amount){
		if (amount != 0) {
			velocityY+=amount;
		}else{
			velocityY=0;
		}
	}
	
	public float getVelocityX() {
		return velocityX;
	}
	public float getVelocityY() {
		return velocityY;
	}
	public boolean isGrounded() {
		return grounded;
	}
	
	public double getHealth() {
		return health;
	}
	
	public void setHealth(double damage) {
		health -=damage;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	public boolean isDead() {
		if (health<=0) {
			die();
			return true;
		}else {
			return false;
		}
	}
	
	public void die() {
		canMove = false;
		x = -1000;
		y = -1000;
	}
}
