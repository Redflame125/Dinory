import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.Math.random;
import static java.lang.Thread.sleep;

public class GameUI extends JFrame {
    private final JPanel buttonPanel = new JPanel();
    private final int sizeX = 6, sizeY = 6;
    private final JButton[][] btnField = new JButton[sizeX][sizeY];

    private final HashMap<JButton, ImageIcon> buttonImages = new HashMap<>();
    private final HashMap<Integer, ImageIcon> dinoPairs = new HashMap<>();
    private final HashMap<JButton, Integer> buttonPairs = new HashMap<>();
    private final Methoden utils = new Methoden();
    private boolean firstClick = false, imageLoaded = false, flipAllowed = true;
    private JButton firstButton;
    private int waitPeriod;
    public GameUI(int waitPeriod) {
        super("Dinory");
        this.waitPeriod = waitPeriod;
        if (waitPeriod < 1000) {
            this.waitPeriod = 1000;
            System.out.println("Wait period too short, setting to 1000ms");
        } else if (waitPeriod > 5000) {
            this.waitPeriod = 5000;
            System.out.println("Wait period too long, setting to 5000ms");
        }
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600);
        setVisible(true);
        setResizable(false);
        add(buttonPanel);

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(dim.width/2 - getSize().width/2, dim.height/2 - getSize().height/2);

        buttonPanel.setLayout(new GridLayout(sizeX, sizeY));

        ArrayList<JButton> buttonList = new ArrayList<>();

        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++){
                btnField[i][j] = new JButton();
                btnField[i][j].setSize(83, 83);
                btnField[i][j].setBackground(Color.WHITE);
                btnField[i][j].setBorder(BorderFactory.createLineBorder(Color.WHITE));
                buttonPanel.add(btnField[i][j]);
                btnField[i][j].setIcon(utils.createImageIcon("Backside.png"));
                buttonList.add(btnField[i][j]);

                int finalI = i;
                int finalJ = j;
                btnField[i][j].addActionListener(e -> {
                    JButton btn = (JButton) e.getSource();
                    imageLoaded = false;
                    if (flipAllowed) {
                        handleButtonClicked(btn, finalI, finalJ);

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
        imageLoader(buttonList);
    }

    private void imageLoader(ArrayList<JButton> buttonList) {
        while (buttonList.size() > 0) {
            int randomKey = randomize(buttonList)[0];
            int randomValue = randomize(buttonList)[1];


            int index = (buttonList.size()/2);

            ImageIcon icon = dinoPairs.get(index);

            System.out.println("RandomKey: " + randomKey + " Random Value: " + randomValue + " Index: " + index);

            JButton key = buttonList.get(randomKey);
            JButton value = buttonList.get(randomValue);

            buttonImages.put(key, icon);
            buttonImages.put(value, icon);

            buttonPairs.put(key, index);
            buttonPairs.put(value, index);

            buttonList.remove(randomKey);
            buttonList.remove(randomValue);
        }
    }

    private void initDinoPairs() {
        for (int i = 0; i < 18; i++) {
            dinoPairs.put(i, utils.createImageIcon("Dino"+i+".png"));
        }
    }

    private void handleFlip(JButton btn, JButton firstButton) {
        Thread t = new Thread(() -> {
            try {
                if (!checkPairs(btn, firstButton)) {
                    flipAllowed = false;
                    sleep(waitPeriod);
                    btn.setIcon(utils.createImageIcon("Backside.png"));
                    firstButton.setIcon(utils.createImageIcon("Backside.png"));
                } else {
                    btn.disable();
                    firstButton.disable();
                }
                flipAllowed = true;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        t.start();
    }

    private int[] randomize(ArrayList<JButton> buttonList) {
        int[] random = new int[2];
        int size = buttonList.size();
        random[0] = (int) (random() * buttonList.size());
        random[1] = (int) (random() * buttonList.size());
        if (random[0] != random[1] && random[0] <= size && random[1] <= size && random[0] >= 0 && random[1] >= 0) {
            return random;
        } else {
            return randomize(buttonList);
        }
    }

    private boolean checkPairs(JButton btn, JButton firstButton) {
        int i = buttonPairs.get(btn);
        int j = buttonPairs.get(firstButton);
        System.out.println((i + " " + j + " " + (i == j)));
        return i == j;
    }

    private void handleButtonClicked(JButton btn, int x, int y) {
        System.out.println("Button clicked: " + x + " " + y);
        btn.setBackground(Color.WHITE);
        btn.setIcon(buttonImages.get(btn));
        imageLoaded = true;
    }
}
