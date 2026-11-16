import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Random;

public class GameMaster {
    private boolean isWon;
    private int turns;
    private Player player1;
    private Player player2;
    private ArrayList<Card> deck = new ArrayList<>();
    private boolean dealRandom;

    private void assembleDeck() {
        deck.add( new Card( "Dragon", "Aquira", 174, 26 ) );
        deck.add( new Card( "Ghost", "Brawn", 130, 48 ) );
        deck.add( new Card( "Fairy", "Cerulea", 162, 29 ) );
        deck.add( new Card( "Dragon", "Demi", 147, 28 ) );
        deck.add( new Card( "Ghost", "Elba", 155, 37 ) );
        deck.add( new Card( "Fairy", "Fye", 159, 42 ) );
        deck.add( new Card( "Dragon", "Glyede", 129, 26 ) );
        deck.add( new Card( "Ghost", "Hydran", 163, 35 ) );
        deck.add( new Card( "Fairy", "Ivy", 146, 45 ) );
        deck.add( new Card( "Dragon", "Jet", 170, 24 ) );
        deck.add( new Card( "Ghost", "Kineti", 139, 21 ) );
        deck.add( new Card( "Fairy", "Levi", 160, 43 ) );
        deck.add( new Card( "Dragon", "Meadow", 134, 29 ) );
        deck.add( new Card( "Ghost", "Naidem", 165, 26 ) );
        deck.add( new Card( "Fairy", "Omi", 145, 21 ) );
        deck.add( new Card( "Dragon", "Puddles", 170, 34 ) );
        deck.add( new Card( "Ghost", "Quarrel", 151, 29 ) );
        deck.add( new Card( "Fairy", "Raven", 168, 32 ) );
        deck.add( new Card( "Dragon", "Surge", 128, 27 ) );
        deck.add( new Card( "Ghost", "Takiru", 140, 26 ) );
        deck.add( new Card( "Fairy", "Ustelia", 163, 47 ) );
        deck.add( new Card( "Dragon", "Verwyn", 145, 25 ) );
        deck.add( new Card( "Ghost", "Wyverin", 158, 32 ) );
        deck.add( new Card( "Fairy", "Xios", 155, 27 ) );
        deck.add( new Card( "Dragon", "Yora", 159, 44 ) );
        deck.add( new Card( "Ghost", "Zulu", 125, 46 ) );
    }

    private void assembleExternalDeck() {
        File file = new File("newCards.txt");
        try (Scanner scanner = new Scanner(file)) {

            while (scanner.hasNextLine()) {
                String type = scanner.next();
                String name = scanner.next();
                int health = scanner.nextInt();
                int power = scanner.nextInt();

                deck.add(new Card(type, name, health, power));
            }
        } catch (FileNotFoundException e) {
            System.out.println("File newCards.txt not found! Default deck will be assembled instead.\n");
            assembleDeck();
        }
    }

    public GameMaster(Player a, Player b) {
        assembleDeck();
        player1 = a;
        player2 = b;
        isWon = false;
        turns = 1;
        dealRandom = false;
    }

    public GameMaster(Player a, Player b, String option) {
        dealRandom = option.equalsIgnoreCase("random");
        if (option.equalsIgnoreCase("newCards")) {
            assembleExternalDeck();
        } else {
            assembleDeck();
        }

        player1 = a;
        player2 = b;
        isWon = false;
        turns = 1;
    }

    public GameMaster(Player a, Player b, String option1, String option2) {
        dealRandom = option1.equalsIgnoreCase("random") || option2.equalsIgnoreCase("random");
        if (option1.equalsIgnoreCase("newCards") || option2.equalsIgnoreCase("newCards")) {
            assembleExternalDeck();
        } else {
            assembleDeck();
        }

        player1 = a;
        player2 = b;
        isWon = false;
        turns = 1;
    }

    public boolean checkResistance(String type1, String type2) {
        if (type1.equals("Human") || type2.equals("Human"))
            return false;

        if (type1.equals("Fairy")) {
            return type2.equals("Dragon");
        } else if (type1.equals("Ghost")) {
            return type2.equals("Fairy");
        } else { // Dragon
            return type2.equals("Ghost");
        }
    }

