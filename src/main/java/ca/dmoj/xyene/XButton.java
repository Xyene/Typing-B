package ca.dmoj.xyene;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * General use "button" emulated by a JLabel.
 * A JLabel is much easier to style than a button across all LookAndFeels.
 */
class XButton extends JLabel {
    /**
     * Creates a themed "button".
     *
     * @param labelFont The font.
     * @param str       The label text.
     * @param align     The horizontal alignment.
     * @param callback  The callback to call when the button is clicked.
     */
    public XButton(Font labelFont, String str, int align, Runnable callback) {
        super(str, align);
        setPreferredSize(new Dimension(0, 48));
        setBackground(Game.FILL_GRAY);
        setFont(labelFont);
        setForeground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(Game.BORDER_BLACK, 1, true));

        MouseAdapter adp = new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(Game.HIGHLIGHT_ORANGE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(Game.FILL_GRAY);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                // Call the callback!
                callback.run();
            }
        };
        addMouseListener(adp);
        addMouseMotionListener(adp);
    }

    @Override
    public void paintComponent(Graphics _g) {
        Graphics2D g = (Graphics2D) _g;
        // Make sure that AA is forced
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setColor(getBackground());
        g.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
        super.paintComponent(g);
    }
}
