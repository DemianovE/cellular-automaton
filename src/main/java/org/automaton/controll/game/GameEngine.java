package org.automaton.controll.game;

import javafx.application.Platform;
import javafx.util.Pair;
import org.automaton.controll.game.neighborhood.NeighborhoodStrategy;
import org.automaton.controll.model.GameConfigModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * The singleton class for the game logic modules
 */
public class GameEngine {

    private static final GameEngine INSTANCE = new GameEngine();
    private final GameConfigModel model = GameConfigModel.getInstance();
    private NeighborhoodStrategy selectedNeighborhoodStrategy;

    private Timer gameLoopTimer;
    private TimerTask gameLoopTask;

    private GameEngine() {}

    public static GameEngine getInstance() { return INSTANCE; }

    /**
     * function to make a new timer and star a job
     */
    public void startGame(){
        this.selectedNeighborhoodStrategy = this.model.getSelectedNeighborhood().get().getStrategy();

        gameLoopTimer = new Timer(true); // Create a new daemon Timer
        gameLoopTask = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    model.incrementEpochs();
                    for (int i = 0; i < model.getRowsPrimitive(); i++){
                        for (int j = 0; j < model.getColsPrimitive(); j++){
                            model.setDataGridCoordinate(i, j, performOnePointIteration(i, j));
                        }
                    }

                });
            }
        };

        gameLoopTimer.scheduleAtFixedRate(gameLoopTask, 0, 200);
    }

    /**
     * stop the timer´, used to pause or reset
     */
    public void cancelCurrentTimer() {
        if (gameLoopTimer != null) {
            gameLoopTimer.cancel();
            gameLoopTimer = null;
            gameLoopTask = null;
            System.out.println("Current game loop timer cancelled.");
        }
    }

    /**
     * FUll reset of the game
     */
    public void resetGame(){
        cancelCurrentTimer();

        this.model.setEpochCount(0);
        this.model.resetDataGrid();
    }

    /**
     * The action on specific cell
     * @param x - row of the desired cell
     * @param y - coll of the desired cell
     * @return the value of 1 or 0 depending on the logic
     */
    private int performOnePointIteration(int x, int y){
        List<GridPoint> points = this.selectedNeighborhoodStrategy.pointsNeighborhoods(x, y);

        int lifeCount = 0;
        for (GridPoint point : points){
            lifeCount += this.model.getDataGridCoordinate(point.x(), point.y());
        }

        if (this.model.getDataGridCoordinate(x, y) == 0){
            return (this.selectedNeighborhoodStrategy.getBirthValue() == lifeCount) ? 1 : 0;
        } else {
            return (lifeCount < this.selectedNeighborhoodStrategy.getSurviveMin() || lifeCount > this.selectedNeighborhoodStrategy.getSurviveMax()) ? 0 : 1;
        }
    }
}
