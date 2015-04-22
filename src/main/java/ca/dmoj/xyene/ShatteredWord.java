package ca.dmoj.xyene;

import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * Simulates physics on a group of letters.
 */
public class ShatteredWord implements IRenderable {
    private Image[] renders;
    private FragVector[] frags;

    /**
     * Internal representation of a letter fragment. Origin used is top-left.
     */
    private class FragVector {
        /**
         * Current x position in screen space.
         */
        public double x;
        /**
         * Current y position in screen space.
         */
        public double y;
        /**
         * Original x offset in screen space.
         */
        public double _x;
        /**
         * Original y offset in screen space.
         */
        public double _y;
        /**
         * Velocity on y axis.
         */
        public double dx;
        /**
         * Velocity on x axis.
         */
        public double dy;
        /**
         * Current fragment simulated time.
         */
        public double t;
        /**
         * Rotation of fragment, in degrees.
         */
        public double rot;
        /**
         * Fragment rotation multiplier, either 1 or -1.
         */
        public double rotDir;
    }

    /**
     * Creates a new word shattering simulator.
     *
     * @param x      The x coordinate of the top left section of the word.
     * @param y      The y coordinate of the top left section of the word.
     * @param word   The word to render.
     * @param font   What font to render the word in.
     * @param missed Whether to render the word with the missed style.
     *               // TODO: maybe missed should be changed to a Color object instead
     */
    public ShatteredWord(int x, int y, String word, Font font, boolean missed) {
        renders = new Image[word.length()];
        frags = new FragVector[word.length()];
        double __x = x;
        double __y = y;
        for (int i = 0; i < word.length(); i++) {
            renders[i] = Images.renderImage(word.charAt(i) + "",
                    missed ? new Color(255, 0, 0, 240) : new Color(0, 255, 0, 240), font);
            FragVector v = new FragVector();
            // Set the original offset
            v._x = __x;
            v._y = __y;
            // Increment the x offset by the width of the letter, so that letters don't explode
            // from a single point. Rather, they explode from their original positions, looking much more
            // natural.
            __x += renders[i].getWidth(null);
            // Generate a random angle and magnitude
            double theta = Math.random() * 360;
            double magnitude = 20;
            // Decompose into velocities on the x and y axis
            v.dx = magnitude * Math.cos(Math.toRadians(theta));
            // -sin because origin is top left
            v.dy = magnitude * -Math.sin(Math.toRadians(theta));
            // Select a random direction to rotate the fragment
            // TODO: this could be weighted by the __x position
            v.rotDir = Math.random() >= 0.5 ? -1 : 1;

            // Initialize the screen coordinates
            v.x = v._x;
            v.y = v._y;

            frags[i] = v;
        }
    }

    @Override
    public boolean isActive() {
        // Keep simulating as long as a fragment still exists on screen.
        for (FragVector frag : frags) if (frag != null) return true;
        return false;
    }

    @Override
    public void update(double v) {
        // Clean up invisible fragments
        for (int i = 0; i < frags.length; i++) {
            FragVector frag = frags[i];
            if (frag == null) continue; // Already cleaned up
            // If they're outside the visible area, remove them
            if (frag.x > Game.W || frag.x < 0 || frag.y < -renders[i].getHeight(null) || frag.y > Game.H) {
                frags[i] = null;
                renders[i] = null;
            }
        }
        // Simulate physics on fragments
        for (FragVector frag : frags) {
            if (frag == null) continue;
            frag.t += v / 16; // Increment the time
            // x = Vx * t
            frag.x = frag._x + frag.dx * frag.t;
            // y = Vy * t + 0.5*a*t*t
            // For projectiles, a = g = 9.8
            // However, here I use g = 0 so that words evenly disperse across the screen
            frag.y = frag._y + frag.dy * frag.t;
            // Rotate the fragment
            frag.rot += v;
        }
    }

    @Override
    public void draw(Graphics2D g) {
        // We need to set interpolation to bilinear, because nearest-neighbour produces terrible aliasing
        // when our fragment is rotated. Bicubic filtering looks even nicer, but is way too slow to use in realtime.
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        for (int i = 0; i < frags.length; i++) {
            FragVector frag = frags[i];
            if (frag == null) continue; // Fragment was cleaned up
            // Create a view matrix
            AffineTransform at = new AffineTransform();
            // Rotate the fragment around its center
            at.rotate(Math.toRadians(frag.rot) * frag.rotDir, frag.x, frag.y);
            // And translate it to its screen position
            at.translate(frag.x, frag.y);
            // Finally, draw the current fragment render with the new view matrix
            g.drawImage(renders[i], at, null);
        }
    }
}
