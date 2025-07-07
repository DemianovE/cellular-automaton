package org.automaton.controll.game.neighborhood;

import lombok.Getter;
import org.automaton.controll.game.GridPoint;

import java.util.ArrayList;
import java.util.List;

/**
 * Moore strategy of a rectangular select
 */
@Getter
public class MooreStrategy implements NeighborhoodStrategy {
    private final int surviveMin = 2;
    private final int surviveMax = 3;
    private final int birthValue = 3;

    @Override
    public List<GridPoint> pointsNeighborhoods(int x, int y){
        List<GridPoint> neighbors = new ArrayList<>();
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue;
                neighbors.add(new GridPoint(x + dx, y + dy));
            }
        }
        return neighbors;
    }
}
