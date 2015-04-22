package ca.dmoj.xyene;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class EndGamePanel extends TiledPanel {
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
                g.drawImage(banner, getWidth() / 2 - banner.getWidth() / 2, getHeight() / 2 - banner.getHeight() / 2, null);
            }
        });
//        add(new JLabel(String.format("<html>You scored %d points!</html>", score), JLabel.CENTER) {
//            {
//                setFont(Game.WFONT.deriveFont(30f));
//                setForeground(Color.WHITE);
//                setBorder(BorderFactory.createEmptyBorder(30, 30, 0, 30));
//            }
//
//            @Override
//            public void paintComponent(Graphics _g) {
//                ((Graphics2D) _g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
//                super.paintComponent(_g);
//            }
//        }, BorderLayout.CENTER);
        add(new JPanel() {
            {
                setOpaque(false);
                setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
                add(Box.createHorizontalGlue());
                add(new JPanel() {
                    {
                        setOpaque(false);
                        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
                        add(Box.createVerticalGlue());
                        Font labelFont = Game.WFONT.deriveFont(22f);

                        add(new JPanel() {{
                            setOpaque(false);
                            setPreferredSize(new Dimension(75, 48 * 2 + 10));
                            setLayout(new GridLayout(0, 1, 10, 10));
                            add(new XButton(labelFont, "Menu", JLabel.CENTER, () -> {
                                game.setContent(new MenuPanel(game));
                            }));
                            add(new XButton(labelFont, "Exit", JLabel.CENTER, () -> {
                                System.exit(0);
                            }));
                        }});
                        add(Box.createVerticalGlue());
                    }
                });
                add(Box.createHorizontalGlue());
                setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
            }
        }, BorderLayout.SOUTH);
    }
}
