module com.example.imageeditorfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.swing;
    requires ij;
                            
    opens com.example.imageeditorfx to javafx.fxml;
    exports com.example.imageeditorfx;
}