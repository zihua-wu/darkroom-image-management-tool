import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Image Management Tool — Main Application

 * An image management tool with a dark, photographer-friendly UI.
 * Upload, preview, convert, and apply filters to images.

 * Design Patterns: Factory, Strategy, Observer, Singleton
 * OOP: Inheritance, Encapsulation, Interfaces
 */
public class ImageManagerApp extends Application implements ProcessingObserver {

    private FlowPane filmStrip;
    private Label statusLabel;
    private TextArea infoPane;
    private TextArea historyArea;
    private ImageView hero;
    private ComboBox<String> fmtBox;
    private ComboBox<String> fltBox;
    private VBox dropZone;
    private Label countLabel;
    private StackPane selectedCard = null;

    private final List<ImageFile> images = new ArrayList<>();
    private ImageFile current = null;
    private BufferedImage rawImage = null;
    private BufferedImage filtered = null;

    static final String C0 = "#0c0c0c";
    static final String C1 = "#141414";
    static final String C2 = "#1d1d1d";
    static final String C3 = "#272727";
    static final String C4 = "#333333";
    static final String C5 = "#444444";
    static final String W1 = "#eae6de";
    static final String W2 = "#a09a90";
    static final String W3 = "#605c55";
    static final String AC = "#d4993c";
    static final String OK = "#6b9e78";
    static final String NO = "#b85c5c";

    static final int SIDE_W = 250;

    @Override
    public void start(Stage stage) {
        HistoryManager.getInstance().addObserver(this);

        HBox titleBar = buildTitleBar();

        VBox leftCol = buildLeftColumn(stage);
        VBox rightArea = buildRightArea();
        HBox.setHgrow(rightArea, Priority.ALWAYS);

        HBox middle = new HBox(0, leftCol, rightArea);
        VBox.setVgrow(middle, Priority.ALWAYS);

        VBox dock = buildDock(stage);

        VBox root = new VBox(0, titleBar, middle, dock);
        root.setStyle("-fx-background-color:" + C0 + ";");

        Scene sc = new Scene(root, 1300, 840);
        stage.setTitle("Image Studio");
        stage.setScene(sc);
        stage.setMinWidth(1040);
        stage.setMinHeight(720);
        stage.show();

        HistoryManager.getInstance().recordEvent("Session started");
    }

    // TITLE BAR

    private HBox buildTitleBar() {
        Label dot = new Label("\u25CF");
        dot.setStyle("-fx-text-fill:" + AC + ";-fx-font-size:16;");

        Label name = new Label("Image Studio");
        name.setFont(Font.font("Helvetica Neue", FontWeight.SEMI_BOLD, 18));
        name.setStyle("-fx-text-fill:" + W1 + ";");

        Region gap = new Region();
        HBox.setHgrow(gap, Priority.ALWAYS);

        countLabel = new Label("");
        countLabel.setStyle("-fx-text-fill:" + W3 + ";-fx-font-size:14;");

        HBox bar = new HBox(8, dot, name, gap, countLabel);
        bar.setAlignment(Pos.CENTER);
        bar.setPadding(new Insets(10, 22, 10, 22));
        bar.setStyle("-fx-background-color:" + C1 + ";-fx-border-color:" + C3 + ";-fx-border-width:0 0 1 0;");
        return bar;
    }

    // LEFT COLUMN

