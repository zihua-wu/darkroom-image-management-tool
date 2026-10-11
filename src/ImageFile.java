import java.io.File;
import java.time.LocalDateTime;

/**
 * Abstract base class representing an image file.
 * Demonstrates: Inheritance (base for JpgImage, PngImage, etc.), Encapsulation (private fields + getters).
 */
public abstract class ImageFile {
    private final String filePath;
    private final String fileName;
    private final long fileSize;
    private final int width;
    private final int height;
    private final String format;
    private final LocalDateTime loadedAt;

    /**
     * Constructs an ImageFile with the given properties.
     * @param filePath path to the image file
     * @param fileName name of the file
     * @param fileSize size in bytes
     * @param width image width in pixels
     * @param height image height in pixels
     * @param format image format (e.g., "jpg", "png")
     */
    public ImageFile(String filePath, String fileName, long fileSize,
                     int width, int height, String format) {
        this.filePath = filePath;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.width = width;
        this.height = height;
        this.format = format;
        this.loadedAt = LocalDateTime.now();
    }

    // --- Getters (Encapsulation) ---
    public String getFilePath()       { return filePath; }
    public String getFileName()       { return fileName; }
    public long getFileSize()         { return fileSize; }
    public int getWidth()             { return width; }
    public int getHeight()            { return height; }
    public String getFormat()         { return format; }
    public LocalDateTime getLoadedAt(){ return loadedAt; }

    /**
     * Returns a human-readable file size string.
     */
    public String getFileSizeFormatted() {
        if (fileSize < 1024) return fileSize + " B";
        if (fileSize < 1024 * 1024) return String.format("%.1f KB", fileSize / 1024.0);
        return String.format("%.1f MB", fileSize / (1024.0 * 1024));
    }

    /**
     * Returns the resolution string (e.g., "1920 x 1080").
     */
    public String getResolution() {
        return width + " x " + height;
    }

    /**
     * Abstract method: each subclass describes supported conversions.
     */
    public abstract String[] getSupportedConversions();

    /**
     * Abstract method: each subclass returns a display name.
     */
    public abstract String getFormatDisplayName();

    @Override
    public String toString() {
        return fileName + " (" + getFormatDisplayName() + ", " + getResolution() + ")";
    }
}
