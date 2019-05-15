package game.Model;

import game.Model.*;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import org.pmw.tinylog.*;

import java.util.*;
import java.util.stream.Collectors;


/**
 * Main controller of the game,containing all of its logic.
 */
public class GameService {
    /**
     * Size of the game board.
     */
    private int tablesize=8;
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
     * A referencable integer value. Holds the number of the current turn.
     */
    private INT turn=new INT(0);
    /**
     * Needed to make the AI's moves not instantaneous.
     */
    private int AnimationCounter=0;
    /**
     * Whether or not the black player lost the game.
     */
    private boolean blackPlayerLost=false;
    /**
     * Whether or not the white player lost the game.
     */
    private boolean whitePlayerLost=false;

    /**
     * Constructor without parameters.
     */
    public GameService(){
        generateTableContent();
        if(rule_AllDama)
        {
            opponentsDisks.stream().forEach( o -> { o.makeIntoDama();});
            playersDisks.stream().forEach( o -> { o.makeIntoDama();});
        }

        nextTurn();
        Logger.info("Game has been set up.");
    }

    /**
     * Constructor with parameters.
     * @param vsai              configuration determining if artificial intelligence should be used
     * @param aivsai            configuration determining if both players should be artificial
     * @param aistarts          configuration determining which player is artificial
     * @param rule_AllDama      configuration determining if all pieces should be kings
     * @param rule_forceKill    configuration determining if players should be forced to capture the opponent's pieces if it is passible in that turn
     * @param tablesize         the size of the board
     */
    public GameService(boolean vsai, boolean aivsai, boolean aistarts, boolean rule_AllDama, boolean rule_forceKill, int tablesize){
        this.tablesize=tablesize;
        this.rule_forceKill=rule_forceKill;
        this.rule_AllDama=rule_AllDama;
        this.againstAI=vsai;
        this.AIagaintAI=aivsai;
        this.AIstartsthegame=aistarts;

        generateTableContent();
        if(rule_AllDama)
        {
            opponentsDisks.stream().forEach( o -> { o.makeIntoDama();});
            playersDisks.stream().forEach( o -> { o.makeIntoDama();});
        }
        Logger.info("Game has been set up.");
        nextTurn();

    }

    /**
     * Generates the board and the pieces.
     */
    public void generateTableContent(){

        for(int y=0; y<tablesize; y++) {
            for (int x = 0; x < tablesize; x++) {
                Tile tile = new Tile(x, y);
                tiles.add(tile);
            }
        }

        for(int y=0; y<3 ; y++){
            for(int x=0; x<tablesize;x++){
                if((x+y)%2==0) {
                    Disk disk = new Disk(get(x,y));
                    this.opponentsDisks.add(disk);
                    this.get(x,y).setDisk(disk);
                }
            }
        }

        for(int y=tablesize-3; y<tablesize ; y++){
            for(int x=0; x<tablesize;x++) {
                if ((x + y) % 2 == 0) {
                    Disk disk = new Disk(get(x,y));
                    this.playersDisks.add(disk);
                    this.get(x,y).setDisk(disk);
                }
            }
        }
        Logger.info("Gameboard has been created.");
    }

    /**
     * Pauses the game if AI playing against AI.
     */
    public void Pause(){
        if(AIagaintAI) {
            if (!Pause) {
                Pause = true;
                Logger.info("Game is paused.");
            }
            else {
                Pause = false;
                Logger.info("Game is unpaused.");
            }
        }
    }

    /**
     * Simulates an event in the game.
     * @param target the tile, that have been pressed.
     */
    public void mousePress(Tile target){

        ArrayList<Tile> targetTiles=getTargetTiles();

        if (againstAI)
            this.AIMoves = targetTiles;


        if (killStrikeTile != null) {
            Logger.info("KillStrike available.");
            narrowMoveSetsToKillstrikeOptions();
        }

        if (this.selectedTile != null) {
            actionWithSelectedTile(target);
        }

        selectTile(target,targetTiles);

    }

