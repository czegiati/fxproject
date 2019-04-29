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

public class NewRecordView {
    Stage stage=new Stage();
    GridPane grid=new GridPane();
    GameController controller;

    TextField sizeText=new TextField();
    CheckBox againstAICheckbox=new CheckBox();
    CheckBox AIstarts=new CheckBox();
    CheckBox AIvsAI=new CheckBox();
    CheckBox allDamaRure=new CheckBox();
    CheckBox forcekillrule=new CheckBox();

    Button create=new Button("Create");
    Button close = new Button("Close");
    Button setScored=new Button ("Traditional");

    public NewRecordView(GameController controller){
        this.controller=controller;
        stage.setScene(new Scene(grid));
        stage.setTitle("Creating new game...");

        grid.setHgap(10);
        grid.setHgap(10);

        Text text1=new Text("Board size:");
        grid.add(text1,0,0);
        grid.add(sizeText,1,0);
        sizeText.appendText("8");
        sizeText.setAlignment(CENTER);
        sizeText.setPrefWidth(40);


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

    public Stage getStage() {
        return stage;
    }


    public GridPane getGrid() {
        return grid;
    }

    public GameController getController() {
        return controller;
    }

    public TextField getSizeText() {
        return sizeText;
    }

    public CheckBox getAgainstAICheckbox() {
        return againstAICheckbox;
    }

    public CheckBox getAIstarts() {
        return AIstarts;
    }

    public CheckBox getAIvsAI() {
        return AIvsAI;
    }

    public CheckBox getAllDamaRure() {
        return allDamaRure;
    }

    public CheckBox getForcekillrule() {
        return forcekillrule;
    }

    public Button getCreate() {
        return create;
    }

    public Button getClose() {
        return close;
    }

    public Button getSetScored() {
        return setScored;
    }
}
