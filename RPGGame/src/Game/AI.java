package Game;

import java.awt.Color;


public class AI {

	float AIVelocityX, AIVelocityY;
	private static final float defaultX=850;
	private static final float defaultY = 595;
	private static final double defaultHealth = 200;
	float AIX;
	float AIY;
	double AIHealth;
	private static final float gravity = 0.2f;
	private boolean AIGrounded;
	private boolean canMove = true;
	private Color color = null;
	
	public AI(float x, float y, double health) {
		AIX = x;
		AIY = y;
		AIHealth = health;
	}
	public AI() {
		AIX = defaultX;
		AIY = defaultY;
		AIHealth = defaultHealth;
	}
	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
	}
	public boolean isAIGrounded() {
		return AIGrounded;
	}
	
	public float getAIX() {
		return AIX;
	}
	public float getAIY() {
		return AIY;
	}
	public double getAIHealth() {
		return AIHealth;
	}
	//overloaded method. Runs double regardless
	public void setHealth(int amount) {
		setHealth(amount);
	}
	public void  setHealth(double amount) {
		AIHealth+=amount;
	}
	public void moveAIX(int amount){
		AIX +=amount;
	}
	public void moveAIY(int amount){
		AIY +=amount;
	}
	public boolean isDead() {
		if (AIHealth<=0) {
			die();
			return true;
		}else {
			return false;
		}
	}
	
	public float getAIVelocityX() {
		return AIVelocityX;
	}
	
	public float getAIVelocityY() {
		return AIVelocityY;
	}
	
	public void setAIVelocityX(float amount) {
		if (amount!=0) {
			AIVelocityX+=amount;
		}
		else {
			AIVelocityX =0;
		}
	}
	
	public void setAIVelocityY(float amount) {
		AIVelocityY+=amount;
	}
	public void move() {
		if (canMove) {
			AIVelocityY+= (gravity * 3);
			AIX+=AIVelocityX;
			AIY+=AIVelocityY;
			if (AIY >= 590) {
				AIGrounded = true;
				AIVelocityY = 0;
				AIY = 590;
			} else {
				AIGrounded = false;
			}
		}
	}
	
	public void die() {
		canMove = false;
		AIX = -1000;
		AIY = -1000;
	}

}