    public boolean checkWeakness(String type1, String type2) {
        if (type1.equals("Human") && type2.equals("Human"))
            return false;
        if (type1.equals("Human") || type2.equals("Human"))
            return true;

        if (type1.equals("Dragon")) {
            return type2.equals("Fairy");
        } else if (type1.equals("Fairy")) {
            return type2.equals("Ghost");
        } else { // Ghost
            return type2.equals("Dragon");
        }
    }

    public String dealCard() {
        Random rand = new Random();
        Player activePlayer = (turns % 2 == 1) ? player1 : player2;

        if (activePlayer.handIsFull())
            return activePlayer.getName() + "'s hand is full.";

        Card drawnCard;
        if (dealRandom) {
            drawnCard = deck.remove(rand.nextInt(deck.size() - 1));
        } else {
            drawnCard = deck.removeFirst();
        }

        activePlayer.drawCard(drawnCard);
        turns++;

        return String.format("%s draws %s.\n", activePlayer.getName(), drawnCard.getName());
    }

    public String dealDamage(Card inPlay, Card target) {
        String output = "";
        int power = inPlay.getPower();
        String attackerType = inPlay.getType();
        String targetType = target.getType();

        if (checkResistance(targetType, attackerType)) {
            power /= 2;
            output += String.format("        %s is resistant to %s.\n", targetType, attackerType);
        } else if (checkWeakness(targetType, attackerType)) {
            power *= 2;
            output += String.format("        %s is weak to %s.\n", targetType, attackerType);
        }

        target.takeDamage(power);
        output += String.format("    %s deals %d damage on %s.", inPlay.getName(), power, target.getName());
        output += String.format("\n    %s has %d health left.", target.getName(), target.getHealth());

        return output;
    }

    public boolean hasWinner() {
        return isWon;
    }

    public String gameReport() {
        String output = "---=== GAME SUMMARY ===---";
        output = output.concat(String.format("\nThis game lasted %d turns.", turns));

        output += "\n" + player1.statusReport() + '\n';
        output += "\n" + player2.statusReport();

        return output;
    }

    public String play(String action) {
        Player activePlayer = (turns % 2 == 1) ? player1 : player2;
        Player targetPlayer = (turns % 2 == 1) ? player2 : player1;

        if (action.equals("swap")) {
            Card swapped = activePlayer.getActiveCard();
            turns++;
            return String.format("    %s swaps out %s...\n%s", activePlayer.getName(), swapped.getName(), activePlayer.swap());
        } else if (action.equals("attack")) {
            Card activeCard = activePlayer.getActiveCard();
            Card targetCard = targetPlayer.getActiveCard();

            String output = String.format("    %s attacks with %s.", activePlayer.getName(), activeCard.getName());
            output += "\n" + dealDamage(activeCard, targetCard);

            // Card defeated
            // Discard & draw
            if (targetCard.getHealth() <= 0) {
                targetPlayer.discard();
                output += String.format("\n    %s discards %s.", targetPlayer.getName(), targetCard.getName());

                if (deck.isEmpty()) {
                    output += "\n    The deck is empty.";
                } else {
                    Card drawnCard;

                    if (deck.size() == 1) {
                        drawnCard = deck.removeFirst();
                    } else {
                        Card firstCard = deck.removeFirst();
                        Card secondCard = deck.removeFirst();

                        boolean chooseCard = firstCard.getHealth() * firstCard.getPower() > secondCard.getHealth() * secondCard.getPower();
                        drawnCard = chooseCard ? firstCard : secondCard;
                        deck.add(chooseCard ? secondCard : firstCard);
                    }

                    targetPlayer.drawCard(drawnCard);
                    output += String.format("\n    %s draws %s.", targetPlayer.getName(), drawnCard.getName());
                }

                activePlayer.claimToken();
                output += String.format("\n    %s gets a token!", activePlayer.getName());

                if (activePlayer.getTokens() == 3) {
                    isWon = true;
                    output += String.format("\n%s wins!!!", activePlayer.getName());
                    return output;
                }
            }

            turns++;
            return output;
        }

        return "Invalid action, try again!";
    }
}
