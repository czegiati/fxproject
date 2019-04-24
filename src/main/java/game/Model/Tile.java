package game.Model;

import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Tile extends Rectangle {
    private static Integer tilesize;
    private Integer TileX;
    private Integer TileY;
    private double CenterX;
    private double CenterY;
    private Disk disk=null;
    private Tooltip tooltip;

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

    public static void setTileSize(int size){
        tilesize=size;
    }

    public static Integer getTilesize() {
        return tilesize;
    }

    public static void setTilesize(Integer tilesize) {
        Tile.tilesize = tilesize;
    }

    public double getCenterX() {
        return CenterX;
    }

    public void setCenterX(double centerX) {
        CenterX = centerX;
    }

    public double getCenterY() {
        return CenterY;
    }

    public void setCenterY(double centerY) {
        CenterY = centerY;
    }

    public Integer getTileX() {
        return TileX;
    }

    public void setTileX(Integer tileX) {
        TileX = tileX;
    }

    public Integer getTileY() {
        return TileY;
    }

    public void setTileY(Integer tileY) {
        TileY = tileY;
    }

    public Disk getDisk() {
        return disk;
    }

    public void setDisk(Disk disk) {
        this.disk = disk;

    }

    public Tooltip getTooltip() {
        return tooltip;
    }

    public void setTooltip(Tooltip tooltip) {
        this.tooltip = tooltip;
    }
}
