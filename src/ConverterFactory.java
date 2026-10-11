/**
 * Factory class for creating ImageConverter instances.
 * Demonstrates: Factory Design Pattern.
 */
public class ConverterFactory {

    /**
     * Creates an ImageConverter for the given target format.
     * @param format target format string (e.g., "jpg", "png", "bmp", "gif")
     * @return the appropriate ImageConverter
     * @throws IllegalArgumentException if format is not supported
     */
    public static ImageConverter createConverter(String format) {
        if (format == null || format.isBlank()) {
            throw new IllegalArgumentException("Format cannot be null or empty");
        }

        switch (format.toLowerCase()) {
            case "jpg":
            case "jpeg":
                return new JpgConverter();
            case "png":
                return new PngConverter();
            case "bmp":
                return new BmpConverter();
            case "gif":
                return new GifConverter();
            default:
                throw new IllegalArgumentException("Unsupported format: " + format);
        }
    }

    /**
     * Returns all supported output format names.
     */
    public static String[] getSupportedFormats() {
        return new String[]{"jpg", "png", "bmp", "gif"};
    }
}
