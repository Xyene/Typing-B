package ca.dmoj.xyene;

import sun.swing.SwingUtilities2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class XButton extends JLabel {
    public XButton(Font labelFont, String str, int align, Runnable callback) {
        super(str, align);
        setPreferredSize(new Dimension(0, 48));
        setBackground(Game.TAB_NORMAL_FILL);
        setFont(labelFont);
        setForeground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(Game.TAB_NORMAL_BORDER, 1, true));

        MouseAdapter adp = new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(Game.TAB_HIGHLIGHT_BORDER);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(Game.TAB_NORMAL_FILL);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                callback.run();
            }
        };
        addMouseListener(adp);
        addMouseMotionListener(adp);
    }

    @Override
    public void paintComponent(Graphics _g) {
        Graphics2D g = (Graphics2D) _g;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setColor(getBackground());
        g.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
        super.paintComponent(g);
    }
}
