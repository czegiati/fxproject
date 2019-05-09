package game.View;

import game.Controller.GameController;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import static javafx.geometry.Pos.CENTER;

/**
 * View for the new game window.
 */
public class NewGameView {
    /**
     * Stage of the new game window.
     */
    Stage stage=new Stage();
    /**
     * Grid, the UI elements are placed on it.
     */
    GridPane grid=new GridPane();
    /**
     * Input text, determining the size of the gameboard.
     */
    TextField sizeText=new TextField();
    /**
     * Checkbox for the against AI game rule.
     */
    CheckBox againstAICheckbox=new CheckBox();
    /**
     * Checkbox for the AI starts game rule.
     */
    CheckBox AIstarts=new CheckBox();
    /**
     * Checkbox for the AI vs Ai game rule.
     */
    CheckBox AIvsAI=new CheckBox();
    /**
     * Checkbox  for the all Dama rule.
     */
    CheckBox allDamaRure=new CheckBox();
    /**
     * Checkbox for the force kill rule.
     */
    CheckBox forcekillrule=new CheckBox();
    /**
     * Create button.
     */
    Button create=new Button("Create");
    /**
     * Close button.
     */
    Button close = new Button("Close");
    /**
     * "Traditional" button.
     */
    Button setScored=new Button ("Traditional");

    /**
     * Constructor without parameters.
     */
    public NewGameView(){
        stage.setScene(new Scene(grid));
        stage.setTitle("Creating new game...");

        grid.setHgap(10);
        grid.setHgap(10);

        Text text1=new Text("Board size:");
        grid.add(text1,0,0);
        grid.add(sizeText,1,0);
        sizeText.appendText("8");
        sizeText.setTooltip(new Tooltip("Size of the board, minimum 7 maximum 12, if playing against AI then it must be 8."));
        sizeText.getTooltip().setShowDuration(Duration.INDEFINITE);
        sizeText.getTooltip().setShowDelay(Duration.millis(0));
        sizeText.setAlignment(CENTER);
        sizeText.setPrefWidth(40);

        grid.add(new Text("Playing against AI: "),0,1);
        grid.add(againstAICheckbox,1,1);
        againstAICheckbox.setSelected(true);



        grid.add(new Separator() ,0,2,2,1);
        grid.add(new Text("Let the AI start: "),0,3);
        grid.add(AIstarts,1,3);
        AIstarts.setSelected(false);


        grid.add(new Text("AI against AI:"),0,4);
        grid.add(AIvsAI,1,4);
        AIvsAI.setSelected(false);


        grid.add(new Separator(),0,5,2,1);

        grid.add(new Text("All Dama Rule: "),0,6);
        grid.add(allDamaRure,1,6);
        allDamaRure.setTooltip(new Tooltip("Every Disk starts as kings at the beginning.\n" +
                                        " There is no reward for reaching the other side of the board."));
        allDamaRure.getTooltip().setShowDuration(Duration.INDEFINITE);
        allDamaRure.getTooltip().setShowDelay(Duration.millis(0));

        grid.add(new Text("Force Kill rule:"),0,7);
        grid.add(forcekillrule,1,7);
        forcekillrule.setSelected(true);
        forcekillrule.setTooltip(new Tooltip("When it is your turn, and you are able to kill a Disk\n" +
                                                    ",then this rule forces you to do so."));
        forcekillrule.getTooltip().setShowDuration(Duration.INDEFINITE);
        forcekillrule.getTooltip().setShowDelay(Duration.millis(0));

        HBox pane =new HBox(15);
        pane.getChildren().addAll(create,close,setScored);

        setScored.setTooltip(new Tooltip("Sets the game up according to the game's original rules.\n" +
                                                "You can get onto the High Score Board if you play in this mode."));
        setScored.getTooltip().setShowDuration(Duration.INDEFINITE);
        setScored.getTooltip().setShowDelay(Duration.millis(0));


        grid.add(pane,0,8,2,1);







        grid.setAlignment(CENTER);
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.getChildren().forEach( e ->
        {
            GridPane.setMargin(e, new Insets(5, 10, 5, 10));
            GridPane.setHalignment(e, HPos.CENTER);
            GridPane.setValignment(e, VPos.CENTER);
        });
        this.stage.setAlwaysOnTop(true);
    }

    /**
     * Getter of the new Game's stage.
     * @return Returns the new game's stage.
     */
    public Stage getStage() {
        return stage;
    }

    /**
     * Getter of the textfield.
     * @return Returns the textfield.
     */
    public TextField getSizeText() {
        return sizeText;
    }

    /**
     * Getter of the againstAI checkbox.
     * @return Return the Checkbox.
     */
    public CheckBox getAgainstAICheckbox() {
        return againstAICheckbox;
    }
    /**
     * Getter of the AIstarts checkbox.
     * @return Return the Checkbox.
     */
    public CheckBox getAIstarts() {
        return AIstarts;
    }
    /**
     * Getter of the AIvsAI checkbox.
     * @return Return the Checkbox.
     */
    public CheckBox getAIvsAI() {
        return AIvsAI;
    }
    /**
     * Getter of the AllDamaRule checkbox.
     * @return Return the Checkbox.
     */
    public CheckBox getAllDamaRure() {
        return allDamaRure;
    }
    /**
     * Getter of the ForceKill checkbox.
     * @return Return the Checkbox.
     */
    public CheckBox getForcekillrule() {
        return forcekillrule;
    }
    /**
     * Getter of the create button.
     * @return Return the Button.
     */
    public Button getCreate() {
        return create;
    }
    /**
     * Getter of the close button.
     * @return Return the Button.
     */
    public Button getClose() {
        return close;
    }
    /**
     * Getter of the Traditional button.
     * @return Return the Button.
     */
    public Button getSetScored() {
        return setScored;
    }
}
