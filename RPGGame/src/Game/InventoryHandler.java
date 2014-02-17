package Game;

public class InventoryHandler {

	public static int inventorySlots = 20;
	public static int inventory[][] = new int[inventorySlots][inventorySlots];
	
	public static void addItem(int id, int amount) {
		
		boolean itemAdded = false;
		
		for (int i = 0; i < inventorySlots; i++) {
			if (inventory[0][i] == 0) {
				inventory[0][i] = id;
				inventory[1][i] += amount;
				itemAdded = true;
				break;
			} else if (inventory[i][0] == id) {
				inventory[1][i] += amount;
				itemAdded = true;
				break;
			}
		}
		
		if (!itemAdded) {
			System.out.println("You have no room");
		}
	}
	
	public void removeItem(int id, int amount) {
		boolean itemRemoved = false;
		
		for (int i = 0; i < inventorySlots; i++) {
			if (inventory[i][0] == id) {
				inventory[1][i] -= amount;
				itemRemoved = true;
				break;
			}
		}
		
		if (!itemRemoved) {
			System.out.println("You don't have enough of that.");
		}
	}
	
	public static void main(String[] args) {
		addItem(1000, 2);
		
		for (int i = 0; i < inventorySlots; i++) {
			System.out.println(inventory[0][i]);
		}
	}
	
}
