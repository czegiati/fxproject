package game.Controller;

import game.Model.Database.XMLManager;
import game.Model.GameService;
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
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeType;
import javafx.util.Duration;
import org.pmw.tinylog.Logger;

import java.util.ArrayList;

/**
 * Main controller of the game, visually updates the game elements.
 */
public class MainController {
    /**
     * Main view of the game.
     */
    private GameView view=new GameView();
    /**
     * Interactive services for the game.
     */
    private GameService gameService;
    /**
     * Timeline, which is being used, to update the screen.
     */
    private Timeline timeline;


    /**
     * Constructor without parameters.
     */
    public MainController(){
        Tile.setTileSize(70);
    this.gameService =new GameService();
    generateGraphicalContent();
    setTooltips();
    setEventHandlers();
    ScreenAutoRefresh();

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
public MainController(boolean vsai,boolean aivsai, boolean aistarts,boolean rule_AllDama,boolean rule_forceKill, int tablesize) {
    Tile.setTileSize(70);
    this.gameService =new GameService(vsai,aivsai,aistarts,rule_AllDama,rule_forceKill,tablesize);
    generateGraphicalContent();
    setTooltips();
    setEventHandlers();
    ScreenAutoRefresh();
}
    /**
     * Takes inputs, and decides and calls the model's functions to handle them.
     */
    private void setEventHandlers() {

        this.view.getScreen().getScene().setOnKeyPressed(q -> {
            if (q.getCode() == KeyCode.P) {
                gameService.Pause();
            }
        });


        this.view.getTable().setOnMousePressed(e -> {


                Tile target = (Tile) e.getTarget();
               gameService.mousePress(target);
                Logger.info("Pressed on ("+target.getTileX()+","+target.getTileY()+") tile.");

            });



            this.getView().getStage().setOnHidden(e ->{
                if(this.gameService.isGameOver())
                    Logger.info("Stage Closed");
                else
                    Logger.info("Stage closed before the game ended");
            });

    }

    /**
     * Graphically refreshes the screen .
     */
    private void refresh(){
        {
            gameService.getTiles().forEach(o ->
            {o.setStroke(null);
            if(o.getDisk()!=null && o.getDisk().isDama())
            {
                o.getDisk().setStrokeWidth(8);
                o.getDisk().setStrokeType(StrokeType.INSIDE);
                if(o.getDisk().getFill()== Color.BLACK)
                {
                    o.getDisk().setStroke(Color.rgb(40,40,40));
                }
                else
                {
                    o.getDisk().setStroke(Color.rgb(230,230,230));
                }
            }
            });


            ArrayList<Tile> tiles= gameService.getMoveSetTilesOfSelectedTile(gameService.getTargetTiles());
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
            ArrayList<Tile> tiles = gameService.getTiles();

            tiles.forEach(e ->{
                if(e.getDisk()!=null) {
                    e.getDisk().setCenterY(e.getCenterY());
                    e.getDisk().setCenterX(e.getCenterX());
                }
            });

            gameService.getOpponentsDisks().stream().forEach(o -> {
                if (!tiles.stream().filter(o2 -> o2.getDisk() == o).findFirst().isPresent()) {
                    view.getTable().getChildren().remove(o);
                }
            });

            gameService.getPlayersDisks().stream().forEach(o -> {
                if (!tiles.stream().filter(o2 -> o2.getDisk() == o).findFirst().isPresent()) {
                    view.getTable().getChildren().remove(o);
                }
            });
        }
        if(gameService.isGameOver()) {
            Logger.info("Game has ended.");
            timeline.stop();
            if (gameService.isBlackPlayerLost() && gameService.isTraditional()) {
                XMLManager manager = new XMLManager();
                int aliveDisks = 0;
                for (Tile tile : gameService.getTiles()) {
                    if (gameService.getOpponentsDisks().contains(tile.getDisk())) {
                        aliveDisks++;
                    }
                }
                NewRecordView view = new NewRecordView((int) (80 - gameService.getTurn().value) + aliveDisks * 5);
                if (manager.isHighEnough(new Record("", (int) (80 - gameService.getTurn().value) + aliveDisks * 5)) && gameService.isAIstartsthegame()) {
                    view.getCreate().setOnAction(e -> {
                        Record a = new Record(view.getSizeText().getText(), view.getScore());
                        manager.addRecord(a);
                        view.getStage().close();
                    });
                } else {
                    view.getCreate().setDisable(true);
                    view.getSizeText().setDisable(true);
                }
                view.getClose().setOnAction(e -> {
                    view.getStage().close();
                });
                view.getStage().show();


            } else if (gameService.isWhitePlayerLost()&& gameService.isTraditional()) {
                XMLManager manager = new XMLManager();
                int aliveDisks = 0;
                for (Tile tile : gameService.getTiles()) {
                    if (gameService.getPlayersDisks().contains(tile.getDisk())) {
                        aliveDisks++;
                    }
                }
                NewRecordView view = new NewRecordView((int) (80 - gameService.getTurn().value) + aliveDisks * 5);

                if (manager.isHighEnough(new Record("", (int) (80 - gameService.getTurn().value) + aliveDisks * 5)) && !gameService.isAIstartsthegame()) {

                    view.getCreate().setOnAction(e -> {
                        Record a = new Record(view.getSizeText().getText(), view.getScore());
                        manager.addRecord(a);
                        view.getStage().close();
                    });
                } else {
                    view.getCreate().setDisable(true);
                    view.getSizeText().setDisable(true);
                }
                view.getClose().setOnAction(e -> {
                    view.getStage().close();
                });


                view.getStage().show();
            }
        }


    }


    /**
     * Automatically refreshes the screen, 10 times a second.
     */
    private void ScreenAutoRefresh(){
        timeline=new Timeline();
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.getKeyFrames().add(
                new KeyFrame(Duration.millis(1000/10),
                        new EventHandler<ActionEvent>() {

                            @Override
                            public void handle(ActionEvent t) {
                                refresh();
                            }
                        },
                        new KeyValue[0])
        );

        timeline.playFromStart();
    }


    /**
     * Sets up the tooltips for the board's tiles.
     */
    public void setTooltips(){
        gameService.getTiles().forEach(o ->{
            o.setTooltip(new Tooltip("Tile:("+o.getTileX()+","+o.getTileY()+")"));
        });
        gameService.getTiles().stream().forEach(o->{
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
        this.gameService.getTiles().forEach( o ->{
            if((o.getTileX()+o.getTileY())%2==0)
            {
                o.setFill(Color.rgb(240,175,120));
            }
            else
            {
                o.setFill(Color.rgb(200,120,60));
            }
            o.setX(o.getTileX()*Tile.getTilesize());
            o.setY(o.getTileY()*Tile.getTilesize());
            o.setWidth(Tile.getTilesize());
            o.setHeight(Tile.getTilesize());
            o.setCenterX(o.getTileX()*Tile.getTilesize()+Tile.getTilesize()/2);
            o.setCenterY(o.getTileY()*Tile.getTilesize()+Tile.getTilesize()/2);
        });

        this.gameService.getPlayersDisks().forEach(o ->{
            o.setCenterX(o.getStarterPos().getCenterX());
            o.setCenterY(o.getStarterPos().getCenterY());
            o.setFill(Color.BLACK);
        });

        this.gameService.getOpponentsDisks().forEach(o ->{
            o.setCenterX(o.getStarterPos().getCenterX());
            o.setCenterY(o.getStarterPos().getCenterY());
            o.setFill(Color.WHITE);
        });

        this.gameService.getTiles().forEach(o->{
            this.view.getTable().getChildren().add(o);
        });
        this.gameService.getOpponentsDisks().forEach(o ->{
            this.view.getTable().getChildren().add(o);
        });
        this.gameService.getPlayersDisks().forEach(o ->{
            this.view.getTable().getChildren().add(o);
        });
    }





    /**
     * The view's getter.
     * @return returns the game's main view
     */
    public GameView getView() {
        return view;
    }

    /**
     * Getter of GameService.
     * @return Returns gameService.
     */
    public GameService getGameService() {
        return gameService;
    }

    /**
     * Getter of timeline.
     * @return Returns timeline.
     */
    public Timeline getTimeline() {
        return timeline;
    }
}
