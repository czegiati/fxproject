package game.View;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

/**
 * Main view's menubar, the menubar javafx object, with all of its Submenus, and menu items.
 */
public class GameMenuBar {
    /**
     * Menubar at the top of the window.
     */
    private MenuBar menubar= new MenuBar();
    /**
     * File menu on the menubar.
     */
    private Menu FileMenu=new Menu("File");
    /**
     * Quit menu item of the File menu.
     */
        private MenuItem Quit=new MenuItem("Quit");
    /**
     * New Game menu item of the File menu.
     */
    private MenuItem newGame=new MenuItem("New Game");
    /**
     * View menu of the menubar.
     */
    private Menu ViewMenu = new Menu("View");
    /**
     * Scoreboard menu item of the View menu.
     */
        private MenuItem scoreBoard=new MenuItem("Scoreboard");
    /**
     * EventLog menu item of the View menu.
     */
    private MenuItem eventlog =new MenuItem("EventLog");
    /**
     * Information menu itm of the View menu.
     */
        private MenuItem setupInfo=new MenuItem("Information");

    /**
     * Constructor without parameters .
     */
    public GameMenuBar(){
        FileMenu.getItems().addAll(newGame,Quit);
        ViewMenu.getItems().addAll(eventlog,scoreBoard,setupInfo);
        this.menubar.getMenus().addAll(FileMenu,ViewMenu);
    }

    /**
     * Getter of the menubar.
     * @return Returns the menubar on the top of the window.
     */
    public MenuBar get(){
        return this.menubar;
    }

    /**
     * Getter of the Quit menu item.
     * @return Returns the Quit menu item.
     */
    public MenuItem getQuit() {
        return Quit;
    }

    /**
     * Getter og the new game menu item.
     * @return Returns the new game menu item.
     */
    public MenuItem getNewGame() {
        return newGame;
    }

    /**
     * Getter of the Scoreboard menu item.
     * @return Returns the scoreboard menu item.
     */
    public MenuItem getScoreBoard() {
        return scoreBoard;
    }

    /**
     * Getter of hte eventlog menu item.
     * @return Returns the eventlog menu item.
     */
    public MenuItem getEventlog() {
        return eventlog;
    }

    /**
     * Getter of the information menu item.
     * @return Returns the information menu item.
     */
    public MenuItem getSetupInfo() {
        return setupInfo;
    }
}
