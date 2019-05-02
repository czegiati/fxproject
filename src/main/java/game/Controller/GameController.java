package game.Controller;

import game.Model.*;
import game.Model.Database.XMLManager;
import game.View.GameView;

import game.View.NewRecordView;
import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeType;
import javafx.util.Duration;

import java.util.*;
import java.util.stream.Collectors;


/**
 * Main controller of the game,containing all of its logic.
 */
public class GameController {
    /**
     * Size of the game board.
     */
    private int tablesize=8;
    /**
     * Main view of the game.
     */
    private GameView view=new GameView();
    /**
     * List of tiles generated, size of this list is determined by the tablesize.
     */
    private ArrayList<Tile> tiles=new ArrayList<Tile>();
    /**
     * List of pieces controlled by the black player.
     */
    private ArrayList<Disk> playersDisks=new ArrayList<Disk>();
    /**
     * List of pieces controlled by the white player.
     */
    private ArrayList<Disk> opponentsDisks =new ArrayList<Disk>();
    /**
     * A map of the possible moves. The key contains the tiles, from where moves starts. The value contains the moves that can be started from the tile stored as key.
     */
    private Map<Tile,ArrayList<Move>> moveSets= new HashMap<Tile,ArrayList<Move>>();
    /**
     * The tile which was selected by the player. If null, then there is no tile selected.
     */
    private Tile selectedTile=null;
    /**
     * A referencable integer value. Holds the number of the current turn.
     */
    private INT turn=new INT(0);
    /**
     * Controller of the ingame logger, visually situated at the bottom of the window.
     */
    private TextAreaController eventLog;
    /**
     * Controller of the menubar at the top of the window.
     */
    private MenuBarConroller menubarController;
    /**
     * Whether or not the game has ended.
     */
    private boolean gameOver=false;
    /**
     * If killsrike is active in the game, then holds the tile, from which the killstrike can be continued.
     * If null, then there is no possible killstrike.
     */
    private Tile killStrikeTile;
    /**
     *Whether or not the AllDama rule is active.
     */
    private boolean rule_AllDama=false;
    /**
     * Whether or not the ForceKill rule is active.
     */
    private boolean rule_forceKill=true;
    /**
     * Whether or not the AI used in the game.
     */
    private boolean againstAI=true;
    /**
     * Whether or not the AI starts the game.
     */
    private boolean AIstartsthegame=false;
    /**
     * Whether or not the AI is playing against itself.
     */
    private boolean AIagaintAI=false;
    /**
     * Contains the possible moves for the AI.
     */
    private ArrayList<Tile> AIMoves=new ArrayList<Tile>();
    /**
     * Whether or not the game is paused. Only used, when AIagainstAI is active.
     */
    private boolean Pause=false;
    /**
     * A field used to visualize the AI's movements withe delay, instead of it just happening instantaneously.
     */
    private int AnimationCounter=0;


    /**
     * Constructor without parameters
     */
    public GameController(){
        Tile.setTileSize(70);
        Disk.setDiskRange(Tile.getTilesize()/2-Tile.getTilesize()/12);
        generateTableContent();
        if(rule_AllDama)
        {
            opponentsDisks.stream().forEach( o -> { o.makeIntoDama();});
            playersDisks.stream().forEach( o -> { o.makeIntoDama();});
        }
        eventLog= new TextAreaController(view.getEventLog(),turn);
        menubarController=new MenuBarConroller(view.getMenubar(),this);
        setTooltips();
        setKeys();
        nextTurn();



    }

    /**
     * Constructor with parameters
     * @param vsai              configuration determining if artificial intelligence should be used
     * @param aivsai            configuration determining if both players should be artificial
     * @param aistarts          configuration determining which player is artificial
     * @param rule_AllDama      configuration determining if all pieces should be kings
     * @param rule_forceKill    configuration determining if players should be forced to capture the opponent's pieces if it is passible in that turn
     * @param tablesize         the size of the board
     */
    public GameController(boolean vsai,boolean aivsai, boolean aistarts,boolean rule_AllDama,boolean rule_forceKill, int tablesize){
        this.tablesize=tablesize;
        this.rule_forceKill=rule_forceKill;
        this.rule_AllDama=rule_AllDama;
        this.againstAI=vsai;
        this.AIagaintAI=aivsai;
        this.AIstartsthegame=aistarts;

        Tile.setTileSize(70);
        Disk.setDiskRange(Tile.getTilesize()/2-Tile.getTilesize()/12);
        generateTableContent();
        if(rule_AllDama)
        {
            opponentsDisks.stream().forEach( o -> { o.makeIntoDama();});
            playersDisks.stream().forEach( o -> { o.makeIntoDama();});
        }
        eventLog= new TextAreaController(view.getEventLog(),turn);
        menubarController=new MenuBarConroller(view.getMenubar(),this);
        setTooltips();
        setKeys();
        nextTurn();



    }

