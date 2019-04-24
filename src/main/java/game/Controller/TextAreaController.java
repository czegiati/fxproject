package game.Controller;

import game.Model.INT;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class TextAreaController {
    TextArea textArea;
    boolean whiteplayer = true;
    String allText= new String("");
    INT turn;

    TextAreaController(TextArea area, INT turn) {
        this.textArea = area;
        textArea.setStyle("-fx-opacity: 1.0;");
        textArea.setPrefHeight(25);
        this.turn=turn;
    }

    public void append(String string) {
        if (turn.value%2==0)
        {
            allText=allText+("\n"+"WHITE: "+string);
        this.textArea.setText("WHITE: "+string);
        this.textArea.setScrollTop(Double.MAX_VALUE);
        }
        else
            {
                allText=allText+("\n"+"BLACK: "+string);
                this.textArea.setText("BLACK: "+string);
                this.textArea.setScrollTop(Double.MAX_VALUE);
            }
}
    public void detailedLogWindow(){
        Stage stage=new Stage();
        stage.setScene(new Scene(new HBox(new TextArea(allText))));
        stage.setAlwaysOnTop(true);
        stage.show();
    }



}
