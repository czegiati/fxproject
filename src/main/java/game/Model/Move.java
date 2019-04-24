package game.Model;

public class Move {
    Tile moveTo=null;
    Tile killedIfMoved=null;

   public Move(Tile moveTo,Tile killedIfMoved)
    {
        this.moveTo=moveTo;
        this.killedIfMoved=killedIfMoved;
    }

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

    public Tile getMoveTo() {
        return moveTo;
    }

    public void setMoveTo(Tile moveTo) {
        this.moveTo = moveTo;
    }

    public Tile getKilledIfMoved() {
        return killedIfMoved;
    }

    public void setKilledIfMoved(Tile killedIfMoved) {
        this.killedIfMoved = killedIfMoved;
    }
}
