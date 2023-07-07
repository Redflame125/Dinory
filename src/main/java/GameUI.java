import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReferenceArray;

import static java.lang.Thread.sleep;

public class GameUI extends JFrame {
    private final HashMap<JButton, ImageIcon> buttonImages = new HashMap<>();
    private final HashMap<Integer, ImageIcon> dinoPairs = new HashMap<>();
    private final HashMap<JButton, Integer> buttonPairs = new HashMap<>();
    private final Utils utils = new Utils(); // Hilfsklasse für die Bilder
    private final int waitPeriod; // Wartezeit zwischen den Zügen
    private final int x = 6, y = 6; // Größe des Spielfelds
    private final int playerCount;
    private int currentPlayer = 0;
    private boolean clickedOnce = false, flipAllowed = true; // FlipFlops
    private JButton firstButton, doubleClickCheck; // Cache für den ersten Button beim Klick
    // Konstruktor
    public GameUI(int waitPeriod, int playerCount, AtomicReferenceArray<String> playerNames) {
        super("Dinory"); // JFrame Erstellen

        // Init WaitPeriod
        this.waitPeriod = waitPeriod;

        // Init Size
        int width = x * 100, height = y * 100;

        // Init PlayerCount
        this.playerCount = playerCount;


        // Init PlayerNames
        HashMap<String, Integer> playerScores = new HashMap<>();
        for (int i = 0; i < playerCount; i++) {
            playerScores.put(playerNames.get(i), 0);
        }

        System.out.println(playerNames.get(currentPlayer) + " ist am Zug");

        // JFrame Attribute
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(width, height);
        setVisible(true);
        setResizable(false);
        JPanel buttonPanel = new JPanel();
        add(buttonPanel);

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(dim.width/2 - getSize().width/2, dim.height/2 - getSize().height/2);

        initDinoPairs();


        buttonPanel.setLayout(new GridLayout(x, y));


        ArrayList<JButton> buttonList = new ArrayList<>(); // Cache für die Buttons

        // Create buttons
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++){
                JButton button = new JButton();
                button.setSize(83, 83);
                button.setBackground(Color.WHITE);
                button.setBorder(BorderFactory.createLineBorder(Color.WHITE));
                buttonPanel.add(button);
                button.setIcon(utils.createImageIcon("Backside.png"));
                buttonList.add(button);

                button.addActionListener(e -> {
                    JButton btn = (JButton) e.getSource();


                    if (flipAllowed) {
                        btn.setIcon(buttonImages.get(btn));

                        if (clickedOnce && btn != firstButton) {
                            clickedOnce = false;
                            handleFlip(btn, firstButton);
                            doubleClickCheck = btn;

                            currentPlayer++;
                            if (currentPlayer == playerCount) {
                                currentPlayer = 0;
                            }
                            System.out.println(playerNames.get(currentPlayer) + " ist am Zug");
                        } else if (doubleClickCheck != btn) {
                            clickedOnce = true;
                            firstButton = btn;
                        }
                    }
                });
            }
        }


        mixer(buttonList);

        new GameManager(this);
    }

    // Randomize the dino pairs
    private void mixer(ArrayList<JButton> buttonList) {
        Random random = new Random();

        // Generate random pairs
        while (buttonList.size() > 0) {
            int randomKey = random.nextInt(buttonList.size());
            int randomValue = random.nextInt(buttonList.size());

            while (randomValue == randomKey) {
                randomValue = random.nextInt(buttonList.size());
            }

            // Calculates number of pairs
            int index = (buttonList.size() / 2) - 1;

            // Get random dino image
            ImageIcon icon = dinoPairs.get(index);

            JButton key = buttonList.get(randomKey);
            JButton value = buttonList.get(randomValue);

            buttonImages.put(key, icon);
            buttonImages.put(value, icon);

            buttonPairs.put(key, index);
            buttonPairs.put(value, index);

            buttonList.remove(randomKey);
            if (randomKey < randomValue) {
                randomValue--;
            }
            buttonList.remove(randomValue);
        }
    }

    // Initialize dino pairs
    private void initDinoPairs() {
        ArrayList<ImageIcon> randomList = new ArrayList<>();
        for (int i = 0; i < 18; i++) {
            randomList.add(utils.createImageIcon("Dino/Dino" + i + ".png"));
        }
        // Shuffle the cards
        Collections.shuffle(randomList);

        for (int i = 0; i < 18; i++) {
            dinoPairs.put(i, randomList.get(i));
        }
    }

    // Handle flip
    private void handleFlip(JButton btn, JButton firstButton) {
        new Thread(() -> {
            try {
                if (!utils.checkPairs(btn, firstButton, buttonPairs)) {
                    flipAllowed = false;
                    sleep(waitPeriod);
                    btn.setIcon(utils.createImageIcon("Backside.png"));
                    firstButton.setIcon(utils.createImageIcon("Backside.png"));
                } else {
                    btn.setEnabled(false);
                    firstButton.setEnabled(false);
                }
                flipAllowed = true;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
