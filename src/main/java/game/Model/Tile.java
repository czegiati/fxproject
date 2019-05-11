package game.Model;

import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Graphical and logical component of the board.
 */
public class Tile extends Rectangle {
    /**
     * Size of every tile.
     */
    private static Integer tilesize;
    /**
     * Row of the tile on the board.
     */
    private Integer TileX;
    /**
     * Column of the tile on the board.
     */
    private Integer TileY;
    /**
     * Center X position of Tile (in pixels).
     */
    private double CenterX;
    /**
     * Center Y position of Tile (in pixels).
     */
    private double CenterY;
    /**
     * Contained piece. Null if there is no piece on this tile.
     */
    private Disk disk=null;
    /**
     * Tooltip, that shows up on hovered.
     */
    private Tooltip tooltip;

    /**
     * Constructor with parameters
     * @param x column of tile
     * @param y row of tile
     */
    public Tile(int x,int y){
        this.TileX=x;
        this.TileY=y;
    }

    /**
     * Changes the size of every tile.
     * @param size new size of every tile
     */
    public static void setTileSize(int size){
        tilesize=size;
    }

    /**
     * tilesize' getter.
     * @return size of every tile
     */
    public static Integer getTilesize() {
        return tilesize;
    }

    /**
     * Center position of the tile, getter.
     * @return Center position of the tile,X coordinate
     */
    public double getCenterX() {
        return CenterX;
    }

    /**
     * Center position of the tile,getter.
     * @return Center position of the tile,Y coordinate
     */
    public double getCenterY() {
        return CenterY;
    }

    /**
     * Position on board.
     * @return column of tile on board
     */
    public Integer getTileX() {
        return TileX;
    }

    /**
     * Position on board.
     * @return row of tile on board
     */
    public Integer getTileY() {
        return TileY;
    }

    /**
     * getter of contained disk.
     * @return contained disk
     */
    public Disk getDisk() {
        return disk;
    }

    /**
     * Sets a disk on this tile.
     * @param disk new contained disk
     */
    public void setDisk(Disk disk) {
        this.disk = disk;

    }

    /**
     * Getter of tooltip.
     * @return returns the tiles tooltip
     */
    public Tooltip getTooltip() {
        return tooltip;
    }

    /**
     * Setter of tooltip.
     * @param tooltip sets tooltip.
     */
    public void setTooltip(Tooltip tooltip) {
        this.tooltip = tooltip;
    }

    /**
     * Setter of CenterX.
     * @param centerX Sets CenterX to this tile's center on the x axis.
     */
    public void setCenterX(double centerX) {
        CenterX = centerX;
    }
    /**
     * Setter of CenterY.
     * @param centerY Sets CenterY to this tile's center on the y axis.
     */
    public void setCenterY(double centerY) {
        CenterY = centerY;
    }
}
