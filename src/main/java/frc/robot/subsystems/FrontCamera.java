package frc.robot.subsystems;

import java.util.ArrayList;
import java.util.List;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.CvSink;
import edu.wpi.first.cscore.CvSource;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.support.ShapeObject;


// https://docs.wpilib.org/en/stable/docs/software/vision-processing/roborio/using-the-cameraserver-on-the-roborio.html
// http://www.java2s.com/example/java-api/org/opencv/imgproc/moments/get_m10-0-0.html
// https://github.com/akaifi/MultiObjectTrackingBasedOnColor
public class FrontCamera extends SubsystemBase {

    // max number of objects to be detected in frame
    final int MAX_NUM_OBJECTS = 50;
    // minimum and maximum object area
    final int MIN_OBJECT_AREA = 20 * 20;
    Mat cameraFeed;
    Mat threshold;
    Mat HSV;

    public FrontCamera() {}

    private void drawObject(ArrayList<ShapeObject> theObjects, Mat temp, List<MatOfPoint> contours,
            Mat hierarchy) {

        for (int i = 0; i < theObjects.size(); i++) {
            Imgproc.drawContours(this.cameraFeed, contours, i, theObjects.get(i).getColor(), 3, 8,
                    hierarchy);
            Imgproc.circle(this.cameraFeed,
                    new Point(theObjects.get(i).getXPos(), theObjects.get(i).getYPos()), 5,
                    theObjects.get(i).getColor());
            Imgproc.putText(this.cameraFeed,
                    Double.toString(theObjects.get(i).getXPos()) + " , "
                            + Double.toString(theObjects.get(i).getYPos()),
                    new Point(theObjects.get(i).getXPos(), theObjects.get(i).getYPos() + 20), 1, 1,
                    theObjects.get(i).getColor());
            Imgproc.putText(this.cameraFeed, theObjects.get(i).getType(),
                    new Point(theObjects.get(i).getXPos(), theObjects.get(i).getYPos() - 20), 1, 2,
                    theObjects.get(i).getColor());
        }
    }

    private void drawObject(ArrayList<ShapeObject> theObjects) {

        for (int i = 0; i < theObjects.size(); i++) {

            Imgproc.circle(this.cameraFeed,
                    new Point(theObjects.get(i).getXPos(), theObjects.get(i).getYPos()), 10,
                    new Scalar(0, 0, 255));
            Imgproc.putText(this.cameraFeed,
                    Double.toString(theObjects.get(i).getXPos()) + " , "
                            + Double.toString(theObjects.get(i).getYPos()),
                    new Point(theObjects.get(i).getXPos(), theObjects.get(i).getYPos() + 20), 1, 1,
                    new Scalar(0, 255, 0));
            Imgproc.putText(this.cameraFeed, theObjects.get(i).getType(),
                    new Point(theObjects.get(i).getXPos(), theObjects.get(i).getYPos() - 30), 1, 2,
                    theObjects.get(i).getColor());
        }
    }

