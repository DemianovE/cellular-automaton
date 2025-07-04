package org.automaton.controll.model;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import lombok.Getter;
import lombok.Setter;
import org.automaton.controll.game.GameStatus;
import org.automaton.controll.game.MapMode;
import org.automaton.controll.game.NeighborhoodType;

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

    private final SimpleBooleanProperty isGameRunning = new  SimpleBooleanProperty(false);

    private final SimpleIntegerProperty epochCount = new  SimpleIntegerProperty(0);

    private GameConfigModel() {}

    public static GameConfigModel getInstance() {
        return INSTANCE;
    }

    public void setIsGameRunning(boolean isGameRunning) { this.isGameRunning.set(isGameRunning); }
    public void setGameStatus(GameStatus gameStatus) { this.gameStatus.set(gameStatus); }

    public int getRowsPrimitive(){ return this.rows.get(); }
    public int getColsPrimitive(){ return this.cols.get(); }

}