    /**
     * Generates the board and the pieces
     */
    public void generateTableContent(){

        for(int y=0; y<tablesize; y++) {
            for (int x = 0; x < tablesize; x++) {
                Tile tile = new Tile(x, y);
                tiles.add(tile);
                view.getTable().getChildren().add(tile);
            }
        }

        for(int y=0; y<3 ; y++){
            for(int x=0; x<tablesize;x++){
                if((x+y)%2==0) {
                    double centerX = x * Tile.getTilesize()+Tile.getTilesize()/2;
                    double centerY = y * Tile.getTilesize()+Tile.getTilesize()/2;
                    Disk disk = new Disk(centerX, centerY,get(x,y));
                    disk.setFill(Color.WHITE);
                    this.opponentsDisks.add(disk);
                    this.get(x,y).setDisk(disk);
                    this.view.getTable().getChildren().add(disk);
                }
            }
        }

        for(int y=tablesize-3; y<tablesize ; y++){
            for(int x=0; x<tablesize;x++) {
                if ((x + y) % 2 == 0) {
                    double centerX = x * Tile.getTilesize()+Tile.getTilesize()/2;
                    double centerY = y * Tile.getTilesize()+Tile.getTilesize()/2;
                    Disk disk = new Disk(centerX, centerY,get(x,y));
                    disk.setFill(Color.BLACK);
                    this.playersDisks.add(disk);
                    this.get(x,y).setDisk(disk);
                    this.view.getTable().getChildren().add(disk);
                }
            }
        }
    }

    /**
     * Sets up the tooltips for the board's tiles
     */
    public void setTooltips(){
        tiles.stream().forEach( o->{
            o.getTooltip().setStyle(
                    "     -fx-background-radius: 0 0 0 0;\n" +
                    "     -fx-background-color: rgba(100,100,100,0.5)");
            o.setOnMouseEntered( e ->{
                        o.getTooltip().show(
                                o,
                                o.getCenterX()+o.getParent().getScene().getWindow().getX()+Tile.getTilesize()/2,
                                o.getCenterY()+o.getParent().getScene().getWindow().getY());

                    });
            o.setOnMouseExited(e ->{
               o.getTooltip().hide();
            });
                });

    }

