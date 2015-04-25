package ca.dmoj.xyene;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Nice little splash panel.
 */
public class SplashPanel extends TiledPanel {
    private final Game game;
    private KeyAdapter keys;

    /**
     * Creates a SplashPanel view.
     *
     * @param game The parent game.
     */
    public SplashPanel(Game game) {
        this.game = game;
        setLayout(new BorderLayout());
        add(new JPanel() {
            private BufferedImage banner, message, copyright;
            private Image bee;

            {
                setOpaque(false);
                banner = Images.renderImage("typing-", Color.WHITE, Game.WFONT.deriveFont(55f));
                message = Images.renderImage("type to begin!", Color.WHITE, Game.WFONT.deriveFont(15f).deriveFont(Font.ITALIC | Font.BOLD));
                copyright = Images.renderImage("Copyright \u00a9 Tudor Brindus 2015, All Rights Reserved", Color.WHITE, Game.WFONT.deriveFont(9f));

                try {
                    bee = ImageIO.read(Game.class.getClassLoader().getResourceAsStream("bee2.png"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void paintComponent(Graphics _g) {
                Graphics2D g = (Graphics2D) _g;
                // Enable bicubic interpolation for the title
                // its /extremely/ slow, but we're not actively rendering so we can use all our resources
                // on making the title look as nice as we can
                g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                AffineTransform at = new AffineTransform();
                int x = getWidth() / 5;
                int y = getHeight() / 2 + getHeight() / 20;
                at.rotate(Math.toRadians(-40), x, y);
                at.translate(x, y);
                g.drawImage(banner, at, null);
                at.translate(getWidth() / 2 - 50, 80);
                g.drawImage(bee, at, null);
                g.drawImage(message, 250, 230, null);
                g.drawImage(copyright, 10, getHeight() - copyright.getHeight(), null);
            }
        }, BorderLayout.CENTER);

        Runnable start = () -> {
            new Thread(() -> {
                SwingUtilities.invokeLater(() -> {
                    // Here we highlight the background
                    setBackground(Game.HIGHLIGHT_ORANGE);
                    // and immediately paint, without scheduling on the EDT (we're already on the EDT)
                    paintImmediately(getBounds());
                });
                // Give the impression of very fancy loading even though there's nothing to load
                // ;-)
                try {
                    Thread.sleep(400);
                } catch (InterruptedException ignored) {
                }
                // Read the dictionary file. Sure, we already waited 400ms, but the dictionary read is almost instant.
                Dictionary.init();
                SwingUtilities.invokeLater(() -> {
                    // This next part will take a long time, and our frame won't be repainted during this time
                    // therefore, the highlight appears as a sort of "loading" recognition
                    game.setContent(new MenuPanel(game));
                });
            }).start();
        };
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                start.run();
            }
        });
        game.addKeyListener(keys = new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                start.run();
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeNotify() {
        super.removeNotify();
        // Make sure we stop listening after this view is destroyed.
        game.removeKeyListener(keys);
    }
}
