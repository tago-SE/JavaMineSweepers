/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

/**
 *
 * @author T.Redaelli, Nordlund
 */
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToolBar;
import javafx.scene.effect.Glow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.effect.Lighting;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;

import model.Model;
import model.Zone;
import model.Score;

public final class View extends BorderPane implements Observer,Serializable{
    private final ToolBar toolbar = new ToolBar();
    
    private Stage stage;
    private final Model model;
    private final VBox vbox = new VBox();               // Move to menu section?
    private Tile[][] tile;                         // Are we removing this or what?
    private GridPane gridPane;
    private HBox Hbox = new HBox();
    private Score score;
    private Label label ;
    /**
     * Constructor for single view
     * @param someStage
     * @param model_ 
     * also provides scene with root node.
     */
    public View(Stage someStage, Model model_) {
        model = model_;
        stage = someStage;
        this.setStyle("-fx-background-color: #D8BFD8;");
        score = new Score();
        Scene scene = new Scene(this);
        initGridInterface();
        initMenu();
        initToolbar();
        
        stage.setTitle("Minesweepers");
        stage.setScene(scene);
        stage.show();
        
    }
    /**
     * initializes toolbar. 
     * Where we see the score
     */
    public void initToolbar(){
        Hbox.setPadding(new Insets(10, 20, 20, 50));
        Hbox.setSpacing(10);
        //Method of using CSS on controller superclass
        //insipired by jewelsea
        //http://stackoverflow.com/questions/25042517/javafx-2-resizable-rectangle-containing-text
        label = new Label("00:00");
        label.setStyle("-fx-font-size: 25px; -fx-text-fill: yellow;-fx-background-color: black;-fx-padding: 10px");
        label.setEffect(new Glow());
        label.setMaxWidth(250);
        label.setWrapText(true);
        
        
        //vbox.setSpacing(10);
        toolbar.setEffect(new Lighting());
        toolbar.getItems().add(Hbox);
        
        Hbox.getChildren().addAll(label);
        vbox.getChildren().add(toolbar);
    }
    /**
     * initialize eventhandlers
     * @param controller 
     */
    public void addEventHandlers(Controller controller) {
        initMenuEventHandlers(controller);
    }
    
