package AlphaTests.CubeLogic;

import java.util.ArrayList;
import java.util.List;

public class Cube {
    private List<int[][]> cornerCubelets;
    private List<int[][]> edgeCubelets;
    private List<int[][]> middleCubelets;

    public Cube() {
        this.cornerCubelets = new ArrayList<>();
        this.edgeCubelets = new ArrayList<>();
        this.middleCubelets = new ArrayList<>();
    }

    public int[][] addCubelets(int[][] cornerCubelets, int[][] edgeCubelets, int[][] middleCubelets, int[][] cubelet) {
//       for (
        return null;
    }



    private void rotateSide(int axis, int depth, int angle) {
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
                            int h = eTemp[0][1];
                            eTemp[0][1] = eTemp[0][2];
                            eTemp[0][2] = h;
                            eTemp[0][2] = -eTemp[0][2];
                            int color = eTemp[1][2];
                            eTemp[1][2] = eTemp[1][1];
                            eTemp[1][1] =  color;
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
                            int h = eTemp[0][0];
                            eTemp[0][0] = eTemp[0][2];
                            eTemp[0][2] = h;
                            eTemp[0][0] = -eTemp[0][0];
                            int color = eTemp[1][2];
                            eTemp[1][2] = eTemp[1][0];
                            eTemp[1][0] =  color;
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
                            int h = eTemp[0][0];
                            eTemp[0][0] = eTemp[0][1];
                            eTemp[0][1] = h;
                            eTemp[0][1] = -eTemp[0][1];
                            int color = eTemp[1][1];
                            eTemp[1][1] = eTemp[1][0];
                            eTemp[1][0] = color;
                        }
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

                if (axis == 0) {
                    if (mTemp[0][2] == 0) {
                        int h = mTemp[0][1];
                        mTemp[0][1] = mTemp[0][2];
                        mTemp[0][2] = h;
                        mTemp[0][1] = -mTemp[0][1];
                        int color = mTemp[1][1];
                        mTemp[1][1] = mTemp[1][2];
                        mTemp[1][2] = color;
                    }
                } else if (axis == 1) {
                    if (mTemp[0][2] == 0) {
                        int h = mTemp[0][0];
                        mTemp[0][0] = mTemp[0][2];
                        mTemp[0][2] = h;
                        mTemp[0][0] = -mTemp[0][0];
                        int color = mTemp[1][0];
                        mTemp[1][0] = mTemp[1][2];
                        mTemp[1][2] = color;
                    }
                } else {
                    if (mTemp[0][1] == 0) {
                        int h = mTemp[0][0];
                        mTemp[0][0] = mTemp[0][1];
                        mTemp[0][1] = h;
                        mTemp[0][0] = -mTemp[0][0];
                        int color = mTemp[1][0];
                        mTemp[1][0] = mTemp[1][1];
                        mTemp[1][1] = color;
                    }
                }
            }
        }
        return mTemp;
    }
        //TODO: Methode um Seiten des Cubes zu rotieren

//         axis -> (0; 1; 2) > (x, y, z)
//         depth -> (-1; 0; 1) > (hintere Ebene; mittlere Ebene; vordere Ebene)
//         angle -> (-2; -1; 1; 2) > (-180°; -90°; 90°; 180°) (Clockwise +; Counter Clockwise -)

}

