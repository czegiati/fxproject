package game.View;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GameView {
    Group table=new Group();
    VBox screen = new VBox();
    Stage stage=new Stage();
    TextArea eventLog=new TextArea();

    GameMenuBar menubar=new GameMenuBar();

    public GameView(){
        eventLog.setDisable(true);

        screen.getChildren().addAll(menubar.get(),table,eventLog);
        stage.setScene(new Scene(screen));
    }

    public Group getTable() {
        return table;
    }

    public void setTable(Group table) {
        this.table = table;
    }

    public VBox getScreen() {
        return screen;
    }

    public void setScreen(VBox screen) {
        this.screen = screen;
    }

    public GameMenuBar getMenubar() {
        return menubar;
    }

    public void setMenubar(GameMenuBar menubar) {
        this.menubar = menubar;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public TextArea getEventLog() {
        return eventLog;
    }

    public void setEventLog(TextArea eventLog) {
        this.eventLog = eventLog;
    }
}
