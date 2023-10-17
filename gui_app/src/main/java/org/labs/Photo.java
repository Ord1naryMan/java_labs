package org.labs;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class Photo implements Displayable {

    private static final String F_NAME_1 = "src/main/resources/png1.png";
    private static final String F_NAME_2 = "src/main/resources/png2.png";

    private final ImageIcon image1;
    private final ImageIcon image2;

    private final Map<Integer, ImageIcon> images;

    public final JLabel jLabel;

    {
        image1 = new ImageIcon(readImage(F_NAME_1)
                .getScaledInstance(
                        760,
                        200,
                        BufferedImage.TYPE_INT_ARGB
                ));

        image2 = new ImageIcon(readImage(F_NAME_2)
                .getScaledInstance(
                        760,
                        200,
                        BufferedImage.TYPE_INT_ARGB
                ));
        jLabel = new JLabel();
        images = Map.of(
                1, image1,
                2, image2
        );
    }


    @Override
    public void display(MainFrame frame) {
        jLabel.setIcon(image1);
        jLabel.setLocation(20, 300);
        jLabel.setSize(760, 200);
        frame.add(jLabel);
    }

    public void setImage(int num) {
        if (num <= 0 || num > images.size()) {
            throw new IllegalArgumentException("no such image");
        }
        jLabel.setIcon(images.get(num));
    }

    private static BufferedImage readImage(String location) {
        BufferedImage bufferedImage;

        File imageFile = new File(location);
        if (!imageFile.exists() || !imageFile.canRead()) {
            throw new IllegalArgumentException("This file does not exists or unreadable");
        }
        try {
            bufferedImage = ImageIO.read(new File(location));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (bufferedImage == null) {
            throw new RuntimeException("Image cannot be edited, try again later.");
        }
        return bufferedImage;
    }
}
