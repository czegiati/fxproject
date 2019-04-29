package game.Model;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;

/**
 * The graphical Checker pieces
 */
public class Disk extends Circle {
    private static int diskRange=30;
    private boolean isDama=false;

    /**
     * Constructor with parameters
     * @param x the circle's center's X coordinate
     * @param y the circle's center's Y coordinate
     * @param pos the tile, where the checker piece is situated in
     */
    public Disk(double x,double y,Tile pos){
        this.setCenterX(x);
        this.setCenterY(y);
        this.setRadius(diskRange);
        this.setDisable(true);
    }

    /**
     * Sets the range of all the circles
     * @param diskRange range of circle
     */
    public static void setDiskRange(int diskRange) {
        Disk.diskRange = diskRange;
    }

    /**
     * isDama's getter
     * @return true if piece is a king
     */
    public boolean isDama() {
        return isDama;
    }

    /**
     * Makes piece into a king, als updates its graphical representation
     */
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
