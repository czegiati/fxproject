package game;

import game.Controller.GameController;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override

    public void start(Stage primaryStage) throws Exception{
        GameController controller=new GameController();
        controller.getView().getStage().show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
