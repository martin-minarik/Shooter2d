package myproject.shooter2d;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.*;
import java.util.*;


public class GameController implements IControlleer
{

    private Game game;

    private Shooter2dApp app;

    @FXML
    Pane gamePane;

    @FXML
    Group gameGroup;

    @FXML
    Canvas canvasMinimap;

    @FXML
    Label scoreLabel;

    @FXML
    Label livesLabel;

    @FXML
    Label bombsLabel;

    @FXML
    Label highestScoreLabel;

    private AnimationTimer animationTimer;

    private Set<KeyCode> keysDown = new HashSet<>();

    private int highestScore = 0;
    private int score = 0;

    public void start()
    {
        this.loadHighestScore("highest_score.txt");
        this.game = new Game(gameGroup, gamePane.getWidth(), gamePane.getHeight());
        this.game.setController(new GameListenerImpl());


        animationTimer = new AnimationTimerImpl();
        animationTimer.start();

        drawSimulateScene(0);
    }

    public void stop()
    {

    }



    private final class AnimationTimerImpl extends AnimationTimer
    {
        private Long previous;

        double bulletTimer = 0;
        double bombTimer = 0;

        @Override
        public void handle(long now)
        {

            if (previous != null) {
                double deltaT = (now - previous) / 1e9;
                drawSimulateScene(deltaT);


                int horizontal_k = (key(KeyCode.D) - key(KeyCode.A));
                int vertical_k = (key(KeyCode.S) - key(KeyCode.W));

                if (horizontal_k != 0 || vertical_k != 0)
                    game.playerMove(horizontal_k, vertical_k);


                if (key(KeyCode.SPACE) == 1 && bulletTimer <= 0) {
                    game.playerFire();
                    bulletTimer = 0.5;
                }

                if (key(KeyCode.B) == 1 && bombTimer <= 0) {
                    game.useBomb();
                    bombTimer = 2;
                }

                bombTimer -= deltaT;
                bulletTimer -= deltaT;
            }
            previous = now;
        }
    }

    private void drawSimulateScene(double deltaT)
    {
        game.drawMinimap(canvasMinimap);
        game.simulate(deltaT);
    }

    @FXML
    public void keyPressed(KeyEvent event)
    {
        keysDown.add(event.getCode());
    }

    @FXML
    public void keyReleased(KeyEvent event)
    {
        keysDown.remove(event.getCode());
    }

    private int key(KeyCode k)
    {
        return keysDown.contains(k) ? 1 : 0;
    }


    private class GameListenerImpl implements IGameListener
    {

        @Override
        public void stateChanged(int score, int lives, int bombs)
        {
            GameController.this.score = score;
            if (score > GameController.this.highestScore)
                GameController.this.highestScore = score;

            GameController.this.scoreLabel.setText("" + score);
            GameController.this.livesLabel.setText("" + lives);
            GameController.this.bombsLabel.setText("" + bombs);
            GameController.this.highestScoreLabel.setText("" + GameController.this.highestScore);

        }

        @Override
        public void gameOver()
        {
            GameController.this.gameOver();
        }

    }

    public void gameOver()
    {
        this.animationTimer.stop();
        try
        {
            stopGame();
            this.saveHighestScore("highest_score.txt");
            Stage stage = (Stage) gamePane.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(Shooter2dApp.class.getResource("view/game-over-view.fxml"));
            Pane root = fxmlLoader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            root.requestFocus();
            IControlleer controller = fxmlLoader.getController();
            ((GameOverController) controller).setScore(GameController.this.score);
            ((GameOverController) controller).setHighestScore(GameController.this.highestScore);
            controller.start();

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void setHighestScore(int score)
    {
        this.highestScore = score;
        this.highestScoreLabel.setText("" + score);
    }

    private void stopGame()
    {
    }

    public void saveHighestScore(String filepath)
    {
        try
        {
            File file = new File(filepath);
            file.createNewFile();
            try (FileOutputStream fos = new FileOutputStream(file, false);)
            {
                byte[] data = Integer.toString(highestScore).getBytes();
                fos.write(data);
                fos.flush();
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void loadHighestScore(String filepath)
    {
        try (BufferedReader br = new BufferedReader(new FileReader(filepath));)
        {
            setHighestScore(Integer.parseInt(br.readLine()));
        } catch (FileNotFoundException e)
        {
            setHighestScore(0);
        } catch (IOException e)
        {
            e.printStackTrace();
        }

    }
}
