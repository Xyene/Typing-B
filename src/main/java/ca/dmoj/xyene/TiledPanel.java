package ca.dmoj.xyene;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Base class for all containers. Fills the background with Game#BACKGROUND_TILE.
 *
 * 2015-04-24
 *
 * @author Tudor (Xyene)
 */
public class TiledPanel extends JPanel {
    {
        setBackground(Game.BACKGROUND);
    }

    /**
     * {@inheritDoc}
     */
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
