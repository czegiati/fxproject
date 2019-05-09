package game.Model;

/**
 * Information about a possible move, that can be executed.
 */
public class Move {
    /**
     * Where the piece will move.
     */
    Tile moveTo=null;
    /**
     * Which tile's piece wil be killed if the move gets executed.
     */
    Tile killedIfMoved=null;

    /**
     * Contructor with parameters.
     * @param moveTo where the piece should be moved
     * @param killedIfMoved what tile's piece will be captured upon execution
     */
   public Move(Tile moveTo,Tile killedIfMoved)
    {
        this.moveTo=moveTo;
        this.killedIfMoved=killedIfMoved;
    }

    /**
     * Executes move.
     * @param movedTile the starter position of the move
     */
    public void execute(Tile movedTile){
       Disk a=movedTile.getDisk();
       moveTo.setDisk(a);
       a.setCenterX(moveTo.getCenterX());
       a.setCenterY(moveTo.getCenterY());
       movedTile.setDisk(null);

       if(killedIfMoved!=null)
       {
           killedIfMoved.setDisk(null);
       }
    }

    /**
     * MOveTo's getter.
     * @return returns the tile, where the controlled piece will end up after execution
     */
    public Tile getMoveTo() {
        return moveTo;
    }

    /**
     * Sets a new position,where the selected tile will move.
     * @param moveTo new position after execution
     */
    public void setMoveTo(Tile moveTo) {
        this.moveTo = moveTo;
    }

    /**
     * killedIfMoved's getter.
     * @return which tile's piece will be captured
     */
    public Tile getKilledIfMoved() {
        return killedIfMoved;
    }

}
