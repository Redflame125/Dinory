import javax.swing.*;
import java.awt.*;

import static java.lang.Thread.sleep;

public class GameUI extends JFrame {
    private final JPanel buttonPanel = new JPanel();
    private final int sizeX = 6, sizeY = 6;
    private final JButton[][] btnField = new JButton[sizeX][sizeY];
    private final Methoden utils = new Methoden();
    private boolean firstClick = false, imageLoaded = false, flipAllowed = true;
    private JButton firstButton;
    private int waitPeriod;
    public GameUI(int waitPeriod) {
        super("Dinory");
        this.waitPeriod = waitPeriod;
        if (waitPeriod < 500) {
            this.waitPeriod = 500;
            System.out.println("Wait period too short, setting to 500ms");
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

        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++){
                btnField[i][j] = new JButton();
                btnField[i][j].setSize(83, 83);
                btnField[i][j].setBackground(Color.WHITE);
                btnField[i][j].setBorder(BorderFactory.createLineBorder(Color.WHITE));
                buttonPanel.add(btnField[i][j]);
                btnField[i][j].setIcon(utils.createImageIcon("Backside.png"));

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
    }

    private void handleFlip(JButton btn, JButton firstButton) {
        Thread t = new Thread(() -> {
            try {
                flipAllowed = false;
                sleep(waitPeriod);
                btn.setIcon(utils.createImageIcon("Backside.png"));
                firstButton.setIcon(utils.createImageIcon("Backside.png"));
                flipAllowed = true;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        t.start();
    }

    private void handleButtonClicked(JButton btn, int x, int y) {
        System.out.println("Button clicked: " + x + " " + y);
        btn.setBackground(Color.WHITE);
        btn.setIcon(utils.createImageIcon("Dino3.png"));
        imageLoaded = true;
    }
}
