package myproject.shooter2d;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class TitleScreenController implements IControlleer {

    @FXML
    Pane rootPane;


    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @FXML
    private void play() {
        try {
            Stage stage = (Stage) rootPane.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(Shooter2dApp.class.getResource("view/game-view.fxml"));
            Pane root = fxmlLoader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            root.requestFocus();
            IControlleer controller = fxmlLoader.getController();
            controller.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
