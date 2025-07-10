package org.automaton.control.game.neighborhood;

import lombok.Getter;
import org.automaton.control.game.GridPoint;

import java.util.List;

/**
 * Von Neumann cross-selection strategy
 */
@Getter
public class VonNeumannStrategy extends AbstractNeumannStrategy {
    private final int surviveMin = 1;
    private final int surviveMax = 2;
    private final int birthValue = 2;

    @Override
    public List<GridPoint> pointsNeighborhoods(int x, int y) {
        return this.pointsVonNeumannGeneral(x, y, 1);
    }
}
