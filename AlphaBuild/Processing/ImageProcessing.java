package AlphaBuild.Processing;

import AlphaBuild.Model.Model;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.lang.reflect.Array;
import java.util.*;

public class ImageProcessing implements Observer {

    private Model model;

    /**
     * Searches for a cube in a image
     */
    public void findCube() {
        // Get a image form the model and clone it
        Mat image = model.getOriginalImage().clone();

        // Skips the processing if no image was selected
        if (image == null) return;

        // Convert the image from brg to hsv
        Imgproc.cvtColor(image, image, Imgproc.COLOR_BGR2HSV);

        // Split the hvs image into three individual channels (h, s and v) and store them into a array list
        // -> 0 = hue (color); 1 = saturation; 2 = value (brightness)
        List<Mat> splittedMat = new ArrayList<>();
        Core.split(image, splittedMat);



        // Keep the image with the value channel (brightness map = grayscale image)
        Mat greyImg = splittedMat.get(2);
        debugOutput(greyImg, "1_greyscale");

        // Blur the image with the kernel size 15
        Imgproc.GaussianBlur(greyImg, greyImg, new Size(15, 15), 0);
        debugOutput(greyImg, "2_blur");

        // Apply the canny algorithm to the image (edge detection)
        Imgproc.Canny(greyImg, greyImg, 5.0, 17.0);
        debugOutput(greyImg, "3_canny");

        // sagt wie die linien dicker gemacht werden sollen
        Mat linesBigger = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3,3));
        Imgproc.dilate(greyImg, greyImg, linesBigger);  // linien werden dicker gemacht
        debugOutput(greyImg, "4_linienDicker");

        // alle linien werden in Liste gespeichert
        List<MatOfPoint> konturen = new ArrayList<>();
        Imgproc.findContours(greyImg, konturen, new Mat(), Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

        // es zeichnet die konturen auf ein neues bild in weiß
        Mat mat = new Mat(1080, 1920, CvType.CV_8UC3);
        for(int index = 0; index < konturen.size(); index++){
            Imgproc.drawContours(mat, konturen, index, new Scalar(255, 255, 255), 5);
        }
        debugOutput(mat, "5_konturen");

        // verbindet die linien die zueinander stehen und erkennt die annäherungen (baut 4 ecke die die linien annähernt entsprechen)
        List<MatOfPoint2f> annäherungen = new ArrayList<>();
        for(int index = 0; index < konturen.size(); index++){
            annäherungen.add(new MatOfPoint2f());
            Imgproc.approxPolyDP(new MatOfPoint2f(konturen.get(index).toArray()), annäherungen.get(index),
                    0.1 * Imgproc.arcLength(new MatOfPoint2f(konturen.get(index).toArray()),    // arcLength rechnet die Länge von einer Linie aus
                            true), true);       // true: schließt alle Linien bzw. Formen aus, die nicht geschlossen sind
        }

        // umwandelt MatOfPoint2f in MatOfPoint damit wir das anzeigen können (zeichen/ "Imgproc.drawContours(...)")
        List<MatOfPoint> umgewandelteAnnäherung = new ArrayList<>();
        for(int index = 0; index < annäherungen.size(); index++){
            MatOfPoint konturen_2 = new MatOfPoint();
            annäherungen.get(index).convertTo(konturen_2, CvType.CV_32S);
            umgewandelteAnnäherung.add(konturen_2);
        }
        Mat mat_2 = new Mat(1080, 1920, CvType.CV_8UC3);
        // es zeichnet die konturen auf ein neues bild in weiß
        for(int index = 0; index < umgewandelteAnnäherung.size(); index++){
            Imgproc.drawContours(mat_2, umgewandelteAnnäherung, index, new Scalar(255, 255, 255), 5);
        }
        debugOutput(mat_2, "6_annäherungen");

        System.out.println("annährungn: " + annäherungen.size());

        MatOfPoint2f viereck = new MatOfPoint2f();
        double größteFläche = 0;
        for(MatOfPoint2f annäherung : annäherungen){
            if(annäherung.rows() != 4){
                continue;
            }
            Point[] points = annäherung.toArray();

            double[] y_werte = {points[0].y, points[1].y, points[2].y, points[3].y};
            Arrays.sort(y_werte);
            double wert_1 = Math.abs(y_werte[0] - y_werte[1]);
            double wert_2 = Math.abs(y_werte[2] - y_werte[3]);
            if(wert_1 > 15 || wert_2 > 15){
                continue;
            }

            if(größteFläche < Math.abs(y_werte[0] - y_werte[3])){
                größteFläche = Math.abs(y_werte[0] - y_werte[3]);
                viereck = annäherung;
            }
        }



        Mat mat_3 = new Mat(1080, 1920, CvType.CV_8UC3);
        Point[] punkt = viereck.toArray();
        Imgproc.rectangle(mat_3, punkt[0], punkt[2], new Scalar(255, 255, 255), 5);



        debugOutput(mat_3, "8_eckenkontrolle");

        model.callObservers("cubeFound");
    }

    /**
     * Print an debug image
     * @param mat The image to be saved
     * @param name The name of the output
     */
    public void debugOutput(Mat mat, String name) {
        Imgcodecs.imwrite("src/AlphaBuild/Resources/Images/Processed/" + name + ".jpg", mat);
    }

    @Override
    public void update(Observable o, Object arg) {
        switch ((String)arg) {
            case "image loaded":
                findCube();
                break;
        }
    }

    public void initModel(Model model) {
        this.model = model;
    }
}