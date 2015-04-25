package ca.dmoj.xyene;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Base Game frame class.
 */
public class Game extends JFrame {
    /**
     * Window height.
     */
    public static final int H = 350;
    /**
     * Window width.
     */
    public static final int W = 480;
    /**
     * Background panel tile.
     */
    public static BufferedImage BACKGROUND_TILE;
    /**
     * Common font base.
     */
    public static Font WFONT = new Font("Verdana", Font.BOLD, 15);
    /**
     * A nice shade of orange.
     */
    public static final Color HIGHLIGHT_ORANGE = new Color(237, 140, 0, 240);
    /**
     * A nicer shade of gray.
     */
    public static final Color FILL_GRAY = new Color(0, 0, 0, 100);
    /**
     * Straight up black.
     */
    public static final Color BORDER_BLACK = new Color(0, 0, 0);
    /**
     * Greyish-black tint for panel tile mask.
     */
    public static final Color BACKGROUND = new Color(64, 64, 64);
    /**
     * Game updates (frames) per second.
     */
    public static final int GAME_UPS = 80;

    static {
        try {
            // Load the tile image
            BACKGROUND_TILE = ImageIO.read(Game.class.getClassLoader().getResourceAsStream("squares.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Hold the current screen renderer.
     */
    private JComponent currentComponent;

    /**
     * Creates a new Game.
     */
    public Game() {
        super("typing-B");
        setLayout(new BorderLayout());
        setSize(new Dimension(W, H));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        // Game starts with a splash panel
        setContent(new SplashPanel(this));
    }

    /**
     * Sets the current view.
     *
     * @param comp The view to set.
     */
    public void setContent(JComponent comp) {
        if (currentComponent != null) remove(currentComponent);
        add(currentComponent = comp, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    public static void main(String[] argv) {
        try {
            // These two properties must be set to force AA in most Swing components
            System.setProperty("awt.useSystemAAFontSettings", "false");
            System.setProperty("swing.aatext", "true");
            // Because Metal just looks bad
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | UnsupportedLookAndFeelException | IllegalAccessException ignored) {
        }
        // Create and display the game on the EDT
        SwingUtilities.invokeLater(() -> {
            Game nu = new Game();
            nu.setVisible(true);
        });
    }
}
