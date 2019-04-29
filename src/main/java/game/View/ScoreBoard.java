package game.View;

import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ScoreBoard {
    ListView<String> ListView=new ListView<>();
    Stage stage =new Stage();
    VBox SceneParent=new VBox(10);

    public ScoreBoard(){
        stage.setTitle("High Scores");
        stage.setAlwaysOnTop(true);
        stage.setScene(new Scene(SceneParent));
        SceneParent.getChildren().add(ListView);
        ListView.setPrefHeight(400);
        ListView.setPrefWidth(400);
    }

    public javafx.scene.control.ListView<String> getListView() {
        return ListView;
    }

    public void setListView(javafx.scene.control.ListView<String> listView) {
        ListView = listView;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public VBox getSceneParent() {
        return SceneParent;
    }

    public void setSceneParent(VBox sceneParent) {
        SceneParent = sceneParent;
    }
}
