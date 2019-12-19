package AlphaTests;

public class SortAlgorithm {

    public static void main(String[] args) {
        ColorScheme colorScheme = new ColorScheme();
        int[][] firstSide = colorScheme.get(0);

        int[] edge = firstSide.g
    }

    private static int[][] rotateClockwise(int[][] input) {
        int[][] output = new int[3][3];
        for (int y = 0; y < 3; ++y)
            for (int x = 0; x < 3; ++x)
                output[y][x] = input[2 - x][y];
        return output;
    }
}
