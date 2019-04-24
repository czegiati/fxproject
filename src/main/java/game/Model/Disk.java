package game.Model;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;

public class Disk extends Circle {
    private static int diskRange=30;
    private boolean isDama=false;

    public Disk(double x,double y,Tile pos){
        this.setCenterX(x);
        this.setCenterY(y);
        this.setRadius(diskRange);
        this.setDisable(true);
    }

    public static int getDiskRange() {
        return diskRange;
    }

    public static void setDiskRange(int diskRange) {
        Disk.diskRange = diskRange;
    }

    public boolean isDama() {
        return isDama;
    }

    public void makeIntoDama() {
        isDama=true;
        this.setStrokeWidth(8);
        this.setStrokeType(StrokeType.INSIDE);
        if(this.getFill()== Color.BLACK)
        {
            this.setStroke(Color.rgb(40,40,40));
        }
         else
        {
            this.setStroke(Color.rgb(230,230,230));
        }
        }
}
