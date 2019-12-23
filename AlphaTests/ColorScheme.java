package AlphaTests;

import java.util.ArrayList;
import java.util.List;

public class ColorScheme {

    List<int[][]> sortedColors;

    public ColorScheme() {
        List<int[][]> unsortedColors = new ArrayList<>();
        unsortedColors.add(new int[][] {
                {5, 2, 1},
                {0, 1, 2},
                {3, 2, 3}
        });
        unsortedColors.add(new int[][] {
                {0, 3, 0},
                {4, 0, 0},
                {4, 2, 5}
        });
        unsortedColors.add(new int[][] {
                {0, 5, 4},
                {0, 2, 1},
                {4, 4, 3}
        });
        unsortedColors.add(new int[][] {
                {2, 0, 0},
                {5, 5, 4},
                {5, 4, 1}
        });
        unsortedColors.add(new int[][] {
                {1, 5, 2},
                {5, 3, 1},
                {2, 1, 4}
        });
        unsortedColors.add(new int[][] {
                {5, 3, 3},
                {3, 4, 1},
                {2, 3, 1}
        });

        this.sortedColors = sortSides(unsortedColors);
    }

    /**
     * Gives the 3x1 edge of the given side
     * @param sideIndex side index between 0 and 5
     * @param edgeIndex edge index between 0 and 3
     * @return The edge, consisting of three colors
     */
    public int[] getEdge(int sideIndex, int edgeIndex) {
        switch (edgeIndex) {
            case 0:
                return new int[] {
                        sortedColors.get(sideIndex)[0][0],
                        sortedColors.get(sideIndex)[1][0],
                        sortedColors.get(sideIndex)[2][0]
                };
            case 1:
                return new int[] {
                        sortedColors.get(sideIndex)[2][0],
                        sortedColors.get(sideIndex)[2][1],
                        sortedColors.get(sideIndex)[2][2]
                };
            case 2:
                return new int[] {
                        sortedColors.get(sideIndex)[2][2],
                        sortedColors.get(sideIndex)[1][2],
                        sortedColors.get(sideIndex)[0][2]
                };
            case 3:
                return new int[] {
                        sortedColors.get(sideIndex)[0][2],
                        sortedColors.get(sideIndex)[0][1],
                        sortedColors.get(sideIndex)[0][0]
                };
        }
        return null;
    }

    private List<int[][]> sortSides(List<int[][]> unsorted) {
        List<int[][]> sorted = new ArrayList<>();
        for (int i = 0; i < 6; i++)
            sorted.add(new int[3][3]);
        for (int[][] singleSide : unsorted) sorted.set(singleSide[1][1], singleSide);
        return sorted;
    }

    public int[][] get(int index) {
        return sortedColors.get(index);
    }
}
