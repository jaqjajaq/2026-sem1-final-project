import java.util.ArrayList;

public class Player {
    private final String name;
    private boolean hasFullHand;
    private int tokens;
    private ArrayList<Card> cards = new ArrayList<>();

    public Player(String n) {
        name = n;
        hasFullHand = false;
        tokens = 0;
    }

    public String getName() {
        return name;
    }

    public int getTokens() {
        return tokens;
    }

    public boolean handIsFull() {
        return hasFullHand;
    }

    public Card getActiveCard() {
        return cards.getFirst();
    }

    public void claimToken() {
        tokens++;
    }

    public void drawCard(Card c) {
        cards.add(c);

        if (cards.size() == 5)
            hasFullHand = true;
    }

    public void discard() {
        cards.removeFirst();

        hasFullHand = false;
    }

    private int findCard() {
        int found = 0;
        for (int i = 1; i < cards.size(); i++) {
            Card currentCard = cards.get(found);
            Card newCard = cards.get(i);

            int determiningProduct = newCard.getHealth() * newCard.getPower();

            if (determiningProduct > (currentCard.getHealth() * currentCard.getPower()))
                found = i;
        }

        return found;
    }

    public String swap() {
        if (cards.size() == 1)
            return "    " + name + " has no other card to swap with. Turn forfeited.";

        Card activeCard = getActiveCard();
        int newCardIndex = findCard();
        Card newCard = cards.get(newCardIndex);

        cards.set(0, newCard);
        // can use Collections.swap()
        cards.set(newCardIndex, activeCard);

        return String.format("    %s is now active with %d health.", newCard.getName(), newCard.getHealth());
    }

    public String statusReport() {
        String report = name.toUpperCase();

        String longestName = "";
        String longestHealth = "";
        for (Card c : cards) {
            if (c != null) {
                if (c.getName().length() > longestName.length()) {
                    longestName = c.getName();
                }

                String healthString = Integer.toString(c.getHealth());
                if (healthString.length() > longestHealth.length()) {
                    longestHealth = healthString;
                }
            }
        }

        for (Card c : cards) {
            String name = c.getName();
            String health = Integer.toString(c.getHealth());
            String padName = " ".repeat(longestName.length() - name.length());
            String padHealth = " ".repeat(longestHealth.length() - health.length());
            report += "\n    " + padName + name + " : " + padHealth + health;
        }

        return report;
    }
}
