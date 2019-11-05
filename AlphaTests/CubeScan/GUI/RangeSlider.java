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
    private int foundColor = 0;
    private Line highlightTrack;
    private Thumb lowThumb, highThumb;


    public RangeSlider(double min, double max, double lowValue, double highValue, int width, int foundColor) {
        this.min = min;
        this.max = max;
        this.lowValue.set(lowValue);
        this.highValue.set(highValue);
        this.width = width;
        this.foundColor = foundColor;
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
        divider.setStartX(((double)width / (max - min) * foundColor) - 1);
        divider.setEndX(((double)width / (max - min) * foundColor) + 1);
        divider.setLayoutY(3.5);
        divider.setStroke(Color.web("#e8eaf5"));

        lowThumb = new Thumb(lowValue.doubleValue());
        highThumb = new Thumb(highValue.doubleValue());
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
                else if (foundColor != 0 && getX() > getFoundColorX()) setX(getFoundColorX());
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
                else if (foundColor != 0 && getX() < getFoundColorX()) setX(getFoundColorX());
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
        highThumb.setValue(value);
    }
    public DoubleProperty highValueProperty() {
        return highValue;
    }

    public int getFoundColor() {
        return this.foundColor;
    }
    public int getFoundColorX() {
        return (int)Math.round(width / (max - min) * foundColor);
    }
    public void setFoundColor(int foundColor) {
        this.foundColor = foundColor;
    }
}
