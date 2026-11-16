public class Card {
    private final String type;
    private final String name;
    private int health;
    private final int power;

    public Card(String t, String n, int h, int p) {
        type = t;
        name = n;
        health = h;
        power = p;
    }

    public int getHealth() {
        return health;
    }

    public String getName() {
        return name;
    }

    public int getPower() {
        return power;
    }

    public String getType() {
        return type;
    }

    public void takeDamage(int d) {
        health -= d;
    }
}