    private VBox buildLeftColumn(Stage stage) {
        Label hd = secLabel("LIBRARY");

        Button add = new Button("\u2191  Upload Images");
        add.setStyle(
                "-fx-background-color:" + AC + ";" +
                        "-fx-text-fill:" + C0 + ";" +
                        "-fx-font-size:14;-fx-font-weight:bold;" +
                        "-fx-padding:8 18;-fx-background-radius:18;-fx-cursor:hand;"
        );
        add.setMaxWidth(Double.MAX_VALUE);
        add.setOnAction(e -> pickFiles(stage));

        // Drop zone — fully centered
        Label arrow = new Label("\u2193");
        arrow.setStyle("-fx-font-size:40;-fx-text-fill:" + C4 + ";");
        arrow.setMaxWidth(Double.MAX_VALUE);
        arrow.setAlignment(Pos.CENTER);

        Label hint = new Label("or drop files here");
        hint.setStyle("-fx-text-fill:" + W3 + ";-fx-font-size:14;");
        hint.setMaxWidth(Double.MAX_VALUE);
        hint.setAlignment(Pos.CENTER);

        dropZone = new VBox(6, arrow, hint);
        dropZone.setAlignment(Pos.CENTER);
        dropZone.setMaxWidth(Double.MAX_VALUE);
        dropZone.setPadding(new Insets(40, 0, 40, 0));

        filmStrip = new FlowPane(6, 6);
        filmStrip.setPadding(new Insets(4));
        filmStrip.setStyle("-fx-background-color:transparent;");
        filmStrip.getChildren().add(dropZone);

        ScrollPane scroll = new ScrollPane(filmStrip);
        scroll.setFitToWidth(true);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setStyle(
                "-fx-background:" + C1 + ";" +
                        "-fx-background-color:" + C1 + ";" +
                        "-fx-border-color:transparent;"
        );
        VBox.setVgrow(scroll, Priority.ALWAYS);

        setupDrop(filmStrip);

        // Activity Log
        Label logHdr = secLabel("ACTIVITY LOG");
        historyArea = new TextArea("");
        historyArea.setEditable(false);
        historyArea.setPrefHeight(180);
        historyArea.setMinHeight(180);
        historyArea.setMaxHeight(180);
        historyArea.setWrapText(true);
        historyArea.setStyle(
                "-fx-control-inner-background:" + C2 + ";" +
                        "-fx-text-fill:" + W3 + ";" +
                        "-fx-font-family:'Menlo','Courier New',monospace;" +
                        "-fx-font-size:11.5;" +
                        "-fx-border-color:" + C3 + ";" +
                        "-fx-border-radius:6;-fx-background-radius:6;"
        );

        VBox col = new VBox(10, hd, add, scroll, logHdr, historyArea);
        col.setPadding(new Insets(14, 12, 12, 20));
        col.setPrefWidth(SIDE_W);
        col.setMinWidth(SIDE_W);
        col.setMaxWidth(SIDE_W);
        col.setStyle("-fx-background-color:" + C1 + ";-fx-border-color:" + C3 + ";-fx-border-width:0 1 0 0;");
        return col;
    }

    // RIGHT AREA

    private VBox buildRightArea() {
        hero = new ImageView();
        hero.setFitWidth(620);
        hero.setFitHeight(400);
        hero.setPreserveRatio(true);

        StackPane canvas = new StackPane(hero);
        canvas.setStyle(
                "-fx-background-color:" + C2 + ";" +
                        "-fx-background-radius:8;" +
                        "-fx-border-color:" + C3 + ";" +
                        "-fx-border-radius:8;"
        );
        VBox.setVgrow(canvas, Priority.ALWAYS);

        Label pHdr = secLabel("PROPERTIES");
        infoPane = new TextArea("Select an image from the library");
        infoPane.setEditable(false);
        infoPane.setPrefHeight(180);
        infoPane.setMinHeight(180);
        infoPane.setMaxHeight(180);
        infoPane.setWrapText(true);
        infoPane.setStyle(
                "-fx-control-inner-background:" + C2 + ";" +
                        "-fx-text-fill:" + W2 + ";" +
                        "-fx-font-family:'Menlo','Courier New',monospace;" +
                        "-fx-font-size:13;" +
                        "-fx-border-color:" + C3 + ";" +
                        "-fx-border-radius:6;-fx-background-radius:6;"
        );

        VBox area = new VBox(10, canvas, pHdr, infoPane);
        area.setPadding(new Insets(14, 16, 12, 12));
        area.setStyle("-fx-background-color:" + C0 + ";");
        return area;
    }

    // DOCK

    private VBox buildDock(Stage stage) {
        // Filter
        Label aL = secLabel("APPLY FILTER");
        fltBox = chip("Select filter...");
        fltBox.setPrefWidth(150);
        fltBox.getItems().addAll(FilterFactory.getAvailableFilters());

        Button aPrev = btn("Preview Filter", AC);
        aPrev.setOnAction(e -> doPreview());
        Button aSave = btn("Save Filtered", OK);
        aSave.setOnAction(e -> doSaveFilter(stage));
        Button aReset = btn("Reset Original", NO);
        aReset.setOnAction(e -> doReset());

        HBox filterRow = new HBox(8, fltBox, aPrev, aSave, aReset);
        filterRow.setAlignment(Pos.CENTER_LEFT);
        VBox aBox = new VBox(6, aL, filterRow);
        HBox.setHgrow(aBox, Priority.ALWAYS);

        // Convert
        Label eL = secLabel("CONVERT FORMAT");
        fmtBox = chip("Select format...");
        fmtBox.setPrefWidth(150);
        fmtBox.getItems().addAll(ConverterFactory.getSupportedFormats());

        Button eBtn = btn("Convert & Save", AC);
        eBtn.setOnAction(e -> doConvert(stage));

        HBox convertRow = new HBox(8, fmtBox, eBtn);
        convertRow.setAlignment(Pos.CENTER_LEFT);
        VBox eBox = new VBox(6, eL, convertRow);

        HBox tools = new HBox(20, aBox, vLine(), eBox);
        tools.setAlignment(Pos.CENTER_LEFT);
        tools.setPadding(new Insets(10, 22, 8, 22));

        statusLabel = new Label("\u2713 Ready — upload images to get started");
        statusLabel.setStyle("-fx-text-fill:" + OK + ";-fx-font-size:13;");
        HBox stat = new HBox(statusLabel);
        stat.setPadding(new Insets(4, 22, 6, 22));

        VBox dock = new VBox(0, tools, stat);
        dock.setStyle("-fx-background-color:" + C1 + ";-fx-border-color:" + C3 + ";-fx-border-width:1 0 0 0;");
        return dock;
    }

