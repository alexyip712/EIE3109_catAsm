package hk.edu.polyu.eie3109.assignment;

import android.graphics.Bitmap;

public class Coordinates {
    private int x = 100;
    private int y = 0;
    private Bitmap bitmap;
    public Coordinates(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
    public void setX(int x) {
        this.x = x;
    }
    public int getX() {
        return x;
    }
    public void setY(int y) {
        this.y = y;
    }
    public int getY() {
        return y;
    }
}
