package cubiq.renderer;

import de.hshl.obj.loader.OBJLoader;
import de.hshl.obj.loader.Resource;
import de.hshl.obj.loader.objects.Mesh;
import de.hshl.obj.loader.objects.Surface;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class ObjectLoader {
    OBJLoader loader = new OBJLoader();

    public void texture_loader(){
        loader.setLoadTextureCoordinates(true);
        loader.setLoadNormals(false);
        loader.setGenerateIndexedMeshes(true);
        Resource file = Resource.bundled(Paths.get("objects/tisch.obj"));
        try {
            loader.setPrintWarnings(false);
            Mesh mesh = loader.loadMesh(file);
            List<Surface> surfaces = loader.loadSurfaces(file);
            float[] vertices = mesh.getVertices();
            System.out.println("objLoader...\n\n");
            System.out.println(Arrays.toString(vertices));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}