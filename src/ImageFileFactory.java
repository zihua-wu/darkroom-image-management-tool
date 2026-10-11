import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

/**
 * Factory class for creating the correct ImageFile subclass based on file extension.
 * Demonstrates: Factory Design Pattern.
 */
public class ImageFileFactory {

    /**
     * Creates an ImageFile instance from a File object.
     * Reads the image to get width/height, determines type from extension.
     * @param file the image file
     * @return the appropriate ImageFile subclass instance
     * @throws Exception if the file cannot be read or format is unsupported
     */
    public static ImageFile createImageFile(File file) throws Exception {
        if (file == null || !file.exists()) {
            throw new IllegalArgumentException("File does not exist: " + file);
        }

        BufferedImage img = ImageIO.read(file);
        if (img == null) {
            throw new IllegalArgumentException("Cannot read image: " + file.getName());
        }

        String name = file.getName();
        String ext = getExtension(name).toLowerCase();
        long size = file.length();
        int w = img.getWidth();
        int h = img.getHeight();
        String path = file.getAbsolutePath();

        switch (ext) {
            case "jpg":
            case "jpeg":
                return new JpgImage(path, name, size, w, h);
            case "png":
                return new PngImage(path, name, size, w, h);
            case "bmp":
                return new BmpImage(path, name, size, w, h);
            case "gif":
                return new GifImage(path, name, size, w, h);
            default:
                throw new IllegalArgumentException("Unsupported image format: " + ext);
        }
    }

    /**
     * Extracts file extension from a filename.
     */
    private static String getExtension(String fileName) {
        int dot = fileName.lastIndexOf('.');
        if (dot < 0 || dot == fileName.length() - 1) {
            return "";
        }
        return fileName.substring(dot + 1);
    }
}
