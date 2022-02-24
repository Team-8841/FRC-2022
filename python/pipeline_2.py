import cv2
import numpy as np

# global variables go here:
testVar = 0

# To change a global variable inside a function,
# re-declare it the global keyword


def incrementTestVar():
    global testVar
    testVar = testVar + 1
    if testVar == 100:
        print("test")
    if testVar >= 200:
        print("print")
        testVar = 0


def drawDecorations(image):
    cv2.putText(image,
                'Limelight python script!',
                (0, 230),
                cv2.FONT_HERSHEY_SIMPLEX,
                .5, (0, 255, 0), 1, cv2.LINE_AA)

# runPipeline() is called every frame by Limelight's backend.


def runPipeline(image, llrobot):
    frameHSV = cv2.cvtColor(image, cv2.COLOR_BGR2HSV)

    lowHue = 89
    lowSat = 0
    lowVal = 0
    highHue = 125
    highSat = 255
    highVal = 255

    colorLow = np.array([lowHue, lowSat, lowVal])
    colorHigh = np.array([highHue, highSat, highVal])

    # Show the first mask
    mask = cv2.inRange(frameHSV, colorLow, colorHigh)
    im2, contours, _ = cv2.findContours(
        mask, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)

    if len(contours) > 0:
        cv2.drawContours(image, contours, -1, 255, 2)
        largestContour = max(contours, key=cv2.contourArea)
        x, y, w, h = cv2.boundingRect(largestContour)

        cv2.rectangle(image, (x, y), (x+w, y+h), (0, 255, 255), 2)
        llpython = [1, x, y, w, h, 9, 8, 7]

    return contours, image, llpython
