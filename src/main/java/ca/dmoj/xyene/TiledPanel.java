package ca.dmoj.xyene;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class TiledPanel extends JPanel {
    {
        setBackground(Game.BACKGROUND_COLOR);
    }

    @Override
    public void paintComponent(Graphics _g) {
        super.paintComponent(_g);
        BufferedImage i = Game.BACKGROUND_TILE;
        for (int x = 0; x < getWidth(); x += i.getWidth()) {
            for (int y = 0; y < getHeight(); y += i.getHeight()) {
                _g.drawImage(i, x, y, null);
            }
        }
    }
}
