package org.automaton.control.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import lombok.Getter;
import lombok.Setter;
import org.automaton.control.enums.GameStatus;
import org.automaton.control.enums.InputType;
import org.automaton.control.enums.MapMode;
import org.automaton.control.enums.NeighborhoodType;
import java.util.Random;

/**
 * Singleton model which is holding all data for the game for the components to talk to.
 */
@Getter
@Setter
public class GameConfigModel {

    private static final GameConfigModel INSTANCE = new GameConfigModel();

    private final SimpleIntegerProperty rows = new SimpleIntegerProperty(50);
    private final SimpleIntegerProperty cols = new SimpleIntegerProperty(50);
    private final SimpleIntegerProperty livePercent = new SimpleIntegerProperty(20);

    private final SimpleObjectProperty<MapMode> selectedMode = new SimpleObjectProperty<>(MapMode.FINITE);
    private final SimpleObjectProperty<NeighborhoodType> selectedNeighborhood = new SimpleObjectProperty<>(NeighborhoodType.VON_NEUMANN);
    private final SimpleObjectProperty<GameStatus> gameStatus = new SimpleObjectProperty<>(GameStatus.STOPED);
    private final SimpleObjectProperty<InputType> gameInputType = new SimpleObjectProperty<>(InputType.AUTOMATIC);

    private final ObjectProperty<int[][]> gridData = new SimpleObjectProperty<>(new int[50][50]);

    private final SimpleIntegerProperty epochCount = new  SimpleIntegerProperty(0);
    private final SimpleIntegerProperty LiveCount = new  SimpleIntegerProperty(0);

    private Random randomNo = new Random();

    private GameConfigModel() {}

    public static GameConfigModel getInstance() {
        return INSTANCE;
    }

    public void setGameStatus(GameStatus gameStatus) { this.gameStatus.set(gameStatus); }

    public int getRowsPrimitive(){ return this.rows.get(); }
    public int getColsPrimitive(){ return this.cols.get(); }
    public int getLivePercentPrimitive(){ return this.livePercent.get(); }
    public int getEpochCountPrimitive(){ return this.epochCount.get(); }

    public void reshapeDataGrip(){
        this.gridData.set(new int[this.getRowsPrimitive()][this.getColsPrimitive()]);
    }

    public void resetDataGrid(){
        for (int x=0; x < this.getRowsPrimitive(); x++) {
            for (int y=0; y < this.getColsPrimitive(); y++) {
                if (this.getGameInputType().get() == InputType.AUTOMATIC) {
                    this.setDataGridCoordinate(x, y, randomNo.nextDouble() < (double) this.getLivePercentPrimitive() / 100 ? 1 : 0);
                } else {
                    this.setDataGridCoordinate(x, y, 0);
                }

            };
        }
    }

    public void incrementEpochs(){
        this.epochCount.set(this.epochCount.get() + 1);
    }

    /**
     * The function is a bit tricky. We use a simple formula for the change for the 2D coordinate into 1D, to save in memory only the 1D array pointer and not map of pointers,
     * But we have finite and infinite maps, based on that we have a rule, for the out of bounds, if infinite we reset the coordinate to navigate the torus, in case of finite,
     * draft the 1 or 0 in the random selection.
     * @param x - the roed coordinate
     * @param y -  the column coordinate
     * @return the value of 1 or 0 from the grid map
     */
    public int getDataGridCoordinate(int x, int y){
        x = checkCoordinate(x, this.getRowsPrimitive());
        y = checkCoordinate(y, this.getColsPrimitive());

        return this.gridData.get()[x][y];
    }

    public void setDataGridCoordinate(int x, int y, int value){
        int numRows = this.gridData.get().length;
        int numCols = this.gridData.get()[0].length;
        int[][] newGrid = new int[numRows][numCols];

        for (int r = 0; r < numRows; r++) {
            System.arraycopy(this.gridData.get()[r], 0, newGrid[r], 0, numCols);
        }

        newGrid[x][y] = value;

        this.gridData.set(newGrid);
    }

    private int checkCoordinate(int coordinate, int max) {
        if (coordinate < 0 || coordinate >= max){
            if (this.getSelectedMode().get() == MapMode.FINITE){
                return 0;
            } else {
                if (coordinate < 0 ) { return max + coordinate; } // switch to the end of the coordinate line
                else { return coordinate - max; } // switch to the start of the coordinate panel
            }
        } else { return coordinate; }
    }

    public void toggleDataGridCoordinate(int x, int y){ this.setDataGridCoordinate(x, y, (this.gridData.get()[x][y] == 1) ? 0 : 1); }
    public void setEpochCount(int epochCount){ this.epochCount.set(epochCount); }
    public void setLiveCount(int  liveCount){ this.LiveCount.set(liveCount); }

}