    /**
     * Sets up the input handling for the game
     * P - pauses the game in an AI against AI game
     * Mouse - sets up the games main controls
     */
    public void setKeys(){


    this.view.getScreen().getScene().setOnKeyPressed(q ->{


        if(q.getCode()==KeyCode.P) {
            if(AIagaintAI) {
                if (!Pause)
                    Pause = true;
                else
                    Pause = false;
            }
        }
    });


    this.view.getTable().setOnMousePressed(e ->{


            /**
            * Removes the stroke from every tiles if clicked
            * */
            Tile target = (Tile) e.getTarget();
            tiles.forEach( o -> o.setStroke(null));
            ArrayList<Tile> targetTiles;
            Map<Tile,ArrayList<Move>>targetTilesMap =new HashMap<>();

    /**
    * Generates the list of the possible moves
    * */
            if(turn.value%2==0) {
                targetTilesMap=  moveSets.entrySet().stream().filter(o -> opponentsDisks
                        .contains(o.getKey().getDisk()))
                       .collect(Collectors.toMap(o -> o.getKey(), o-> o.getValue()));
                targetTiles= (ArrayList<Tile>)targetTilesMap.keySet().stream().collect(Collectors.toList());

                if(targetTilesMap.values().stream().filter(o -> o.stream().filter( q-> q.getKilledIfMoved()!=null).findFirst().isPresent()).findFirst().isPresent()
                 && rule_forceKill)
                {
                    targetTilesMap.values().forEach( value -> {
                       for(Move m: value)
                       {
                           if(m.getKilledIfMoved()==null)
                           {
                               m.setMoveTo(null);
                           }
                       }
                    });
                }
            }
            else{
                targetTilesMap=  moveSets.entrySet().stream().filter(o -> playersDisks
                        .contains(o.getKey().getDisk()))
                        .collect(Collectors.toMap(o -> o.getKey(), o-> o.getValue()));
                targetTiles= (ArrayList<Tile>)targetTilesMap.keySet().stream().collect(Collectors.toList());

                if(targetTilesMap.values().stream().filter(o -> o.stream().filter( q-> q.getKilledIfMoved()!=null).findFirst().isPresent()).findFirst().isPresent()
                && rule_forceKill)
                {
                    /** must kill an enemy*/
                    targetTilesMap.values().forEach( value -> {
                        for(Move m: value)
                        {
                            if(m.getKilledIfMoved()==null)
                            {
                                m.setMoveTo(null);
                            }
                        }
                    });
                }

            }



            if(againstAI)
                this.AIMoves=targetTiles;

            /**
            * Killstrike: if a disk preformed a kill and is able to preform another (from its new position),
            * then instead of ending its turn,
            * it will continue to preform kills, until its out of kills to have
            */
            if(killStrikeTile!=null)
            {
                /**remove every move that is not starts from the killer disks position*/
                this.moveSets.entrySet().stream().filter( o -> o.getKey()!=killStrikeTile).forEach(q -> {
                    q.getValue().clear();
                });

                /**make every move unusable where the player doesnt preform a kill*/
                for(Move m: moveSets.get(killStrikeTile))
                {
                    if(m.getKilledIfMoved()==null){
                        m.setMoveTo(null);
                    }
                }
            }

            /**execute selected move*/
            if(this.selectedTile!=null)
            {
                /**see if such move exists in moveSets*/
                if(moveSets.get(selectedTile).stream().filter(o -> o.getMoveTo()==target).findFirst().isPresent())
                {
                    moveSets.get(selectedTile).stream().filter(o -> o.getMoveTo()==target).findFirst().get().execute(selectedTile);

                        /**
                        * see if move resulted in a kill
                        * if it did, then
                        * see if it triggers a killstrike or not
                        */
                    if(moveSets.get(selectedTile).stream().filter(o -> o.getMoveTo()==target).findFirst().get().getKilledIfMoved()==null) {
                        /**
                        * If no kill happens then it ends the killstrike, and ends the turn
                        */
                        eventLog.append("\t Selected disk moves to ("
                                +target.getTileX()+","+target.getTileY()+").");
                        this.killStrikeTile=null;
                        nextTurn();
                    }
                    else
                    {
                        /**
                        * If a kill happend then it examines if a killstrike can be started
                        */
                        this.moveSets.clear();
                        tiles.forEach( o ->{
                            this.setMoveSets(o);
                        });
                        if(moveSets.entrySet().stream().filter(o -> o.getKey()==target && o.getValue().stream()
                                .filter( q -> q.getKilledIfMoved()!= null).findFirst().isPresent())
                        .findFirst().isPresent()) {
                            /**
                            *We start the killstrike by calling the nextturn() function and reducing the number of turns,
                            * basicly giving another move to the player
                            * We make the clicked tile( new position if selected Disk) the killStrike target,
                            * so we can remove the unwanted elements from the moveSet (above)
                            * */
                            eventLog.append("\t Preformed a kill on ("
                                    +(selectedTile.getTileX()+target.getTileX())/2
                                    +","
                                    +(selectedTile.getTileY()+target.getTileY())/2
                                    +") with selected Disk, starts/continues kilStrike. Moves to ("
                                    +target.getTileX()
                                    +","
                                    +target.getTileY()
                                    +")");
                            this.killStrikeTile = target;
                            this.turn.value--;
                            nextTurn();
                        }
                        else {
                            /**
                            *If we cannot preform a new kill from the new position, the we end
                            * the turn and end the killstrike
                            * */
                            eventLog.append("\t Preformed a kill on ("
                                    +(selectedTile.getTileX()+target.getTileX())/2
                                    +","
                                    +(selectedTile.getTileY()+target.getTileY())/2
                                    +") with selected Disk, cannot start/continue kilStrike. Moves to ("
                                    +target.getTileX()
                                    +","
                                    +target.getTileY()
                                    +")");
                            killStrikeTile=null;
                            nextTurn();
                        }
                    }

                }
            }


            /**
            * If targetTileList(which contains the tiles of the moves, which we can preform),
            * contains the clicked tile, we make the moves, which we can preform from the clicked tile,
             * visible by adding a red stroke to their target tile
             */
            if(target.getDisk()!=null && targetTiles.contains(target) && !gameOver)
            {
                    eventLog.append("\t Player selected Disk at ("+target.getTileX()+","+target.getTileY()+"),"
                    +moveSets.get(target).stream().filter(o -> o.getMoveTo()!=null).count()+ " moves are possible from this position.");
                this.selectedTile=target;
                moveSets.get(target).forEach( o -> {
                    if(o.getMoveTo()!=null) {
                        o.getMoveTo().setStroke(Color.RED);
                        o.getMoveTo().setStrokeWidth(5);
                        o.getMoveTo().setStrokeType(StrokeType.INSIDE);
                    }
                });

            }
            else
            {
                selectedTile=null;
            }

        });
    }

