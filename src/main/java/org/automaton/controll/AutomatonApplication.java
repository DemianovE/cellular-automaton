package org.automaton.controll;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import org.automaton.controll.game.GameEngine;
import org.automaton.controll.game.GameStatus;
import org.automaton.controll.model.GameConfigModel;
import org.automaton.controll.ui.ConfigurationPanel;
import org.automaton.controll.ui.GameFieldPanel;

import java.io.IOException;

public class AutomatonApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        BorderPane root = new BorderPane();

        ConfigurationPanel configurationPanel = ConfigurationPanel.getInstance();
        GameFieldPanel gameFieldPanel = GameFieldPanel.getInstance();
        GameConfigModel model = GameConfigModel.getInstance();
        GameEngine gameEngine = GameEngine.getInstance();

        root.setLeft(configurationPanel);
        root.setCenter(gameFieldPanel);

        configurationPanel.getStartButton().setOnAction(e -> {
            model.setGameStatus(GameStatus.RUNNING);
            gameEngine.startGame();
        });

        configurationPanel.getPauseButton().setOnAction(e -> {
            if (model.getGameStatus().get().equals(GameStatus.RUNNING)) {
                model.setGameStatus(GameStatus.PAUSED);
                gameEngine.cancelCurrentTimer();
            } else {
                model.setGameStatus(GameStatus.RUNNING);
                gameEngine.startGame();
            }
        });

        configurationPanel.getResetButton().setOnAction(e -> {
            model.setGameStatus(GameStatus.STOPED);
            gameEngine.resetGame();
        });


        Scene scene = new Scene(root, 1200, 800); // Initial width and height
        primaryStage.setTitle("Automaton Simulation");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}


