import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Methoden {
    public BufferedImage reader(String path) {
        try {
            return ImageIO.read(Objects.requireNonNull(getClass().getResource(path)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ImageIcon createImageIcon(String path) {
        return new ImageIcon(reader(path));
    }
}
