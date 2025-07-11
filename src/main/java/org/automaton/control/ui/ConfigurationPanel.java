package org.automaton.control.ui;

import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import lombok.Getter;

import org.automaton.control.enums.InputType;
import org.automaton.control.factories.UiComponentFactory;
import org.automaton.control.enums.GameStatus;
import org.automaton.control.enums.MapMode;
import org.automaton.control.enums.NeighborhoodType;
import org.automaton.control.model.GameConfigModel;

import java.util.Objects;
import java.util.Arrays;

/**
 * Represents the singleton class object configuration panel of JavaFX application
 * It is an extension of the VBox which generates all children
 */
public class ConfigurationPanel extends VBox {

    /** instance of the model for configurations */
    private final GameConfigModel model = GameConfigModel.getInstance();

    /** The singleton saving INSTANCE */
    private static final ConfigurationPanel INSTANCE = new ConfigurationPanel();

    @Getter private Button startButton = new Button("Start");
    @Getter private Button resetButton = new Button("Reset");
    @Getter private Button pauseButton = new Button("Pause");
    @Getter private Button randomiseButton = new Button("Randomise");

    /**
     * Creates a new ConfigurationPanel instance and sets the children
     */
    private ConfigurationPanel() {
        this.setPadding(new Insets(10));
        this.setSpacing(10);

        this.setPrefWidth(300);
        this.setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #ccc; -fx-border-width: 0 1 0 0;");

        this.getChildren().addAll(
                createGridConfiguration(),
                createGameConfiguration(),
                UiComponentFactory.createSpacer(),
                createFooterButtonBox()
        );

        this.getStylesheets().add(Objects.requireNonNull(getClass().getResource("segmented-button-styles.css")).toExternalForm());
    }

    /**
     * The function to make sure the class is singleton
     * @return INSTANCE of once created class
     */
    public static ConfigurationPanel getInstance() {
        return INSTANCE;
    }

    /**
     * Function to create a grid configuration box
     * @return VBox object of the grid configuration
     */
    private VBox createGridConfiguration() {
        VBox gridConfiguration = new VBox(10);

        Label gridLabel = UiComponentFactory.createLabel("Grid Configuration", 20);

        Pair<HBox, DoubleProperty> rowsControl = UiComponentFactory.createSliderBox("Rows", 10, 100, 45);
        Pair<HBox, DoubleProperty> colsControl = UiComponentFactory.createSliderBox("Cols", 10, 100, 45);

        this.model.getRows().bind(rowsControl.getValue());
        this.model.getCols().bind(colsControl.getValue());

        HBox mapModeToggle = UiComponentFactory.createToggleButtonsBox(
                "Mode",
                14,
                MapMode.FINITE,
                MapMode.FINITE.getDisplayName(),
                MapMode.INFINITE,
                MapMode.INFINITE.getDisplayName(),
                this.model.getSelectedMode()
        );

        HBox gridModeToggle = UiComponentFactory.createToggleButtonsBox(
                "Input",
                14,
                InputType.AUTOMATIC,
                InputType.AUTOMATIC.getDisplayName(),
                InputType.MANUAL,
                InputType.MANUAL.getDisplayName(),
                this.model.getGameInputType()
        );

        // move all sub-label items on the right
        rowsControl.getKey().setPadding(new Insets(0, 0, 0, 30));
        colsControl.getKey().setPadding(new Insets(0, 0, 0, 30));
        mapModeToggle.setPadding(new Insets(0, 0, 0, 30));
        gridModeToggle.setPadding(new Insets(0, 0, 0, 30));
        this.randomiseButton.setPadding(new Insets(0, 0, 0, 30));

        rowsControl.getKey().disableProperty().bind(this.model.getGameStatus().isNotEqualTo(GameStatus.STOPED));
        colsControl.getKey().disableProperty().bind(this.model.getGameStatus().isNotEqualTo(GameStatus.STOPED));
        mapModeToggle.disableProperty().bind(this.model.getGameStatus().isNotEqualTo(GameStatus.STOPED));
        gridModeToggle.disableProperty().bind(this.model.getGameStatus().isNotEqualTo(GameStatus.STOPED));
        this.randomiseButton.disableProperty().bind(this.model.getGameStatus().isNotEqualTo(GameStatus.STOPED));

        gridConfiguration.getChildren().addAll(
                gridLabel,
                rowsControl.getKey(),
                colsControl.getKey(),
                mapModeToggle,
                gridModeToggle,
                this.randomiseButton

        );
        return gridConfiguration;
    }

    /**
     * Create the game configuration panel
     * @return the VBox instance of the game configuration
     */
    private VBox createGameConfiguration() {
        VBox gameConfiguration = new VBox(10);

        Label gameLabel = UiComponentFactory.createLabel("Game Configuration", 20);

        HBox neighborhoodSelect = UiComponentFactory.createRadioButtonBox(
                "Neighborhood",
                14,
                Arrays.asList(NeighborhoodType.values()),
                this.model.getSelectedNeighborhood()
        );

        Pair<HBox, DoubleProperty> livePercent = UiComponentFactory.createSliderBox("Live %", 10, 80, 20);
        this.model.getLivePercent().bind(livePercent.getValue());

        neighborhoodSelect.setPadding(new Insets(0, 0, 0, 30));
        livePercent.getKey().setPadding(new Insets(0, 0, 0, 30));

        neighborhoodSelect.disableProperty().bind(this.model.getGameStatus().isNotEqualTo(GameStatus.STOPED));
        livePercent.getKey().disableProperty().bind(this.model.getGameStatus().isNotEqualTo(GameStatus.STOPED));

        gameConfiguration.getChildren().addAll(
                gameLabel,
                neighborhoodSelect,
                livePercent.getKey()
        );

        return gameConfiguration;
    }

    /**
     * Create a footer buttons for the start and reset
     * @return HBox instance with buttons
     */
    private HBox createFooterButtonBox(){
        HBox buttonBox = new HBox(15);
        buttonBox.getChildren().addAll(this.startButton, this.pauseButton, this.resetButton);

        HBox.setHgrow(this.startButton, Priority.ALWAYS);
        HBox.setHgrow(this.pauseButton, Priority.ALWAYS);
        HBox.setHgrow(this.resetButton, Priority.ALWAYS);
        this.startButton.setMaxWidth(Double.MAX_VALUE);
        this.pauseButton.setMaxWidth(Double.MAX_VALUE);
        this.resetButton.setMaxWidth(Double.MAX_VALUE);

        this.startButton.disableProperty().bind(this.model.getGameStatus().isNotEqualTo(GameStatus.STOPED));
        this.pauseButton.disableProperty().bind(this.model.getGameStatus().isEqualTo(GameStatus.STOPED));
        this.resetButton.disableProperty().bind(this.model.getGameStatus().isEqualTo(GameStatus.STOPED));
        
        // fpr pause/continue logic
        pauseButton.textProperty().bind(
                Bindings.when(model.getGameStatus().isEqualTo(GameStatus.PAUSED))
                        .then("continue")
                        .otherwise("pause")
        );

        return buttonBox;
    }
}
