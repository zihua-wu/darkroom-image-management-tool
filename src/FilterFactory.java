/**
 * Factory class for creating ImageFilter instances.
 * Demonstrates: Factory Design Pattern.
 */
public class FilterFactory {

    /**
     * Creates a filter by name.
     * @param filterName the name of the filter
     * @return the ImageFilter instance
     * @throws IllegalArgumentException if filter name is unknown
     */
    public static ImageFilter createFilter(String filterName) {
        switch (filterName.toLowerCase()) {
            case "grayscale":       return new GrayscaleFilter();
            case "invert":          return new InvertFilter();
            case "sepia":           return new SepiaFilter();
            case "brighten":        return new BrightnessFilter(50);
            case "darken":          return new BrightnessFilter(-50);
            case "blur":            return new BlurFilter();
            default:
                throw new IllegalArgumentException("Unknown filter: " + filterName);
        }
    }

    /**
     * Returns all available filter names.
     */
    public static String[] getAvailableFilters() {
        return new String[]{"Grayscale", "Invert", "Sepia", "Brighten", "Darken", "Blur"};
    }
}
