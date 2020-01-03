package AlphaTests.CubeLogic;

import java.util.ArrayList;
import java.util.List;

public class Cube {
    private List<int[][]> cornerCubelets;
    private List<int[][]> edgeCubelets;
    private List<int[][]> middleCubelets;
    private final int x_AXIS = 0;
    private final int y_AXIS = 1;
    private final int z_AXIS = 2;

    public Cube() {
        this.cornerCubelets = new ArrayList<>();
        this.edgeCubelets = new ArrayList<>();
        this.middleCubelets = new ArrayList<>();
    }

    public int[][] addCubelets(int[][] cornerCubelets, int[][] edgeCubelets, int[][] middleCubelets, int[][] cubelet) {
        for ()

    }

    private rotateSide(int axis, int depth, int angle) {
        if (depth == -1) {
            angle = -angle;
        }

        for (int i = 0; i < cornerCubelets.size(); i++) {
            if (cornerCubelets.get(i)[0][axis] == depth) { //Wenn axis == depth ist, dann entsprechende Einträge rotieren
                rotateCornerCubelet(angle, axis, i);
            }
        }

        for (int i = 0; i < edgeCubelets.size(); i++) {
            if (edgeCubelets.get(i)[0][axis] == depth) {
                rotateEdgeCubelet(angle, axis, i);
            }
        }

        for (int i = 0; i < middleCubelets.size(); i++) {
            if (middleCubelets.get(i)[0][axis] == depth) {
                rotateMiddleCubelet(angle, axis, i);
            }
        }
    }

    private int[][] rotateCornerCubelet(int angle, int axis, int index) {
        int[][] cTemp = cornerCubelets.get(index).clone();

        if (angle == -1) angle = 3;
        if (angle == -2) angle = 2;

        for (int j = 0; j < angle; j++) {
            for (int i = 0; i < cTemp[0][i]; i++) {

                if (axis == 0) {
                if (cTemp[0][1] == cTemp[0][2]) {
                        cTemp[0][2] = -cTemp[0][2];
                        int color = cTemp[1][2];
                        cTemp[1][2] = cTemp[1][1];
                        cTemp[1][1] = color;

                    } else {
                        cTemp[0][1] = -cTemp[0][1];
                        int color = cTemp[1][1];
                        cTemp[1][1] = cTemp[1][2];
                        cTemp[1][2] = color;
                    }
                } else if (axis == 1) {
                if (cTemp[0][0] == cTemp[0][2]) {
                        cTemp[0][0] = -cTemp[0][0];
                        int color = cTemp[1][2];
                        cTemp[1][2] = cTemp[1][0];
                        cTemp[1][0] = color;
                    } else {
                        cTemp[0][2] = -cTemp[0][2];
                        int color = cTemp[1][2];
                        cTemp[1][2] = cTemp[1][0];
                        cTemp[1][0] = color;
                    }
                } else {
                if (cTemp[0][0] == cTemp[0][1]) {
                        cTemp[0][1] = -cTemp[0][1];
                        int color = cTemp[1][1];
                        cTemp[1][1] = cTemp[1][0];
                        cTemp[1][1] = color;
                    } else {
                        cTemp[0][0] = -cTemp[0][0];
                        int color = cTemp[1][1];
                        cTemp[1][1] = cTemp[1][0];
                        cTemp[1][1] = color;
                    }
                }
            }
        }
        return cTemp;
    }

