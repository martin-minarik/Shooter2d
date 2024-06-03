package myproject.shooter2d;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;

import java.util.Objects;

public class GameOverController implements IControlleer
{

    @FXML
    Pane rootPane;

    @FXML
    private Label highestScoreLabel;

    @FXML
    private Label scoreLabel;



    private int highestScore;

    @Override
    public void start()
    {
        AudioClip clip = new AudioClip(Objects.requireNonNull(getClass().getResource("sound/die.wav")).toString());
        clip.play();
    }

    @Override
    public void stop()
    {
    }

    @FXML
    private void playAgainPressed() {
        try
        {
            Stage stage = (Stage) rootPane.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(Shooter2dApp.class.getResource("view/game-view.fxml"));
            Pane root = fxmlLoader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            root.requestFocus();
            IControlleer controller = fxmlLoader.getController();

            ((GameController) controller).setHighestScore(this.highestScore);

            controller.start();

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void setScore(int score)
    {
        this.scoreLabel.setText("" + score);
    }

    public void setHighestScore(int score)
    {
        this.highestScore = score;
        this.highestScoreLabel.setText("" + score);
    }
}
