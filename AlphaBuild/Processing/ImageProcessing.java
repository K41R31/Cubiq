package AlphaBuild.Processing;

import AlphaBuild.Model.Model;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class ImageProcessing implements Observer {

    private Model model;

    @Override
    public void update(Observable o, Object arg) {
        switch ((String)arg) {
            case "image loaded":
                imageLoaded();
                break;
        }
    }

    public void initModel(Model model) {
        this.model = model;
    }

    public void imageLoaded(){
        Mat image = model.getImage();
        Imgproc.cvtColor(image, image, Imgproc.COLOR_BGR2HSV);      // von rgb in hsv umgewandelt
        List<Mat> splittedMat = new ArrayList<>();          //
        Core.split(image, splittedMat);         // farben werden nach hsv geteilt = (0) h: farbe, (1) s: sättigung, (2) v: helligkeit
        Mat greyImg = splittedMat.get(2);       // (2 / v von hsv also helligkeit) wenn nur Helligkeit gelesen wird, ist das bild grau/schwaz/weiß
        debugOutput(greyImg, "1_graustufe");
        Imgproc.GaussianBlur(greyImg, greyImg, new Size(15, 15), 0);    // bild wird blur, mit stärke 13
        debugOutput(greyImg, "2_blur");
        Imgproc.Canny(greyImg, greyImg, 5.0, 17.0);      // canny
        debugOutput(greyImg, "3_canny");
    }

    public void debugOutput(Mat img, String name){
        Imgcodecs.imwrite("src/AlphaBuild/Resources/Images/" + name + ".jpeg", img);  // das bild in das verzeichnis abspeichern, was mitgegeben wurde
    }
}