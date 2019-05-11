package game.Model;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;

/**
 * The graphical Checker pieces.
 */
public class Disk extends Circle {
    /**
     * Size of every piece.
     */
    private static int diskRange=30;
    /**
     * Whether or not this piece is a king.
     */
    private boolean isDama=false;
    /**
     * The tile, the checker piece is placed on.
     */
    private Tile starterpos;
    /**
     * Constructor with parameters.
     * @param pos the tile, where the checker piece is situated in.
     */
    public Disk(Tile pos){
        this.starterpos=pos;
        this.setRadius(diskRange);
        this.setDisable(true);
    }

    /**
     * isDama's getter.
     * @return true if piece is a king
     */
    public boolean isDama() {
        return isDama;
    }

    /**
     * Makes piece into a king, als updates its graphical representation.
     */
    public void makeIntoDama() {
        isDama=true;
        }

    /**
     * Getter of starterpos
     * @return Returns the starter position of this disk
     */
    public Tile getStarterPos() {
        return starterpos;
    }
}
