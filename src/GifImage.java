/**
 * Represents a GIF image file.
 * Demonstrates: Inheritance (extends ImageFile).
 */
public class GifImage extends ImageFile {

    public GifImage(String filePath, String fileName, long fileSize, int width, int height) {
        super(filePath, fileName, fileSize, width, height, "gif");
    }

    @Override
    public String[] getSupportedConversions() {
        return new String[]{"jpg", "png", "bmp"};
    }

    @Override
    public String getFormatDisplayName() {
        return "GIF Image";
    }
}