    /**
     * The view's getter
     * @return returns the game's main view
     */
    public GameView getView() {
        return view;
    }

    /**
     * Generates the moves the player can make from the given tile
     * @param tile the tile we want to generate the movements for
     */
    private void setMoveSets(Tile tile)
    {
        moveSets.put(tile,new ArrayList<>());

        if(tile.getDisk()==null)
            return;

        int i;
        if(this.opponentsDisks.contains(tile.getDisk()))
        {
            i=-1;
        }
        else
            i=1;

        if(!tile.getDisk().isDama()) {
            this.tiles.stream()
                    .filter(o -> o.getTileY() == tile.getTileY() - i || o.getTileY() == tile.getTileY() - (2 * i))

                    .forEach(o -> {
                        if (o.getTileY() == tile.getTileY() - i) {
                            if (o.getTileX() == tile.getTileX() - i) {
                                if (o.getDisk() == null)
                                    this.moveSets.get(tile).add(new Move(o, null));
                            } else if (o.getTileX() == tile.getTileX() + i) {
                                if (o.getDisk() == null)
                                    this.moveSets.get(tile).add(new Move(o, null));
                            }

                        } else {

                            if (o.getTileX() == tile.getTileX() - (2 * i)) {
                                if (o.getDisk() == null && isPresent(o.getTileX() + i, o.getTileY() + i)) {
                                    if (get(o.getTileX() + i, o.getTileY() + i).getDisk() != null &&
                                            !isInTheSameTeam(tile.getDisk(), get(o.getTileX() + i, o.getTileY() + i).getDisk())) {
                                        this.moveSets.get(tile).add(new Move(o, get(o.getTileX() + i, o.getTileY() + i)));
                                    }
                                }
                            }

                            else if (o.getTileX() == tile.getTileX() + (2 * i)) {
                                if (o.getDisk() == null && isPresent(o.getTileX() - i, o.getTileY() + i)) {
                                    if (get(o.getTileX() - i, o.getTileY() + i).getDisk() != null &&
                                            !isInTheSameTeam(tile.getDisk(), get(o.getTileX() - i, o.getTileY() + i).getDisk()))
                                        this.moveSets.get(tile).add(new Move(o, get(o.getTileX() - i, o.getTileY() + i)));
                                }
                            }
                        }
                    });
        }

            else
        {
            Tile checked;
            for (int x = -1, y = -1;x==-1; x--, y--) {
                if (isPresent(tile.getTileX() + x, tile.getTileY() + y))
                {

                    checked = get(tile.getTileX() + x, tile.getTileY() + y);
                    if(get(checked.getTileX(),checked.getTileY()).getDisk()==null)
                    {
                        moveSets.get(tile).add(new Move(checked,null));
                        break;
                    }
                    else
                    {
                        if(isPresent(checked.getTileX()-1,checked.getTileY()-1))
                        {
                            if(get(checked.getTileX()-1,checked.getTileY()-1).getDisk()==null
                                    && !isInTheSameTeam(tile.getDisk(),checked.getDisk()))
                            {
                                moveSets.get(tile).add(new Move(get(checked.getTileX()-1,checked.getTileY()-1),checked));
                                break;
                            }
                        }
                        else break;
                    }
                }
                else break;
            }

            for(int x=1,y=-1;x==1;x++,y--)
            {
                if (isPresent(tile.getTileX() + x, tile.getTileY() + y))
                {
                    checked = get(tile.getTileX() + x, tile.getTileY() + y);
                    if(get(checked.getTileX(),checked.getTileY()).getDisk()==null)
                    {
                        moveSets.get(tile).add(new Move(checked,null));
                        break;
                    }
                    else
                    {
                        if(isPresent(checked.getTileX()+1,checked.getTileY()-1))
                        {
                            if(get(checked.getTileX()+1,checked.getTileY()-1).getDisk()==null
                                    && !isInTheSameTeam(tile.getDisk(),checked.getDisk()))
                            {
                                moveSets.get(tile).add(new Move(get(checked.getTileX()+1,checked.getTileY()-1),checked));
                                break;
                            }
                        }
                        else break;
                    }
                }
                else break;
            }

            for(int x=1,y=1;x==1;x++,y++)
            {
                if (isPresent(tile.getTileX() + x, tile.getTileY() + y))
                {
                    checked = get(tile.getTileX() + x, tile.getTileY() + y);
                    if(get(checked.getTileX(),checked.getTileY()).getDisk()==null)
                    {
                        moveSets.get(tile).add(new Move(checked,null));
                        break;
                    }
                    else
                    {
                        if(isPresent(checked.getTileX()+1,checked.getTileY()+1))
                        {
                            if(get(checked.getTileX()+1,checked.getTileY()+1).getDisk()==null
                                    && !isInTheSameTeam(tile.getDisk(),checked.getDisk()))
                            {
                                moveSets.get(tile).add(new Move(get(checked.getTileX()+1,checked.getTileY()+1),checked));
                                break;
                            }
                        }
                        else break;
                    }
                }
                else break;
            }
            for(int x=-1,y=1;x==-1;x--,y++)
            {
                if (isPresent(tile.getTileX() + x, tile.getTileY() + y))
                {
                    checked = get(tile.getTileX() + x, tile.getTileY() + y);
                    if(get(checked.getTileX(),checked.getTileY()).getDisk()==null )
                    {
                        moveSets.get(tile).add(new Move(checked,null));
                        break;
                    }
                    else
                    {
                        if(isPresent(checked.getTileX()-1,checked.getTileY()+1))
                        {
                            if(get(checked.getTileX()-1,checked.getTileY()+1).getDisk()==null
                                    && !isInTheSameTeam(tile.getDisk(),checked.getDisk()))
                            {
                                moveSets.get(tile).add(new Move(get(checked.getTileX()-1,checked.getTileY()+1),checked));
                                break;
                            }
                        }
                        else break;
                    }
                }
                else break;
            }
        }
        }

