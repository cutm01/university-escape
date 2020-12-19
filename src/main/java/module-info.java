module main {
    requires javafx.controls;
    requires javafx.fxml;

    opens cz.vse.java.cutm01.adventure.main to javafx.fxml;
    opens cz.vse.java.cutm01.adventure.ui to javafx.fxml;

    exports cz.vse.java.cutm01.adventure.main;
}
