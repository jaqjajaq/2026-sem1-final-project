import java.util.Scanner;

public class GameConsole {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        Player player1 = new Player(args[0]);
        Player player2 = new Player(args[1]);

        System.out.printf("Welcome, %s and %s!\n", player1.getName(), player2.getName());
        System.out.println("The game begins.\n");

        GameMaster game;
        switch (args.length) {
            case 3:
                game = new GameMaster(player1, player2, args[2]);
                break;
            case 4:
                game = new GameMaster(player1, player2, args[2], args[3]);
                break;
            default:
                game = new GameMaster(player1, player2);
        }

        // Deal each player 5 cards (10 total)
        for (int i = 0; i < 10; i++) {
            System.out.println(game.dealCard());
        }

        while (!game.hasWinner()) {
            System.out.print("Attack or Swap? ");
            String action = input.nextLine();

            // bonus
            System.out.println(game.play(action.toLowerCase()) + "\n");
        }

        System.out.println(game.gameReport());
    }
}
