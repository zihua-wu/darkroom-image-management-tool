/**
 * Represents a BMP image file.
 * Demonstrates: Inheritance (extends ImageFile).
 */
public class BmpImage extends ImageFile {

    public BmpImage(String filePath, String fileName, long fileSize, int width, int height) {
        super(filePath, fileName, fileSize, width, height, "bmp");
    }

    @Override
    public String[] getSupportedConversions() {
        return new String[]{"jpg", "png", "gif"};
    }

    @Override
    public String getFormatDisplayName() {
        return "BMP Image";
    }
}
