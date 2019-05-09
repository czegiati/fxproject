package game.Controller;

import game.Model.Database.XMLManager;
import game.Model.Record;
import game.Model.Tile;
import game.View.GameView;
import game.View.NewRecordView;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeType;
import javafx.util.Duration;
import org.pmw.tinylog.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MainController {
    /**
     * Main view of the game.
     */
    private GameView view=new GameView();
    /**
     * Interactive services for the game.
     */
    private GameController gamecontroller;
    /**
     * A field used to visualize the AI's movements withe delay, instead of it just happening instantaneously.
     */
    private int AnimationCounter=0;

    private Timeline timeline;



    public MainController(){
        Tile.setTileSize(70);
    this.gamecontroller=new GameController();
    generateGraphicalContent();
    setTooltips();
    setEventHandlers();
    nextTurn();

    }
public MainController(boolean vsai,boolean aivsai, boolean aistarts,boolean rule_AllDama,boolean rule_forceKill, int tablesize) {
    Tile.setTileSize(70);
    this.gamecontroller=new GameController(vsai,aivsai,aistarts,rule_AllDama,rule_forceKill,tablesize);
    generateGraphicalContent();
    setTooltips();
    setEventHandlers();
    nextTurn();
}
    /**
     * Takes inputs, and decides and calls the model's functions to handle them.
     */
    private void setEventHandlers() {

        this.view.getScreen().getScene().setOnKeyPressed(q -> {
            if (q.getCode() == KeyCode.P) {
                gamecontroller.Pause();
            }
        });


        this.view.getTable().setOnMousePressed(e -> {


                Tile target = (Tile) e.getTarget();
                if(gamecontroller.mousePress(target)){
                    nextTurn();
                    refresh();
                }

                Logger.info("Pressed on ("+target.getTileX()+","+target.getTileY()+") tile.");


            refresh();
            });



            this.getView().getStage().setOnHidden(e ->{
                if(this.gamecontroller.isGameOver())
                    Logger.info("Stage Closed");
                else
                    Logger.info("Stage closed before the game ended");
            });

    }

    private void refresh(){
        {
            gamecontroller.getTiles().forEach(o -> o.setStroke(null));


            ArrayList<Tile> tiles=gamecontroller.getMoveSetTilesOfSelectedTile(gamecontroller.getTargetTiles());
            if(tiles!=null)
            {
                tiles.stream()
                        .forEach(o ->{
                            o.setStroke(Color.RED);
                            o.setStrokeWidth(5);
                            o.setStrokeType(StrokeType.INSIDE);
                        });
            }
        }

        {
            ArrayList<Tile> tiles =gamecontroller.getTiles();

            gamecontroller.getOpponentsDisks().stream().forEach(o -> {
                if (!tiles.stream().filter(o2 -> o2.getDisk() == o).findFirst().isPresent()) {
                    view.getTable().getChildren().remove(o);
                }
            });

            gamecontroller.getPlayersDisks().stream().forEach(o -> {
                if (!tiles.stream().filter(o2 -> o2.getDisk() == o).findFirst().isPresent()) {
                    view.getTable().getChildren().remove(o);
                }
            });
        }

    }


    /**
     * Sets up the tooltips for the board's tiles.
     */
    public void setTooltips(){
        gamecontroller.getTiles().stream().forEach( o->{
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
     * Generates the tiles and the pieces into the screen.
     */
    public void generateGraphicalContent(){
        this.gamecontroller.getTiles().forEach( o->{
            this.view.getTable().getChildren().add(o);
        });
        this.gamecontroller.getOpponentsDisks().forEach(o ->{
            this.view.getTable().getChildren().add(o);
        });
        this.gamecontroller.getPlayersDisks().forEach(o ->{
            this.view.getTable().getChildren().add(o);
        });
    }

    /**
     * Visually remove/modify elements from/on the screen, check if game-ending conditions are met, generate movements for the new turn, and of course end the turn/give the turn to the other player (which is being examined via checking the turn variable).
     */
    public void nextTurn(){
        gamecontroller.refreshDamas();
        gamecontroller.refreshMoveSets();

        List<Tile> playerMoveStartTiles=gamecontroller.getMoveSets().entrySet().stream().filter(o -> !o.getValue().isEmpty())
                .filter(o -> gamecontroller.getPlayersDisks().contains(o.getKey().getDisk()))
                .map(p -> p.getKey())
                .collect(Collectors.toList());

        List<Tile> opponentMoveStartTiles=gamecontroller.getMoveSets().entrySet().stream().filter(o -> !o.getValue().isEmpty())
                .filter(o -> gamecontroller.getOpponentsDisks().contains(o.getKey().getDisk()))
                .map(p -> p.getKey())
                .collect(Collectors.toList());

        if(playerMoveStartTiles.isEmpty() && gamecontroller.getTurn().value%2==0){
           gamecontroller.setGameOver(true);
            Logger.info("The game has ended.");

        }
        else if(opponentMoveStartTiles.isEmpty() && gamecontroller.getTurn().value%2==1)
        {
            gamecontroller.setGameOver(true);
            Logger.info("The game has ended.");
        }

        if(gamecontroller.isGameOver() && playerMoveStartTiles.isEmpty() && gamecontroller.isTraditional())
        {
            XMLManager manager=new XMLManager();
            int aliveDisks=0;
            for(Tile tile:gamecontroller.getTiles())
            {
                if(gamecontroller.getOpponentsDisks().contains(tile.getDisk()))
                {
                    aliveDisks++;
                }
            }
            NewRecordView view=new NewRecordView((int)(80-gamecontroller.getTurn().value)+aliveDisks*5);
            if(manager.isHighEnough(new Record("", (int)(80-gamecontroller.getTurn().value)+aliveDisks*5))
                    && gamecontroller.isAIstartsthegame())
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
        else if(gamecontroller.isGameOver() && opponentMoveStartTiles.isEmpty() && gamecontroller.isTraditional())
        {
            XMLManager manager=new XMLManager();
            int aliveDisks=0;
            for(Tile tile:gamecontroller.getTiles())
            {
                if(gamecontroller.getPlayersDisks().contains(tile.getDisk()))
                {
                    aliveDisks++;
                }
            }
            NewRecordView view=new NewRecordView((int)(80-gamecontroller.getTurn().value)+aliveDisks*5);

            if(manager.isHighEnough(new Record("", (int)(80-gamecontroller.getTurn().value)+aliveDisks*5)) && !gamecontroller.isAIstartsthegame())
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

        if(gamecontroller.isGameOver()){return;}




        /** give controls to opponent */

        gamecontroller.getTurn().value++;

        int i=0;
        if(gamecontroller.isAIstartsthegame())
        {
            i=1;
        }
        if(gamecontroller.isAIagaintAI()){
            i=gamecontroller.getTurn().value%2;
        }

        if(gamecontroller.isAIsTurn())
        AI_Aimation(i);


    }


    /**
     * Delays the AI's moves.
     * @param i 0 or 1 determining which player's turn is it
     */
    private void AI_Aimation(int i){
        if(timeline!=null)
            timeline.stop();
        timeline=new Timeline();

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.getKeyFrames().add(
                new KeyFrame(Duration.millis(40),
                        new EventHandler<ActionEvent>() {

                            @Override
                            public void handle(ActionEvent t) {
                                AnimationCounter++;
                                if(AnimationCounter>=10 && !gamecontroller.isPause())
                                {
                                    timeline.stop();
                                    AnimationCounter=0;
                                   if(gamecontroller.AI(i))
                                   {
                                       refresh();
                                       nextTurn();
                                   }
                                   else
                                   {
                                       refresh();
                                       gamecontroller.getTurn().value--;
                                       nextTurn();
                                   }
                                }
                            }
                        },
                        new KeyValue[0]) // don't use binding
        );


        timeline.playFromStart();
    }



    /**
     * The view's getter.
     * @return returns the game's main view
     */
    public GameView getView() {
        return view;
    }

    public GameController getGamecontroller() {
        return gamecontroller;
    }

    public Timeline getTimeline() {
        return timeline;
    }
}
