package view;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
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
import javafx.scene.effect.InnerShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;

import model.Model;
import model.Zone;

public final class View extends BorderPane {
    
    private Stage stage;
    private final Model model;
    private final VBox vbox = new VBox();               // Move to menu section?
    private Tile[][] tile;                         // Are we removing this or what?
    private GridPane gridPane;
   
    public View(Stage someStage, Model model_) {
        model = model_;
        stage = someStage;
        this.setStyle("-fx-background-color: #D8BFD8;");   
        Scene scene = new Scene(this);
        initGridInterface();
        initMenu(); 
        stage.setTitle("Minesweepers");
        stage.setScene(scene);
        stage.show();    
    }
    
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
    
    private void initMenu() {
        MenuBar menuBar = new MenuBar();
        menuFile.getItems().addAll(optRestart, optPauseOrResume, optSave, optLoad, optExit);
        menuDifficulty.getItems().addAll(optBeginner, optIntermediate, optAdvanced);
        menuOptions.getItems().add(menuDifficulty);        
        menuBar.getMenus().addAll(menuFile, menuOptions, menuHelp);
        vbox.getChildren().add(menuBar);
        this.setTop(vbox);
    }
    
    private void initMenuEventHandlers(Controller controller) {
        optBeginner.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                controller.handleBeginnerOption();
            }
        });
        
        optIntermediate.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                controller.handleIntermediateOption();
            }
        });
        
        optAdvanced.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                controller.handleAdvancedOption();
            }
        });
        
        optRestart.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                controller.handleResetOption();
            }
        });
        
        optPauseOrResume.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                controller.handlePauseOption(); 
            }
        });
        
        optSave.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Save!");
            }
        });
        
        optLoad.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Load!");
            }
        });
        
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
    
    public void initGridInterface(){
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
        this.setBottom(gridPane);
    }
    
    public Tile getTile(int x, int y) {
        return tile[x][y];
    }
    
    private void setDefaultTile(int x, int y) {
        tile[x][y].setColor(Color.BLACK);
        tile[x][y].setEffect(new InnerShadow());
    }
    
    
    public void detonateTile(int x, int y) {
        tile[x][y].setColor(Color.RED);
    }
    
    public void flagTile(int x, int y) {
        tile[x][y].setColor(Color.BLUE);
    }
    
    public void unflagTile(int x, int y) {
        this.setDefaultTile(x, y);
    }
    
    public void revealTile(int x, int y) {
        int numberOfBombs = model.getNearbyBombs(x, y);
        if (numberOfBombs != 0) {
            tile[x][y].viewText("" + numberOfBombs);
        }
            tile[x][y].setColor(Color.WHITE);  
    }
    
    // *************************************************************************
    // Alerts
    // *************************************************************************
    
    public void alertPause() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Paused");
        alert.setHeaderText("The game is paused.");
        alert.setContentText("Press OK to resume.");
        alert.showAndWait();
    }
    
    public boolean alertRestart() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Reset Confirmation");
        alert.setHeaderText("The game is about to generate a new mine field.");
        alert.setContentText("Are you ok with this?");
        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;
    }
    
    public boolean alertGameOver() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText("You lost and the game is about to generate a new mine field.");
        alert.setContentText("Are you ok with this?");
        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;
    }
    
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
}
