package game.Controller;

import game.Model.Database.XMLManager;
import game.View.GameMenuBar;
import game.View.NewGameView;
import game.View.ScoreBoard;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

public class MenuBarConroller {

    private GameMenuBar menubar;
    private GameController gamecontroller;

    MenuBarConroller(GameMenuBar menubar,GameController gc){
        this.menubar=menubar;
        this.gamecontroller=gc;
        XMLManager a=new XMLManager();
        setUp();
    }

    public void setUp(){


        menubar.getNewGame().setOnAction(e ->{
            NewGameView a=new NewGameView(this.gamecontroller);
            a.getStage().show();

            {}
            a.getSizeText().setOnKeyTyped( o ->{
                OnlyNumericInput(o, a.getSizeText());
            });


            a.getCreate().setOnAction( p-> {
                    if(Integer.parseInt(a.getSizeText().getText())>=7 && Integer.parseInt(a.getSizeText().getText())<=12){
                            if(a.getAgainstAICheckbox().isSelected() && Integer.parseInt(a.getSizeText().getText()) != 8)
                                return;

                            a.getStage().close();
                            gamecontroller.getView().getStage().close();
                            this.gamecontroller =
                                    new GameController(
                                             a.getAgainstAICheckbox().isSelected()
                                            , a.getAIvsAI().isSelected()
                                            , a.getAIstarts().isSelected()
                                            , a.getAllDamaRure().isSelected()
                                            , a.getForcekillrule().isSelected()
                                            , Integer.parseInt(a.getSizeText().getText()));
                            gamecontroller.getView().getStage().show();

                    }
            });

            a.getClose().setOnAction( q ->{
                a.getStage().close();
            });

            a.getSetScored().setOnAction( q ->{
                a.getAgainstAICheckbox().setSelected(true);
                a.getSizeText().setText("8");
                a.getAllDamaRure().setSelected(false);
                a.getAIstarts().setSelected(false);
                a.getAIvsAI().setSelected(false);
                a.getForcekillrule().setSelected(true);
            });
        });


        menubar.getQuit().setOnAction(e ->{
            this.gamecontroller.getView().getStage().close();
        });

        menubar.getEventlog().setOnAction(e ->{
            this.gamecontroller.getEventLog().detailedLogWindow();
        });

        menubar.getScoreBoard().setOnAction(e ->{
            ScoreBoard scoreboard=new ScoreBoard();
            scoreboard.getStage().show();

        });


    }

    public void OnlyNumericInput(KeyEvent event, TextField textfiled){
        if(Character.isLetter(event.getCharacter().charAt(0))
                || Character.isDefined(event.getCharacter().charAt(0))
                && !event.getCharacter().matches("\b")
                && !Character.isDigit(event.getCharacter().charAt(0) )){
            textfiled.deleteText(textfiled.getText().length()-1,textfiled.getText().length());
        }
    }


}
