import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Observer interface for receiving processing event notifications.
 * Demonstrates: Observer Design Pattern.
 */
interface ProcessingObserver {
    void onEvent(String eventDescription);
}

/**
 * Manages image processing history using the Observer pattern.
 * When an action occurs (upload, convert, filter), all registered observers are notified.
 */
public class HistoryManager {
    private static HistoryManager instance; // Singleton
    private final List<ProcessingObserver> observers = new ArrayList<>();
    private final List<String> history = new ArrayList<>();
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("HH:mm:ss");

    // Private constructor (Singleton pattern)
    private HistoryManager() {}

    /**
     * Returns the single instance of HistoryManager.
     * Demonstrates: Singleton Design Pattern.
     */
    public static HistoryManager getInstance() {
        if (instance == null) {
            instance = new HistoryManager();
        }
        return instance;
    }

    /**
     * Registers an observer.
     */
    public void addObserver(ProcessingObserver observer) {
        observers.add(observer);
    }

    /**
     * Records an event and notifies all observers.
     */
    public void recordEvent(String description) {
        String entry = "[" + LocalDateTime.now().format(FMT) + "] " + description;
        history.add(entry);
        for (ProcessingObserver obs : observers) {
            obs.onEvent(entry);
        }
    }

    /**
     * Returns a copy of the full history log.
     */
    public List<String> getHistory() {
        return new ArrayList<>(history);
    }

    /**
     * Clears all history.
     */
    public void clearHistory() {
        history.clear();
    }
}
