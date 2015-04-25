package ca.dmoj.xyene;

import java.awt.*;

/**
 * Interface for all widgets that may be rendered.
 */
public interface IRenderable {
    /**
     * Should this widget persist to the next frame?
     *
     * @return Whether this widget is active.
     */
    boolean isActive();

    /**
     * Run update logic on this widget.
     *
     * @param v The ratio between the target frame rate (Game#GAME_UPS) and the actual frame rate.
     */
    void update(double v);

    /**
     * Paint this widget.
     *
     * @param g The graphics object to render to.
     */
    void draw(Graphics2D g);
}
