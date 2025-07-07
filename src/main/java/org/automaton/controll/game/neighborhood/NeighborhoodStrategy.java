package org.automaton.controll.game.neighborhood;

import lombok.Getter;
import org.automaton.controll.game.GridPoint;
import java.util.List;

/**
 * Interface for strategies of the neighbor selection
 */
public interface NeighborhoodStrategy {
    /**
     * The function to return the points which will be used in the coefficient calculation
     * @param x - the row coordinate of the point
     * @param y - the col coordinate of the point
     * @return the list of points
     */
    List<GridPoint> pointsNeighborhoods(int x, int y);

    int getSurviveMin();
    int getSurviveMax();
    int getBirthValue();
}
