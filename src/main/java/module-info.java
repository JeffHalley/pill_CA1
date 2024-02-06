module com.example.pill_ca1 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.pill_ca1 to javafx.fxml;
    exports com.example.pill_ca1;
}