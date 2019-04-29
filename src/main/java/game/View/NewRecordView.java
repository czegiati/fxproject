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

public class NewRecordView {
    Stage stage=new Stage();
    GridPane grid=new GridPane();
    Integer score;
    TextField nameText=new TextField();

    Button create=new Button("Add");
    Button close = new Button("Cancel");

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

    public Stage getStage() {
        return stage;
    }

    public TextField getSizeText() {
        return nameText;
    }

    public Button getCreate() {
        return create;
    }

    public Button getClose() {
        return close;
    }

    public Integer getScore() {
        return score;
    }
}
