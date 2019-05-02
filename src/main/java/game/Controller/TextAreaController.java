package game.Controller;

import game.Model.INT;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * Used to store and read previous actions in the game
 */
public class TextAreaController {
    /**
     * The graphical TextArea on the bottom of the game's window.
     */
    TextArea textArea;
    /**
     * String containing every game event.
     */
    String allText= new String("");

    /**
     * Reference to the current turn integer.
     */
    INT turn;

    /**
     * Constructor with parameters
     * @param area lists the previous event
     * @param turn current turn
     */
    TextAreaController(TextArea area, INT turn) {
        this.textArea = area;
        textArea.setStyle("-fx-opacity: 1.0;");
        textArea.setPrefHeight(25);
        this.turn=turn;
    }

    /**
     * Appends to the end of the
     * @param string current events discretion
     */
    public void append(String string) {
        if (turn.value%2==0)
        {
            allText=allText+("\n("+turn.value+")WHITE: "+string);
        this.textArea.setText("("+turn.value+")WHITE: "+string);
        this.textArea.setScrollTop(Double.MAX_VALUE);
        }
        else
            {
                allText=allText+("\n("+turn.value+")BLACK: "+string);
                this.textArea.setText("("+turn.value+")BLACK: "+string);
                this.textArea.setScrollTop(Double.MAX_VALUE);
            }
}

    /**
     * Creates a new window containing a Text Area describing the current game's events until this point
     */
    public void detailedLogWindow(){
        Stage stage=new Stage();
        stage.setScene(new Scene(new HBox(new TextArea(allText))));
        stage.setAlwaysOnTop(true);
        stage.show();
    }



}
