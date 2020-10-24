module main {
    requires javafx.controls;

    opens cz.vse.java.cutm01.adventure.main to
        javafx.fxml;

    exports cz.vse.java.cutm01.adventure.main;
}
