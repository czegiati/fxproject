package game.Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceTest {

    @BeforeEach
    void setUp(){
        Tile.setTileSize(70);
    }

    @Test
    void narrowMoveSetsToKillstrikeOptions() {
        GameService a=new GameService();
        int originalSize= a.getMoveSets().size();
        a.setKillStrikeTile(a.getTiles().stream().filter(o -> o.getDisk()!= null).findFirst().get());
        a.getMoveSets().get(a.getKillStrikeTile()).add(new Move(new Tile(99,99),new Tile(100,100)));
        a.narrowMoveSetsToKillstrikeOptions();

        assertTrue( a.getMoveSets().entrySet().stream().filter(o -> o.getValue().size()>0).collect(Collectors.toList()).size()==1);
        assertEquals(a.getKillStrikeTile(),a.getMoveSets().entrySet().stream().filter(o -> o.getValue().size()>0).findFirst().get().getKey());

    }

    @Test
    void actionWithSelectedTile() {
        GameService a=new GameService();

        Tile startTile=a.getTiles().stream()
                .filter(o -> o.getDisk()!=null)
                .filter(o  ->a.getPlayersDisks().contains(o.getDisk()))
                .findFirst().get();

        Tile randomWhitePieceTile=a.getTiles().stream()
                .filter(o -> o.getDisk()!=null)
                .filter(o  ->a.getOpponentsDisks().contains(o.getDisk()))
                .findFirst().get();

        Tile newUnusedPos =a.getTiles().stream()
                .filter(o -> o.getDisk()==null)
                .findFirst().get();

        a.getMoveSets().get(startTile).add(new Move(newUnusedPos,randomWhitePieceTile));
        ArrayList<Tile> targetTiles=new ArrayList<>();
        targetTiles.add(startTile);
         a.selectTile(startTile,targetTiles);
         assertTrue(randomWhitePieceTile.getDisk()!=null);

         a.actionWithSelectedTile(newUnusedPos);

         assertTrue(randomWhitePieceTile.getDisk()==null);
    }

    @Test
    void selectTile() {
        GameService a=new GameService();

       a.selectTile(a.get(0,5),a.getTargetTiles());
       assertTrue(a.getSelectedTile()==null);
       a.selectTile(a.get(1,5),a.getTargetTiles());
       assertTrue(a.getSelectedTile()!=null);
    }


    @Test
    void generateTargetTilesMap() {
        GameService a=new GameService();
        Tile firstBlackDisksTileA=a.getTiles().stream()
                .filter(o -> o.getDisk()!=null)
                .filter(o -> a.getPlayersDisks().contains(o.getDisk()))
                .findFirst().get();
        a.getMoveSets().get(firstBlackDisksTileA).add(new Move(new Tile(99,99),new Tile(100,100)));
        Map<Tile,ArrayList<Move>> generatedTargetTilesMapA=a.generateTargetTilesMap();
        int counterA=0;
        int countA2=0;
        for(Tile tile: generatedTargetTilesMapA.keySet())
        {
            for(Move move : generatedTargetTilesMapA.get(tile))
            {
                countA2++;
                if(move.getMoveTo()==null)
                    counterA++;
            }
        }

        GameService b=new GameService(false,false,false,false,false,8);
        Map<Tile,ArrayList<Move>> generatedTargetTilesMapB=b.generateTargetTilesMap();

        Tile firstBlackDisksTileB=b.getTiles().stream()
                .filter(o -> o.getDisk()!=null)
                .filter(o -> b.getPlayersDisks().contains(o.getDisk()))
                .findFirst().get();
        b.getMoveSets().get(firstBlackDisksTileB).add(new Move(new Tile(99,99),new Tile(100,100)));

        int counterB=0;
        int countB2=0;
        for(Tile tile: generatedTargetTilesMapB.keySet())
        {
            for(Move move : generatedTargetTilesMapB.get(tile))
            {
                countB2++;
                if(move.getKilledIfMoved()!=null)
                    counterB++;
            }
        }

        assertTrue(counterA!=0  && counterB!=0);


    }

    @Test
    void refreshDamas() {
        GameService a=new GameService();
        Disk BlackDama=a.getTiles().stream().filter(o -> o.getDisk()!=null).filter(o -> a.getPlayersDisks().contains(o.getDisk())).findFirst().get().getDisk();
        Disk WhiteDama= a.getTiles().stream().filter(o -> o.getDisk()!=null).filter(o -> a.getOpponentsDisks().contains(o.getDisk())).findFirst().get().getDisk();

        assertFalse(BlackDama.isDama());
        a.get(0,0).setDisk(BlackDama);
        a.refreshDamas();
        assertTrue(BlackDama.isDama());

        assertFalse(WhiteDama.isDama());
        a.get(a.getTablesize()-1,a.getTablesize()-1).setDisk(WhiteDama);
        a.refreshDamas();
        assertTrue(WhiteDama.isDama());
    }

    @Test
    void nextTurn(){
        GameService a=new GameService();
        a.nextTurn();
        assertFalse(a.isWhitePlayerLost() || a.isBlackPlayerLost());

        a.getTiles().forEach(o ->{
            if(a.getPlayersDisks().contains(o.getDisk()))
                o.setDisk(null);
        });
        a.nextTurn();
        assertTrue(a.isWhitePlayerLost() || a.isBlackPlayerLost());
    }

}