import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Interface for image conversion operations.
 * Demonstrates: Interface usage and polymorphism.
 */
public interface ImageConverter {

    /**
     * Converts the given image to the target format and saves to outputFile.
     * @param sourceImage the source BufferedImage
     * @param outputFile the destination file
     * @throws Exception if conversion fails
     */
    void convert(BufferedImage sourceImage, File outputFile) throws Exception;

    /**
     * Returns the target format name (e.g., "jpg", "png").
     */
    String getTargetFormat();

    /**
     * Returns a human-readable description of this converter.
     */
    String getDescription();
}
