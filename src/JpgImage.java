/**
 * Represents a JPEG image file.
 * Demonstrates: Inheritance (extends ImageFile).
 */
public class JpgImage extends ImageFile {

    public JpgImage(String filePath, String fileName, long fileSize, int width, int height) {
        super(filePath, fileName, fileSize, width, height, "jpg");
    }

    @Override
    public String[] getSupportedConversions() {
        return new String[]{"png", "bmp", "gif"};
    }

    @Override
    public String getFormatDisplayName() {
        return "JPEG Image";
    }
}
