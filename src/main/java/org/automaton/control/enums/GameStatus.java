package org.automaton.control.enums;

import lombok.Getter;
import org.automaton.control.common.HasDisplayName;

@Getter
public enum GameStatus implements HasDisplayName {
    PAUSED("Paused"), STOPED("Stoped"), RUNNING("Running");

    public final String displayName;
    GameStatus(String displayName) {
        this.displayName = displayName;
    }
}
