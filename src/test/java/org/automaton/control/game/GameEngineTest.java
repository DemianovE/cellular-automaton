package org.automaton.control.game;

import org.automaton.control.enums.GameStatus;
import org.automaton.control.enums.NeighborhoodType;
import org.automaton.control.model.GameConfigModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class GameEngineTest {

    private GameEngine gameEngine;
    private GameConfigModel model;

    @BeforeEach
    public void setup(){
        this.gameEngine = GameEngine.getInstance();
        this.model = GameConfigModel.getInstance();

        this.model.getRows().set(5);
        this.model.getCols().set(5);

        this.model.getLivePercent().set(0);
        this.model.getGameStatus().set(GameStatus.STOPED);
        this.model.getEpochCount().set(0);

        int[][] grid = {
                {0, 0, 0, 0, 0},
                {0, 1, 0, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 1, 1, 1, 1},
                {0, 1, 1, 0, 0}
        };

        this.model.getGridData().set(grid);
        this.model.getSelectedNeighborhood().set(NeighborhoodType.MOORE);

        this.gameEngine.setSelectedNeighborhoodStrategy();
    }

    @Test
    void calculateNextStepCellStatus_LiveCellUnderpopulated_Dies() {
        assertThat(this.gameEngine.performOnePointIteration(1, 1)).isEqualTo(0);
    }

    @Test
    void calculateNextStepCellStatus_LiveCellOverpopulated_Dies(){
        assertThat(this.gameEngine.performOnePointIteration(4, 2)).isEqualTo(0);
    }

    @Test
    void calculateNextStepCellStatus_LiveCellSurvival_Survives(){
        assertThat(this.gameEngine.performOnePointIteration(3, 1)).isEqualTo(1);
    }

    @Test
    void calculateNextStepCellStatus_DeadCellBorn_Born(){
        assertThat(this.gameEngine.performOnePointIteration(2, 1)).isEqualTo(1);
    }


    @Test
    void calculateOneFullEpoch(){
        this.gameEngine.performOneEpochIteration();

        int[][] resultGrid = {
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 1, 0, 1, 0},
                {0, 1, 0, 1, 0},
                {0, 1, 0, 0, 0}
        };

        assertThat(model.getGridData().get()).isEqualTo(resultGrid);
    }
}
