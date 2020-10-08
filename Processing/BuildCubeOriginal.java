package Processing;

import IO.DebugOutput;

import java.util.ArrayList;
import java.util.List;

public class BuildCubeOriginal {

    private List<int[][]> sortedScheme;
    private List<int[][]> finalizedSchme;


    public BuildCubeOriginal(List<int[][]> inputScheme) {
        sortedScheme = sortScheme(inputScheme);

        List<int[]> possibleFirstCombinations = new ArrayList<>();
        List<int[]> possibleSecondCombinations = new ArrayList<>();
        List<int[]> possibleThirdCombinations = new ArrayList<>();
        List<int[]> possibleFourthCombinations = new ArrayList<>();

        if (!colorsExistsNineTimes()) {
            System.err.println("WRONG COLOR SCHEME");
            return;
        }

        //TODO Seiten auf Achsensymmetrie überprüfen (zB Schachbrettmuster oder gelöste Seiten)

        // Seiten, die oben an die weiße Seite passen
        for (int sideIndex = 1; sideIndex < 5; sideIndex++) {
            for (int edgeIndex = 0; edgeIndex < 4; edgeIndex++) {

                int[] edge0 = getEdge(0, 0);
                int[] edge1 = getEdge(sideIndex, edgeIndex);

                if (edgesCouldBeNeighbours(edge0, edge1))
                    possibleFirstCombinations.add(new int[]{sideIndex, edgeIndex});
            }
        }

        System.out.println("first: " + possibleFirstCombinations.size());

        // Seiten, die an den Partner von weiß oben und rechts an die weiße Seite passen
        for (int[] possibleFirstCombination : possibleFirstCombinations) {

            int[] edge0 = getEdge(possibleFirstCombination[0], nextEdgeCounterClockWise(possibleFirstCombination[1]));
            int[] edge1 = getEdge(0, 1);

            for (int sideIndex = 1; sideIndex < 5; sideIndex++) {
                if (sideIndex == possibleFirstCombination[0]) continue;

                for (int edgeIndex = 0; edgeIndex < 4; edgeIndex++) {
                    int[] edge2 = getEdge(sideIndex, edgeIndex);
                    int[] edge3 = getEdge(sideIndex, nextEdgeClockWise(edgeIndex));

                    if (edgesCouldBeNeighbours(edge0, edge3) && edgesCouldBeNeighbours(edge1, edge2)) {
                        possibleSecondCombinations.add(new int[]{possibleFirstCombination[0], possibleFirstCombination[1], sideIndex, edgeIndex});
                    }
                }
            }
        }

        System.out.println("second: " + possibleSecondCombinations.size());

        for (int[] possibleSecondCombination : possibleSecondCombinations) {

            int[] edge0 = getEdge(possibleSecondCombination[2], nextEdgeCounterClockWise(possibleSecondCombination[3]));
            int[] edge1 = getEdge(0, 2);

            // Alle Seiten bis auf Weiß, Gelb, die erste und die zweite Seite
            for (int sideIndex = 1; sideIndex < 5; sideIndex++) {
                if (sideIndex == possibleSecondCombination[0] || sideIndex == possibleSecondCombination[2]) continue;

                for (int edgeIndex = 0; edgeIndex < 4; edgeIndex++) {
                    int[] edge2 = getEdge(sideIndex, edgeIndex);
                    int[] edge3 = getEdge(sideIndex, nextEdgeClockWise(edgeIndex));

                    if (edgesCouldBeNeighbours(edge0, edge3) && edgesCouldBeNeighbours(edge1, edge2)) {
                        possibleThirdCombinations.add(new int[]{possibleSecondCombination[0], possibleSecondCombination[1], possibleSecondCombination[2], possibleSecondCombination[3], sideIndex, edgeIndex});
                    }
                }
            }
        }

        System.out.println("third: " + possibleThirdCombinations.size());

        // Fourth
        for (int[] possibleThirdCombination : possibleThirdCombinations) {

            int[] edge0 = getEdge(possibleThirdCombination[4], nextEdgeCounterClockWise(possibleThirdCombination[5]));
            int[] edge1 = getEdge(0, 3);
            int[] edge5 = getEdge(possibleThirdCombination[0], nextEdgeClockWise(possibleThirdCombination[1]));

            // Alle Seiten bis auf Weiß, Gelb, die erste, die zweite und die dritte Seite
            for (int sideIndex = 1; sideIndex < 5; sideIndex++) {
                if (sideIndex == possibleThirdCombination[0] || sideIndex == possibleThirdCombination[2] || sideIndex == possibleThirdCombination[4])
                    continue;

                for (int edgeIndex = 0; edgeIndex < 4; edgeIndex++) {
                    int[] edge2 = getEdge(sideIndex, edgeIndex);
                    int[] edge3 = getEdge(sideIndex, nextEdgeClockWise(edgeIndex));
                    int[] edge4 = getEdge(sideIndex, nextEdgeCounterClockWise(edgeIndex));

                    if (edgesCouldBeNeighbours(edge0, edge3) && edgesCouldBeNeighbours(edge1, edge2) && edgesCouldBeNeighbours(edge5, edge4)) {
                        possibleFourthCombinations.add(new int[]{possibleThirdCombination[0], possibleThirdCombination[1], possibleThirdCombination[2], possibleThirdCombination[3], possibleThirdCombination[4], possibleThirdCombination[5], sideIndex, edgeIndex});
                    }
                }
            }
        }

        System.out.println("fourth: " + possibleFourthCombinations.size());

        // Last side
        for (int[] possibleFourthCombination: possibleFourthCombinations) {
            for (int i = 0, x = 0; i < 8; i = i + 2, x++) {

                int[] edge0 = getEdge(possibleFourthCombination[i], nextOppositeEdge(possibleFourthCombination[i+1]));
                int[] edge1 = getEdge(5, x);

                if (!edgesCouldBeNeighbours(edge0, edge1)) break;
                if (i == 6) System.out.println("Possible Solution");
                // Es können hier noch mehrere Möglichkeiten bestehen, da Achsensymetrische Seiten (nur die Werte müssen übereinstimmen zB Blau mittig gegenüber von grün)
                // von dem Algorithmus nicht erkannt werden können
                // TODO Komplett achsensymetrische Seiten ignorieren, da die Rotation hier keinen Unterschied macht
                // TODO Bei Farbwert achsensymetrischen Seiten nicht nur einzelne Seiten betrachten sondern Steine zusammenbauen und Möglichkeit bei Doppelten Steinen aussortieren

                if (x == 3) x = 0;
            }

            System.out.println("NEXT COMBINATION");
            /*
            for (int j = 0; j < 4; j++) {
                for (int ei = j, l = 0; l <= 6; l = l + 2, ei++) {
                    edge0 = getEdge(5, ei);
                    //edge0 = mirrorEdge(edge0);
                    edge1 = getEdge(possibleCombination[l], nextOppositeEdge(possibleCombination[l + 1]));
                    if (!edgesCouldBeNeighbours(edge0, edge1)) {
                        System.err.println("No solution found for side " + j);
                        break;
                    }
                    if (l == 6) {
                        System.out.println("Combination found");
                        if (finalizedSchme == null) finalizedSchme = finalizeScheme(possibleCombination, j);
                        else System.err.println("Can't build the cube -> More than one combination was found!");
                    }
                    if (ei == 3) ei = -1;
                }
            }
             */
        }
    }

    private List<int[][]> finalizeScheme(int[] finalCombination, int lastLayerOrientation) {
        List<int[][]> scheme = new ArrayList<>();
        scheme.add(sortedScheme.get(0));
        scheme.add(rotateClockwise(sortedScheme.get(finalCombination[0]), finalCombination[1]));
        scheme.add(rotateClockwise(sortedScheme.get(finalCombination[2]), finalCombination[3]));
        scheme.add(rotateClockwise(sortedScheme.get(finalCombination[4]), finalCombination[5]));
        scheme.add(rotateClockwise(sortedScheme.get(finalCombination[6]), finalCombination[7]));
        //scheme.add(mirrorSide()) // TODO Rotation, mirror und so
        return scheme;
    }

    private int[][] rotateClockwise(int[][] array, int steps) {
        int[][] newArray = new int[3][3];
        for (int i = 0; i < steps; i++) {
            for (int y = 0; y < 3; y++) {
                for (int x = 0; x < 3; x++) {
                    newArray[y][x] = array[2 - x][y];
                }
            }
        }
        return newArray;
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
     * Returnes the 3x1 edge of the given side
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

    public int[][] get(int index) {
        return sortedScheme.get(index);
    }
}
