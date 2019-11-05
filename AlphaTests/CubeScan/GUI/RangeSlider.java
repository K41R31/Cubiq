package AlphaTests.CubeScan.GUI;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

public class RangeSlider extends AnchorPane {

    private double min, max;
    private DoubleProperty lowValue = new SimpleDoubleProperty(), highValue = new SimpleDoubleProperty();
    private double lowThumbX, highThumbX;
    private int width;
    private int colorFoundValue = 0;
    private Line highlightTrack;


    public RangeSlider() {
        this(0, 255, 20, 80, 170);
    }

    public RangeSlider(double min, double max, double lowValue, double highValue, int width) {
        this.min = min;
        this.max = max;
        this.lowValue.set(lowValue);
        this.highValue.set(highValue);
        this.width = width;
        initPane();
        buildRangeSlider();
    }

    private void buildRangeSlider() {
        Line backgorundTrack = new Line();
        backgorundTrack.setStartX(0);
        backgorundTrack.setEndX(getPrefWidth());
        backgorundTrack.setLayoutY(3.5);
        backgorundTrack.setStroke(Color.web("#595a5e"));

        highlightTrack = new Line();
        highlightTrack.setStartX(width / (max - min) * lowValue.doubleValue());
        highlightTrack.setEndX(width / (max - min) * highValue.doubleValue());
        highlightTrack.setLayoutY(3.5);
        highlightTrack.setStroke(Color.web("#f95e5c"));

        Line divider = new Line();
        divider.setStartX(((double)width / 2) - 1);
        divider.setEndX(((double)width / 2) + 1);
        divider.setLayoutY(3.5);
        divider.setStroke(Color.web("#e8eaf5"));
        divider.setVisible(false);

        Thumb lowThumb = new Thumb(lowValue.doubleValue());
        Thumb highThumb = new Thumb(highValue.doubleValue());
        lowThumb.isLowThumb();
        highThumb.isHighThumb();

        getChildren().addAll(backgorundTrack, highlightTrack, divider, lowThumb, highThumb);
    }

    private void initPane() {
        setMinWidth(USE_PREF_SIZE);
        setMinHeight(USE_PREF_SIZE);
        setPrefWidth(width);
        setPrefHeight(7);
        setMaxWidth(USE_PREF_SIZE);
        setMaxHeight(USE_PREF_SIZE);
    }

    private class Thumb extends Rectangle {
        Thumb(double value) {
            setArcWidth(0);
            setArcHeight(0);
            setFill(Color.web("#f95e5c"));
            setStrokeWidth(2);
            setStroke(Color.web("#1a1b1f"));
            setWidth(8);
            setHeight(7);
            setLayoutX(-6);
            setValue(value);
        }
        private void setValue(double value) {
            setX(Math.round(width / (max - min) * value));
        }
        private void isLowThumb() {
            lowThumbX = getX();
            setOnMouseDragged(mouseEvent -> {
                toFront();
                setX(Math.round(mouseEvent.getX()));
                if (getX() < 0) setX(0);
                else if (colorFoundValue != 0 && getX() > getColorFoundX()) setX(getColorFoundX());
                else if (getX() > highThumbX) setX(highThumbX - 1);
                lowThumbX = getX();
                lowValue.set(getX() / width * (max - min));
                highlightTrack.setStartX(getX());
            });
        }
        private void isHighThumb() {
            highThumbX = getX();
            setOnMouseDragged(mouseEvent -> {
                toFront();
                setX(Math.round(mouseEvent.getX()));
                if (getX() > width) setX(width);
                else if (colorFoundValue != 0 && getX() < getColorFoundX()) setX(getColorFoundX());
                else if (getX() < lowThumbX) setX(lowThumbX + 1);
                highThumbX = getX();
                highValue.set(getX() / width * (max - min));
                highlightTrack.setEndX(getX());
            });
        }
    }

    public double getLowValue() {
        return lowValue.get();
    }
    public void setLowValue(double value) {
        lowValue.set(value);
    }
    public DoubleProperty lowValueProperty() {
        return lowValue;
    }

    public double getHighValue() {
        return highValue.get();
    }
    public void setHighValue(double value) {
        highValue.set(value);
    }
    public DoubleProperty highValueProperty() {
        return highValue;
    }

    public int getColorFoundValue() {
        return this.colorFoundValue;
    }
    public int getColorFoundX() {
        return (int)Math.round(width / (max - min) * colorFoundValue);
    }
    public void setColorFoundValue(int colorFoundValue) {
        this.colorFoundValue = colorFoundValue;
    }
}
