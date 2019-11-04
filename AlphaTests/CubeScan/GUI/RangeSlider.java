package AlphaTests.CubeScan.GUI;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

public class RangeSlider extends AnchorPane {

    private double min, max;
    private DoubleProperty lowValue = new SimpleDoubleProperty(), highValue = new SimpleDoubleProperty();
    private double lowThumbX, highThumbX;
    private int width;
    private HighlightTrack highlightTrack;


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
        Line trackBackground = new Line();
        trackBackground.setStartX(0);
        trackBackground.setStroke(Color.web("#595a5e"));
        trackBackground.setLayoutY(4.5);
        trackBackground.setEndX(getPrefWidth());

        highlightTrack = new HighlightTrack();

        Thumb lowThumb = new Thumb(lowValue.doubleValue());
        Thumb highThumb = new Thumb(highValue.doubleValue());
        lowThumb.isLowThumb();
        highThumb.isHighThumb();

        getChildren().addAll(trackBackground, highlightTrack, lowThumb, highThumb);
    }

    private void initPane() {
        setMinWidth(USE_PREF_SIZE);
        setMinHeight(USE_PREF_SIZE);
        setPrefWidth(width);
        setPrefHeight(8);
        setMaxWidth(USE_PREF_SIZE);
        setMaxHeight(USE_PREF_SIZE);
    }

    private class HighlightTrack extends Line {
        HighlightTrack() {
            setStartX(width / (max - min) * lowValue.doubleValue());
            setStroke(Color.web("#f95e5c"));
            setLayoutY(4.5);
            setEndX(width / (max - min) * highValue.doubleValue());
        }
        private void setStart() {
            setStartX(lowThumbX);
        }
        private void setEnd() {
            setEndX(highThumbX);
        }
    }

    private class Thumb extends Rectangle {
        Thumb(double value) {
            setArcWidth(0);
            setArcHeight(0);
            setFill(Color.web("#f95e5c"));
            setStrokeWidth(2);
            setStroke(Color.web("#1a1b1f"));
            setWidth(9);
            setHeight(8);
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
                else if (getX() > highThumbX) setX(highThumbX - 1);
                else if (getX() > width) setX(width);
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
                else if (getX() < lowThumbX) setX(lowThumbX + 1);
                else if (getX() < 0) setX(0);
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
}
