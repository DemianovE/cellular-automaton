package org.automaton.control.game;

/**
 * The record used ot store all data which is used to correctly draw and detect the grid based on the parameters.
 * Basically, this is encapsulation of the required calculations.
 * @param rows - the number of the rows in the grid
 * @param cols - the number of the columns in the grid
 * @param cellSize - the size of the cell (of the sides, as we have square)
 * @param totalGridWidth - the total width of the grid (smaller than canvas size, used for grid drawing zB.)
 * @param totalGridHeight - the total height of the grid
 * @param offsetX - the offset by X axis of the grid to place it in the center of canvas
 * @param offsetY - the offset by Y axis of the grid to place it in the center of canvas
 */
public record GridDrawingMetrics(
        int rows,
        int cols,
        double cellSize,
        double totalGridWidth,
        double totalGridHeight,
        double offsetX,
        double offsetY
) {

    /**
     * Function to make new GridDrawingMetrics record based on the current state of the grid
     * @param rows - number of the required rows
     * @param cols - number of the required columńs
     * @param availableGridWidth - the all available grid width of the current canvas
     * @param availableGridHeight - the all available grid height of the current canvas
     * @return new record of the GridDrawingMetrics with calculated grid constants for the drawing and manual click detection
     */
    public static GridDrawingMetrics calculate(int rows, int cols, double availableGridWidth, double availableGridHeight) {
        double effectiveCanvasSize = Math.min(availableGridWidth, availableGridHeight);

        double possibleCellWidth = effectiveCanvasSize / cols;
        double possibleCellHeight = effectiveCanvasSize / rows;

        double cellSize = Math.min(possibleCellWidth, possibleCellHeight);

        double totalGridWidth = cellSize * cols;
        double totalGridHeight = cellSize * rows;

        double offsetX = (availableGridWidth - totalGridWidth) / 2;
        double offsetY = (availableGridHeight - totalGridHeight) / 2;

        return new  GridDrawingMetrics(rows, cols, cellSize, totalGridWidth, totalGridHeight, offsetX, offsetY);
    }
}
