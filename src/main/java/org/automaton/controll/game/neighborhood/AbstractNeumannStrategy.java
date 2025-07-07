package org.automaton.controll.game.neighborhood;

import org.automaton.controll.game.GridPoint;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractNeumannStrategy implements NeighborhoodStrategy{

    /**
     * The function to implement Neumann support function
     * @param x - row coordinate of the point
     * @param y - col coordinate of the point
     * @param offset - the offset
     * @return the list of points
     */
    protected List<GridPoint> pointsVonNeumannGeneral(int x, int y, int offset){
        List<GridPoint> neighbors = new ArrayList<>();
        for (int d = -1 * offset; d <= offset; d++) if (d != 0)  {
            neighbors.add(new GridPoint(x + d, y));
            neighbors.add(new GridPoint(x, y + d));
        }
        return neighbors;
    }
}
