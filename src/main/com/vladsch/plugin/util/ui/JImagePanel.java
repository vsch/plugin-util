package com.vladsch.plugin.util.ui;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class JImagePanel extends JPanel {
    private BufferedImage myImage;

    public JImagePanel() {
        this(null);
    }

    public JImagePanel(BufferedImage image) {
        myImage = image;
    }

    public BufferedImage getImage() {
        return myImage;
    }

    public void setImage(final BufferedImage image) {
        myImage = image;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (myImage != null) {
            g.drawImage(myImage, 0, 0, this); // see javadoc for more info on the parameters
        }
    }
}