     /**
      * For accessing the tile of the board a the given X and Y coordinates.
      * @param x : tile's X coordinate in table
      * @param y : tile's Y coordinate in table
      * @returns the tile with the given coordinates
      */
    private Tile get(int x,int y)
    {
       return this.tiles.stream().filter(o -> o.getTileX().equals(x) && o.getTileY().equals(y)).findFirst().get();
    }

    /**
     * Checks if the Tile at row X and column Y coordinates is exists.
    * @param x : tile's X coordinate in table
    * @param y : tile's Y coordinate in table
    * @return returns true, if such tile exists
    * */
    private boolean isPresent(int x, int y)
    {
        return this.tiles.stream().filter(o -> o.getTileX().equals(x) && o.getTileY().equals(y)).findFirst().isPresent();
    }

    /**
     * Checks whether or not the parameter disks are from the same team.
     * @param one first piece to compare
     * @param two  second piece, which the first should be compared to
     * @return decides whether or not the two pieces are from the same team
     */
    private boolean isInTheSameTeam(Disk one,Disk two){
        if((this.playersDisks.contains(one) && this.playersDisks.contains(two))
        || (this.opponentsDisks.contains(one) && this.opponentsDisks.contains(two)))
        {
            return true;
        }
        else
            return false;
    }
    /**
     * visually remove/modify elements from/on the screen,
     * check if game-ending conditions are met,
     * generate movements for the new turn,
     * and of course end the turn/give the turn to the other player (which is being examined via checking the turn variable)
     */
    private void nextTurn(){

        for(Tile tile:this.tiles)
        {
            if(this.playersDisks.contains(tile.getDisk()) && tile.getTileY()==0
            || this.opponentsDisks.contains(tile.getDisk()) && tile.getTileY()==tablesize-1)
            {
                tile.getDisk().makeIntoDama();
            }
        }


        moveSets.clear();
        tiles.forEach( o -> this.setMoveSets(o));

        opponentsDisks.stream().forEach(o -> {
            if(!tiles.stream().filter(e -> e.getDisk()==o).findFirst().isPresent())
            {
                view.getTable().getChildren().remove(o);
            }
        });

        playersDisks.stream().forEach( o -> {
            if(!tiles.stream().filter(e -> e.getDisk()==o).findFirst().isPresent())
            {
                view.getTable().getChildren().remove(o);
            }
        });

        List<Tile> playerMoveStartTiles=moveSets.entrySet().stream().filter(o -> !o.getValue().isEmpty())
                .filter(o -> playersDisks.contains(o.getKey().getDisk()))
                .map(p -> p.getKey())
                .collect(Collectors.toList());

        List<Tile> opponentMoveStartTiles=moveSets.entrySet().stream().filter(o -> !o.getValue().isEmpty())
                .filter(o -> opponentsDisks.contains(o.getKey().getDisk()))
                .map(p -> p.getKey())
                .collect(Collectors.toList());

        if(playerMoveStartTiles.isEmpty() && turn.value%2==0){
            gameOver=true;

        }
        else if(opponentMoveStartTiles.isEmpty() && turn.value%2==1)
        {
            gameOver=true;
            eventLog.append("\t Player Won in:"+turn.value+" turns");
        }

        if(gameOver && playerMoveStartTiles.isEmpty() && isTraditional())
        {
            XMLManager manager=new XMLManager();
            int aliveDisks=0;
            for(Tile tile:tiles)
            {
                if(opponentsDisks.contains(tile.getDisk()))
                {
                    aliveDisks++;
                }
            }
            NewRecordView view=new NewRecordView((int)(80-turn.value)+aliveDisks*5);
            if(manager.isHighEnough(new Record("", (int)(80-turn.value)+aliveDisks*5))&& AIstartsthegame)
            {
                view.getCreate().setOnAction(e ->{
                    Record a=new Record(view.getSizeText().getText(),view.getScore());
                    manager.addRecord(a);
                    view.getStage().close();
                });
            }
            else
            {
                view.getCreate().setDisable(true);
            }
            view.getClose().setOnAction(e ->{view.getStage().close();});
            view.getStage().show();
        }
        else if(gameOver && opponentMoveStartTiles.isEmpty() && isTraditional())
        {
            XMLManager manager=new XMLManager();
            int aliveDisks=0;
            for(Tile tile:tiles)
            {
                if(playersDisks.contains(tile.getDisk()))
                {
                    aliveDisks++;
                }
            }
            NewRecordView view=new NewRecordView((int)(80-turn.value)+aliveDisks*5);

            if(manager.isHighEnough(new Record("", (int)(80-turn.value)+aliveDisks*5)) && !AIstartsthegame)
            {

                view.getCreate().setOnAction(e ->{
                    Record a=new Record(view.getSizeText().getText(),view.getScore());
                    manager.addRecord(a);
                    view.getStage().close();
                });
            }
            else
                {
                view.getCreate().setDisable(true);
            }
            view.getClose().setOnAction(e ->{view.getStage().close();});


            view.getStage().show();
        }

        if(gameOver){return;}




        /** give controls to opponent */

        turn.value++;

        int i=0;
        if(this.AIstartsthegame)
        {
            i=1;
        }
        if(AIagaintAI){
            i=turn.value%2;
        }

       AI_Aimation(i);


    }

