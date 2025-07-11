package org.automaton.control.game;

import javafx.application.Platform;
import org.automaton.control.enums.InputType;
import org.automaton.control.game.neighborhood.NeighborhoodStrategy;
import org.automaton.control.model.GameConfigModel;

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
        setSelectedNeighborhoodStrategy();

        gameLoopTimer = new Timer(true); // Create a new daemon Timer
        gameLoopTask = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    performOneEpochIteration();
                });
            }
        };

        gameLoopTimer.scheduleAtFixedRate(gameLoopTask, 0, 100);
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
        if (this.model.getGameInputType().get() == InputType.AUTOMATIC){
            this.model.resetDataGrid();
        } else {
            this.model.resetDataToZero();
        }
    }

    public void performOneEpochIteration(){
        model.incrementEpochs();

        int[][] newGrid = new int[model.getRowsPrimitive()][model.getColsPrimitive()];

        int survive = 0;
        for (int i = 0; i < model.getRowsPrimitive(); i++){
            for (int j = 0; j < model.getColsPrimitive(); j++){
                int resultForCell = performOnePointIteration(i, j);
                newGrid[i][j] = resultForCell;
                if (resultForCell == 1) survive++;
            }
        }

        model.setLiveCount(survive);
        model.getGridData().set(newGrid);
    }

    /**
     * The action on specific cell
     * @param x - row of the desired cell
     * @param y - coll of the desired cell
     * @return the value of 1 or 0 depending on the logic
     */
    public int performOnePointIteration(int x, int y){
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

    public void setSelectedNeighborhoodStrategy() {
        this.selectedNeighborhoodStrategy = this.model.getSelectedNeighborhood().get().getStrategy();;
    }
}
