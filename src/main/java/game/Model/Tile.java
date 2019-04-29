package game.Model;

import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Graphical and logical component of the board
 */
public class Tile extends Rectangle {
    private static Integer tilesize;
    private Integer TileX;
    private Integer TileY;
    private double CenterX;
    private double CenterY;
    private Disk disk=null;
    private Tooltip tooltip;

    /**
     * Constructor with parameters
     * @param x column of tile
     * @param y row of tile
     */
    public Tile(int x,int y){
        this.TileX=x;
        this.TileY=y;
        if((x+y)%2==0)
        {
            this.setFill(Color.rgb(240,175,120));
        }
        else
        {
            this.setFill(Color.rgb(200,120,60));
        }

        this.setX(x*tilesize);
        this.setY(y*tilesize);
        this.setWidth(tilesize);
        this.setHeight(tilesize);

        this.CenterX=x*tilesize+tilesize/2;
        this.CenterY=y*tilesize+tilesize/2;
        tooltip=new Tooltip("Tile:("+x+","+y+")");
    }

    /**
     * Changes the size of every tile
     * @param size new size of every tile
     */
    public static void setTileSize(int size){
        tilesize=size;
    }

    /**
     * tilesize' getter
     * @return size of every tile
     */
    public static Integer getTilesize() {
        return tilesize;
    }

    /**
     * Center position of the tile, getter
     * @returnCenter position of the tile,X coordinate
     */
    public double getCenterX() {
        return CenterX;
    }

    /**
     * Center position of the tile,getter
     * @returnCenter position of the tile,Y coordinate
     */
    public double getCenterY() {
        return CenterY;
    }

    /**
     * Position on board
     * @return column of tile on board
     */
    public Integer getTileX() {
        return TileX;
    }

    /**
     * Position on board
     * @return row of tile on board
     */
    public Integer getTileY() {
        return TileY;
    }

    /**
     * getter of contained disk
     * @return contained disk
     */
    public Disk getDisk() {
        return disk;
    }

    /**
     * Sets a disk on this tile
     * @param disk new contained disk
     */
    public void setDisk(Disk disk) {
        this.disk = disk;

    }

    /**
     * Getter of tooltip
     * @return returns the tiles tooltip
     */
    public Tooltip getTooltip() {
        return tooltip;
    }

}
