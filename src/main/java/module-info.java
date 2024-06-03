module myproject.shooter2d {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;


    opens myproject.shooter2d to javafx.fxml;
    exports myproject.shooter2d;
}