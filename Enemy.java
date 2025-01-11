// Enemy.java
class Enemy {
    private String name;
    private int hp;

    public Enemy(String name, int hp) {
        this.name = name;
        this.hp = hp;
    }

    public String getName() {
        return name;
    }

    public int getHp() {
        return hp;
    }

    public void takeDamage(int damage) {
        hp -= damage;
        System.out.println(name + " takes " + damage + " damage.");
    }

    public void attack(Player player) {
        int damage = 30; // Standard attack damage
        player.takeDamage(damage);
        System.out.println(name + " attacks and deals " + damage + " damage.");
    }
}