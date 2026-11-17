import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

// names
// start of the game
// show each cards
// result of last action
// game statistics

public class GameGUI {
    JFrame frame;
    Player player1;
    Player player2;
    GameMaster game;

    StartMenu startMenu;
    GameUI gameUI;
    End end;

    private class End extends JPanel {

    }

    private class GameUI extends JPanel {
        public GameUI() {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

            JPanel actions = new JPanel();

            JPanel player1Cards = new JPanel();
            player1Cards.add(new JLabel(player1.getName()));

            JPanel player2Cards = new JPanel();
            player2Cards.add(new JLabel(player2.getName()));

            add(actions);
            add(player1Cards);
            add(player2Cards);
        }
    }

    private class StartMenu extends JPanel {
        public StartMenu() {
            JTextField player1Name = new JTextField(20);
            JTextField player2Name = new JTextField(20);
            JButton startGame = new JButton("Start Game");

            ActionListener startListener = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    String name1 = player1Name.getText();
                    String name2 = player2Name.getText();

                    player1 = new Player(name1);
                    player2 = new Player(name2);

                    game = new GameMaster(player1, player2);

                    gameUI = new GameUI();

                    frame.setContentPane(gameUI);
                    frame.revalidate();
                    frame.repaint();
                }
            };

            startGame.addActionListener(startListener);

            add(player1Name);
            add(player2Name);
            add(startGame);
        }
    }

    public GameGUI() {
        frame = new JFrame("Final Project");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null); // Center on screen

        startMenu = new StartMenu();

        frame.setContentPane(startMenu);
        frame.setVisible(true);
    }
}