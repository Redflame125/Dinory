import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

@SuppressWarnings("StructuralWrap")
public class Utils {
    private final HashMap<String, ImageIcon> imageIconCache = new HashMap<>(); // Cache for ImageIcons
    private final HashMap<String, BufferedImage> bufferedImageCache = new HashMap<>(); // Cache for Images

    // Creates a BufferedImage from a path
    public BufferedImage reader(String path) {
        if (!bufferedImageCache.containsKey(path)) { // If the BufferedImage is not in the cache, read it from the path
            try {
                bufferedImageCache.put(path, ImageIO.read(Objects.requireNonNull(getClass().getResource(path)))); // Add the BufferedImage to the cache
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return bufferedImageCache.get(path); // Return the BufferedImage from the cache
    }

    // Creates an ImageIcon from a BufferedImage
    public ImageIcon createImageIcon(String path) {
        if (!imageIconCache.containsKey(path)) { // If the ImageIcon is not in the cache, create it from the BufferedImage
            imageIconCache.put(path, new ImageIcon(reader(path))); // Add the ImageIcon to the cache
        }
        return imageIconCache.get(path); // Return the ImageIcon from the cache
    }

    // Checks if the two buttons are the same pair
    public boolean checkPairs(JButton btn, JButton firstButton, HashMap<JButton, Integer> buttonPairs) {
        int i = buttonPairs.get(btn); // Get the pair ID of the first button
        int j = buttonPairs.get(firstButton); // Get pair ID of the second button
        return i == j; // Return if the two buttons are the same pair
    }
}