    /**
    * To handle the enemies turns. Artificially triggers the input listener.
    * @param i on which turn should the AI be able to move
    * */
    private void AI(int i){

        if(againstAI&& turn.value%2==i){
            /**
            * It selects a tile that does not contains a disk,at first, so the AIMoves containing the clickable tiles
            * can be generated, and saved
            */
            boolean disrupted=false;
            Tile temp=tiles.stream().filter(o -> o.getDisk()==null).findFirst().get();
            Event.fireEvent(temp, MouseEvent(temp));

            Random randomGen=new Random();

            /**
            * We pick a random tile from AIMoves, and trigger a click on it (the tile gets selected)
            * we examine if its disk can move. If not, then we keep regeneration it, until it has at least a move.
            *
            * Important: It cannot be an infinite cycle, because in nextTurn() method, we already made sure
            * the game ends if a player cannot make a move
            */
            int selectedIndex=randomGen.nextInt(AIMoves.size());
            while(moveSets.get(AIMoves.get(selectedIndex)).stream().filter( o -> o.getMoveTo()!=null).count()<1)
                {
                    selectedIndex=randomGen.nextInt(AIMoves.size());
                }

            Tile selected= AIMoves.get(selectedIndex);

            Event.fireEvent(selected, MouseEvent(selected));


            /**
            * We dig through the moveSets to see, we can still move with our selected disk
            * if not, then the whole process start again...
            * */
            selectedIndex=randomGen.nextInt(moveSets.get(selected).size());
            if(!moveSets.get(selected).stream().filter(o -> o.getMoveTo()!=null).findFirst().isPresent()){
                disrupted=true;
                AI(i);
            }

            /**
            * We keep picking a random move from the list of moves of the selected disk
            * until we find one that can be played
            *
            * Finally, when we find it we trigger another event in order to execute the move
            * */
            if(!disrupted) {
                while (moveSets.get(selected).get(selectedIndex).getMoveTo() == null) {
                    selectedIndex = randomGen.nextInt(moveSets.get(selected).size());
                }
                Event.fireEvent(moveSets.get(selected).get(selectedIndex).getMoveTo(), MouseEvent(moveSets.get(selected).get(selectedIndex).getMoveTo()));
            }
            }
    }

