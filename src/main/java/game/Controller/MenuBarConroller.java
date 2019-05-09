package game.Controller;

import game.Model.Database.XMLManager;
import game.Model.Record;
import game.View.GameMenuBar;
import game.View.GameView;
import game.View.NewGameView;
import game.View.ScoreBoard;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.pmw.tinylog.Logger;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Controls the game's menubar
 */
public class MenuBarConroller {
    /**
     * Games main logic.
     */
    GameController gamecontroller;
    /**
     * The game's menubar view.
     */
    private GameMenuBar menubar;
    /**
     * The main game controller.
     */
    MainController MainController;
    /**
     * Constructor without parameters parameters
     */
    public MenuBarConroller(){
        MainController =new MainController();
        this.menubar=MainController.getView().getMenubar();
        this.gamecontroller=MainController.getGamecontroller();
        setUp();
        this.MainController.getView().getStage().show();
    }

    /**
     * Configures the menus on the menubar
     */
    public void setUp(){
        menubar.getNewGame().setOnAction(e ->{
            NewGameView a=new NewGameView();
            a.getStage().show();
            Logger.info("New Game Window has been created.");
            a.getSizeText().setOnKeyTyped( o ->{
                OnlyNumericInput(o, a.getSizeText());
            });


            a.getCreate().setOnAction( p-> {
                if(a.getSizeText().getText().equals("")) {a.getSizeText().setText("8"); Logger.info("No input found, default input as used. ");}
                if(a.getSizeText().getText().chars().allMatch(o -> Character.isDigit(o)))
                    if(Integer.parseInt(a.getSizeText().getText())>=7 && Integer.parseInt(a.getSizeText().getText())<=12){
                        if(a.getAgainstAICheckbox().isSelected() && Integer.parseInt(a.getSizeText().getText()) != 8)
                            return;

                        a.getStage().close();
                        if(MainController.getTimeline()!=null)
                        MainController.getTimeline().stop();
                        MainController.getView().getStage().close();
                        this.MainController =
                                new MainController(
                                        a.getAgainstAICheckbox().isSelected()
                                        , a.getAIvsAI().isSelected()
                                        , a.getAIstarts().isSelected()
                                        , a.getAllDamaRure().isSelected()
                                        , a.getForcekillrule().isSelected()
                                        , Integer.parseInt(a.getSizeText().getText()));
                        this.menubar=MainController.getView().getMenubar();
                        setUp();
                        MainController.getView().getStage().show();
                        Logger.info("New game has been created.");
                    }
                    else{
                        Logger.info("Wrong numeric input.");
                    }
                else{
                    Logger.info("Wrong character input.");
                }

            });


            a.getClose().setOnAction( q ->{
                a.getStage().close();
                Logger.info("New Game Window has been closed.");
            });


            a.getSetScored().setOnAction( q ->{
                a.getAgainstAICheckbox().setSelected(true);
                a.getSizeText().setText("8");
                a.getAllDamaRure().setSelected(false);
                a.getAIstarts().setSelected(false);
                a.getAIvsAI().setSelected(false);
                a.getForcekillrule().setSelected(true);
                Logger.info("Game Options has been set to traditional.");
            });
        });


        menubar.getQuit().setOnAction(e ->{
            this.MainController.getView().getStage().close();
            Logger.info("Quited from the game.");
        });


        menubar.getEventlog().setDisable(true);



        menubar.getScoreBoard().setOnAction(e ->{
            ScoreBoard scoreboard=new ScoreBoard();
            XMLManager manager=new XMLManager();
            ArrayList<Record>records=manager.getRecordList();
            records=(ArrayList<Record>) records.stream().sorted(Comparator.comparing(Record::getScore).reversed()).collect(Collectors.toList());
            ArrayList<String> recordStringList=new ArrayList<>();
            int index=0;
            for(Record record:records)
            {
                index++;
                recordStringList.add(index+".\t"+record.getName()+" :\t\t "+record.getScore()+" points");
            }
            ObservableList<String> items= FXCollections.observableArrayList(recordStringList);

            scoreboard.getListView().setItems(items);
            scoreboard.getListView().autosize();
            scoreboard.getSceneParent().autosize();
            scoreboard.getStage().getScene().getWindow().sizeToScene();
            scoreboard.getStage().show();
            Logger.info("ScoreBoard Window has been created.");
        });


        menubar.getSetupInfo().setOnAction(e ->{
            Stage stage=new Stage();
            VBox box=new VBox();
            stage.setScene(new Scene(box));
            String informations="";
            informations+=("Game's setup information:\t\t\t\n");
            if(gamecontroller.isAgainstAI()) informations+="\t*playing against AI"; else informations+="\t*Playing against another player";
            if(gamecontroller.isAIagaintAI() && gamecontroller.isAgainstAI()) informations+="\n\t*AI playing against AI";
            if(gamecontroller.isAIstartsthegame() && gamecontroller.isAgainstAI()) informations+="\n\t*AI started the game";
            if(gamecontroller.isRule_AllDama()) informations+="\n\t*All disks started as kings";
            if(gamecontroller.isRule_forceKill()) informations+="\n\t*Player is forced to kill an enemy disk, if is able to do so";
            if(gamecontroller.isTraditional()) informations+="\n\n\t *is a traditional game"; else informations+="\n\n\t *is not a traditional game";
            Text text= new Text(informations);
            box.getChildren().add(text);
            box.setAlignment(Pos.CENTER);
            stage.setAlwaysOnTop(true);
            stage.setWidth(400);
            stage.setHeight(170);
            stage.setTitle("Game Setup Information");
            stage.show();
            Logger.info("Information Window has been created.");
        });

    }

    /**
     * Forces the textfield parameter to only accept numeric characters
     * @param event pressed  event key's code
     * @param textfield the text field that needs to be configured
     */
    public void OnlyNumericInput(KeyEvent event, TextField textfield){
        if(Character.isLetter(event.getCharacter().charAt(0))
                || Character.isDefined(event.getCharacter().charAt(0))
                && !event.getCharacter().matches("\b")
                && !Character.isDigit(event.getCharacter().charAt(0) )){
            textfield.deleteText(textfield.getText().length()-1,textfield.getText().length());
        }
    }

    public GameView getView(){
        return MainController.getView();
    }


}