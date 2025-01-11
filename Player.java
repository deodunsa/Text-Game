import java.util.*;

class Player {
    private int hp;
    private double damageMultiplier;
    private boolean faster;
    private List<Item> inventory = new ArrayList<>();
    private static final int INVENTORY_LIMIT = 4;

    public void setAttributes(int hp, double damageMultiplier, boolean faster) {
        this.hp = hp;
        this.damageMultiplier = damageMultiplier;
        this.faster = faster;
    }

    public int getHp() {
        return hp;
    }

    public boolean isFaster() {
        return faster;
    }

    public void takeDamage(int damage) {
        hp -= damage;
        System.out.println("You take " + damage + " damage. Remaining HP: " + hp);
    }

    public int attack() {
        return (int) (20 * damageMultiplier); // Base damage is 20, scaled by multiplier
    }

    public void addItem(Item item) {
        if (inventory.size() < INVENTORY_LIMIT) {
            inventory.add(item);
        } else {
            System.out.println("Your inventory is full! You must drop an item to pick this up.");
        }
    }

    public void usePotion() {
        if (hasItem("Potion")) {
            hp += 20; // Restore 20 HP
            inventory.removeIf(item -> item.name.equalsIgnoreCase("Potion"));
        }
    }

    public boolean hasItem(String name) {
        return inventory.stream().anyMatch(item -> item.name.equalsIgnoreCase(name));
    }
}
