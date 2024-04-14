package hk.edu.polyu.eie3109.assignment;





import android.app.Service;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.*;

public class Panel extends SurfaceView implements SurfaceHolder.Callback {
    private Bitmap bmp;
    private GameThread thread;
    private int x=20;
    private int y=20;
    private MediaPlayer removeSound;
    private MediaPlayer spawnSound;
    private static int pettime;

    private static int unpet;

    private ArrayList<GraphicObject> graphics = new ArrayList<GraphicObject>();

    public Panel(Context context) {
        super(context);
        getHolder().addCallback(this);

        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);

        thread = new GameThread(getHolder(), this);
        setFocusable(true);
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        Coordinates coords;
        int x, y;
        canvas.drawColor(Color.BLACK);
        for(GraphicObject graphic: graphics) {
            coords = graphic.getCoordinates();
            x = coords.getX();
            y = coords.getY();
            canvas.drawBitmap(bmp, x, y, null);
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        synchronized (thread.getSurfaceHolder()) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                int x = (int) event.getX();
                int y = (int) event.getY();
                GraphicObject Remove = null;

                for (GraphicObject graphic : graphics) {
                    int width = graphic.getGraphic().getWidth();
                    int height = graphic.getGraphic().getHeight();
                    int xx = graphic.getCoordinates().getX();
                    int yy = graphic.getCoordinates().getY();
                    if (x >= xx && x <= xx + width && y >= yy && y <= yy + height) {
                        Remove = graphic;
                        break;
                    }
                }
                if (Remove != null) {
                    Uri uri = Uri.parse("android.resource://" + getContext().getPackageName() + "/" + R.raw.cat1);
                    try {
                        if (removeSound != null && removeSound.isPlaying()) {
                            removeSound.pause();
                        } else {
                            removeSound = new MediaPlayer();
                            removeSound.setDataSource(getContext(), uri);
                            removeSound.prepare();
                        }
                        removeSound.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    graphics.remove(Remove);
                    unpet++;
                    if(unpet%10==0) {
                        CharSequence pet = "Removed ";
                        CharSequence times = " Cats";
                        int duration = Toast.LENGTH_SHORT;
                        String text = pet + String.valueOf(unpet) + times;
                        Toast.makeText(getContext(), text, duration).show();
                        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
                    }

                } else {
                    Uri uri = Uri.parse("android.resource://" + getContext().getPackageName() + "/" + R.raw.cat2);
                    try {
                        if (spawnSound != null && spawnSound.isPlaying()) {
                            spawnSound.pause();
                        } else {
                            spawnSound = new MediaPlayer();
                            spawnSound.setDataSource(getContext(), uri);
                            spawnSound.prepare();
                        }
                        spawnSound.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    GraphicObject graphic = new GraphicObject(bmp);
                    int bmpW = graphic.getGraphic().getWidth();
                    int bmpH = graphic.getGraphic().getHeight();
                    graphic.getCoordinates().setX(x - bmpW/2);
                    graphic.getCoordinates().setY(y - bmpH/2);
                    graphics.add(graphic);
                    pettime++;
                    if(pettime>=100){
                        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcherk);
                        if(pettime==100){
                            int duration = Toast.LENGTH_LONG;
                            String text = "Long Live the KING CATs";
                            Toast.makeText(getContext(), text, duration).show();
                        }

                    }else if(pettime%10==0) {
                        CharSequence pet = "Petted ";
                        CharSequence times = " times";
                        int duration = Toast.LENGTH_SHORT;
                        String text = pet + String.valueOf(pettime) + times;
                        Toast.makeText(getContext(), text, duration).show();
                        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcherx);
                    }else{
                        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
                    }
                    if(pettime==25) {
                        String xm = "Merry Christmas";
                        int duration = Toast.LENGTH_SHORT;
                        Toast.makeText(getContext(), xm, duration).show();
                        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcherx);
                    }

                }
            }
            return true;
        }
    }



    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread.setRunning(true);
        thread.start();
    }


    public void updateMovement() {
        Coordinates coord;
        Movement movement;
        int x, y;
        for(GraphicObject graphic: graphics) {
            coord = graphic.getCoordinates();
            movement = graphic.getMovement();
            x = (movement.getXDirection()==Movement.X_DIRECTION_RIGHT)?coord.getX()+movement.getXSpeed():coord.getX()-movement.getXSpeed();
//check x if reaches border
            if(x < 0) {
                movement.toggleXDirection();
                coord.setX(-x);
            } else if(x + graphic.getGraphic().getWidth() > getWidth()) {
                movement.toggleXDirection();
                coord.setX(x + getWidth() - (x+graphic.getGraphic().getWidth()));
            } else {
                coord.setX(x);
            }
            y =
                    (movement.getYDirection()==Movement.Y_DIRECTION_DOWN)?coord.getY()+movement.getYSpeed():coord.getY()-movement.getYSpeed();
//check y if reaches border
            if(y < 0) {
                movement.toggleYDirection();
                coord.setY(-y);
            } else if(y + graphic.getGraphic().getHeight() > getHeight()) {
                movement.toggleYDirection();
                coord.setY(y + getHeight() - (y+graphic.getGraphic().getHeight()));
            } else {
                coord.setY(y);
            }
        }
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        thread.setRunning(false);
        while(retry) {
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
                //keep trying here
            }
        }
    }
}
