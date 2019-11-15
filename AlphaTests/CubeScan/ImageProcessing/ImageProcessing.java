package AlphaTests.CubeScan.ImageProcessing;

import AlphaTests.CubeScan.Models.CubeScanModel;
import org.opencv.core.*;
import org.opencv.features2d.FeatureDetector;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import java.util.*;

import static org.opencv.imgproc.Imgproc.COLOR_HSV2RGB;
import static org.opencv.imgproc.Imgproc.initUndistortRectifyMap;
import static org.opencv.imgproc.Imgproc.rectangle;

public class ImageProcessing implements Observer {

    private CubeScanModel model;

    private double meanColorRect = 60; //TODO Ins Model, meanColorRect % 2 != 0


    private void readColorsFromGrid() {
        Mat frameOfWebcamStream = model.getOriginalImage().clone();
        Scalar[][] colors = new Scalar[3][3];
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                Point searchPoint = model.getSearchPointGrid()[x][y];
                if (model.isUseMeanColor()) {
                    Mat mask = new Mat(frameOfWebcamStream.height(), frameOfWebcamStream.width(), CvType.CV_8U, new Scalar(0));

                    rectangle(mask, new Point(searchPoint.x - meanColorRect / 2, searchPoint.y - meanColorRect / 2), new Point(searchPoint.x + meanColorRect / 2, searchPoint.y + meanColorRect / 2), new Scalar(255), Core.FILLED);
                    colors[x][y] = Core.mean(frameOfWebcamStream, mask);

                    //TODO aus irgend einem Grund kommt beim Mittelstein (Beim GAN Cube) Blau raus
                    if (x == 0 && y == 1) {
                        /*
                        Scalar scalar = Core.mean(frameOfWebcamStream, mask);
                        System.out.print("color: " + scalar.val[0] * 2 + ", ");
                        System.out.print(scalar.val[1] / 2.55 + ", ");
                        System.out.println(scalar.val[2] / 2.55);
                        Imgcodecs.imwrite("orgImage.jpg", frameOfWebcamStream);
                        */
                    }

                } else {
                    colors[x][y] = new Scalar(
                            frameOfWebcamStream.get((int)searchPoint.y, (int)searchPoint.x)[0],
                            frameOfWebcamStream.get((int)searchPoint.y, (int)searchPoint.x)[1],
                            frameOfWebcamStream.get((int)searchPoint.y, (int)searchPoint.x)[2]
                    );
                }
            }
        }
        model.setGridColors(colors);
    }

    private void checkForCube() {
        Mat frameOfWebcamStream = model.getOriginalImage();
        Mat[][] binaryMatArray = new Mat[3][3];
        Mat[][] blobMatArray = new Mat[3][3];
        MatOfKeyPoint keypoints = new MatOfKeyPoint();
        List<KeyPoint>[][] totalBlobList = new List[3][3];
        FeatureDetector detector = FeatureDetector.create(FeatureDetector.SIMPLEBLOB);
        detector.read("src/AlphaTests/CubeScan/Resources/SavedData/blobdetectorparams1.xml");

        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {

                List<KeyPoint> blobList;

                //Apply mask to image
                Mat mask = new Mat(frameOfWebcamStream.size(), CvType.CV_8U, new Scalar(0));
                Imgproc.rectangle(mask, model.getFrameOrigin(), new Point(model.getFrameOrigin().x + model.getSearchFrameSize(), model.getFrameOrigin().y + model.getSearchFrameSize()), new Scalar(255), Core.FILLED);
                Mat processedFrame = new Mat(frameOfWebcamStream.size(), CvType.CV_8U, new Scalar(0));
                Imgproc.cvtColor(processedFrame, processedFrame, Imgproc.COLOR_GRAY2BGR);
                frameOfWebcamStream.copyTo(processedFrame, mask);

                //TODO Blur-Filter dauern lange
                //TODO Für jedes Bild ein Rangeslider-paar
                //Image Operations
                if (model.getGaBl() != 0)
                    Imgproc.GaussianBlur(processedFrame, processedFrame, new Size(model.getGaBl(), model.getGaBl()), model.getGaBl(), model.getGaBl());
                if (model.getMeBl() != 0) Imgproc.medianBlur(processedFrame, processedFrame, model.getMeBl());

                //TODO Range Slider Werte
                //new Scalar(model.getLoHu(), model.getLoSa(), model.getLoVa()),
                //new Scalar(model.getHiHu(), model.getHiSa(), model.getHiVa()), processedFrame);

                double[] color = model.getGridColors()[x][y].val;

                int rt = 5;
                Imgproc.cvtColor(processedFrame, processedFrame, Imgproc.COLOR_HSV2BGR);
                Imgcodecs.imwrite("test.jpg", processedFrame);
                Imgproc.cvtColor(processedFrame, processedFrame, Imgproc.COLOR_BGR2HSV);

                if (color[0] - rt < 0) {
                    Mat lowerRedMat = new Mat(), upperRedMat = new Mat();
                    Core.inRange(processedFrame, new Scalar(0, color[1] - 50, color[2] - 50), new Scalar(color[0] + rt, color[1] + 50, color[2] + 50), lowerRedMat);
                    Core.inRange(processedFrame, new Scalar(179 + color[0] - rt, color[1] - 50, color[2] - 50), new Scalar(179, color[1] + 50, color[2] + 50), upperRedMat);
                    Core.add(lowerRedMat, upperRedMat, processedFrame);
                    if (x == 1 && y == 0) System.out.println("Low: " + 0 + "-" + Math.round(color[0] + rt) + ", High: " + Math.round(179 + color[0] - rt) + "-" + 179);
                }
                else if (color[0] + rt > 179) {
                    Mat lowerRedMat = new Mat(), upperRedMat = new Mat();
                    Core.inRange(processedFrame, new Scalar(0, color[1] - 50, color[2] - 50), new Scalar(color[0] + rt - 179, color[1] + 50, color[2] + 50), lowerRedMat);
                    Core.inRange(processedFrame, new Scalar(color[0] - rt, color[1] - 50, color[2] - 50), new Scalar(179, color[1] + 50, color[2] + 50), upperRedMat);
                    Core.add(lowerRedMat, upperRedMat, processedFrame);
                    if (x == 1 && y == 0) System.out.println("Low: " + rt + "-" + Math.round(color[0] + rt - 179) + ", High: " + Math.round(color[0] - rt) + "-" + 179);
                }
                else {
                    Core.inRange(processedFrame, new Scalar(color[0] - rt, color[1] - 50, color[2] - 50), new Scalar(color[0] + rt, color[1] + 50, color[2] + 50), processedFrame);
                    if (x == 1 && y == 0) System.out.println(Math.round(color[0] - rt) + " - " + Math.round(color[0] + rt));
                }

                /*
                //TODO  0, 150, 155/ 180, 244, 176
                double lowRed, highRed;
                if (color[0] - 5 < 0 || color[0] + 5 > 180) {
                    lowRed = 0;
                    highRed = 179;
                } else {
                    lowRed = color[0] - 8;
                    highRed = color[0] + 8;
                }
                Core.inRange(processedFrame, new Scalar(lowRed, color[1] - 50, color[2] - 50), new Scalar(highRed, color[1] + 50, color[2] + 50), processedFrame);
                */

                binaryMatArray[x][y] = processedFrame;

                //Detect Blobs
                detector.detect(processedFrame, keypoints);
                blobList = keypoints.toList();
                Mat blobMat = model.getOriginalImage().clone();
                for (KeyPoint foundBlob : blobList) Imgproc.circle(blobMat, foundBlob.pt, (int) foundBlob.size / 2, new Scalar(0, 0, 255), 1);
                Imgproc.cvtColor(blobMat, blobMat, Imgproc.COLOR_HSV2BGR);
                blobMatArray[x][y] = blobMat;

                totalBlobList[x][y] = blobList;
            }
        }

        int counter = 0;

        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                for (int i = 0; i < totalBlobList[x][y].size(); i++) {
                    Point BlobPosition = totalBlobList[x][y].get(i).pt;
                    Point girdPos = model.getSearchPointGrid()[x][y];

                    if (BlobPosition.x < girdPos.x - meanColorRect / 2) continue;
                    if (BlobPosition.x > girdPos.x + meanColorRect / 2) continue;
                    if (BlobPosition.y < girdPos.y - meanColorRect / 2) continue;
                    if (BlobPosition.y > girdPos.y + meanColorRect / 2) continue;
                    counter++;
                }
            }
        }

        if (counter > 7) {
            System.out.println("-------------------------------");
            System.out.println("Cube found: " + counter + " / 9");
            System.out.print("\n");
            for (int y = 0; y < 3; y++) {
                for (int x = 0; x < 3; x++) {
                    System.out.print(colorDetection(model.getGridColors()[x][y]));
                    if (x < 2) System.out.print(", ");
                }
                System.out.print("\n");
            }
            System.out.println("-------------------------------");
        }
        else System.out.println("No Cube found: " + counter + " / 9");

        model.setBinaryImages(binaryMatArray);
        model.setBlobImages(blobMatArray);
        model.updateImageViews();
    }

    private String colorDetection(Scalar color) {
        if (color.val[1] < 100 && color.val[2] > 160) return "WHITE";
        else if (color.val[0] < 5) return "RED";
        else if (color.val[0] < 18) return "ORANGE";
        else if (color.val[0] < 36) return "YELLOW";
        else if (color.val[0] < 100) return "GREEN";
        else if (color.val[0] < 128) return "BLUE";
        else if (color.val[0] < 181) return "RED";
        return "NO COLOR DETECTED";
    }

    @Override
    public void update(Observable o, Object arg) {
        switch ((String)arg) {
            case "processImages":
                checkForCube();
                break;
            case "readColorsFromGrid":
                readColorsFromGrid();
                break;
        }
    }

    public void initModel(CubeScanModel model) {
        this.model = model;
    }
}
