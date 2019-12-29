package AlphaTests.CubeLogic;

import javax.swing.*;

public class Cube {

    private void LoadCubeData() {

        //TODO: Methode zum Aufrufen der List für Cube-Daten

        /**
         *  Einmaliges aufrufen der Liste, zu Beginn des Durchlaufs.
         */
    }

    private void CubeMapping() {

        //TODO: Methode um vorher geladene Daten auf den 3D Würfel zu mappen

        /**
         *  Mithilfe dieser Methode werden die Daten aus der Computervision
         *  korrekt auf die Cubelets gemapped.
         *
         *  Ausrichtung der Mittelsteine zu Beginn des Mappings:
         *      - Weiß = -Y (unten)
         *      - Gelb = Y
         *      - Blau = -X
         *      - Grün = X (links)
         *      - Rot = -Z
         *      - Orange Z (vorne)
         *
         *  Belegung der Ecksteine, in Z-Reihenfolge von o.L. nach u.R., anschließend hintere Reihe genauso.
         *  Ebenfalls werde auch hier schon die Farbkoordinaten (FK) betrachtet.
         *      E00 = (X=1, Y=1, Z=1), FK= (X=1, BLAU) (Y=1, ROT) (Z=1, WEIß)
         *      E01 = (X=-1, Y=1, Z=1), FK= (X=-1, GRÜN) (Y= GELB) (Z=1, GELB)
         *      ...
         *      E07 = (X=1, Y=1, Z=-1), FK= (X=1, GRÜN) (Y=1, WEIß) (Z=-1, GRÜN)
         *
         *  TODO: Diese Belegung wird mittels CubeStatus gespeichert und nach Veränderung angepasst.
         */

    }

    private void CubeStatus() {

        //TODO: Methode zum Zwischenspeichern des Cube Zustandes beispielsweise in Form einer Arraylist

        /**
         *  Stand pro Veränderung speichern, also nach Veränderung speichern, damit auf dieser Basis weitere Änderungen
         *  gemacht werden können. Eventuell auch Loggen, für Debug Zwecke.
         */

    }

    private void RotateSides() {

        //TODO: Methode um Seiten des Cubes zu rotieren

        /**
         *  Alle Steine einer Seite x auswählen, um diese dann um eine Achse a rotieren zu lassen.
         *  Z.b. alle Steine mit X=0 um 90° CW.
         *  Richtungsangabe erfolgt durch CW/CCW (Clockwise oder Counterclockwise)
         */

    }

    private void OutputToPP() {

        //TODO: Methode um Zustand des Cubes an Renderer auszugeben

    }



    }

}