    private int[][] rotateEdgeCubelet(int angle, int axis, int index) {
        int[][] eTemp = edgeCubelets.get(index).clone();

        if (angle == -1) angle = 3;
        if (angle == -2) angle = 2;

        for (int j = 0; j < angle; j++) {
            for (int i = 0; i < eTemp[0][i]; i++) {

                if (axis == 0) {
                    if (eTemp[0][0] == 0) {
                        if (eTemp[0][1] == eTemp[0][2]) {
                            int color = eTemp[1][2];
                            eTemp[1][2] = eTemp[1][1];
                            eTemp[1][1] = color;
                            eTemp[0][1] = -eTemp[0][1];
                        } else if (eTemp[0][2] == 0) {
                        int color = eTemp[1][2];
                        eTemp[1][2] = eTemp[1][1];
                        eTemp[1][1] =  color;
                        eTemp[0][2] = -eTemp[0][2];
                        }
                    }
                } else if (axis == 1) {
                    if (eTemp[0][1] == 0) {
                        if (eTemp[0][0] == eTemp[0][2]) {
                            int color = eTemp[1][2];
                            eTemp[1][2] = eTemp[1][0];
                            eTemp[1][0] = color;
                            eTemp[0][0] = -eTemp[0][0];
                        } else if (eTemp[0][0] == 0) {
                            int color = eTemp[1][2];
                            eTemp[1][2] = eTemp[1][1];
                            eTemp[1][1] =  color;
                            eTemp[0][0] = -eTemp[0][0];
                        }
                    }
                } else if (axis == 2) {
                    if (eTemp[0][2] == 0) {
                        if (eTemp[0][0] == eTemp[0][1]) {
                            int color = eTemp[1][0];
                            eTemp[1][0] = eTemp[1][1];
                            eTemp[1][1] =  color;
                            eTemp[0][1] = -eTemp[0][1];
                        } else if (eTemp[0][1] == 0) {
                            int color = eTemp[1][1];
                            eTemp[1][1] = eTemp[1][0];
                            eTemp[1][0] = color;
                            eTemp[][] = -eTemp[][];
                        }
                    }
                        int color = eTemp[1][0];



                    }
                }
            }
        }
        return eTemp;
    }

    private int[][] rotateMiddleCubelet(int angle, int axis, int index) {
        int[][] mTemp = middleCubelets.get(index).clone();

        if (angle == -1) angle = 3;
        if (angle == -2) angle = 2;

        for (int j = 0; j < angle; j++) {
            for (int i = 0; i < mTemp[0][i]; i++) {

                if (axis == 0)  {
                    if (mTemp[0][2] == 0) {
                        int h = mTemp[0][1];
                        mTemp[0][1] = mTemp[0][2];
                        mTemp[0][2] =  h;
                        mTemp[0][1] = -mTemp[0][1];
                    }
                } else if (axis == 1) {
                    if (mTemp[0][2] == 0) {
                        int h = mTemp[0][0];
                        mTemp[0][0] = mTemp[0][2];
                        mTemp[0][2] =  h;
                        mTemp[0][0] = -mTemp[0][0];
                    }
                } else {
                    if (mTemp[0][1] == 0) {
                        int h = mTemp[0][0];
                        mTemp[0][0] = mTemp[0][1];
                        mTemp[0][1] =  h;
                        mTemp[0][0] = -mTemp[0][0];
                    }
                }
            }
            return mTemp;
        }
        //TODO: Methode um Seiten des Cubes zu rotieren

//         axis -> (0; 1; 2) > (x, y, z)
//         depth -> (-1; 0; 1) > (hintere Ebene; mittlere Ebene; vordere Ebene)
//         angle -> (-2; -1; 1; 2) > (-180°; -90°; 90°; 180°) (Clockwise +; Counter Clockwise -)


        /**
         *  Alle Steine einer Seite x auswählen, um diese dann um eine Achse a rotieren zu lassen.
         *  Z.b. alle Steine mit X=0 um 90° CW.
         *  Richtungsangabe erfolgt durch CW/CCW (Clockwise oder Counterclockwise)
         *
         *
         *  Ecksteine:
         *  90° Rotation um y-Achse: {x, y, z} => {z, y, x}
         *  90° Rotation um x-Achse: {x, y, z} => {x, z, y}
         *  90° Rotation um z-Achse: {x, y, z} => {y, x, z}
         *
         *  Beispiel:
         *  E00 = (X=-1, Y=1, Z=1), FK= (X=-1, BLAU) (Y=1, ROT) (Z=1, WEIß)
         *  TODO: 90° Rotation y-Achse CW
         *  E00 = (X=-1, Y=1, Z=-1), FK= (Z=-1, BLAU) (Y=1, ROT) (X=-1, WEIß)
         *  TODO: 90° Rotation y-Achse CW
         *  E00 = (X=1, Y=1, Z=-1), FK = (X=1, BLAU) (Y=1, ROT) (Z=-1, WEIß)
         *  TODO: 90° Rotation y-Achse CW
         *  E00 = (X=1, Y=1, Z=1), FK= (Z=1, BLAU) (Y=1, ROT) (X=1, WEIß)
         *  TODO: 90° Rotation y-Achse CW
         *  E00 = (X=-1, Y=1, Z=1), FK= (X=-1, BLAU) (Y=1, ROT) (Z=1, WEIß)
         *
         */

        private void CubeStatus(){

            //TODO: Methode zum Zwischenspeichern des Cube Zustandes beispielsweise in Form einer Arraylist

            /**
             *  Stand pro Veränderung speichern, also nach Veränderung speichern, damit auf dieser Basis weitere Änderungen
             *  gemacht werden können. Eventuell auch Loggen, für Debug Zwecke.
             */

        }

        private void CubeMapping(){

            //TODO: Methode um vorher geladene Daten auf den 3D Würfel zu mappen

            /**
             *  Mithilfe dieser Methode werden die Daten aus der Computervision
             *  korrekt auf die Cubelets gemapped.
             *
             *  Ausrichtung der Mittelsteine zu Beginn des Mappings:
             *      - 0 Weiß = -Y (unten)
             *      - 5 Gelb = Y
             *      - 4 Blau = -X
             *      - 1 Grün = X (links)
             *      - 2 Rot = -Z
             *      - 3 Orange Z (vorne)
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

        private void OutputToPP(){

            //TODO: Methode um Zustand des Cubes an Renderer auszugeben

        }


    }
