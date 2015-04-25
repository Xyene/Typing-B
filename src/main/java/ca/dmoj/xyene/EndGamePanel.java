package ca.dmoj.xyene;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Game end screen.
 */
public class EndGamePanel extends TiledPanel {
    /**
     * Creates a new EndGamePanel view.
     * @param game The parent game.
     * @param score The score the player scored.
     */
    public EndGamePanel(Game game, int score) {
        setLayout(new BorderLayout());
        add(new JPanel() {
            private BufferedImage banner;

            {
                setOpaque(false);
                banner = Images.renderImage(String.format("You scored %d points!", score), Color.WHITE, Game.WFONT.deriveFont(30f));
                setBorder(BorderFactory.createEmptyBorder(30, 30, 0, 30));
            }

            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Center at the top of the screen
                g.drawImage(banner, getWidth() / 2 - banner.getWidth() / 2, getHeight() / 2 - banner.getHeight() / 2, null);
            }
        });
        add(new JPanel() {
            {
                setOpaque(false);
                setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
                // Horizontally align
                add(Box.createHorizontalGlue());
                add(new JPanel() {
                    {
                        setOpaque(false);
                        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
                        // Vertically align
                        add(Box.createVerticalGlue());
                        Font labelFont = Game.WFONT.deriveFont(22f);

                        add(new JPanel() {
                            {
                                setOpaque(false);
                                setPreferredSize(new Dimension(75, 48 * 2 + 10));
                                setLayout(new GridLayout(0, 1, 10, 10));
                                add(new XButton(labelFont, "Menu", JLabel.CENTER, () -> {
                                    game.setContent(new MenuPanel(game));
                                }));
                                add(new XButton(labelFont, "Exit", JLabel.CENTER, () -> {
                                    System.exit(0);
                                }));
                            }
                        });
                        add(Box.createVerticalGlue());
                    }
                });
                add(Box.createHorizontalGlue());
                // 30px bottom padding
                setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
            }
        }, BorderLayout.SOUTH);
    }
}
