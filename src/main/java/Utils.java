import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

public class Utils {
    private final HashMap<String, ImageIcon> imageIconCache = new HashMap<>(); // Cache für die Icons
    private final HashMap<String, BufferedImage> bufferedImageCache = new HashMap<>(); // Cache für die Bilder

    // Liest ein Bild aus dem Ordner resources
    public BufferedImage reader(String path) {
        if (!bufferedImageCache.containsKey(path)) {
            try {
                bufferedImageCache.put(path, ImageIO.read(Objects.requireNonNull(getClass().getResource(path))));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return bufferedImageCache.get(path);
    }

    // Erstellt ein ImageIcon aus einem Bild
    public ImageIcon createImageIcon(String path) {
        if (!imageIconCache.containsKey(path)) {
            imageIconCache.put(path, new ImageIcon(reader(path)));
        }
        return imageIconCache.get(path);
    }

    public boolean checkPairs(JButton btn, JButton firstButton, HashMap<JButton, Integer> buttonPairs) {
        int i = buttonPairs.get(btn);
        int j = buttonPairs.get(firstButton);
        return i == j;
    }

    public Object sortScores(Object playerScores) {

        return playerScores;}
}
