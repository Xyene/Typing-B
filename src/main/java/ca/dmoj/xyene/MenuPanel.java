package ca.dmoj.xyene;

import javax.swing.*;
import java.awt.*;

/**
 * Show the user a menu.
 * <p>
 * 2015-04-24
 *
 * @author Tudor (Xyene)
 */
public class MenuPanel extends TiledPanel {

    /**
     * Creates a MenuPanel view.
     *
     * @param game The parent game.
     */
    public MenuPanel(Game game) {
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
                    // The height component here is pretty ad-hoc
                    setPreferredSize(new Dimension(75, 48 * 3 - 4 * 14));
                    setLayout(new GridLayout(0, 1, 10, 10));
                    add(new XButton(labelFont, "Start", JLabel.CENTER, () -> {
                        game.setContent(new GamePanel(game));
                    }));
                    add(new XButton(labelFont, "About", JLabel.CENTER, () -> {
                        JDialog dialog = new JDialog(game, "About", true /* its modal */) {
                            {
                                JDialog self = this;
                                setLayout(new BorderLayout());
                                setUndecorated(true);
                                add(new TiledPanel() {
                                    {
                                        setLayout(new BorderLayout());
                                        add(new JTextPane() {
                                            {
                                                setOpaque(false);
                                                setForeground(Color.WHITE);
                                                setFocusable(false);
                                                setFont(labelFont.deriveFont(12f));
                                                setText("Copyright © Tudor Brindus 2015, All Rights Reserved\n\n" +
                                                        "You have 2:00. How many words can you type in this time?\n" +
                                                        "Each correct word will score you 1 point. Each incorrect word will deduct " +
                                                        "2 points.");
                                                setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
                                            }
                                        }, BorderLayout.CENTER);
                                        add(new XButton(labelFont, "Got it.", JLabel.CENTER, () -> {
                                            self.setVisible(false);
                                            self.dispose();
                                        }), BorderLayout.SOUTH);
                                        setBorder(BorderFactory.createEmptyBorder(0, 50, 20, 50));
                                    }
                                }, BorderLayout.CENTER);
                                setPreferredSize(new Dimension(350, 240));
                                pack();
                                setLocationRelativeTo(game);
                                this.getRootPane().setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Game.HIGHLIGHT_ORANGE));
                            }
                        };
                        dialog.setVisible(true);
                    }));
                    add(new XButton(labelFont, "Exit", JLabel.CENTER, () -> System.exit(0)));
                }});
                add(Box.createVerticalGlue());
            }
        });
        add(Box.createHorizontalGlue());
    }
}
