package org.automaton.controll.factories;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Pair;
import org.automaton.controll.common.HasDisplayName;

import java.util.List;

/**
 * The Factory type class for generic UI components creation
 */
public class UiComponentFactory {

    private UiComponentFactory() {}

    /**
     * Function to create a spacer for the content, footer desperation
     * @return Region object pacer
     */
    public static Region createSpacer(){
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        return spacer;
    }

    /**
     * Function to make a box of a Label Slider combo
     * @param labelValue - the text passed to the label
     * @param minValue - minimum value of the slider
     * @param maxValue - maximum value of the slider
     * @param startValue - start value of the slider
     * @return the pair of the HBox object and slider data for binding
     */
    public static Pair<HBox, DoubleProperty> createSliderBox(String labelValue, int minValue, int maxValue, int startValue) {
        HBox sliderBox = new HBox(10);

        Label sliderLabel = UiComponentFactory.createLabel(labelValue, 14);

        Slider slider = createSlider(minValue, maxValue, startValue);

        sliderBox.getChildren().addAll(sliderLabel, slider);
        HBox.setHgrow(slider, Priority.ALWAYS);

        return new Pair<>(sliderBox, slider.valueProperty());
    }

    /**
     * Function to create a generic label
     * @param text - the text we want to see in label
     * @param textSize - the size of desired text
     * @return Label type object
     */
    public static Label createLabel(String text, int textSize) {
        Label label = new Label(text);
        label.setStyle("-fx-font-size: " + textSize + "px;");
        return label;
    }

    /**
     * The function to make the individual Slider
     * @param minValue - minimum value of the slider
     * @param maxValue - maximum value of the slider
     * @param startValue - start value of the slider
     * @return Slider object
     */
    public static Slider createSlider(int minValue, int maxValue, int startValue) {
        Slider slider = new Slider(minValue, maxValue, startValue);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setMajorTickUnit(25);
        slider.setBlockIncrement(10);

        return slider;
    }

    /**
     * The function to make a dynamic toggle box with label
     * @param labelText - the text of the desired label
     * @param labelSize - the size of the desired label
     * @param leftButtonValue - the actual value for the left button
     * @param leftButtonText - the text for the left button
     * @param rightButtonValue - the actual value for the right button
     * @param rightButtonText - the text for the right button
     * @param targetProperty - the param which store current value
     * @return HBox object
     * @param <T> - enum used in the toggle value saving
     */
    public static <T extends Enum<T>> HBox createToggleButtonsBox(String labelText, int labelSize, T leftButtonValue, String leftButtonText, T rightButtonValue, String rightButtonText, ObjectProperty<T> targetProperty) {
        ToggleButton leftButton = new ToggleButton(leftButtonText);
        ToggleButton rightButton = new ToggleButton(rightButtonText);

        ToggleGroup toggleGroup = new ToggleGroup();
        leftButton.setToggleGroup(toggleGroup);
        rightButton.setToggleGroup(toggleGroup);

        leftButton.getStyleClass().add("left-segment");
        rightButton.getStyleClass().add("right-segment");

        leftButton.setSelected(true);
        targetProperty.setValue(leftButtonValue);

        toggleGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle == rightButton) {
                targetProperty.set(rightButtonValue);
            } else {
                targetProperty.set(leftButtonValue);
            }
        });

        HBox buttonBox = new HBox(0);
        buttonBox.getStyleClass().add("segmented-button-group");
        buttonBox.getChildren().addAll(leftButton, rightButton);
        buttonBox.setAlignment(Pos.CENTER);

        Label label = createLabel(labelText, labelSize);
        HBox combinedBox = new HBox(10);
        combinedBox.getChildren().addAll(label, buttonBox);
        return combinedBox;
    }

    /**
     * The function to make the radio button select from the enum list
     * @param labelText - the text of the label
     * @param labelSize - the size of the label
     * @param enumValues - the list of the enums for the select
     * @param targetProperty - the target which will be saving the value selected
     * @return the HBox object
     * @param <T> the enum type, which will be passed
     */
    public static <T extends Enum<T>> HBox createRadioButtonBox(String labelText, int labelSize, List<T> enumValues, ObjectProperty<T> targetProperty){
        VBox radioButtonBox = new  VBox(10);
        ToggleGroup toggleGroup = new ToggleGroup();

        for (int i = 0; i < enumValues.size(); i++) {
            T enumValue = enumValues.get(i);
            RadioButton radioButton = new RadioButton();

            if (enumValue instanceof HasDisplayName) {
                radioButton.setText(((HasDisplayName) enumValue).getDisplayName());
            } else {
                radioButton.setText(enumValue.name());
            }

            radioButton.setToggleGroup(toggleGroup);
            radioButton.setUserData(enumValue);

            radioButtonBox.getChildren().add(radioButton);

            if (i == 0) {
                radioButton.setSelected(true);
                targetProperty.set(enumValue);
            }
        }

        toggleGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle != null) {
                RadioButton selectedRadioButton = (RadioButton) newToggle;
                @SuppressWarnings("unchecked")
                T selectedEnumValue = (T) selectedRadioButton.getUserData();
                targetProperty.set(selectedEnumValue);
            } else {
                targetProperty.set(null);
            }
        });

        HBox radioBox = new HBox(10);
        Label  label = createLabel(labelText, labelSize);
        radioBox.getChildren().addAll(label, radioButtonBox);
        return radioBox;
    }
}