    // *************************************************************************
    // Menu
    // *************************************************************************
    private Menu menuFile = new Menu("File");
    private Menu menuOptions = new Menu("Options");
    private Menu menuHelp = new Menu("Help");
    private Menu menuDifficulty = new Menu("Difficulty");
    private MenuItem optRestart = new MenuItem("Reset");
    private MenuItem optPauseOrResume = new MenuItem("Pause");
    private MenuItem optSave = new MenuItem("Save");
    private MenuItem optLoad = new MenuItem("Load");
    private MenuItem optExit = new MenuItem("Exit");
    private MenuItem optBeginner = new MenuItem("Beginner");
    private MenuItem optIntermediate = new MenuItem("Intermediate");
    private MenuItem optAdvanced = new MenuItem("Advanced");
    /**
     * Initialize menu
     */
    private void initMenu() {
        MenuBar menuBar = new MenuBar();
        menuFile.getItems().addAll(optRestart, optPauseOrResume, optSave, optLoad, optExit);
        menuDifficulty.getItems().addAll(optBeginner, optIntermediate, optAdvanced);
        menuOptions.getItems().add(menuDifficulty);
        menuBar.getMenus().addAll(menuFile, menuOptions, menuHelp);
        vbox.getChildren().add(menuBar);
        this.setTop(vbox);
    }
    /**
     * 
     * @param controller 
     * event on beginner setting
     */
    private void initMenuEventHandlers(Controller controller) {
        optBeginner.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                controller.handleBeginnerOption();
            }
        });
        /**
     * 
     * @param controller 
     * event on beginner intermediate
     */
        optIntermediate.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                controller.handleIntermediateOption();
            }
        });
            /**
     * 
     * @param controller 
     * event on beginner advance
     */
        optAdvanced.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                controller.handleAdvancedOption();
            }
        });
            /**
     * 
     * @param controller 
     * event for resetting game
     */
        optRestart.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                controller.handleResetOption();
            }
        });
     /* 
     * @param controller 
     * event for pause/resume game
     */
        optPauseOrResume.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                controller.handlePauseOption();
            }
        });
    /* 
     * @param controller 
     * event for saving game
     */
        optSave.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Save!");
            //    FileChooser choose = new FileChooser();
            //    choose.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text doc(*.txt)", "*.txt"));
                 FileChooser chooser = new FileChooser();
	    chooser.getExtensionFilters().add(new ExtensionFilter("Edge files", "*.ser"));  
            File f = chooser.showSaveDialog(stage);
            ArrayList<String> list = new ArrayList<String>();
            list.add("thing");
            list.add("Safas");
                //if(!f.getName().contains(".")) {
                // f = new File(f.getAbsolutePath() + ".txt");
                // System.out.println("saa");
                 // }
                  if (f != null) {
	            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(f))) {
	                out.writeObject(list);
                        System.out.println("F");
	            } catch (Exception exc) {
	                exc.printStackTrace();
	            }
	        }
               }
        });
         /* 
     * @param controller 
     * event for loading game
     */
        optLoad.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Load!");
            }
        });
         /* 
     * @param controller 
     * event for exiting game
     */
        optExit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.exit(0);
            }
        });
    }
    
    // *************************************************************************
    // Grid
    // *************************************************************************
    /**
     * paint a grid with custom made
     * tiles
     */
    public void initGridInterface(){
        score.setScoreToZero();
        int xMax = model.getGrid().getNumColumns();
        int yMax = model.getGrid().getNumRows();
        if (gridPane == null)
            gridPane = new GridPane();
        else {
            this.getChildren().remove(gridPane);
            gridPane = new GridPane();
        }
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        // Configure this to match the dimensions of the grid
        gridPane.setMinSize(1500, 600);
        
        gridPane.setVgap(1);
        gridPane.setHgap(1);
        tile = new Tile[xMax][yMax];
        for (int y = 0; y < yMax; y++) {
            for (int x = 0; x < xMax; x++) {
                tile[x][y] = new Tile(30, 30);
                this.setDefaultTile(x, y);
                gridPane.add(tile[x][y], x, y);
            }
        }
        //   this.initToolbar();
        this.setBottom(gridPane);
    }
    /**
     * 
     * @param x
     * @param y
     * @returns specific tile
     */
    public Tile getTile(int x, int y) {
        return tile[x][y];
    }
    /**
     * sets tile Black
     * @param x
     * @param y 
     */
    private void setDefaultTile(int x, int y) {
        tile[x][y].setColor(Color.BLACK);
        tile[x][y].setEffect(new InnerShadow());
    }
    
    /**
     * Colors the tile red at explosion
     * @param x
     * @param y 
     */
    public void detonateTile(int x, int y) {
        tile[x][y].setColor(Color.RED);
        // tile[x][y].viewText(string);
    }
    /**
     * Flag the tiles, turn it blue
     * @param x
     * @param y 
     */
    public void flagTile(int x, int y) {
        tile[x][y].setColor(Color.BLUE);
    }
    /**
     * unflag the tile
     * @param x
     * @param y 
     */
    public void unflagTile(int x, int y) {
        this.setDefaultTile(x, y);
    }
    /**
     * reveal the tile change to safe white color
     * @param x
     * @param y 
     */
    public void revealTile(int x, int y) {
        int numberOfBombs = model.getNearbyBombs(x, y);
        if (numberOfBombs != 0) {
            tile[x][y].viewText("" + numberOfBombs, Color.BEIGE);
        }
        tile[x][y].setColor(Color.WHITE);
    }
    
    // *************************************************************************
    // Alerts
    // *************************************************************************
    /**
     * Alert menu
     */
    public void alertPause() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Paused");
        alert.setHeaderText("The game is paused.");
        alert.setContentText("Press OK to resume.");
        alert.showAndWait();
    }
    /**
     * Alert restart, when you want to reset
     * @returns a boolean
     */
    public boolean alertRestart() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Reset Confirmation");
        alert.setHeaderText("The game is about to generate a new mine field.");
        alert.setContentText("Are you ok with this?");
        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;
    }
    /**
     * pop-up window, to restart game
     * @return 
     */
    public boolean alertGameOver() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText("You lost and the game is about to generate a new mine field.");
        alert.setContentText("Are you ok with this?");
        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;
    }
    /**
     *  alert window in case of victory
     * @return 
     */    
    public String alertVictory() {
        String name = "Anonymous";
        TextInputDialog dialog = new TextInputDialog(name);
        dialog.setTitle("Victory!");
        dialog.setHeaderText("Congratulations, you have cleared all the mines in " + model.getElapsedSeconds() + " seconds.");
        dialog.setContentText("Please enter your name:");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){
            name = result.get();
        };
        return name;
    }
    /**
     * reseive update from observables.
     * @param o 
     * @param arg 
     */
    @Override
    public void update(Observable o, Object arg) {
        score.increaseScore();
        System.out.println(score.toString());
        label.setText(String.valueOf(score.getScore()));
    }
} 
