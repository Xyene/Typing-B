package ca.dmoj.xyene;

import javax.swing.*;
import java.awt.*;

public class MenuPanel extends TiledPanel {
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
                    setPreferredSize(new Dimension(75, 48 * 3 - 4 * 14));
                    setLayout(new GridLayout(0, 1, 10, 10));
                    add(new XButton(labelFont, "Start", JLabel.CENTER, () -> {
                        game.setContent(new GamePanel(game));
                    }));
                    add(new XButton(labelFont, "About", JLabel.CENTER, () -> {
                    }));
                    add(new XButton(labelFont, "Exit", JLabel.CENTER, () -> System.exit(0)));
                }});
                add(Box.createVerticalGlue());
            }
        });
        add(Box.createHorizontalGlue());
    }
}
