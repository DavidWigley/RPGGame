package Game;


public class AI {

	float AIVelocityX, AIVelocityY;
	private static final float defaultX=850;
	private static final float defaultY = 595;
	private static final double defaultHealth = 200;
	float AIX;
	float AIY;
	double AIHealth;
	boolean AIDead = false;
	
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
	
	public float getAIX() {
		return AIX;
	}
	public float getAIY() {
		return AIY;
	}
	public double getAIHealth() {
		return AIHealth;
	}
	public void moveAIX(int amount){
		AIX +=amount;
	}
	public void moveAIY(int amount){
		AIY +=amount;
	}
}