    /**
     * Removes those Tiles from moveSet, which are not the target tiles. Then removes moves without a kill target.
     */
    public void narrowMoveSetsToKillstrikeOptions(){
        this.moveSets.entrySet().stream().filter(o -> o.getKey() != killStrikeTile)
                .forEach(q -> {
                    q.getValue().clear();
                });

        for (Move m : moveSets.get(killStrikeTile)) {
            if (m.getKilledIfMoved() == null) {
                m.setMoveTo(null);
            }
        }
    }



    /**
     * Executes the selected action on the selectedTile.
     * @param target the selected action
     */
    public void actionWithSelectedTile(Tile target){
        if (moveSets.get(selectedTile).stream().filter(o -> o.getMoveTo() == target).findFirst().isPresent()) {
            moveSets.get(selectedTile).stream().filter(o -> o.getMoveTo() == target).findFirst().get().execute(selectedTile);

            Logger.info("Move executed.");
            if (moveSets.get(selectedTile).stream().filter(o -> o.getMoveTo() == target).findFirst().get().getKilledIfMoved() == null) {
                Logger.info("Move did not result a kill, killstrike ended.");
                this.killStrikeTile = null;
                nextTurn();
            } else {
                Logger.info("Move resulted in a kill.");
                this.moveSets.clear();
                tiles.forEach(o -> {
                    this.setMoveSets(o);
                });
                if (moveSets.entrySet().stream().filter(o -> o.getKey() == target && o.getValue().stream()
                        .filter(q -> q.getKilledIfMoved() != null).findFirst().isPresent())
                        .findFirst().isPresent()) {
                    Logger.info("Kill triggered another killstrike.");
                    this.killStrikeTile = target;
                    this.turn.value--;
                    nextTurn();
                } else {
                    Logger.info("Killstrike ended.");
                    killStrikeTile = null;
                    nextTurn();
                }
            }

        }
    }
    /**
     * Sets the selectedTiles if conditions are met.
     * @param target tile to be selected
     * @param targetTiles the selectable tiles
     */
    public void selectTile(Tile target,ArrayList<Tile> targetTiles){
    if (target.getDisk() != null && targetTiles.contains(target) && !gameOver) {
        selectedTile=target;
        Logger.info("Clicked tile has been selected.");
    } else {
        selectedTile = null;
    }
}


