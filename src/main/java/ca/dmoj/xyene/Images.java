package ca.dmoj.xyene;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.util.HashMap;

import static ca.dmoj.xyene.Game.WFONT;

public class Images {
    // A cache with a bad policy is another term for a memory leak.
    private static final HashMap<Object, BufferedImage> CACHE_FOREVER_IMAGE_CACHE = new HashMap<>();
    // I don't want to type this every time.
    private static final GraphicsConfiguration GFX_CONF = GraphicsEnvironment.getLocalGraphicsEnvironment()
            .getDefaultScreenDevice()
            .getDefaultConfiguration();

    /**
     * Not instantiable.
     */
    private Images() {
        throw new AssertionError();
    }

    /**
     * Creates a transparent mask.
     *
     * @param pre   The image to generate a mask from.
     * @param alpha The opacity of the mask.
     * @return The mask image, 20x20px larger than the pre image.
     */
    public static BufferedImage createMask(BufferedImage pre, float alpha) {
        // Create an optimized transparent image
        BufferedImage buf = GFX_CONF.createCompatibleImage(pre.getWidth() + 20, pre.getHeight() + 20, Transparency.TRANSLUCENT);
        Graphics2D g = buf.createGraphics();

        // Draw the region to be shaded
        g.drawImage(pre, 0, 0, pre.getWidth() + 5, pre.getHeight() + 5, null);

        // Normally we use the SRC OVER rule, but here we want to shade on top of what has already been drawn
        AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_IN, alpha);
        g.setComposite(ac);

        // Flood the entire image region: only the previously drawn parts will get coloured due to the SRC IN rule above.
        g.setColor(new Color(0, 0, 0, alpha));
        g.fillRect(0, 0, pre.getWidth() + 20, pre.getHeight() + 20);

        // Release the Graphics instance
        g.dispose();
        return buf;
    }

    /**
     * Renders text to an image.
     *
     * @param word  The text.
     * @param color The color.
     * @param font  The font.
     * @return The image.
     */
    public static BufferedImage renderImage(String word, Color color, Font font) {
        return renderImage(word, color, font, true, 0.5f);
    }

    /**
     * Renders text to an image.
     *
     * @param word  The text.
     * @param color The color.
     * @param font  The font.
     * @param blur  Should the text be blurred?
     * @param alpha The opacity.
     * @return The image.
     */
    public static BufferedImage renderImage(String word, Color color, Font font, boolean blur, float alpha) {
        /**
         * Key to hash entries into the cache.
         */
        class _Key {
            String word;
            Color color;
            Font font;

            public _Key(String word, Color color, Font font) {
                this.word = word;
                this.color = color;
                this.font = font;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;

                _Key key = (_Key) o;

                if (!color.equals(key.color)) return false;
                if (!word.equals(key.word)) return false;
                if (!font.equals(key.font)) return false;

                return true;
            }

            @Override
            public int hashCode() {
                int result = word.hashCode();
                result = 31 * result + color.hashCode();
                result = 31 * result + font.hashCode();
                return result;
            }
        }
        // Create the hash key and return the image if it exists
        _Key key = new _Key(word, color, font);
        BufferedImage k = CACHE_FOREVER_IMAGE_CACHE.get(key);
        if (k != null) return k;
        // Render the image in a headless context
        FontRenderContext frc = new FontRenderContext(new AffineTransform(), true, true);
        int w = (int) Math.ceil(font.getStringBounds(word, frc).getWidth());
        int h = (int) Math.ceil(font.getStringBounds(word, frc).getHeight());

        k = GFX_CONF.createCompatibleImage(w, h, Transparency.TRANSLUCENT);

        Graphics2D g = k.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setColor(new Color(0, 0, 0, 0));
        g.fillRect(0, 0, w, h);
        g.setFont(font);
        g.setColor(color);
        g.drawString(word, 0, font.getSize());

        // 5x5 convolve kernel
        float[] matrix = {
                1 / 32f, 1 / 16f, 1 / 8f, 1 / 16f, 1 / 32f,
                1 / 16f, 1 / 8f, 1 / 4f, 1 / 8f, 1 / 16f,
                1 / 8f, 1 / 4f, 1 / 2f, 1 / 4f, 1 / 8f,
                1 / 16f, 1 / 8f, 1 / 4f, 1 / 8f, 1 / 16f,
                1 / 32f, 1 / 16f, 1 / 8f, 1 / 16f, 1 / 32f,
        };
        // 3x3 convole kernel, for slower machines
        float[] _matrix = {
                1 / 16f, 1 / 8f, 1 / 16f,
                1 / 8f, 1 / 4f, 1 / 8f,
                1 / 16f, 1 / 8f, 1 / 16f,
        };

        if (blur) {
            BufferedImage m = createMask(k, alpha);
            BufferedImageOp op = new ConvolveOp(new Kernel(3, 3, matrix));
            BufferedImage n = GFX_CONF.createCompatibleImage(m.getWidth(), m.getHeight(), Transparency.TRANSLUCENT);
            m = op.filter(m, n);
            BufferedImage l = GFX_CONF.createCompatibleImage(k.getWidth() + 10, k.getHeight() + 10, Transparency.TRANSLUCENT);
            Graphics2D illeg = l.createGraphics();
            // Consolidate the two buffers
            illeg.drawImage(m, 1, 1, null);
            illeg.drawImage(k, 0, 0, null);
            k = l;
        }

        CACHE_FOREVER_IMAGE_CACHE.put(key, k);
        return k;
    }
}
