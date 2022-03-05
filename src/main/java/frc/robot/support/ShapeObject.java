package frc.robot.support;

import org.opencv.core.Scalar;

public class ShapeObject {

    double xPos;
    double yPos;
    Scalar HSVmin;
    Scalar HSVmax;
    Scalar Color;
    String type;

    public ShapeObject() {
        // set values for default constructor
        setType("Object");
        setColor(new Scalar(0, 0, 0));

    }

    public ShapeObject(String name) {

        setType("Object");

        if (name.equals("blue")) {

            // TODO: use "calibration mode" to find HSV min
            // and HSV max values

            this.setHSVmin(new Scalar(92, 0, 0));
            this.setHSVmax(new Scalar(124, 256, 256));

            // BGR value for Blue:
            setColor(new Scalar(255, 0, 0));

        }
        if (name.equals("green")) {

            // TODO: use "calibration mode" to find HSV min
            // and HSV max values

            this.setHSVmin(new Scalar(34, 50, 50));
            this.setHSVmax(new Scalar(80, 220, 200));

            // BGR value for Green:
            setColor(new Scalar(0, 255, 0));

        }
        if (name.equals("yellow")) {

            // TODO: use "calibration mode" to find HSV min
            // and HSV max values

            this.setHSVmin(new Scalar(20, 124, 123));
            this.setHSVmax(new Scalar(30, 256, 256));

            // BGR value for Yellow:
            setColor(new Scalar(0, 255, 255));

        }
        if (name.equals("red")) {

            // TODO: use "calibration mode" to find HSV min
            // and HSV max values

            this.setHSVmin(new Scalar(0, 200, 0));
            this.setHSVmax(new Scalar(19, 255, 255));

            // BGR value for Red:
            setColor(new Scalar(0, 0, 255));

        }
    }

    public void close() {}

    public double getXPos() {

        return this.xPos;

    }

    public void setXPos(double d) {

        this.xPos = d;

    }

    public double getYPos() {

        return this.yPos;

    }

    public void setYPos(double d) {

        this.yPos = d;

    }

    public Scalar getHSVmin() {

        return this.HSVmin;

    }

    public Scalar getHSVmax() {

        return this.HSVmax;
    }

    public void setHSVmin(Scalar min) {

        this.HSVmin = min;
    }

    public void setHSVmax(Scalar max) {

        this.HSVmax = max;
    }

    public void setColor(Scalar c) {
        this.Color = c;
    }

    public Scalar getColor() {
        return this.Color;
    }

    public String getType() {
        return type;
    }

    public void setType(String t) {
        type = t;
    }
}
