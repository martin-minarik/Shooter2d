package myproject.shooter2d;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class Shooter2dApp extends Application
{

    @Override
    public void start(Stage stage) throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(Shooter2dApp.class.getResource("view/title-screen-view.fxml"));
        Pane root = fxmlLoader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.resizableProperty().set(false);
        stage.setTitle("Shooter2d");
        stage.show();
        root.requestFocus();

        IControlleer controller = fxmlLoader.getController();
        controller.start();
    }

    public static void main(String[] args)
    {
        launch();
    }
}