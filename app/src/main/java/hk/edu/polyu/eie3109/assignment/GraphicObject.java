package hk.edu.polyu.eie3109.assignment;

import android.graphics.Bitmap;
import android.view.SurfaceHolder;

public class GraphicObject {
    private Bitmap bitmap;
    private Coordinates coordinates;
    private Movement movement;
    public GraphicObject (Bitmap bitmap) {
        this.bitmap = bitmap;
        this.coordinates = new Coordinates(bitmap);
        this.movement = new Movement();
    }
    public Bitmap getGraphic() {
        return bitmap;
    }
    public Coordinates getCoordinates() {
        return coordinates;
    }
    public Movement getMovement() {
        return movement;
    }

}

