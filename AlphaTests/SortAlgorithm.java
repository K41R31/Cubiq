package AlphaTests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SortAlgorithm {

    public static void main(String[] args) {

        List<int[]> possibleFirstThings = new ArrayList<>();
        List<int[]> possibleSecondThings = new ArrayList<>();
        List<int[]> possibleThirdThings = new ArrayList<>();
        List<int[]> possibleFourthThings = new ArrayList<>();
        ColorScheme colorScheme = new ColorScheme();

        if (!colorsExistsNineTimes(colorScheme)) {
            System.err.println("WRONG COLOR SCHEME");
            return;
        }

        // Seiten, die oben an die weiße Seite passen
        for (int sideIndex = 1; sideIndex < 5; sideIndex++)
            for (int edgeIndex = 0; edgeIndex < 4; edgeIndex++) {

                int[] edge0 = colorScheme.getEdge(0, 0);
                int[] edge1 = colorScheme.getEdge(sideIndex, edgeIndex);

                if (edgesCouldBeNeighbours(edge0, edge1))
                    possibleFirstThings.add(new int[]{sideIndex, edgeIndex});
            }

        // Seiten, die an den Partner von weiß oben und rechts an die weiße Seite passen
        for (int i = 0; i < possibleFirstThings.size(); i++) {

            int[] edge0 = colorScheme.getEdge(possibleFirstThings.get(i)[0], nextEdgeCounterClockWise(possibleFirstThings.get(i)[1]));
            int[] edge1 = colorScheme.getEdge(0, 1);

            for (int sideIndex = 1; sideIndex < 5; sideIndex++) {
                if (sideIndex == possibleFirstThings.get(i)[0]) continue;

                for (int edgeIndex = 0; edgeIndex < 4; edgeIndex++) {
                    int[] edge2 = colorScheme.getEdge(sideIndex, edgeIndex);
                    int[] edge3 = colorScheme.getEdge(sideIndex, nextEdgeClockWise(edgeIndex));

                    if (edgesCouldBeNeighbours(edge0, edge3) && edgesCouldBeNeighbours(edge1, edge2)) {
                        possibleSecondThings.add(new int[] {possibleFirstThings.get(i)[0], possibleFirstThings.get(i)[1], sideIndex, edgeIndex});
                    }
                }
            }
        }

        for (int i = 0; i < possibleSecondThings.size(); i++) {

            int[] edge0 = colorScheme.getEdge(possibleSecondThings.get(i)[2], nextEdgeCounterClockWise(possibleSecondThings.get(i)[3]));
            int[] edge1 = colorScheme.getEdge(0, 2);

            // Alle Seiten bis auf Weiß, Gelb, die erste und die zweite Seite
            for (int sideIndex = 1; sideIndex < 5; sideIndex++) {
                if (sideIndex == possibleSecondThings.get(i)[0] || sideIndex == possibleSecondThings.get(i)[2]) continue;

                for (int edgeIndex = 0; edgeIndex < 4; edgeIndex++) {
                    int[] edge2 = colorScheme.getEdge(sideIndex, edgeIndex);
                    int[] edge3 = colorScheme.getEdge(sideIndex, nextEdgeClockWise(edgeIndex));

                    if (edgesCouldBeNeighbours(edge0, edge3) && edgesCouldBeNeighbours(edge1, edge2)) {
                        possibleThirdThings.add(new int[] {possibleSecondThings.get(i)[0], possibleSecondThings.get(i)[1], possibleSecondThings.get(i)[2], possibleSecondThings.get(i)[3], sideIndex, edgeIndex});
                    }
                }
            }
        }

        // Fourth
        for (int i = 0; i < possibleThirdThings.size(); i++) {

            int[] edge0 = colorScheme.getEdge(possibleThirdThings.get(i)[4], nextEdgeCounterClockWise(possibleThirdThings.get(i)[5]));
            int[] edge1 = colorScheme.getEdge(0, 3);
            int[] edge5 = colorScheme.getEdge(possibleThirdThings.get(i)[0], nextEdgeClockWise(possibleThirdThings.get(i)[1]));

            // Alle Seiten bis auf Weiß, Gelb, die erste, die zweite und die dritte Seite
            for (int sideIndex = 1; sideIndex < 5; sideIndex++) {
                if (sideIndex == possibleThirdThings.get(i)[0] || sideIndex == possibleThirdThings.get(i)[2]|| sideIndex == possibleThirdThings.get(i)[4]) continue;

                for (int edgeIndex = 0; edgeIndex < 4; edgeIndex++) {
                    int[] edge2 = colorScheme.getEdge(sideIndex, edgeIndex);
                    int[] edge3 = colorScheme.getEdge(sideIndex, nextEdgeClockWise(edgeIndex));
                    int[] edge4 = colorScheme.getEdge(sideIndex, nextEdgeCounterClockWise(edgeIndex));

                    if (edgesCouldBeNeighbours(edge0, edge3) && edgesCouldBeNeighbours(edge1, edge2) && edgesCouldBeNeighbours(edge5, edge4)) {
                        possibleFourthThings.add(new int[] {possibleThirdThings.get(i)[0], possibleThirdThings.get(i)[1], possibleThirdThings.get(i)[2], possibleThirdThings.get(i)[3], possibleThirdThings.get(i)[4], possibleThirdThings.get(i)[5], sideIndex, edgeIndex});
                    }
                }
            }
        }

        // Last side
        for (int[] possibleFourthThing : possibleFourthThings) {
            for (int j = 0; j < 4; j++) {
                for (int i = 0; i < 4; i++) {
                    int[] edge0 = colorScheme.getEdge(possibleFourthThing[i * 2], nextOppositeEdge(possibleFourthThing[i * 2 + 1]));
                    int edge1EdgeIndex = i;
                    for (int r = j; r > 0; r--)
                        edge1EdgeIndex = nextEdgeClockWise(edge1EdgeIndex);
                    int[] edge1 = colorScheme.getEdge(5, edge1EdgeIndex);
                    if (!edgesCouldBeNeighbours(edge0, edge1)) continue;
                    if (i == 3) System.out.println("COMBINATION FOUND");
                }
            }
        }

        System.out.println("First round: " + possibleFirstThings.size());
        int counter = 0;
        int[] sameComb = new int[] {0, 0};
        for (int[] possibleFourthThing : possibleFourthThings) {
            int side = possibleFourthThing[0];
            int edge = possibleFourthThing[1];
            if (side != sameComb[0] || edge != sameComb[1]) counter++;
            sameComb[0] = side;
            sameComb[1] = edge;
            
        }
        System.out.println("Fourth round: " + counter);

        for (int[] possibleFourthThing : possibleFourthThings) {
            System.out.println(Arrays.toString(possibleFourthThing));
        }
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
    private static boolean edgesCouldBeNeighbours(int[] edge0, int[] edge1) {
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
    private static int nextEdgeCounterClockWise(int input) {
        if (input > 0) return input - 1;
        else return 3;
    }

    /**
     * Returns the edge that is located next to the given edge index
     * @param input The index of the starting edge
     * @return The index of the next edge clock wise
     */
    private static int nextEdgeClockWise(int input) {
        if (input < 3) return input + 1;
        else return 0;
    }

    /**
     * Returns the edge that is located at the opposite edge
     * @param input The index of the starting edge
     * @return The index of the edge at the opposite side
     */
    private static int nextOppositeEdge(int input) {
        if (input <= 1) return input + 2;
        else return input - 2;
    }

    private static boolean colorsExistsNineTimes(ColorScheme colorScheme) {
        int[] colorCounter = new int[] {0, 0, 0, 0, 0, 0};
        for (int i = 0; i < 6; i++) {
            for (int y = 0; y < 3; y++) {
                for (int x = 0; x < 3; x++) {
                    switch(colorScheme.get(i)[x][y]) {
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
}
