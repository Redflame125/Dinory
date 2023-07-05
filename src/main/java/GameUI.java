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
    private boolean firstClick = false, imageLoaded = false, flipAllowed = true; // FlipFlops
    private JButton firstButton; // Cache für den ersten Button beim Klick
    // Konstruktor
    public GameUI(int waitPeriod, int playerCount, AtomicReferenceArray<String> playerNames) {
        super("Dinory"); // JFrame Erstellen



        // Init WaitPeriod
        if (waitPeriod < 1000) {
            waitPeriod = 1000;
            throw new IllegalArgumentException("Wartezeit muss größer als 1000 sein! \nDie Wartezeit wird auf 1 Sekunde gesetzt.");
        } else if (waitPeriod > 10000) {
            waitPeriod = 10000;
            throw new IllegalArgumentException("Wartezeit muss kleiner als 10 Sekunden sein! \nDie Wartezeit wird auf 10 Sekunden gesetzt.");
        } else {
            this.waitPeriod = waitPeriod;
        }

        // Init Size
        int width = x * 100, height = y * 100;


        // Init PlayerCount
        if (playerCount <= 0) {
            throw new IllegalArgumentException("Es muss mindestens ein Spieler vorhanden sein! \nDie Anzahl der Spieler wird auf 1 gesetzt.");
            // playerCount = 1;
        }


        // Init PlayerNames
        for (int i = 0; i < playerCount; i++) {
            HashMap<String, Integer> playerScores = new HashMap<>();
            playerScores.put(playerNames.get(i), 0);
        }


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
                JButton[][] btnField = new JButton[x][y];
                btnField[i][j] = new JButton();
                btnField[i][j].setSize(83, 83);
                btnField[i][j].setBackground(Color.WHITE);
                btnField[i][j].setBorder(BorderFactory.createLineBorder(Color.WHITE));
                buttonPanel.add(btnField[i][j]);
                btnField[i][j].setIcon(utils.createImageIcon("Backside.png"));
                buttonList.add(btnField[i][j]);

                btnField[i][j].addActionListener(e -> {
                    JButton btn = (JButton) e.getSource();
                    imageLoaded = false;
                    if (flipAllowed) {
                        handleButtonClicked(btn);

                    if (firstClick && imageLoaded) {
                        firstClick = false;
                        handleFlip(btn, firstButton);
                    } else {
                        firstClick = true;
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

        while (buttonList.size() > 1) {
            int randomKey = random.nextInt(buttonList.size());
            int randomValue = random.nextInt(buttonList.size());

            while (randomValue == randomKey) {
                randomValue = random.nextInt(buttonList.size());
            }

            int index = buttonList.size() / 2;
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
        Collections.shuffle(randomList);

        for (int i = 0; i < 18; i++) {
            dinoPairs.put(i, randomList.get(i));
        }
    }

    // Handle flip
    private void handleFlip(JButton btn, JButton firstButton) {
        Thread t = new Thread(() -> {
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
        });
        t.start();
    }

    // Handle button click
    private void handleButtonClicked(JButton btn) {
        btn.setIcon(buttonImages.get(btn));
        imageLoaded = true;
    }
}
