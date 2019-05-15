package game.View;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Main view if the game.
 */
public class GameView {
    /**
     * The group containing the gameboard.
     */
    Group table=new Group();
    /**
     *  The main screen, directly bellow the Scene object.
     */
    VBox screen = new VBox();
    /**
     * The main stage of the game.
     */
    Stage stage=new Stage();
    /**
     * The eventlog at the bottom of the main window.
     */
    TextArea eventLog=new TextArea();
    /**
     * MenuBar class, created to store the menubar, and its submenus and menu items.
     */
    GameMenuBar menubar=new GameMenuBar();

    /**
     * Constructor without parameters.
     */
    public GameView(){
        eventLog.setDisable(true);
        screen.getChildren().addAll(menubar.get(),table);
        stage.setScene(new Scene(screen));
    }

    /**
     * Getter of group containing the gameboard items.
     * @return returns the gameboards's group.
     */
    public Group getTable() {
        return table;
    }

    /**
     * Getter of the main screen.
     * @return Returns the main screen.
     */
    public VBox getScreen() {
        return screen;
    }

    /**
     * Getter of the MenuBar object.
     * @return Returns the menubar object.
     */
    public GameMenuBar getMenubar() {
        return menubar;
    }

    /**
     * etter of the main stage ogf the game.
     * @return Returns the main stage.
     */
    public Stage getStage() {
        return stage;
    }

    /**
     * Setter of the main stage of the game.
     * @param stage Stage of the game's main window.
     */
    public void setStage(Stage stage) {
        table=new Group();
        screen = new VBox();
        eventLog=new TextArea();
        menubar=new GameMenuBar();
        this.stage = stage;
        eventLog.setDisable(true);
        screen.getChildren().addAll(menubar.get(),table,eventLog);
        stage.setScene(new Scene(screen));
    }

    /**
     * Getter of the Eventlog at the bottom of the window.
     * @return returns the EventLog TextArea at the bottom of the window.
     */
    public TextArea getEventLog() {
        return eventLog;
    }

}
