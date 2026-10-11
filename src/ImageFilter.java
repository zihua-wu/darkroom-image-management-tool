import java.awt.image.BufferedImage;

/**
 * Interface for image filters.
 * Demonstrates: Interface + Strategy Pattern (interchangeable filter algorithms).
 */
public interface ImageFilter {

    /**
     * Applies this filter to the given image and returns the result.
     * @param source the original image
     * @return a new BufferedImage with the filter applied
     */
    BufferedImage apply(BufferedImage source);

    /**
     * Returns the display name of this filter.
     */
    String getName();
}
