package AlphaTests;

import java.util.ArrayList;
import java.util.List;

public class ColorScheme {

    List<int[][]> sortedColors;

    public ColorScheme() {
        List<int[][]> unsortedColors = new ArrayList<>();
        List<int[][]> sortedColors = new ArrayList<>();
        unsortedColors.add(new int[][] {
                {1, 0, 0},
                {4, 2, 0},
                {4, 0, 5}
        });
        unsortedColors.add(new int[][] {
                {1, 4, 2},
                {1, 1, 1},
                {4, 5, 0}
        });
        unsortedColors.add(new int[][] {
                {1, 3, 5},
                {4, 3, 5},
                {0, 0, 5}
        });
        unsortedColors.add(new int[][] {
                {4, 5, 3},
                {2, 4, 2},
                {3, 1, 1}
        });
        unsortedColors.add(new int[][] {
                {3, 3, 0},
                {3, 5, 3},
                {5, 1, 2}
        });
        unsortedColors.add(new int[][] {
                {2, 2, 2},
                {4, 0, 5},
                {3, 2, 0}
        });

        sortedColors = sortSides(unsortedColors);
    }

    public int[] getEdge(int index) {

    }

    private List<int[][]> sortSides(List<int[][]> unsorted) {
        List<int[][]> sorted = new ArrayList<>();
        for (int[][] singleSide : unsorted) sorted.set(singleSide[1][1], singleSide);
        return sorted;
    }

    public int[][] get(int index) {
        return sortedColors.get(index);
    }
}
