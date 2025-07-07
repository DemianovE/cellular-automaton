package org.automaton.controll.ui;

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
import org.automaton.controll.factories.UiComponentFactory;
import org.automaton.controll.model.GameConfigModel;

/**
 * Main function to make the UI component of game field
 * It makes all the actions to generate grid and statistics
 */
public class GameFieldPanel extends BorderPane {

    private GraphicsContext gc;
    private Canvas gameCanvas;

    private static final GameFieldPanel INSTANCE = new GameFieldPanel();

    private final GameConfigModel model = GameConfigModel.getInstance();

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

        this.model.getRows().addListener((obs, oldVal, newVal) -> redraw());
        this.model.getCols().addListener((obs, oldVal, newVal) -> redraw());
        this.model.getLivePercent().addListener((obs, oldVal, newVal) -> redraw());
        this.model.getEpochCount().addListener((obs, oldVal, newVal) -> redraw());

        this.gameCanvas.widthProperty().addListener((obs, oldVal, newVal) -> redraw());
        this.gameCanvas.heightProperty().addListener((obs, oldVal, newVal) -> redraw());

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
        Label liveDeadCount = UiComponentFactory.createLabel("Live/Dead Count: 0", 14);

        epochLabel.setStyle("-fx-text-fill: white;");
        liveDeadCount.setStyle("-fx-text-fill: white;");

        statsBox.getChildren().addAll(epochLabel, liveDeadCount);

        epochLabel.textProperty().bind(Bindings.format("Epoch: %d", this.model.getEpochCount()));

        return statsBox;
    }

    /**
     * The main "Magic" of the dynamic grid creation and update. Is used to make the UI actually do its work
     */
    private void redraw(){
        // generate when changing the size of the grid
        if (this.model.getEpochCountPrimitive() == 0) {
            this.model.reshapeDataGrip();
            this.model.resetDataGrid();
        }

        int rows = this.model.getRowsPrimitive();
        int cols = this.model.getColsPrimitive();

        // basically, we delete grid and re-draw each time
        this.gc.clearRect(0, 0, this.gameCanvas.getWidth(), this.gameCanvas.getHeight());

        double cellWidth = this.gameCanvas.getWidth() / cols;
        double cellHeight = this.gameCanvas.getHeight() / rows;

        // we make squares to fit into the grid. SO need to make with smaller from two options
        double cellSize = Math.min(cellWidth, cellHeight);

        double totalGridWidth = cellSize * cols;
        double totalGridHeight = cellSize * rows;

        // fancy way of making the grid be centered by making coordinate always considers the offset from the side
        double offsetX = (this.gameCanvas.getWidth() - totalGridWidth) / 2.0;
        double offsetY = (this.gameCanvas.getHeight() - totalGridHeight) / 2.0;


        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                // here the live dead logic will be implemented, for tests we make just white

                if (this.model.getDataGridCoordinate(r, c) == 1) {
                    this.gc.setFill(Color.BLACK);
                } else { this.gc.setFill(Color.WHITE); }

                this.gc.fillRect(offsetX + c * cellSize, offsetY + r * cellSize, cellSize, cellSize); // remember about offset, when calculation
            }
        }

        // optional grid, for now mandatory to see that code works (due to only white cells)
        this.gc.setStroke(Color.GRAY);
        this.gc.setLineWidth(0.5);
        for (int r = 0; r <= rows; r++) {
            this.gc.strokeLine(offsetX, offsetY + r * cellSize, offsetX + totalGridWidth, offsetY + r * cellSize);
        }
        for (int c = 0; c <= cols; c++) {
            this.gc.strokeLine(offsetX + c * cellSize, offsetY, offsetX + c * cellSize, offsetY + totalGridHeight);
        }


    }
}