    /**
     * Changes turns, makes pieces in the right position kings, resets movesets, and ends the game,if the conditions are met.
     */
    public void nextTurn(){

        this.refreshDamas();


        moveSets.clear();
        tiles.forEach( o -> this.setMoveSets(o));


        List<Tile> playerMoveStartTiles=moveSets.entrySet().stream().filter(o -> !o.getValue().isEmpty())
                .filter(o -> playersDisks.contains(o.getKey().getDisk()))
                .map(p -> p.getKey())
                .collect(Collectors.toList());

        List<Tile> opponentMoveStartTiles=moveSets.entrySet().stream().filter(o -> !o.getValue().isEmpty())
                .filter(o -> opponentsDisks.contains(o.getKey().getDisk()))
                .map(p -> p.getKey())
                .collect(Collectors.toList());

        if(playerMoveStartTiles.isEmpty()){
            gameOver=true;
            blackPlayerLost=true;
            Logger.info("The game has ended. Black player lost.");

        }
        else if(opponentMoveStartTiles.isEmpty())
        {
            gameOver=true;
            whitePlayerLost=true;
            Logger.info("The game has ended. White player lost.");
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
     * Gets the list of Tiles, where the player can move the selected piece. Null if no tile is selected, or there are no moves, that can be executed.
     * @param targetTiles Tiles,from where moves can be executed.
     * @return List of Tiles, where the player can move the selected piece.
     */
    public ArrayList<Tile> getMoveSetTilesOfSelectedTile(ArrayList<Tile> targetTiles){
        if(selectedTile!=null) {
            if (selectedTile.getDisk() != null && targetTiles.contains(selectedTile) && !gameOver)
                return (ArrayList<Tile>) moveSets.get(selectedTile).stream()
                        .filter(o -> o.getMoveTo() != null)
                        .map(o -> o.getMoveTo())
                        .collect(Collectors.toList());
            else
                return null;
        }
        else
            return null;

    }




    /**
     * Generates a list containing the tiles, where moves can be executed from.
     * @return list containing the tiles, where moves can be executed from.
     */
    public ArrayList<Tile> getTargetTiles(){
        ArrayList<Tile> targetTiles;

            targetTiles= (ArrayList<Tile>)generateTargetTilesMap().keySet().stream().collect(Collectors.toList());

        return targetTiles;
    }

    /**
     * Generates a map containing Tiles, where moves can be executed from, as keys and Moves belonging to these Tiles as values.
     * @return a map, of tile and move pairs.
     */
    public Map<Tile,ArrayList<Move>> generateTargetTilesMap(){


        Map<Tile,ArrayList<Move>> targetTilesMap;

        if(turn.value%2==0) {
            targetTilesMap=  moveSets.entrySet().stream().filter(o -> opponentsDisks
                    .contains(o.getKey().getDisk()))
                    .collect(Collectors.toMap(o -> o.getKey(), o-> o.getValue()));

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
        return targetTilesMap;
    }

    /**
     * Generates the moves the player can make from the given tile.
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
      * @return the tile with the given coordinates
      */
     public Tile get(int x, int y)
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
     * Makes pieces into damas.
     */
    public void refreshDamas(){
        for(Tile tile:this.tiles)
        {
            if(this.playersDisks.contains(tile.getDisk()) && tile.getTileY()==0
                    || this.opponentsDisks.contains(tile.getDisk()) && tile.getTileY()==tablesize-1)
            {
                tile.getDisk().makeIntoDama();
            }
        }
    }

    /**
     * Refreshes the moveSets map's values.
     */
    public void refreshMoveSets(){
        moveSets.clear();
        tiles.forEach( o -> this.setMoveSets(o));
    }


    /**
    * To handle the enemies turns. Artificially triggers the input listener.
    * @param i on which turn should the AI be able to move
    * */
    public void AI(int i) {
        try {
            if (againstAI && turn.value % 2 == i) {
                /**
                 * It selects a tile that does not contains a disk,at first, so the AIMoves containing the clickable tiles
                 * can be generated, and saved
                 */
                boolean disrupted = false;
                Tile temp = tiles.stream().filter(o -> o.getDisk() == null).findFirst().get();
                mousePress(temp);

                Random randomGen = new Random();

                /**
                 * We pick a random tile from AIMoves, and trigger a click on it (the tile gets selected)
                 * we examine if its disk can move. If not, then we keep regeneration it, until it has at least a move.
                 *
                 * Important: It cannot be an infinite cycle, because in nextTurn() method, we already made sure
                 * the game ends if a player cannot make a move
                 */
                int selectedIndex = randomGen.nextInt(AIMoves.size());
                while (moveSets.get(AIMoves.get(selectedIndex)).stream().filter(o -> o.getMoveTo() != null).count() < 1) {
                    selectedIndex = randomGen.nextInt(AIMoves.size());
                }

                Tile selected = AIMoves.get(selectedIndex);

                mousePress(selected);


                /**
                 * We dig through the moveSets to see, we can still move with our selected disk
                 * if not, then the whole process start again...
                 * */
                selectedIndex = randomGen.nextInt(moveSets.get(selected).size());
                if (!moveSets.get(selected).stream().filter(o -> o.getMoveTo() != null).findFirst().isPresent()) {
                    disrupted = true;
                    AI(i);
                }

                /**
                 * We keep picking a random move from the list of moves of the selected disk
                 * until we find one that can be played
                 *
                 * Finally, when we find it we trigger another event in order to execute the move
                 * */
                if (!disrupted) {
                    while (moveSets.get(selected).get(selectedIndex).getMoveTo() == null) {
                        selectedIndex = randomGen.nextInt(moveSets.get(selected).size());
                    }
                    mousePress(moveSets.get(selected).get(selectedIndex).getMoveTo());
                }
            }
        }catch(StackOverflowError overflow){
            Logger.error("AI error occurred");
        }
    }



    /**
     * delays the AI's moves.
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
     * Whether or not the game is set according to the original rules.
     * @return true if has the same rules as the original game
     */
    public boolean isTraditional(){
        if(!this.AIagaintAI && againstAI && !this.rule_AllDama && this.rule_forceKill)
            return true;
        else
            return false;
    }

    /**
     * Tablesize's getter.
     * @return returns the size of the board
     */
    public int getTablesize() {
        return tablesize;
    }

    /**
     * Rule_AllDama field's getter.
     * @return whether the AllDamaRule is active
     */
    public boolean isRule_AllDama() {
        return rule_AllDama;
    }

    /**
     * Rule_forceKill's getter.
     * @return whether the forceKill rule is active
     */
    public boolean isRule_forceKill() {
        return rule_forceKill;
    }

    /**
     * AIvsAI's getter.
     * @return whether the AI should be used or not
     */
    public boolean isAgainstAI() {
        return againstAI;
    }

    /**
     * AIstartsthegame's getter.
     * @return whether the AI can start the game or not
     */
    public boolean isAIstartsthegame() {
        return AIstartsthegame;
    }

    /**
     * AIagainstAI's getter.
     * @return whether or not the AI is playing against itself
     */
    public boolean isAIagaintAI() {
        return AIagaintAI;
    }

    /**
     * Getter of tiles.
     * @return Returns the list of board tiles.
     */
    public ArrayList<Tile> getTiles() {
        return tiles;
    }

    /**
     * Getter of PlayersDisks.
     * @return Returns the list of black player's pieces.
     */
    public ArrayList<Disk> getPlayersDisks() {
        return playersDisks;
    }

    /**
     * Getter of opponentsDisks.
     * @return Returns the list of white player's pieces.
     */
    public ArrayList<Disk> getOpponentsDisks() {
        return opponentsDisks;
    }

    /**
     * Getter of MoveSets.
     * @return Returns the list of moves, that cn be executed.
     */
    public Map<Tile, ArrayList<Move>> getMoveSets() {
        return moveSets;
    }

    /**
     * Getter of selectedTile.
     * @return Returns the selectedTile.
     */
    public Tile getSelectedTile() {
        return selectedTile;
    }

    /**
     * Getter of GameOver.
     * @return Whether or not the game has ended.
     */
    public boolean isGameOver() {
        return gameOver;
    }

    /**
     * Getter of KillStikeTile.
     * @return Returns the tile, where the killstrike can continue from,
     */
    public Tile getKillStrikeTile() {
        return killStrikeTile;
    }

    /**
     * Getter of AIMoves.
     * @return Returns list of Tiles from where the AI can execute moves.
     */
    public ArrayList<Tile> getAIMoves() {
        return AIMoves;
    }

    /**
     * Getter of Pause.
     * @return Returns true, if the game is paused, only works in AI vs AI games.
     */
    public boolean isPause() {
        return Pause;
    }

    /**
     * Getter of Turn.
     * @return Returns a referencable Integer.
     */
    public INT getTurn() {
        return turn;
    }

    /**
     * Getter of BlackPlayerLost.
     * @return True if the black player have lost the game.
     */
    public boolean isBlackPlayerLost() {
        return blackPlayerLost;
    }

    /**
     * Getter of WhitePlayerMoves.
     * @return Returns true, if white player lost.
     */
    public boolean isWhitePlayerLost() {
        return whitePlayerLost;
    }

    /**
     * Getter of killstrike tile.
     * @param killStrikeTile Tile, where the killstrike can be continued, if there is no killstrike available, then it is null.
     */
    public void setKillStrikeTile(Tile killStrikeTile) {
        this.killStrikeTile = killStrikeTile;
    }
}

