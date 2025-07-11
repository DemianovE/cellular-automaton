package org.automaton.control.ui;

import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.automaton.control.enums.GameStatus;
import org.automaton.control.enums.InputType;
import org.automaton.control.factories.UiComponentFactory;
import org.automaton.control.game.GameEngine;
import org.automaton.control.game.GridDrawingMetrics;
import org.automaton.control.model.GameConfigModel;

import javafx.scene.input.MouseEvent;

/**
 * Main function to make the UI component of game field
 * It makes all the actions to generate grid and statistics
 */
public class GameFieldPanel extends BorderPane {

    private GraphicsContext gc;
    private Canvas gameCanvas;

    private static final GameFieldPanel INSTANCE = new GameFieldPanel();

    private final GameConfigModel model = GameConfigModel.getInstance();
    private final ConfigurationPanel  configurationPanel = ConfigurationPanel.getInstance();
    private final GameEngine gameEngine = GameEngine.getInstance();

    private GridDrawingMetrics gridMetrics;

    private GameFieldPanel(){
        this.setStyle("-fx-background-color: #444;");

        this.setTop(createTopStatsBox());
        this.setCenter(createGridBox());
    }

    /**
     * Singleton function to make on a UI component
     * @return the singleton object of the class
     */
    public static GameFieldPanel getInstance() {
        return INSTANCE;
    }

    /**
     * Make the StackPane object of the Grid box
     * Also, makes binding to the size changes as well as will hold the binding to the data change, to update it automatically when the engine makes new data set
     * @return StackPane object
     */
    private StackPane createGridBox(){
        this.gameCanvas = new Canvas();
        this.gc = this.gameCanvas.getGraphicsContext2D();

        StackPane canvasContainer = new StackPane();
        canvasContainer.getChildren().add(this.gameCanvas);

        BorderPane.setMargin(canvasContainer, new Insets(0, 20, 20, 20));
        StackPane.setAlignment(this.gameCanvas, Pos.CENTER);

        this.gameCanvas.widthProperty().bind(canvasContainer.widthProperty());
        this.gameCanvas.heightProperty().bind(canvasContainer.heightProperty());

        gameCanvas.addEventHandler(MouseEvent.MOUSE_CLICKED, this::handleCanvasClick);

        this.model.getRows().addListener((obs, oldVal, newVal) -> {
            this.model.reshapeDataGrip();
            this.model.resetDataGrid();
            redraw();
        });
        this.model.getCols().addListener((obs, oldVal, newVal) -> {
            this.model.reshapeDataGrip();
            this.model.resetDataGrid();
            redraw();
        });


        this.model.getLivePercent().addListener((obs, oldVal, newVal) -> {
            this.model.resetDataGrid();
            redraw();
        });
        this.model.getEpochCount().addListener((obs, oldVal, newVal) -> redraw());
        this.model.getGridData().addListener((obs, oldVal, newVal) -> redraw());

        this.model.getGameInputType().addListener((obs, oldVal, newVal) -> {
            if (newVal == InputType.MANUAL) { this.model.resetDataToZero(); }
            else {
                this.model.resetDataGrid();
                redraw();
            }
        });

        this.gameCanvas.widthProperty().addListener((obs, oldVal, newVal) -> {
            this.model.reshapeDataGrip();
            this.model.resetDataGrid();
            redraw();
        });
        this.gameCanvas.heightProperty().addListener((obs, oldVal, newVal) -> {
            this.model.reshapeDataGrip();
            this.model.resetDataGrid();
            redraw();
        });

        this.configurationPanel.getRandomiseButton().setOnAction(e -> {
            this.model.resetDataGrid();
            redraw();
        });

        redraw();
        return canvasContainer;
    }

