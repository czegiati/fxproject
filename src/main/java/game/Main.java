package game;

import game.Controller.GameController;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Main class of the Application. Does not contain any fields or methods expect the main and the start methods.
 */
public class Main extends Application {

    /**
     * In a JavaFX Application the start method is used as the main method
     * @param primaryStage Main stage/window of the application.
     */
    @Override
    public void start(Stage primaryStage){
        GameController controller=new GameController();
        controller.getView().getStage().show();
    }

    /**
     * Main method of the application.
     * @param args Arguments passed in.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
