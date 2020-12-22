package cubiq.models;

import java.util.List;
import java.util.Observable;

public class CubeModel extends Observable {

    private List<int[]> edgePieces, cornerPieces;


    public List<int[]> getEdgePieces() {
        return edgePieces;
    }

    public void setEdgePieces(List<int[]> edgePieces) {
        this.edgePieces = edgePieces;
    }

    public List<int[]> getCornerPieces() {
        return cornerPieces;
    }

    public void setCornerPieces(List<int[]> cornerPieces) {
        this.cornerPieces = cornerPieces;
    }
}