    private void morphOps() {

        // create structuring element that will be used to "dilate" and "erode" image.
        // the element chosen here is a 3px by 3px rectangle
        Mat erodeElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3));
        // dilate with larger element so make sure object is nicely visible
        Mat dilateElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(8, 8));

        Imgproc.erode(this.threshold, this.threshold, erodeElement);
        Imgproc.erode(this.threshold, this.threshold, erodeElement);

        Imgproc.dilate(this.threshold, this.threshold, dilateElement);
        Imgproc.dilate(this.threshold, this.threshold, dilateElement);
    }


    private void trackFilteredObject() {
        ArrayList<ShapeObject> objects = new ArrayList<ShapeObject>();
        Mat temp = new Mat();
        this.threshold.copyTo(temp);
        // these two vectors needed for output of findContours
        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Mat hierarchy = new Mat();
        // find contours of filtered image using openCV findContours function
        Imgproc.findContours(temp, contours, hierarchy, Imgproc.RETR_CCOMP,
                Imgproc.CHAIN_APPROX_SIMPLE);
        // use moments method to find our filtered object
        boolean objectFound = false;
        if (contours.size() > 0) {
            int numObjects = contours.size();
            // if number of objects greater than MAX_NUM_OBJECTS we have a noisy filter
            if (numObjects < MAX_NUM_OBJECTS) {
                for (int index = 0; index >= 0; index = contours.size()) {
                    Moments moment = Imgproc.moments(contours.get(index));
                    double area = moment.m00;
                    // if the area is less than 20 px by 20px then it is probably just noise
                    // if the area is the same as the 3/2 of the image size, probably just a bad
                    // filter
                    // we only want the object with the largest area so we safe a reference area
                    // each
                    // iteration and compare it to the area in the next iteration.
                    if (area > MIN_OBJECT_AREA) {
                        ShapeObject object = new ShapeObject();

                        object.setXPos(moment.m10 / area);
                        object.setYPos(moment.m01 / area);

                        objects.add(object);

                        objectFound = true;

                    } else {
                        objectFound = false;
                    }
                }
                // let user know you found an object
                if (objectFound == true) {
                    // draw object location on screen
                    drawObject(objects);
                }
            } else {
                Imgproc.putText(this.cameraFeed, "TOO MUCH NOISE! ADJUST FILTER", new Point(0, 50),
                        1, 2, new Scalar(0, 0, 255), 2);
            }
        }
    }


    private void trackFilteredObject(ShapeObject theObject) {

        ArrayList<ShapeObject> objects = new ArrayList<ShapeObject>();
        Mat temp = new Mat();
        this.threshold.copyTo(temp);
        // these two vectors needed for output of findContours
        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Mat hierarchy = new Mat();
        // find contours of filtered image using openCV findContours function
        Imgproc.findContours(temp, contours, hierarchy, Imgproc.RETR_CCOMP,
                Imgproc.CHAIN_APPROX_SIMPLE);
        // use moments method to find our filtered object
        boolean objectFound = false;
        if (contours.size() > 0) {
            int numObjects = contours.size();
            // if number of objects greater than MAX_NUM_OBJECTS we have a noisy filter
            if (numObjects < MAX_NUM_OBJECTS) {
                for (int index = 0; index >= 0; index = contours.size()) {

                    Moments moment = Imgproc.moments(contours.get(index));
                    double area = moment.m00;

                    // if the area is less than 20 px by 20px then it is probably just noise
                    // if the area is the same as the 3/2 of the image size, probably just a bad
                    // filter
                    // we only want the object with the largest area so we safe a reference area
                    // each
                    // iteration and compare it to the area in the next iteration.
                    if (area > MIN_OBJECT_AREA) {

                        ShapeObject object = new ShapeObject();

                        object.setXPos(moment.m10 / area);
                        object.setYPos(moment.m01 / area);
                        object.setType(theObject.getType());
                        object.setColor(theObject.getColor());

                        objects.add(object);

                        objectFound = true;

                    } else {
                        objectFound = false;
                    }
                }
                // let user know you found an object
                if (objectFound == true) {
                    // draw object location on screen
                    drawObject(objects, temp, contours, hierarchy);
                }

            } else {
                Imgproc.putText(this.cameraFeed, "TOO MUCH NOISE! ADJUST FILTER", new Point(0, 50),
                        1, 2, new Scalar(0, 0, 255), 2);
            }
        }
    }

    public void detection() {

        UsbCamera camera = CameraServer.startAutomaticCapture();
        // Set the resolution
        camera.setResolution(640, 480);

        // Get a CvSink. This will capture Mats from the camera
        CvSink cvSink = CameraServer.getVideo();
        // Setup a CvSource. This will send images back to the Dashboard
        CvSource outputStream = CameraServer.putVideo("Rectangle", 640, 480);

        Mat mat = new Mat();

        while (true) {
            ShapeObject blue = new ShapeObject("blue");
            ShapeObject red = new ShapeObject("red");

            // first find blue objects
            Imgproc.cvtColor(this.cameraFeed, this.HSV, Imgproc.COLOR_BGR2HSV);
            Core.inRange(this.HSV, blue.getHSVmin(), blue.getHSVmax(), this.threshold);
            morphOps();
            trackFilteredObject(blue);
            // then reds
            Imgproc.cvtColor(this.cameraFeed, this.HSV, Imgproc.COLOR_BGR2HSV);
            Core.inRange(this.HSV, red.getHSVmin(), red.getHSVmax(), this.threshold);
            morphOps();
            trackFilteredObject(red);

            // Put a rectangle on the image
            Imgproc.rectangle(this.cameraFeed, new Point(100, 100), new Point(400, 400),
                    new Scalar(255, 255, 255), 5);
            // Give the output stream a new image to display
            outputStream.putFrame(this.cameraFeed);
        }
    }

}
