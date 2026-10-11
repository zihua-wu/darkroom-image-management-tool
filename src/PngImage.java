/**
 * Represents a PNG image file.
 * Demonstrates: Inheritance (extends ImageFile).
 */
public class PngImage extends ImageFile {

    public PngImage(String filePath, String fileName, long fileSize, int width, int height) {
        super(filePath, fileName, fileSize, width, height, "png");
    }

    @Override
    public String[] getSupportedConversions() {
        return new String[]{"jpg", "bmp", "gif"};
    }

    @Override
    public String getFormatDisplayName() {
        return "PNG Image";
    }
}
