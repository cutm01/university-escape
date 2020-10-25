package cz.vse.java.cutm01.adventure.main;

public class SystemInfo {

    public static final String LINE_SEPARATOR = System.getProperty("line.separator");

    public static String javaVersion() {
        return System.getProperty("java.version");
    }

    public static String javafxVersion() {
        return System.getProperty("javafx.version");
    }
}
