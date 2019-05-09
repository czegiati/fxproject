package game.View;

import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * View of the ScoreBoard window.
 */
public class ScoreBoard {
    /**
     * List containing the top records.
     */
    ListView<String> ListView=new ListView<>();
    /**
     * Stage of the ScoreBoard.
     */
    Stage stage =new Stage();
    /**
     * Main screen of the scoreboard.
     */
    VBox SceneParent=new VBox(10);

    /**
     * Constructor without parameters.
     */
    public ScoreBoard(){
        stage.setTitle("High Scores");
        stage.setAlwaysOnTop(true);
        stage.setScene(new Scene(SceneParent));
        SceneParent.getChildren().add(ListView);
        ListView.setPrefHeight(400);
        ListView.setPrefWidth(400);
    }

    /**
     * Getter of the ListView.
     * @return Returns the ListView.
     */
    public ListView<String> getListView() {
        return ListView;
    }

    /**
     * Getter of the stage.
     * @return Return the stage.
     */
    public Stage getStage() {
        return stage;
    }

    /**
     * Getter of the screen.
     * @return Returns the screen.
     */
    public VBox getSceneParent() {
        return SceneParent;
    }

}
