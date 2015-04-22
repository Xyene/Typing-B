package ca.dmoj.xyene;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Game extends JFrame {
    public static final int H = 350;
    public static final int W = 480;
    public static BufferedImage BACKGROUND_TILE;
    public static Font WFONT = new Font("Verdana", Font.BOLD, 15);
    public static final Color TAB_HIGHLIGHT_FILL = new Color(237, 187, 70, 192);
    public static final Color TAB_HIGHLIGHT_BORDER = new Color(237, 140, 0, 240);
    public static final Color TAB_NORMAL_FILL = new Color(0, 0, 0, 100);//new Color(0, 115, 255, 75);
    public static final Color TAB_NORMAL_BORDER = new Color(0, 0, 0);
    public static final Color BACKGROUND_COLOR = new Color(64, 64, 64);//new Color(0x2E435D);
    public static final Color BACKGROUND_SHADE = new Color(0xd5d5d5);
    public static final Color GAME_DIM = new Color(0, 0, 0, 119);
    public static final int GAME_UPS = 80;

    static {
        try {
            BACKGROUND_TILE = ImageIO.read(Game.class.getClassLoader().getResourceAsStream("squares.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private JComponent currentComponent;

    public Game() {
        super("typing-B");
        setLayout(new BorderLayout());
        setSize(new Dimension(W, H));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setContent(new SplashPanel(this));
        //setContent(new GamePanel(this));
    }

    public void setContent(JComponent comp) {
        if (currentComponent != null) remove(currentComponent);
        add(currentComponent = comp, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    public static void main(String[] argv) {
        try {
            System.setProperty("awt.useSystemAAFontSettings", "false");
            System.setProperty("swing.aatext", "true");
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | UnsupportedLookAndFeelException | IllegalAccessException ignored) {
        }
        Game nu = new Game();
        nu.setVisible(true);
    }
}
