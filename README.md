# Image Management Tool

A JavaFX desktop application for uploading, previewing, converting, and filtering images, built with a dark, photographer-friendly UI.

Originally the final project for INFO5100 (Application Engineering and Development).

## Features

- Upload images via file chooser or drag-and-drop
- Live preview with properties pane (name, format, dimensions)
- Format conversion between JPG, PNG, GIF, and BMP
- Image filters (brightness/contrast/etc. via the Filter menu)
- Right-click to remove an image from the filmstrip
- Error handling for unsupported or corrupt files
- Action history log

## Design

- **Design patterns:** Factory (`ImageFileFactory`, `ConverterFactory`, `FilterFactory`), Strategy (`ImageConverter`, `ImageFilter` implementations), Observer (`ProcessingObserver`), Singleton (`HistoryManager`)
- **OOP:** Inheritance across image types (`ImageFile` → `JpgImage`, `PngImage`, `GifImage`, `BmpImage`), encapsulation, interfaces

See `image_management_tool_class_diagran.png` for the class diagram and `screenshots/` for the app in action.

## Running

Requires Java with JavaFX on the module path (JavaFX is not bundled with modern JDKs).

```bash
javac --module-path <path-to-javafx-lib> --add-modules javafx.controls,javafx.fxml -d out src/*.java
java --module-path <path-to-javafx-lib> --add-modules javafx.controls,javafx.fxml -cp out ImageManagerApp
```

Or open `src/` as a JavaFX project in an IDE (NetBeans/IntelliJ) with a configured JavaFX SDK and run `ImageManagerApp`.

## Credits

Sample images in `examplepics/` are sourced from Unsplash — see `lisence.txt` for attribution and licensing.
