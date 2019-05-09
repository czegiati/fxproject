package game.View;


import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import static javafx.geometry.Pos.CENTER;

/**
 * New Record window, opened when won a game and the game was played with traditional rules.
 */
public class NewRecordView {
    /**
     * Stage og the window.
     */
    Stage stage=new Stage();
    /**
     * Grid, where the UI elements are placed on.
     */
    GridPane grid=new GridPane();
    /**
     * A number, written in a Text object when opened.
     */
    Integer score;
    /**
     * Where the record's name should be written.
     */
    TextField nameText=new TextField();
    /**
     * Add button on the bottom of the window.
     */
    Button create=new Button("Add");
    /**
     * Close Button on the bottom of the window.
     */
    Button close = new Button("Cancel");

    /**
     * Constructor with parameters.
     * @param score Achieved score in the game.
     */
    public NewRecordView(Integer score){
        this.score=score;
        stage.setScene(new Scene(grid));
        stage.setTitle("New HighScore");

        grid.setHgap(10);
        grid.setHgap(10);

        Text text1=new Text("Name:");
        grid.add(text1,0,0);
        grid.add(nameText,1,0);
        //nameText.setText("Player");
        nameText.setAlignment(CENTER);

        Text text2=new Text("Score:");
        grid.add(text2,0,1);
        grid.add(new Text(score.toString()),1,1);


        HBox pane =new HBox(15);
        pane.getChildren().addAll(create,close);
        pane.setAlignment(CENTER);



        grid.add(pane,0,2,2,1);

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
     * Getter of the stage.
     * @return Returns the stage.
     */
    public Stage getStage() {
        return stage;
    }

    /**
     * Getter of the textfield of the record's name.
     * @return Return the textfield.
     */
    public TextField getSizeText() {
        return nameText;
    }

    /**
     * Getter of the Create button.
     * @return Returns the create button.
     */
    public Button getCreate() {
        return create;
    }

    /**
     * Getter of the close button.
     * @return Returns the close button.
     */
    public Button getClose() {
        return close;
    }

    /**
     * Getter of the score integer.
     * @return Returns the score integer.
     */
    public Integer getScore() {
        return score;
    }
}
