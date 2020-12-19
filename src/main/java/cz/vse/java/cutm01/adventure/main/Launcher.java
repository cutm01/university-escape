package cz.vse.java.cutm01.adventure.main;

/**
 * Launcher class is main class of project. Unlike class Start, it does not extend class Application
 * which can cause errors while executing jar file
 */
public class Launcher {
    public static void main(String[] args) {
        Start.main(args);
    }
}
