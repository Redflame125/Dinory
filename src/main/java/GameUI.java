import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReferenceArray;

import static java.lang.Thread.sleep;

public class GameUI extends JFrame {
    public final HashMap<String, Integer> playerScores = new HashMap<>(); // Cache for the PlayerScores
    private final HashMap<JButton, ImageIcon> buttonImages = new HashMap<>(); // Cache for the ButtonImages
    private final HashMap<JButton, Integer> buttonPairs = new HashMap<>(); // ButtonPairs for the checkPairs method
    private final AtomicReferenceArray<ImageIcon> dinoPairs; // Cache for the DinoPairs
    private final Utils utils = new Utils(); // Utils
    private final GameManager game; // GameManager
    private final int waitPeriod; // WaitPeriod between flips
    public int endCounter = 0;
    private int currentPlayer = 0;
    private boolean clickedOnce = false, flipAllowed = true; // FlipFlops
    private JButton firstButton, doubleClickCheck; // Cache für den ersten Button beim Klick

    // Konstruktor
    public GameUI(int waitPeriod, int playerCount, AtomicReferenceArray<String> playerNames, int layout) {
        super("Dinory"); // JFrame Erstellen

        // Init WaitPeriod
        this.waitPeriod = waitPeriod;

        // Init Size
        int width = layout * 100, height = layout * 100;

        // Init PlayerNames
        for (int i = 0; i < playerCount; i++) {
            playerScores.put(playerNames.get(i), 0);
        }

        // Init DinoPairs
        dinoPairs = initDinoPairs((layout * layout) / 2);
        System.out.println(playerNames.get(currentPlayer) + " ist am Zug");

        // JFrame Attribute
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(width, height);
        setAlwaysOnTop(true);
        setVisible(true);
        setResizable(false);
        JPanel buttonPanel = new JPanel();
        add(buttonPanel);

        // Center JFrame
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(dim.width/2 - getSize().width/2, dim.height/2 - getSize().height/2);

        // Layout
        buttonPanel.setLayout(new GridLayout(layout, layout));

        ArrayList<JButton> buttonList = new ArrayList<>(); // Cache for the buttons

        // Create buttons
        for (int i = 0; i < layout; i++) {
            for (int j = 0; j < layout; j++){
                JButton button = new JButton();
                button.setSize(83, 83);
                button.setBackground(Color.WHITE);
                button.setBorder(BorderFactory.createLineBorder(Color.WHITE));
                buttonPanel.add(button);
                button.setIcon(utils.createImageIcon("Backside.png"));
                buttonList.add(button);

                // Action Listener for the buttons
                button.addActionListener(e -> {
                    JButton btn = (JButton) e.getSource();


                    if (flipAllowed) {
                        btn.setIcon(buttonImages.get(btn));

                        if (clickedOnce && btn != firstButton) {
                            clickedOnce = false;
                            handleFlip(btn, firstButton);
                            doubleClickCheck = btn;
                        } else if (doubleClickCheck != btn) {
                            clickedOnce = true;
                            firstButton = btn;
                        }
                    }
                });
            }
        }

        // Randomize the dino pairs
        mixer(buttonList);

        // Init GameManager
        game = new GameManager(this);
    }

    // Randomize the dino pairs
    private void mixer(ArrayList<JButton> buttonList) {
        Random random = new Random();

        // Generates two random different numbers
        while (buttonList.size() > 0) {
            int randomKey = random.nextInt(buttonList.size());
            int randomValue = random.nextInt(buttonList.size());

            while (randomValue == randomKey) {
                randomValue = random.nextInt(buttonList.size());
            }

            // Calculates number of pairs
            int index = (buttonList.size() / 2) - 1;

            // Get the dino pair image
            ImageIcon icon = dinoPairs.get(index);

            // Get random buttons
            JButton key = buttonList.get(randomKey);
            JButton value = buttonList.get(randomValue);

            buttonImages.put(key, icon);
            buttonImages.put(value, icon);

            buttonPairs.put(key, index);
            buttonPairs.put(value, index);

            // Remove the buttons from the list
            buttonList.remove(randomKey);
            if (randomKey < randomValue) {
                randomValue--;
            }
            buttonList.remove(randomValue);
        }
    }

    // Initialize dino pairs
    private AtomicReferenceArray<ImageIcon> initDinoPairs(int size) {
        ArrayList<ImageIcon> randomList = new ArrayList<>(); // Cache for the random dino images

        // Add all the dino images to the list
        for (int i = 0; i < size; i++) {
            randomList.add(utils.createImageIcon("Dino/Dino" + (i) + ".png"));
        }
        // Shuffle the cards
        Collections.shuffle(randomList);

        // Create the array
        AtomicReferenceArray<ImageIcon> dinoPairs = new AtomicReferenceArray<>(new ImageIcon[size]);
        for (int i = 0; i < size; i++) {
            dinoPairs.set(i, randomList.get(i));
        }
        return dinoPairs;
    }

    // Handle flip
    private void handleFlip(JButton btn, JButton firstButton) {
        new Thread(() -> {
            try {
                if (!utils.checkPairs(btn, firstButton, buttonPairs)) { // Check if the buttons are a pair
                    flipAllowed = false;
                    sleep(waitPeriod); // Wait for the waitPeriod

                    // Flip the buttons back
                    btn.setIcon(utils.createImageIcon("Backside.png"));
                    firstButton.setIcon(utils.createImageIcon("Backside.png"));
                    System.out.println("Falsch geraten!");
                    if (currentPlayer == playerScores.size()) {
                        currentPlayer = 0;
                    } else {
                        currentPlayer++;
                    }
                } else {
                    // Disable the buttons
                    btn.setEnabled(false);
                    firstButton.setEnabled(false);
                    System.out.println("Richtig geraten!");
                }
                flipAllowed = true;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
