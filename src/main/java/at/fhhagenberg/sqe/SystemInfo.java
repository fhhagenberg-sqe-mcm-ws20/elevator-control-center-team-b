package at.fhhagenberg.sqe;

/**
 * Gather Systeminfo
 */
public class SystemInfo {

    /**
     * Get java version
     * @return current java version
     */
    public static String javaVersion() {
        return System.getProperty("java.version");
    }

    /**
     * Get javaFX version
     * @return current javaFX version
     */
    public static String javafxVersion() {
        return System.getProperty("javafx.version");
    }

}