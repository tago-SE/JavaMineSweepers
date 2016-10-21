package view;

import java.util.ArrayList;
import java.util.Optional;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import model.Difficulty;
import model.Model;
import model.Status;
import model.Zone;

public final class Controller {
    
    private final View view;
    private final Model model;
    
    public Controller(Model model_, View view_) {
        view = view_;
        model = model_;
        this.addGridSelectionHandles();
    }  
    
    
    public void handleBeginnerOption() {
        model.pause();
        if (view.alertRestart()) {
            model.setDifficulty(Difficulty.BEGINNER);
            reset();
        } else {
            model.resume();
        }
    }
    
    public void handleIntermediateOption() {
        model.pause();
        if (view.alertRestart()) {
            model.setDifficulty(Difficulty.INTERMEDIATE);
            reset();
        } else {
            model.resume();
        }
    }
    
    public void handleAdvancedOption() {
        model.pause();
        if (view.alertRestart()) {
            model.setDifficulty(Difficulty.ADVANCED);
            reset();
        } else {
            model.resume();
        }
    }
    
    private void reset() {
        model.initNewRound();
        view.initGridInterface();
        addGridSelectionHandles();
    }
    
    public void handlePauseOption() {
        model.pause();
        view.alertPause();
        model.resume();
    }
    
    public void handleResetOption() {
        model.pause();
        if (view.alertRestart()) {
            reset();
        } 
        model.resume();
  
    }
    
    private void victory() {
        String name = view.alertVictory();
        
        // Debug
        System.out.println("controller: victory!");
    }

    public void addGridSelectionHandles() {
        int xMax = model.getGrid().getNumColumns();
        int yMax = model.getGrid().getNumRows();
         for (int y = 0; y < yMax; y++) {
            for (int x = 0; x < xMax; x++) {
                Tile t = view.getTile(x, y);
                t.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        Integer x = GridPane.getColumnIndex(t);
                        Integer y = GridPane.getRowIndex(t);

                        Status status = model.getStatus();
                        if ((status == Status.ALIVE) || (status == Status.READY)) {
                            // Flag
                            if (event.getButton() == MouseButton.SECONDARY) {
                                if (!model.isCleared(x, y)) {
                                    model.toggleFlag(x, y);
                                    if (model.isFlagged(x, y)) {
                                        view.flagTile(x, y);
                                        if (model.getStatus() == Status.VICTORIOUS) {
                                            victory();
                                        }
                                    }
                                    else {
                                        view.unflagTile(x, y); 
                                    }
                                }
                            }
                        
                            else if (event.getButton() == MouseButton.PRIMARY){
                                
                                model.clearZone(x, y);
                                
                                // Detonate Mine
                                
                                if (model.getStatus() == Status.DEAD) {
                                    model.pause();
                                    for (Zone z: model.getAllExplosiveZones()) {
                                        view.detonateTile(z.getX(), z.getY());
                                    }
                                    if (view.alertGameOver()) {
                                        reset();
                                    } 
                                }
                                
                                else if (status == Status.ALIVE || status == Status.READY) {
                                    for (Zone z : model.getAndServeRecentlyRevealedZones()) {
                                        view.revealTile(z.getX(), z.getY());
                                    }
                                    if (model.getStatus() == Status.VICTORIOUS) {
                                        victory();
                                    }
                                }
                            }
                        }
                    }        
                });
            }
        }
    }
}
                