    /**
    * Completely useless in terms of game mechanics, but helps keeping the code somewhat cleaner.
    * Used to create MouseEvent with a single parameter.
    * @param tile : uses the tile's coordinates to generate a MouseEvent
    * @return returns the generated MouseEvent
    * */
    private MouseEvent MouseEvent(Tile tile){
        return new MouseEvent(MouseEvent.MOUSE_PRESSED, tile.getX()+10,
                tile.getY()+10, tile.getX(), tile.getY(), MouseButton.PRIMARY, 1, true, true, true, true,
                true, true, true, true, true, true, null);
    }

    /**
     * delays the AI's moves
     * @param i 0 or 1 determining which player's turn is it
     */
    private void AI_Aimation(int i){
        Timeline timeline=new Timeline();

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.getKeyFrames().add(
                new KeyFrame(Duration.millis(20),
                        new EventHandler<ActionEvent>() {

                            @Override
                            public void handle(ActionEvent t) {
                                AnimationCounter++;
                                if(AnimationCounter>=10 && !Pause)
                                {
                                    AnimationCounter=0;
                                    AI(i);
                                   timeline.stop();
                                }
                            }
                        },
                        new KeyValue[0]) // don't use binding
        );

        timeline.playFromStart();
    }


    /**
     * Whether or not the game is set according to the original rules
     * @return true if has the same rules as the original game
     */
    public boolean isTraditional(){
        if(!this.AIagaintAI && againstAI && !this.rule_AllDama && this.rule_forceKill)
            return true;
        else
            return false;
    }

    /**
     * EventLog's getter
     * @return returns EventLog
     */
    public TextAreaController getEventLog() {
        return eventLog;
    }

    /**
     * Tablesize's getter
     * @return returns the size of the board
     */
    public int getTablesize() {
        return tablesize;
    }

    /**
     * Rule_AllDama field's getter
     * @return whether the AllDamaRule is active
     */
    public boolean isRule_AllDama() {
        return rule_AllDama;
    }

    /**
     * Rule_forceKill's getter
     * @return whether the forceKill rule is active
     */
    public boolean isRule_forceKill() {
        return rule_forceKill;
    }

    /**
     * AIvsAI's getter
     * @return whether the AI should be used or not
     */
    public boolean isAgainstAI() {
        return againstAI;
    }

    /**
     * AIstartsthegame's getter
     * @return whether the AI can start the game or not
     */
    public boolean isAIstartsthegame() {
        return AIstartsthegame;
    }

    /**
     * AIagainstAI's getter
     * @return whether or not the AI is playing against itself
     */
    public boolean isAIagaintAI() {
        return AIagaintAI;
    }
}
