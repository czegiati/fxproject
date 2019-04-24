package game.View;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

public class GameMenuBar {
    private MenuBar menubar= new MenuBar();
    private Menu FileMenu=new Menu("File");
        private MenuItem Quit=new MenuItem("Quit");
        private MenuItem newGame=new MenuItem("New Game");
    private Menu ViewMenu = new Menu("View");
        private MenuItem scoreBoard=new MenuItem("Scoreboard");
        private MenuItem eventlog =new MenuItem("EventLog");
        private MenuItem setupInfo=new MenuItem("Information");
    GameMenuBar(){
        FileMenu.getItems().addAll(newGame,Quit);
        ViewMenu.getItems().addAll(eventlog,scoreBoard,setupInfo);
        this.menubar.getMenus().addAll(FileMenu,ViewMenu);
    }

    public MenuBar get(){
        return this.menubar;
    }

    public Menu getFileMenu() {
        return FileMenu;
    }

    public MenuItem getQuit() {
        return Quit;
    }

    public MenuItem getNewGame() {
        return newGame;
    }

    public Menu getViewMenu() {
        return ViewMenu;
    }

    public MenuItem getScoreBoard() {
        return scoreBoard;
    }

    public MenuItem getEventlog() {
        return eventlog;
    }

    public MenuItem getSetupInfo() {
        return setupInfo;
    }
}