    /**
     * Function to make VBox object with he simple statisticks through the binding to make dynamic
     * @return Vbox object
     */
    private VBox createTopStatsBox(){
        VBox statsBox = new VBox(10);
        statsBox.setPadding(new Insets(10));
        statsBox.setSpacing(10);
        statsBox.setAlignment(Pos.CENTER_LEFT);
        statsBox.setStyle("-fx-background-color: #444; -fx-text-fill: white; ");

        Label epochLabel = UiComponentFactory.createLabel("Epoch: 0", 14);
        Label liveDeadCount = UiComponentFactory.createLabel("Live Count: 0", 14);

        epochLabel.setStyle("-fx-text-fill: white;");
        liveDeadCount.setStyle("-fx-text-fill: white;");

        statsBox.getChildren().addAll(epochLabel, liveDeadCount);

        epochLabel.textProperty().bind(Bindings.format("Epoch: %d", this.model.getEpochCount()));
        liveDeadCount.textProperty().bind(Bindings.format("Live Count: %d", this.model.getLiveCount()));

        return statsBox;
    }

    /**
     * The function to handle the manual grid click
     * @param event - the click itself
     */
    private void handleCanvasClick(MouseEvent event){
        if (this.model.getGameStatus().get() != GameStatus.STOPED ||
                this.model.getGameInputType().get() != InputType.MANUAL
            ) { return; }

        double mouseX =  event.getX();
        double mouseY = event.getY();

        double clickXInGrid = mouseX - this.gridMetrics.offsetX();
        double clickYInGrid = mouseY - this.gridMetrics.offsetY();

        if (clickXInGrid >= 0 && clickYInGrid < this.gridMetrics.totalGridWidth() &&
            clickYInGrid >= 0 && clickXInGrid < this.gridMetrics.totalGridHeight()) {

            int clickCol = (int) (clickXInGrid / this.gridMetrics.cellSize());
            int clickRow = (int) (clickYInGrid / this.gridMetrics.cellSize());

            if (clickRow < this.gridMetrics.rows() && clickRow >= 0 &&
                    clickCol < this.gridMetrics.cols() &&  clickCol >= 0) {

                this.model.toggleDataGridCoordinate(clickRow, clickCol);
            }
        }

        redraw();
    }

    /**
     * The main "Magic" of the dynamic grid creation and update. Is used to make the UI actually do its work
     */
    private void redraw(){
        // generate when changing the size of the grid

        if (this.model.getEpochCountPrimitive() == 0) {
            // the reset of the basic metrics calculation
            this.gridMetrics = GridDrawingMetrics.calculate(
                    this.model.getRowsPrimitive(),
                    this.model.getColsPrimitive(),
                    this.gameCanvas.getWidth(),
                    this.gameCanvas.getHeight());
        }

        // basically, we delete the grid and re-draw each time
        this.gc.clearRect(0, 0, this.gameCanvas.getWidth(), this.gameCanvas.getHeight());

        for (int r = 0; r < this.gridMetrics.rows(); r++) {
            for (int c = 0; c < this.gridMetrics.cols(); c++) {
                // here the live dead logic will be implemented, for tests we make just white

                if (this.model.getDataGridCoordinate(r, c) == 1) {
                    this.gc.setFill(Color.BLACK);
                } else { this.gc.setFill(Color.WHITE); }

                this.gc.fillRect(
                        this.gridMetrics.offsetX() + c * this.gridMetrics.cellSize(),
                        this.gridMetrics.offsetY() + r * this.gridMetrics.cellSize(),
                        this.gridMetrics.cellSize(),
                        this.gridMetrics.cellSize());
            }
        }

        // optional grid, for now mandatory to see that code works (due to only white cells)
        this.gc.setStroke(Color.GRAY);
        this.gc.setLineWidth(0.5);
        for (int r = 0; r <= this.gridMetrics.rows(); r++) {
            this.gc.strokeLine(
                    this.gridMetrics.offsetX(),
                    this.gridMetrics.offsetY() + r * this.gridMetrics.cellSize(),
                    this.gridMetrics.offsetX() + this.gridMetrics.totalGridWidth(),
                    this.gridMetrics.offsetY() + r * this.gridMetrics.cellSize());
        }
        for (int c = 0; c <= this.gridMetrics.cols(); c++) {
            this.gc.strokeLine(
                    this.gridMetrics.offsetX() + c * this.gridMetrics.cellSize(),
                    this.gridMetrics.offsetY(),
                    this.gridMetrics.offsetX() + c * this.gridMetrics.cellSize(),
                    this.gridMetrics.offsetY() + this.gridMetrics.totalGridHeight());
        }


    }
}
