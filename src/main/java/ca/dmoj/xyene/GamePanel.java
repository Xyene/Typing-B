package ca.dmoj.xyene;

import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import static ca.dmoj.xyene.Game.*;

public class GamePanel extends TiledPanel {
    private final Game game;
    private String targetWord;
    private BufferedImage wordRender;
    private Timer ticker;
    private List<IRenderable> renders = new ArrayList<>();
    private String currentWord = "";
    private int correct = 0, wrong = 0, score = 0;
    private long timeLeft = 60 * 2 * 1000;
    private KeyAdapter keys;

    /**
     * Creates a new GamePanel view.
     *
     * @param game The parent game.
     */
    public GamePanel(Game game) {
        this.game = game;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addNotify() {
        super.addNotify();
        ticker = new Timer(1000 / GAME_UPS, new ActionListener() {
            private long start = System.currentTimeMillis();

            @Override
            public void actionPerformed(ActionEvent x) {
                long diff = System.currentTimeMillis() - start;
                start = System.currentTimeMillis();
                double v = diff / (1000.0 / GAME_UPS) * 0.8;

                // Time is up!
                if (timeLeft <= 0) {
                    // and the game is over
                    game.setContent(new EndGamePanel(game, score));
                    return;
                }

                if (targetWord != null) timeLeft -= diff;

                for (IRenderable r : renders) r.update(v);
                for (int i = 0; i < renders.size(); i++) if (!renders.get(i).isActive()) renders.remove(i);

                try {
                    GamePanel.this.paintImmediately(0, 0, GamePanel.this.getWidth(), GamePanel.this.getHeight());
                } catch (NullPointerException ignored) {
                    // Java bug, not my fault:
                    // Exception in thread "AWT-EventQueue-0" java.lang.NullPointerException
                    //     at javax.swing.JComponent.paintChildren(Unknown Source)
                }
            }
        });
        ticker.start();
        reset();
        game.addKeyListener(keys = new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (targetWord == null) return;
                char c = Character.toLowerCase(e.getKeyChar());
                if ('\b' != c && ' ' != c) currentWord += c;
                String a = currentWord.toLowerCase();
                String b = targetWord == null ? "\0" : targetWord.toLowerCase();
                if (targetWord != null && !b.startsWith(a) || a.equals(b)) {
                    currentWord = "";
                    boolean missed = !a.equals(b);

                    if (missed) {
                        score -= 2;
                        score = Math.max(0, score);
                        wrong++;
                    } else {
                        score++;
                        correct++;
                    }

                    renders.add(new ShatteredWord(
                            getWidth() / 2 - wordRender.getWidth() / 2,
                            getHeight() / 2 - wordRender.getHeight(),
                            targetWord,
                            WFONT.deriveFont(25f),
                            missed));
                    reset();
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeNotify() {
        super.removeNotify();
        ticker.stop();
        game.removeKeyListener(keys);
    }

    /**
     * Resets the current word, animating the next word drop.
     */
    private void reset() {
        targetWord = null;
        String targetWord = Dictionary.getRandomWord();
        wordRender = Images.renderImage(targetWord, Color.WHITE, WFONT.deriveFont(25f));
        int x = getWidth() / 2 - wordRender.getWidth() / 2, y = getHeight() / 2 - wordRender.getHeight();
        // Drops down a word
        renders.add(new IRenderable() {
            private double _x = x, _y = -50;

            @Override
            public boolean isActive() {
                return !(_y == y);
            }

            @Override
            public void update(double v) {
                _y += v * 5;
                _y = Math.min(_y, y);
                if (_y == y) {
                    GamePanel.this.targetWord = targetWord;
                }
            }

            @Override
            public void draw(Graphics2D g) {
                g.drawImage(wordRender, (int) _x, (int) _y, null);
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void paintComponent(Graphics _g) {
        super.paintComponent(_g);
        Graphics2D g = (Graphics2D) _g;

        // Draw all the widgets!
        for (IRenderable r : renders) r.draw(g);
        if (targetWord != null) {
            int x = getWidth() / 2 - wordRender.getWidth() / 2, y = getHeight() / 2 - wordRender.getHeight();
            g.drawImage(wordRender, x, y, null);
        }
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setColor(Game.HIGHLIGHT_ORANGE);
        String hud = "[ " + currentWord + " ]";
        g.setFont(WFONT.deriveFont(18f).deriveFont(Font.BOLD));
        int w = g.getFontMetrics().stringWidth(hud);
        g.drawString(hud, W / 2 - w / 2, H - (int) (WFONT.getSize() * 5));
        double acc = Math.max(0, correct / (double) (correct + wrong)) * 100;
        g.drawString(String.format("%d %s", score, acc == Double.NaN ? "" : String.format("(%.1f%%)", acc)), WFONT.getSize() / 2, WFONT.getSize() + WFONT.getSize() / 2);

        int minutes = (int) (timeLeft / 1000 / 60);
        int seconds = (int) ((timeLeft - minutes * 1000 * 60) / 1000);
        String time = String.format("%01d:%02d", minutes, seconds);
        w = g.getFontMetrics().stringWidth(time);

        g.drawString(time, W - w - WFONT.getSize(), WFONT.getSize() + WFONT.getSize() / 2);
    }
}
