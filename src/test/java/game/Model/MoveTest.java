package game.Model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MoveTest {

    @Test
    void execute() {
        Tile original=new Tile(0,0);
        original.setDisk(new Disk(original));
        Tile killed=new Tile(1,1);
        killed.setDisk(new Disk(killed));
        Tile newPos=new Tile(2,2);
        Move move=new Move(newPos,killed);
        move.execute(original);
        assertTrue(original.getDisk()==null && killed.getDisk()==null && newPos.getDisk()!=null);
    }
}