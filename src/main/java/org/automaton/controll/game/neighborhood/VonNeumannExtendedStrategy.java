package org.automaton.controll.game.neighborhood;

import lombok.Getter;
import org.automaton.controll.game.GridPoint;

import java.util.List;

/**
 * The extended cross-selection of Von Neumann
 */
@Getter
public class VonNeumannExtendedStrategy extends AbstractNeumannStrategy {
    private final int surviveMin = 2;
    private final int surviveMax = 3;
    private final int birthValue = 3;

    @Override
    public List<GridPoint> pointsNeighborhoods(int x, int y) {
        return this.pointsVonNeumannGeneral(x, y, 2);
    }
}