    // ACTIONS

    private void pickFiles(Stage stage) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Select Images to Upload");
        fc.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Images", "*.jpg", "*.jpeg", "*.png", "*.bmp", "*.gif"));
        List<File> picked = fc.showOpenMultipleDialog(stage);
        if (picked != null) for (File f : picked) ingest(f);
    }

    private void ingest(File f) {
        try {
            ImageFile img = ImageFileFactory.createImageFile(f);
            images.add(img);

            if (dropZone.getParent() == filmStrip)
                filmStrip.getChildren().remove(dropZone);

            Image th = new Image(new FileInputStream(f), 100, 100, true, true);
            ImageView iv = new ImageView(th);
            iv.setFitWidth(76);
            iv.setFitHeight(76);

            StackPane card = new StackPane(iv);
            card.setPadding(new Insets(3));
            card.setStyle(thumbStyle(C4));
            card.setOnMouseEntered(e -> { if (card != selectedCard) card.setStyle(thumbStyle(C5)); });
            card.setOnMouseExited(e -> { if (card != selectedCard) card.setStyle(thumbStyle(C4)); });
            card.setOnMouseClicked(e -> select(img, f, card));

            ContextMenu menu = new ContextMenu();
            MenuItem removeItem = new MenuItem("Remove from Library");
            removeItem.setOnAction(ev -> {
                images.remove(img);
                filmStrip.getChildren().remove(card);
                if (current == img) {
                    current = null; rawImage = null; filtered = null;
                    hero.setImage(null);
                    infoPane.setText("Select an image from the library");
                    if (selectedCard == card) selectedCard = null;
                }
                countLabel.setText(images.isEmpty() ? "" : images.size() + " image" + (images.size() != 1 ? "s" : ""));
                if (images.isEmpty()) filmStrip.getChildren().add(dropZone);
                say("\u2713 Removed: " + img.getFileName());
                HistoryManager.getInstance().recordEvent("Removed " + img.getFileName());
            });
            menu.getItems().add(removeItem);
            card.setOnContextMenuRequested(ev -> menu.show(card, ev.getScreenX(), ev.getScreenY()));

            Tooltip.install(card, new Tooltip(img.getFileName()));
            filmStrip.getChildren().add(card);

            countLabel.setText(images.size() + " image" + (images.size() != 1 ? "s" : ""));
            say("\u2713 Uploaded: " + img.getFileName());
            HistoryManager.getInstance().recordEvent("Uploaded " + img.getFileName()
                    + "  " + img.getResolution() + "  " + img.getFileSizeFormatted());
        } catch (Exception ex) {
            err("Upload Failed", ex.getMessage());
        }
    }

    private void select(ImageFile img, File f, StackPane card) {
        try {
            if (selectedCard != null) selectedCard.setStyle(thumbStyle(C4));
            selectedCard = card;
            card.setStyle(thumbStyle(AC));

            current = img; rawImage = ImageIO.read(f); filtered = null;
            hero.setImage(new Image(new FileInputStream(f)));

            infoPane.setText(
                    "File Name:    " + img.getFileName() + "\n" +
                            "Format:       " + img.getFormatDisplayName() + "\n" +
                            "Resolution:   " + img.getResolution() + " px\n" +
                            "File Size:    " + img.getFileSizeFormatted() + "\n" +
                            "Location:     " + img.getFilePath() + "\n" +
                            "Convert to:   " + String.join(", ", img.getSupportedConversions())
            );
            say("\u25B6 Selected: " + img.getFileName());
        } catch (Exception ex) { err("Preview Failed", ex.getMessage()); }
    }

    private void doConvert(Stage stage) {
        if (current == null) { err("No Image Selected", "Please select an image from the library first."); return; }
        if (fmtBox.getValue() == null) { err("No Format Selected", "Please choose a target format."); return; }
        try {
            String fmt = fmtBox.getValue();
            FileChooser fc = new FileChooser();
            fc.setTitle("Save Converted Image");
            fc.setInitialFileName(current.getFileName().replaceAll("\\.[^.]+$", "") + "." + fmt);
            File out = fc.showSaveDialog(stage);
            if (out != null) {
                ConverterFactory.createConverter(fmt).convert(filtered != null ? filtered : rawImage, out);
                say("\u2713 Converted & saved: " + out.getName());
                HistoryManager.getInstance().recordEvent("Converted " + current.getFileName() + " \u2192 " + fmt.toUpperCase());
            }
        } catch (Exception ex) { err("Conversion Failed", ex.getMessage()); }
    }

    private void doPreview() {
        if (current == null) { err("No Image Selected", "Please select an image first."); return; }
        if (fltBox.getValue() == null) { err("No Filter Selected", "Please choose a filter."); return; }
        try {
            say("\u23F3 Applying " + fltBox.getValue() + "...");
            ImageFilter flt = FilterFactory.createFilter(fltBox.getValue());
            filtered = flt.apply(rawImage);
            File tmp = File.createTempFile("img_", ".png"); tmp.deleteOnExit();
            ImageIO.write(filtered, "png", tmp);
            hero.setImage(new Image(new FileInputStream(tmp)));
            say("\u2713 Filter applied: " + flt.getName());
            HistoryManager.getInstance().recordEvent("Applied " + flt.getName() + " to " + current.getFileName());
        } catch (Exception ex) { err("Filter Failed", ex.getMessage()); }
    }

    private void doSaveFilter(Stage stage) {
        if (filtered == null) { err("No Filter Applied", "Preview a filter first."); return; }
        try {
            FileChooser fc = new FileChooser();
            fc.setTitle("Save Filtered Image");
            fc.setInitialFileName(current.getFileName().replaceAll("\\.[^.]+$", "") + "_filtered.png");
            File out = fc.showSaveDialog(stage);
            if (out != null) {
                String ext = out.getName().substring(out.getName().lastIndexOf('.') + 1);
                ConverterFactory.createConverter(ext).convert(filtered, out);
                say("\u2713 Saved: " + out.getName());
                HistoryManager.getInstance().recordEvent("Saved filtered: " + out.getName());
            }
        } catch (Exception ex) { err("Save Failed", ex.getMessage()); }
    }

    private void doReset() {
        if (current == null) return;
        try {
            filtered = null;
            hero.setImage(new Image(new FileInputStream(new File(current.getFilePath()))));
            say("\u21BA Reset to original");
        } catch (Exception ex) { err("Reset Failed", ex.getMessage()); }
    }

    // DRAG & DROP

    private void setupDrop(FlowPane p) {
        p.setOnDragOver(e -> {
            if (e.getDragboard().hasFiles()) e.acceptTransferModes(TransferMode.COPY);
            e.consume();
        });
        p.setOnDragDropped(e -> {
            if (e.getDragboard().hasFiles()) for (File f : e.getDragboard().getFiles()) ingest(f);
            e.setDropCompleted(true); e.consume();
        });
    }

    // OBSERVER

    @Override
    public void onEvent(String ev) {
        Platform.runLater(() -> {
            historyArea.setText(String.join("\n", HistoryManager.getInstance().getHistory()));
            historyArea.setScrollTop(Double.MAX_VALUE);
        });
    }

    // UI HELPERS

    private Button btn(String text, String bg) {
        Button b = new Button(text);
        b.setMinWidth(Region.USE_PREF_SIZE);
        b.setStyle(
                "-fx-background-color:" + bg + ";" +
                        "-fx-text-fill:white;-fx-font-weight:bold;" +
                        "-fx-font-size:14;-fx-padding:9 20;" +
                        "-fx-background-radius:18;-fx-cursor:hand;"
        );
        return b;
    }

    private ComboBox<String> chip(String ph) {
        ComboBox<String> c = new ComboBox<>();
        c.setPromptText(ph);
        c.setStyle(
                "-fx-background-color:" + C3 + ";" +
                        "-fx-border-color:" + C4 + ";" +
                        "-fx-border-radius:16;-fx-background-radius:16;" +
                        "-fx-padding:2 8;-fx-font-size:13;"
        );
        return c;
    }

    private Label secLabel(String t) {
        Label l = new Label(t);
        l.setFont(Font.font("Helvetica Neue", FontWeight.BOLD, 12));
        l.setStyle("-fx-text-fill:" + W3 + ";-fx-padding:0 0 2 1;");
        return l;
    }

    private String thumbStyle(String border) {
        return "-fx-background-color:" + C3 + ";" +
                "-fx-border-color:" + border + ";" +
                "-fx-border-width:2;-fx-border-radius:6;-fx-background-radius:6;" +
                "-fx-cursor:hand;";
    }

    private Region vLine() {
        Region r = new Region();
        r.setPrefWidth(1); r.setMinWidth(1); r.setMaxWidth(1);
        r.setMinHeight(40);
        r.setStyle("-fx-background-color:" + C3 + ";");
        return r;
    }

    private void say(String m) { statusLabel.setText(m); }

    private void err(String t, String m) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle(t); a.setHeaderText(null); a.setContentText(m);
        a.showAndWait();
    }

    public static void main(String[] args) { launch(args); }
}
