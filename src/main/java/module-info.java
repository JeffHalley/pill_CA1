module com.example.pill_ca1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.swing;

    opens com.example.pill_ca1 to javafx.fxml;
    exports com.example.pill_ca1;
}