// Location.java
import java.util.*;

class Location {
    public String name;
    public String description;
    public Map<String, Location> adjacentLocations = new HashMap<>();
    public List<Item> items = new ArrayList<>();

    public Location(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public void addAdjacentLocation(String direction, Location location) {
        adjacentLocations.put(direction.toLowerCase(), location);
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public void removeItem(Item item) {
        items.remove(item);
    }

    public Item findItem(String name) {
        for (Item item : items) {
            if (item.name.equalsIgnoreCase(name)) {
                return item;
            }
        }
        return null;
    }
}