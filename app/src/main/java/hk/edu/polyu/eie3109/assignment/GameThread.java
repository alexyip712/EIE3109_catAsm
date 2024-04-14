package hk.edu.polyu.eie3109.assignment;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class GameThread extends Thread{
    private SurfaceHolder surfaceHolder;
    private Panel panel;
    private boolean run = false;

    public GameThread(SurfaceHolder surfaceHolder, Panel panel) {
        this.surfaceHolder = surfaceHolder;
        this.panel = panel;
    }
    public void setRunning(boolean run) {
        this.run = run;
    }
    public SurfaceHolder getSurfaceHolder() {
        return surfaceHolder;
    }
    @Override
    public void run() {

        super.run();
        Canvas c;
        while (run) {
            c = null;
            try {
                c = surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    panel.onDraw(c);
                    panel.updateMovement();
                }
            } finally {
                if(c != null) {
                    surfaceHolder.unlockCanvasAndPost(c);
                }
            }
        }
    }
}
