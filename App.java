import java.util.*;

public class App {
    static Scanner in = new Scanner(System.in);

    public static void main(String[] args) {
        // Game variables
        String playerName = "";
        String lostSibling = "";
        Location currentLocation;
        Player player = new Player();

        // Prologue
        System.out.println("Welcome to Sibling Rescue!");
        System.out.println("\nPrologue:");
        System.out.println("Edward and Alice, siblings who love adventure, were playing hide-and-seek in the forest.");
        System.out.println("During the game, one of them went too far into the woods and never returned.");
        System.out.println("Now, it’s up to you to choose who to play as and save the other sibling before it’s too late.\n");

        // Character Selection
        while (true) {
            System.out.print("Choose your character (Edward/Alice): ");
            String choice = in.nextLine().trim().toLowerCase();
            if (choice.equals("edward")) {
                playerName = "Edward";
                player.setAttributes(90, 1.5, true); // Edward: 90HP, faster, higher damage
                lostSibling = "Alice";
                break;
            } else if (choice.equals("alice")) {
                playerName = "Alice";
                player.setAttributes(180, 1.0, false); // Alice: 120HP, slower, damage reduction
                lostSibling = "Edward";
                break;
            } else {
                System.out.println("Invalid choice. Please type 'Edward' or 'Alice'.\n");
            }
        }

        // Start the game
        System.out.println("\nYou are playing as " + playerName + ".");
        System.out.println("Your sibling, " + lostSibling + ", is lost deep in the woods. It’s up to you to find them.");
        System.out.println("Prepare yourself for an adventure filled with danger, mystery, and choices.\n");

        // Locations setup
        Location forest = new Location("Forest", "You are in a dense forest. The trees are tall, and the air is thick with mystery.");
        Location deepWoods = new Location("Deep Woods", "The trees are thicker here, and the shadows seem to move on their own.");
        Location cabin = new Location("Cabin", "A small, eerie cabin. Its windows are broken, and the door creaks in the wind.");
        Location basement = new Location("Basement", "The basement is dark and cold. You feel a sinister presence here.");
        Location lockedRoom = new Location("Locked Room", "A room with ominous markings on the walls. Something terrible is here.");

        // Add items to cabin rooms
        cabin.addItem(new Item("Key", "A rusty key that might unlock something important."));
        basement.addItem(new Item("Flashlight Batteries", "Batteries to power your flashlight."));
        lockedRoom.addItem(new Item("Potion", "A potion that restores health."));

        // Link locations
        forest.addAdjacentLocation("north", deepWoods);
        deepWoods.addAdjacentLocation("south", forest);
        deepWoods.addAdjacentLocation("cabin", cabin);
        cabin.addAdjacentLocation("basement", basement);
        basement.addAdjacentLocation("locked room", lockedRoom);

        // Set initial location
        currentLocation = forest;

        // Navigation and exploration loop
        boolean exploring = true;
        boolean battleTriggered = false;
        while (exploring) {
            System.out.println("\nLocation: " + currentLocation.name);
            System.out.println(currentLocation.description);
            System.out.print("Available directions: ");
            currentLocation.adjacentLocations.keySet().forEach(direction -> System.out.print(direction + " "));
            System.out.println("\nItems here: ");
            currentLocation.items.forEach(item -> System.out.println("- " + item.name));
            System.out.println("What would you like to do? (type 'go [direction]', 'take [item]', or 'quit')");

            String input = in.nextLine().trim().toLowerCase();
            if (input.equals("quit")) {
                System.out.println("You have decided to end your adventure. Goodbye!");
                exploring = false;
            } else if (input.startsWith("go ")) {
                String direction = input.substring(3);
                if (currentLocation.adjacentLocations.containsKey(direction)) {
                    currentLocation = currentLocation.adjacentLocations.get(direction);
                    System.out.println("You move " + direction + " to the " + currentLocation.name + ".");
                    if (currentLocation.name.equals("Locked Room")) {
                        battleTriggered = true;
                        break;
                    }
                } else {
                    System.out.println("You can't go " + direction + ".");
                }
            } else if (input.startsWith("take ")) {
                String itemName = input.substring(5);
                Item itemToTake = currentLocation.findItem(itemName);
                if (itemToTake != null) {
                    player.addItem(itemToTake);
                    currentLocation.removeItem(itemToTake);
                    System.out.println("You picked up the " + itemName + ".");
                } else {
                    System.out.println("There is no " + itemName + " here.");
                }
            } else {
                System.out.println("Invalid command. Try again.");
            }
        }

        // Battle System
        if (battleTriggered) {
            System.out.println("\nYou enter the Locked Room and see your sibling, " + lostSibling + ", lying motionless on the floor.");
            System.out.println("Suddenly, their body jolts up, and their eyes glow with a sinister light. They are possessed!");

            Enemy possessedSibling = new Enemy(lostSibling, 180);
            boolean playerWon = battle(player, possessedSibling);

            // Endings
            if (playerWon) {
                System.out.println("You defeated the possessed sibling and saved their soul! You both escape the cabin together.");
                System.out.println("Ending: Victory! You and " + lostSibling + " are reunited.");
            } else {
                System.out.println("As you fall to the ground, you see " + lostSibling + " vanish into the spirit realm with you.");
                System.out.println("Ending: Destiny Bond. You start to lose consciousness and your eyes start to flutter. You try to stay up after the attacks, but the possesed being is too strong. As you fall to the ground, you see " + lostSibling + " crying. The two of you reach out to each other with your fingertips, but a sudden spiritual pressure becomes immense and" + lostSibling + "can no longer handle the force, creating void that englufs you, and" + lostSibling + "into the spirit realm. Your bodies are left in the locked room, with both of your hands interlocked. You regain consciousness in the spirit realm, and you see " + lostSibling + " floating in front of you, smiling. You both embrace with a warm hug, as you guys vow to never lose each other again.");
            }
        }
    }

    public static boolean battle(Player player, Enemy enemy) {
        System.out.println("\nBattle begins!");
        while (player.getHp() > 0 && enemy.getHp() > 0) {
            if (player.isFaster()) {
                playerTurn(player, enemy);
                if (enemy.getHp() > 0) {
                    enemyTurn(player, enemy);
                }
            } else {
                enemyTurn(player, enemy);
                if (player.getHp() > 0) {
                    playerTurn(player, enemy);
                }
            }
        }
        return player.getHp() > 0;
    }

    public static void playerTurn(Player player, Enemy enemy) {
        System.out.println("\nYour Turn:");
        System.out.println("Choose an action: Attack or Use Potion");
        String action = in.nextLine().trim().toLowerCase();

        if (action.equals("attack")) {
            int damage = player.attack();
            enemy.takeDamage(damage);
            System.out.println("You attack and deal " + damage + " damage to the " + enemy.getName() + ".");
        } else if (action.equals("use potion") && player.hasItem("Potion")) {
            player.usePotion();
            System.out.println("You use a Potion and restore 20 HP.");
        } else {
            System.out.println("Invalid action or no Potion available.");
        }
    }

    public static void enemyTurn(Player player, Enemy enemy) {
        System.out.println("\nEnemy's Turn:");
        enemy.attack(player);
    }
}
