import java.awt.image.BufferedImage;
import java.awt.Color;

/**
 * Converts an image to grayscale.
 */
class GrayscaleFilter implements ImageFilter {
    @Override
    public BufferedImage apply(BufferedImage source) {
        int w = source.getWidth();
        int h = source.getHeight();
        BufferedImage result = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                Color c = new Color(source.getRGB(x, y));
                int gray = (int) (0.299 * c.getRed() + 0.587 * c.getGreen() + 0.114 * c.getBlue());
                result.setRGB(x, y, new Color(gray, gray, gray).getRGB());
            }
        }
        return result;
    }

    @Override
    public String getName() { return "Grayscale"; }
}

/**
 * Inverts all colors in an image.
 */
class InvertFilter implements ImageFilter {
    @Override
    public BufferedImage apply(BufferedImage source) {
        int w = source.getWidth();
        int h = source.getHeight();
        BufferedImage result = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                Color c = new Color(source.getRGB(x, y));
                result.setRGB(x, y, new Color(255 - c.getRed(), 255 - c.getGreen(), 255 - c.getBlue()).getRGB());
            }
        }
        return result;
    }

    @Override
    public String getName() { return "Invert Colors"; }
}

/**
 * Applies a sepia tone filter to an image.
 */
class SepiaFilter implements ImageFilter {
    @Override
    public BufferedImage apply(BufferedImage source) {
        int w = source.getWidth();
        int h = source.getHeight();
        BufferedImage result = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                Color c = new Color(source.getRGB(x, y));
                int r = c.getRed(), g = c.getGreen(), b = c.getBlue();

                int newR = Math.min(255, (int) (0.393 * r + 0.769 * g + 0.189 * b));
                int newG = Math.min(255, (int) (0.349 * r + 0.686 * g + 0.168 * b));
                int newB = Math.min(255, (int) (0.272 * r + 0.534 * g + 0.131 * b));

                result.setRGB(x, y, new Color(newR, newG, newB).getRGB());
            }
        }
        return result;
    }

    @Override
    public String getName() { return "Sepia"; }
}

/**
 * Adjusts brightness of an image.
 */
class BrightnessFilter implements ImageFilter {
    private final int adjustment; // -255 to 255

    /**
     * @param adjustment brightness change (-255 to darken, +255 to brighten)
     */
    public BrightnessFilter(int adjustment) {
        this.adjustment = Math.max(-255, Math.min(255, adjustment));
    }

    @Override
    public BufferedImage apply(BufferedImage source) {
        int w = source.getWidth();
        int h = source.getHeight();
        BufferedImage result = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                Color c = new Color(source.getRGB(x, y));
                int r = clamp(c.getRed() + adjustment);
                int g = clamp(c.getGreen() + adjustment);
                int b = clamp(c.getBlue() + adjustment);
                result.setRGB(x, y, new Color(r, g, b).getRGB());
            }
        }
        return result;
    }

    private int clamp(int value) {
        return Math.max(0, Math.min(255, value));
    }

    @Override
    public String getName() {
        return adjustment >= 0 ? "Brighten (+" + adjustment + ")" : "Darken (" + adjustment + ")";
    }
}

/**
 * Applies a blur effect to an image using box blur.
 */
class BlurFilter implements ImageFilter {
    @Override
    public BufferedImage apply(BufferedImage source) {
        int w = source.getWidth();
        int h = source.getHeight();
        BufferedImage result = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        int radius = 3;

        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                int rSum = 0, gSum = 0, bSum = 0, count = 0;

                for (int dx = -radius; dx <= radius; dx++) {
                    for (int dy = -radius; dy <= radius; dy++) {
                        int nx = x + dx, ny = y + dy;
                        if (nx >= 0 && nx < w && ny >= 0 && ny < h) {
                            Color c = new Color(source.getRGB(nx, ny));
                            rSum += c.getRed();
                            gSum += c.getGreen();
                            bSum += c.getBlue();
                            count++;
                        }
                    }
                }
                result.setRGB(x, y, new Color(rSum / count, gSum / count, bSum / count).getRGB());
            }
        }
        return result;
    }

    @Override
    public String getName() { return "Blur"; }
}
