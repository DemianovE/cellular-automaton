package org.automaton.controll.game.neighborhood;

import lombok.Getter;
import org.automaton.controll.common.HasDisplayName;

/**
 * The enum to hold the strategy type amd the pointer to the object
 */
@Getter
public enum NeighborhoodType implements HasDisplayName {
    VON_NEUMANN("Von Neumann", 4, new VonNeumannStrategy()),
    VON_NEUMANN_EXTENDED("Von Neumann ext.", 8, new VonNeumannExtendedStrategy()),
    MOORE("Moore", 8, new MooreStrategy());

    private final String displayName;
    private final int totalNeighbors;
    private final NeighborhoodStrategy strategy;

    NeighborhoodType(String displayName, int totalNeighbors, NeighborhoodStrategy strategy) {
        this.displayName = displayName;
        this.totalNeighbors = totalNeighbors;
        this.strategy = strategy;
    }
}
