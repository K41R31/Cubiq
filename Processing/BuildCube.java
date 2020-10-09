package Processing;

import IO.DebugOutput;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BuildCube {

    private List<int[][]> sortedScheme;
    private List<int[][]> finalizedScheme;


    public BuildCube(List<int[][]> inputScheme) {
        sortedScheme = sortScheme(inputScheme);

        Combinations combinations = new Combinations();

        if (!colorsExistsNineTimes()) {
            System.err.println("WRONG COLOR SCHEME");
            return;
        }

        //TODO Seiten auf Achsensymmetrie überprüfen (zB Schachbrettmuster oder gelöste Seiten)

        // Second side
        // Seiten, die oben an die weiße Seite passen
        for (int side = 1; side < 5; side++) {
            for (int edge = 0; edge < 4; edge++) {

                int[] edge0 = getEdge(0, 0);
                int[] edge1 = getEdge(side, edge);

                if (edgesCouldBeNeighbours(edge0, edge1))
                    combinations.addNewCombination(side, edge);
            }
        }

        int step = 0;

        // Third side
        // Seiten, die an den Partner von weiß oben und rechts an die weiße Seite passen
        for (int i = 0; i < combinations.totalStepCombinations(step); i++) {

            int[] edge0 = getEdge(combinations.getSide(step, i, 0), nextEdgeCounterClockWise(combinations.getEdge(step, i, 0)));
            int[] edge1 = getEdge(0, 1);

            for (int side = 1; side < 5; side++) {
                if (side == combinations.getSide(step, i, 0)) continue;

                for (int edge = 0; edge < 4; edge++) {
                    int[] edge2 = getEdge(side, edge);
                    int[] edge3 = getEdge(side, nextEdgeClockWise(edge));

                    if (edgesCouldBeNeighbours(edge0, edge3) && edgesCouldBeNeighbours(edge1, edge2))
                        combinations.extendCombination(step, i, side, edge);
                }
            }
        }

        step = 1;

        // Fourth side
        for (int i = 0; i < combinations.totalStepCombinations(step); i++) {

            int[] edge0 = getEdge(combinations.getSide(step, i, 1), nextEdgeCounterClockWise(combinations.getEdge(step, i, 1)));
            int[] edge1 = getEdge(0, 2);

            // Alle Seiten bis auf Weiß, Gelb, die erste und die zweite Seite
            for (int side = 1; side < 5; side++) {
                if (side == combinations.getSide(step, i, 0) || side == combinations.getSide(step, i, 1)) continue;

                for (int edge = 0; edge < 4; edge++) {
                    int[] edge2 = getEdge(side, edge);
                    int[] edge3 = getEdge(side, nextEdgeClockWise(edge));

                    if (edgesCouldBeNeighbours(edge0, edge3) && edgesCouldBeNeighbours(edge1, edge2))
                        combinations.extendCombination(step, i, side, edge);
                }
            }
        }

        step = 2;

        // Fifth side
        for (int i = 0; i < combinations.totalStepCombinations(step); i++) {

            int[] edge0 = getEdge(combinations.getSide(step, i, 2), nextEdgeCounterClockWise(combinations.getEdge(step, i, 2)));
            int[] edge1 = getEdge(0, 3);
            int[] edge5 = getEdge(combinations.getSide(step, i, 0), nextEdgeClockWise(combinations.getEdge(step, i, 0)));

            // Alle Seiten bis auf Weiß, Gelb, die erste, die zweite und die dritte Seite
            for (int side = 1; side < 5; side++) {
                if (side == combinations.getSide(step, i, 0) || side == combinations.getSide(step, i, 1) || side == combinations.getSide(step, i, 2))
                    continue;

                for (int edge = 0; edge < 4; edge++) {
                    int[] edge2 = getEdge(side, edge);
                    int[] edge3 = getEdge(side, nextEdgeClockWise(edge));
                    int[] edge4 = getEdge(side, nextEdgeCounterClockWise(edge));

                    if (edgesCouldBeNeighbours(edge0, edge3) && edgesCouldBeNeighbours(edge1, edge2) && edgesCouldBeNeighbours(edge5, edge4))
                        combinations.extendCombination(step, i, side, edge);
                }
            }
        }

        step = 3;

        // Last side
        for (int i = 0; i < combinations.totalStepCombinations(step); i++) {
            for (int edgeOffset = 0; edgeOffset < 4; edgeOffset++) {
                for (int layer = 3, edge = edgeOffset; layer >= 0; layer--, edge++) {

                    if (edge == 4) edge = 0;

                    int[] edge0 = getEdge(combinations.getSide(step, i, layer), nextOppositeEdge(combinations.getEdge(step, i, layer)));
                    int[] edge1 = getEdge(5, edge);

                    if (!edgesCouldBeNeighbours(edge0, edge1)) break;
                    if (layer == 0) {
                        new DebugOutput().printSchemes(sortedScheme);
                        // TODO Test ob es nur eine mögliche Lösung ist
                        new DebugOutput().printSchemes(finalizeScheme(combinations.getCombination(step, i), edgeOffset));
                    }
                    // Es können hier noch mehrere Möglichkeiten bestehen, da Achsensymetrische Seiten (nur die Werte müssen übereinstimmen zB Blau mittig gegenüber von grün)
                    // von dem Algorithmus nicht erkannt werden können
                    // TODO Komplett achsensymetrische Seiten ignorieren, da die Rotation hier keinen Unterschied macht
                    // TODO Bei Farbwert achsensymetrischen Seiten nicht nur einzelne Seiten betrachten sondern Steine zusammenbauen und Kombinationsmöglichkeit bei doppelten Steinen aussortieren
                }
            }
        }
    }

    // TODO Macht noch nicht das was ich will
    private List<int[][]> finalizeScheme(List<int[]> finalCombination, int lastLayerRotation) {
        List<int[][]> scheme = new ArrayList<>();

        // Add the first side (always white)
        scheme.add(sortedScheme.get(0));

        // Add all sides between the white and yellow side
        for (int i = 0; i < 4; i++)
            scheme.add(rotateSideClockwise(sortedScheme.get(finalCombination.get(i)[0]), finalCombination.get(i)[1]));

        // Add the last side (always yellow)
        scheme.add(rotateSideClockwise(sortedScheme.get(5), lastLayerRotation));

        return scheme;
    }

    private int[][] rotateSideCounterclockwise(int[][] input, int steps) {
        int[][] output = new int[3][3];
        for (int x = 0; x < 3; x++)
            for (int y = 0; y < 3; y++)
                output[2-y][x] = input[x][y];
        return output;
    }

    private int[][] rotateSideClockwise(int[][] input, int steps) {
        int[][] output = new int[3][3];
        for (int x = 0; x < 3; x++)
            for (int y = 0; y < 3; y++)
                output[y][2-x] = input[x][y];
        return output;
    }

    private int[][] rotateSide180(int[][] input, int steps) {
        for (int row = 0; row < 3; row++) {
            for (int x = 0, y = 2; x < y; x++, y--) {
                // int temp = input[][];
            }
        }

        int[][] output = new int[3][3];
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                output[x][y] = input[x][y];
            }
        }
        return null;
    }

    /**
     * Tests if the edges could be neighbours.
     * The rules are:
     *  - No single cube part can have the same color more than once
     *  - No cube part can have colors tha are supposed to be on the
     *    opposite side of the cube (white-yellow, blue-green, red-orange)
     * @param edge0 The first edge, containing two corner and one edge pieces (3x1)
     * @param edge1 The second edge, containing two corner and one edge pieces (3x1)
     * @return True if the given 3x1 edges are possible neighbours
     */
    private boolean edgesCouldBeNeighbours(int[] edge0, int[] edge1) {
        for (int i = 0; i < 3; i++)
            if (edge0[i] == edge1[2 - i] || edge0[i] + edge1[2 - i] == 5)
                return false;
        return true;
    }

    /**
     * Returns the edge that is located next to the given edge index
     * @param input The index of the starting edge
     * @return The index of the next edge counter clock wise
     */
    private int nextEdgeCounterClockWise(int input) {
        if (input > 0) return input - 1;
        else return 3;
    }

    /**
     * Returns the edge that is located next to the given edge index
     * @param input The index of the starting edge
     * @return The index of the next edge clock wise
     */
    private int nextEdgeClockWise(int input) {
        if (input < 3) return input + 1;
        else return 0;
    }

    /**
     * Returns the edge that is located at the opposite edge
     * @param input The index of the starting edge
     * @return The index of the edge at the opposite side
     */
    private int nextOppositeEdge(int input) {
        if (input <= 1) return input + 2;
        else return input - 2;
    }

    private boolean colorsExistsNineTimes() {
        int[] colorCounter = new int[] {0, 0, 0, 0, 0, 0};
        for (int i = 0; i < 6; i++) {
            for (int y = 0; y < 3; y++) {
                for (int x = 0; x < 3; x++) {
                    switch(get(i)[x][y]) {
                        case 0:
                            colorCounter[0]++;
                            break;
                        case 1:
                            colorCounter[1]++;
                            break;
                        case 2:
                            colorCounter[2]++;
                            break;
                        case 3:
                            colorCounter[3]++;
                            break;
                        case 4:
                            colorCounter[4]++;
                            break;
                        case 5:
                            colorCounter[5]++;
                            break;
                    }
                }
            }
        }
        for (int i = 0; i < 6; i++) {
            if (colorCounter[i] != 9) return false;
        }
        return true;
    }

    /**
     * Returns the 3x1 edge of the given side
     *
     * @param sideIndex side index between 0 and 5
     * @param edgeIndex edge index between 0 and 3
     * @return The edge, consisting of three colors
     */
    int[] getEdge(int sideIndex, int edgeIndex) {
        switch (edgeIndex) {
            case 0:
                return new int[] {
                        sortedScheme.get(sideIndex)[0][0],
                        sortedScheme.get(sideIndex)[1][0],
                        sortedScheme.get(sideIndex)[2][0]
                }; case 1: return new int[] {
                        sortedScheme.get(sideIndex)[2][0],
                    sortedScheme.get(sideIndex)[2][1],
                    sortedScheme.get(sideIndex)[2][2]
                }; case 2: return new int[] {
                        sortedScheme.get(sideIndex)[2][2],
                    sortedScheme.get(sideIndex)[1][2],
                    sortedScheme.get(sideIndex)[0][2]
                }; case 3: return new int[] {
                        sortedScheme.get(sideIndex)[0][2],
                    sortedScheme.get(sideIndex)[0][1],
                    sortedScheme.get(sideIndex)[0][0]
                };
        }
        return null;
    }

    private List<int[][]> sortScheme(List<int[][]> unsorted) {
        List<int[][]> sorted = new ArrayList<>();
        for (int i = 0; i < 6; i++)
            sorted.add(new int[3][3]);
        for (int[][] singleSide : unsorted) sorted.set(singleSide[1][1], singleSide);
        return sorted;
    }

    private int[] mirrorEdge(int[] input) {
        int[] output = new int[3];
        for (int i = 0; i < 3; i++) {
            output[i] = input[2 - i];
        }
        return output;
    }

    private int[][] mirrorSide(int[][] side) {
        int[][] mirroredSide = new int[3][3];
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                if (x == 0) mirroredSide[x][y] = side[2][y];
                else if (x == 1) mirroredSide[x][y] = side[x][y];
                else mirroredSide[x][y] = side[0][y];
            }
        }
        return mirroredSide;
    }

    /**
     * Combination system class
     */
    private class Combinations extends ArrayList<ArrayList<ArrayList<int[]>>> {

        Combinations() {
            for (int i = 0; i < 5; i++)
                this.add(new ArrayList<>());
        }

        private List<int[]> getCombination(int step, int index) {
            return this.get(step).get(index);
        }

        private int getSide(int step, int index, int layer) {
            return this.get(step).get(index).get(layer)[0];
        }

        private int getEdge(int step, int index, int layer) {
            return this.get(step).get(index).get(layer)[1];
        }

        private void addNewCombination(int side, int edge) {
            ArrayList<int[]> combination = new ArrayList<>();
            combination.add(new int[]{side, edge});
            this.get(0).add(combination);
        }

        private void extendCombination(int step, int index, int side, int edge) {
            ArrayList<int[]> combination = new ArrayList<>(this.get(step).get(index));
            combination.add(new int[]{side, edge});
            this.get(step + 1).add(combination);
        }

        private int totalStepCombinations(int step) {
            return this.get(step).size();
        }
    }

    public int[][] get(int index) {
        return sortedScheme.get(index);
    }
}
