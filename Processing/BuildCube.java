package Processing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BuildCube {

    private List<int[][]> sortedScheme;

    /* TODO
        Symmetrische Seiten verursachen sehr viele Kombinationsmöglichkeiten
        Ein gelöster Würfel resultiert in unzähligen Möglichkeiten (8192)
    */
    public BuildCube(List<int[][]> inputScheme) {
        sortedScheme = sortScheme(inputScheme);

        List<int[]> possibleFirstCombinations = new ArrayList<>();
        List<int[]> possibleSecondCombinations = new ArrayList<>();
        List<int[]> possibleThirdCombinations = new ArrayList<>();
        List<int[]> possibleFourthCombinations = new ArrayList<>();

        if (!colorsExistsNineTimes()) {
            System.err.println("WRONG COLOR SCHEME");
            return;
        }

        // Seiten, die oben an die weiße Seite passen
        for (int sideIndex = 1; sideIndex < 5; sideIndex++)
            for (int edgeIndex = 0; edgeIndex < 4; edgeIndex++) {

                int[] edge0 = getEdge(0, 0);
                int[] edge1 = getEdge(sideIndex, edgeIndex);

                if (edgesCouldBeNeighbours(edge0, edge1))
                    possibleFirstCombinations.add(new int[] {sideIndex, edgeIndex});
            }

        // Seiten, die an den Partner von weiß oben und rechts an die weiße Seite passen
        for (int i = 0; i < possibleFirstCombinations.size(); i++) {

            int[] edge0 = getEdge(possibleFirstCombinations.get(i)[0], nextEdgeCounterClockWise(possibleFirstCombinations.get(i)[1]));
            int[] edge1 = getEdge(0, 1);

            for (int sideIndex = 1; sideIndex < 5; sideIndex++) {
                if (sideIndex == possibleFirstCombinations.get(i)[0]) continue;

                for (int edgeIndex = 0; edgeIndex < 4; edgeIndex++) {
                    int[] edge2 = getEdge(sideIndex, edgeIndex);
                    int[] edge3 = getEdge(sideIndex, nextEdgeClockWise(edgeIndex));

                    if (edgesCouldBeNeighbours(edge0, edge3) && edgesCouldBeNeighbours(edge1, edge2)) {
                        possibleSecondCombinations.add(new int[] {possibleFirstCombinations.get(i)[0], possibleFirstCombinations.get(i)[1], sideIndex, edgeIndex});
                    }
                }
            }
        }

        for (int i = 0; i < possibleSecondCombinations.size(); i++) {

            int[] edge0 = getEdge(possibleSecondCombinations.get(i)[2], nextEdgeCounterClockWise(possibleSecondCombinations.get(i)[3]));
            int[] edge1 = getEdge(0, 2);

            // Alle Seiten bis auf Weiß, Gelb, die erste und die zweite Seite
            for (int sideIndex = 1; sideIndex < 5; sideIndex++) {
                if (sideIndex == possibleSecondCombinations.get(i)[0] || sideIndex == possibleSecondCombinations.get(i)[2]) continue;

                for (int edgeIndex = 0; edgeIndex < 4; edgeIndex++) {
                    int[] edge2 = getEdge(sideIndex, edgeIndex);
                    int[] edge3 = getEdge(sideIndex, nextEdgeClockWise(edgeIndex));

                    if (edgesCouldBeNeighbours(edge0, edge3) && edgesCouldBeNeighbours(edge1, edge2)) {
                        possibleThirdCombinations.add(new int[] {possibleSecondCombinations.get(i)[0], possibleSecondCombinations.get(i)[1], possibleSecondCombinations.get(i)[2], possibleSecondCombinations.get(i)[3], sideIndex, edgeIndex});
                    }
                }
            }
        }

        // Fourth
        for (int i = 0; i < possibleThirdCombinations.size(); i++) {

            int[] edge0 = getEdge(possibleThirdCombinations.get(i)[4], nextEdgeCounterClockWise(possibleThirdCombinations.get(i)[5]));
            int[] edge1 = getEdge(0, 3);
            int[] edge5 = getEdge(possibleThirdCombinations.get(i)[0], nextEdgeClockWise(possibleThirdCombinations.get(i)[1]));

            // Alle Seiten bis auf Weiß, Gelb, die erste, die zweite und die dritte Seite
            for (int sideIndex = 1; sideIndex < 5; sideIndex++) {
                if (sideIndex == possibleThirdCombinations.get(i)[0] || sideIndex == possibleThirdCombinations.get(i)[2]|| sideIndex == possibleThirdCombinations.get(i)[4]) continue;

                for (int edgeIndex = 0; edgeIndex < 4; edgeIndex++) {
                    int[] edge2 = getEdge(sideIndex, edgeIndex);
                    int[] edge3 = getEdge(sideIndex, nextEdgeClockWise(edgeIndex));
                    int[] edge4 = getEdge(sideIndex, nextEdgeCounterClockWise(edgeIndex));

                    if (edgesCouldBeNeighbours(edge0, edge3) && edgesCouldBeNeighbours(edge1, edge2) && edgesCouldBeNeighbours(edge5, edge4)) {
                        possibleFourthCombinations.add(new int[] {possibleThirdCombinations.get(i)[0], possibleThirdCombinations.get(i)[1], possibleThirdCombinations.get(i)[2], possibleThirdCombinations.get(i)[3], possibleThirdCombinations.get(i)[4], possibleThirdCombinations.get(i)[5], sideIndex, edgeIndex});
                    }
                }
            }
        }

        // Last side

        System.out.println("Possible fourth combinations: " + possibleFourthCombinations.size());
        System.out.println(Arrays.toString(possibleFourthCombinations.get(0)));


        int counter = 0;
        for (int i = 0; i < possibleFourthCombinations.size(); i++) {
            int[] edge0;
            int[] edge1;

            for (int j = 0; j < 4; j++) {
                for (int ei = 3 - j, l = 3; l >= 0; l--) {
                    edge0 = getEdge(5, ei);
                    edge1 = getEdge(possibleFourthCombinations.get(i)[0], nextOppositeEdge(possibleFourthCombinations.get(i)[1]));
                    if (!edgesCouldBeNeighbours(edge0, edge1)) break;
                    if (l == 3) counter++;
                    if (ei == 0) ei = 3;
                }
            }
            // TODO Überprüfung ob jeder Stein ein mal vorhanden ist
            // Kantensteine
            for (int j = 0; j < 4; j++) {

            }
        }
        System.out.println("Possibilities found: " + counter);
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

    private List<int[][]> mirrorYellowSide(List<int[][]> scheme) {
        int[][] yellowSide = scheme.get(5);
        int[][] mirroredSide = new int[3][3];
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                if (x == 0) mirroredSide[x][y] = yellowSide[2][y];
                else if (x == 1) mirroredSide[x][y] = yellowSide[x][y];
                else mirroredSide[x][y] = yellowSide[0][y];
            }
        }
        scheme.set(5, mirroredSide);
        return scheme;
    }

    public int[][] get(int index) {
        return sortedScheme.get(index);
    }
}
