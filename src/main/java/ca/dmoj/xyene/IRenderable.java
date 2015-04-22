package ca.dmoj.xyene;

import java.awt.*;

public interface IRenderable {
    boolean isActive();

    void update(double v);

    void draw(Graphics2D g);
}
