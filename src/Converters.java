import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

/**
 * JPEG converter implementation.
 * Demonstrates: Strategy pattern (interchangeable conversion algorithms).
 */
class JpgConverter implements ImageConverter {
    @Override
    public void convert(BufferedImage sourceImage, File outputFile) throws Exception {
        // JPEG doesn't support transparency; convert to RGB if needed
        BufferedImage rgbImage = new BufferedImage(
                sourceImage.getWidth(), sourceImage.getHeight(), BufferedImage.TYPE_INT_RGB);
        rgbImage.createGraphics().drawImage(sourceImage, 0, 0, java.awt.Color.WHITE, null);
        ImageIO.write(rgbImage, "jpg", outputFile);
    }

    @Override
    public String getTargetFormat() { return "jpg"; }

    @Override
    public String getDescription() { return "JPEG - compressed, no transparency"; }
}

/**
 * PNG converter implementation.
 */
class PngConverter implements ImageConverter {
    @Override
    public void convert(BufferedImage sourceImage, File outputFile) throws Exception {
        ImageIO.write(sourceImage, "png", outputFile);
    }

    @Override
    public String getTargetFormat() { return "png"; }

    @Override
    public String getDescription() { return "PNG - lossless, supports transparency"; }
}

/**
 * BMP converter implementation.
 */
class BmpConverter implements ImageConverter {
    @Override
    public void convert(BufferedImage sourceImage, File outputFile) throws Exception {
        ImageIO.write(sourceImage, "bmp", outputFile);
    }

    @Override
    public String getTargetFormat() { return "bmp"; }

    @Override
    public String getDescription() { return "BMP - uncompressed bitmap"; }
}

/**
 * GIF converter implementation.
 */
class GifConverter implements ImageConverter {
    @Override
    public void convert(BufferedImage sourceImage, File outputFile) throws Exception {
        ImageIO.write(sourceImage, "gif", outputFile);
    }

    @Override
    public String getTargetFormat() { return "gif"; }

    @Override
    public String getDescription() { return "GIF - limited colors, supports animation"; }
}
