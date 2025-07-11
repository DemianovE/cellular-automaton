package org.automaton.control.enums;

import lombok.Getter;
import org.automaton.control.common.HasDisplayName;

@Getter
public enum InputType implements HasDisplayName {
    AUTOMATIC("Automatic"), MANUAL("Manual");

    public final String displayName;
    InputType(String displayName) {
        this.displayName = displayName;
    }
}
