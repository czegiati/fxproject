package game.Controller;

import game.Model.*;
import javafx.scene.paint.Color;
import org.pmw.tinylog.*;

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
     * Constructor without parameters.
     */
    public GameController(){
        generateTableContent();
        if(rule_AllDama)
        {
            opponentsDisks.stream().forEach( o -> { o.makeIntoDama();});
            playersDisks.stream().forEach( o -> { o.makeIntoDama();});
        }

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
    public GameController(boolean vsai,boolean aivsai, boolean aistarts,boolean rule_AllDama,boolean rule_forceKill, int tablesize){
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
                    double centerX = x * Tile.getTilesize()+Tile.getTilesize()/2;
                    double centerY = y * Tile.getTilesize()+Tile.getTilesize()/2;
                    Disk disk = new Disk(centerX, centerY,get(x,y));
                    disk.setFill(Color.WHITE);
                    this.opponentsDisks.add(disk);
                    this.get(x,y).setDisk(disk);
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
     * @return whether or not a next turn can be called
     */
    public boolean mousePress(Tile target){

        ArrayList<Tile> targetTiles=getTargetTiles();

        if (againstAI)
            this.AIMoves = targetTiles;


        if (killStrikeTile != null) {
            Logger.info("KillStrike available.");
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

        /**execute selected move*/
        if (this.selectedTile != null) {
            /**see if such move exists in moveSets*/
            if (moveSets.get(selectedTile).stream().filter(o -> o.getMoveTo() == target).findFirst().isPresent()) {
                moveSets.get(selectedTile).stream().filter(o -> o.getMoveTo() == target).findFirst().get().execute(selectedTile);

                Logger.info("Move executed.");
                if (moveSets.get(selectedTile).stream().filter(o -> o.getMoveTo() == target).findFirst().get().getKilledIfMoved() == null) {
                    Logger.info("Move did not result a kill, killstrike ended.");
                    this.killStrikeTile = null;
                    return true;
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
                        return true;
                    } else {
                        Logger.info("Killstrike ended.");
                        killStrikeTile = null;
                        return true;
                    }
                }

            }
        }



        if (target.getDisk() != null && targetTiles.contains(target) && !gameOver) {
            selectedTile=target;
            Logger.info("Clicked tile has beeen selected.");
        } else {
            selectedTile = null;
        }
        return false;
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


        Map<Tile,ArrayList<Move>>targetTilesMap =new HashMap<>();
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
    public boolean AI(int i) {
        boolean AI=false;
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
                    if(mousePress(moveSets.get(selected).get(selectedIndex).getMoveTo()))
                    AI=true;
                }
            }
        }catch(StackOverflowError overflow){
            Logger.error("AI error occurred");
        }
        return AI;
    }

    public boolean isAIsTurn(){
        if(AIagaintAI && AIagaintAI){
            return true;
        }
        if(againstAI)
        {

            if(AIstartsthegame)
            {
                if(turn.value%2==1)
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
            else
            {
                if(turn.value%2==1)
                {
                    return false;
                }
                else
                {
                    return true;
                }
            }
        }
        else return false;
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

    public ArrayList<Tile> getTiles() {
        return tiles;
    }

    public ArrayList<Disk> getPlayersDisks() {
        return playersDisks;
    }

    public ArrayList<Disk> getOpponentsDisks() {
        return opponentsDisks;
    }

    public Map<Tile, ArrayList<Move>> getMoveSets() {
        return moveSets;
    }

    public Tile getSelectedTile() {
        return selectedTile;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public Tile getKillStrikeTile() {
        return killStrikeTile;
    }

    public ArrayList<Tile> getAIMoves() {
        return AIMoves;
    }

    public boolean isPause() {
        return Pause;
    }

    public INT getTurn() {
        return turn;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }
}
