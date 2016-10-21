package model;

import java.util.ArrayList;

public class Model {
    
    private final Clock clock;
    private Grid grid;
    private Difficulty difficulty;
    private int bombs;
    private boolean firstClicked;
    private Status status;
    private int clearedZonesCount;
    private int flaggedZonesCount;
    private int totalClearedZonesNeeded;
    private ArrayList<Zone> flaggedZones;
    private ArrayList<Zone> recentlyRevealedZones;

    
    public Model() {
        clock = new Clock();
        grid = new Grid(0, 0, 0); 
        difficulty = Difficulty.BEGINNER;
   }
    
    public void initNewRound() {
        int width;
        int height;
        switch (difficulty) {
            case BEGINNER:      width =  9; height = 9; bombs = 10; break;
            case INTERMEDIATE:  width =  16; height = 16; bombs = 40; break;
            case ADVANCED:      width =  30; height = 16; bombs = 99; break;
            default: width = 0; height = 0; bombs = 0;
        }
        grid = new Grid(width, height, bombs);
        clock.reset();
        status = Status.READY;
        clearedZonesCount = 0;
        flaggedZonesCount = 0;
        totalClearedZonesNeeded = grid.getZoneCount() - bombs;
        firstClicked = false;
        flaggedZones = new ArrayList<>();
        recentlyRevealedZones  = new ArrayList<>();
    }
   
    public void pause() {
        if (clock.isRunning() && firstClicked) {
            clock.pause();
        }
    }
    
    public void resume() {
        if (!clock.isRunning() && firstClicked) {
            clock.resume();
        }
    }

    public void setDifficulty(Difficulty d) {
        difficulty = d;
    }
    
    private void reveal(Zone zone) {
        if (!zone.isShown()) {
            zone.show();
            recentlyRevealedZones.add(zone);
            clearedZonesCount++;
            debugInfo();
            for (Zone z : zone.getAdjacentZones()) {
                reveal(z);  
            }
        }
    }
  
    private boolean victoryConditions() { 
        if (!((clearedZonesCount == totalClearedZonesNeeded) && (flaggedZonesCount + clearedZonesCount == totalClearedZonesNeeded + bombs))) {
            return false;
        }
        
        for (Zone z : flaggedZones) {
            if (z.hasBomb())
                return false;
        }
        return true;
    }
    
    private void victory() {
        status = Status.VICTORIOUS;
        clock.pause();
    }
    
    // Don't forget to remove this later
    
    public void debugInfo() {
        System.out.print("Flagged: " + flaggedZonesCount);
        System.out.print(" --- Cleared: " + clearedZonesCount);
        System.out.println(" --- Progress: " + clearedZonesCount + "/" + totalClearedZonesNeeded);
    }
    
    public void clearZone(int x, int y) {
        if (!firstClicked) {
            firstClicked = true;
            clock.start();
        }
        Zone zone = grid.getZone(x, y);
        
        if (zone.isFlagged()) {
            toggleFlag(x, y);
        }
        
        if (zone.hasBomb()) {
            status = Status.DEAD;
        }
        else {
            reveal(zone);
            status = Status.ALIVE;
            if (victoryConditions()) {
                    victory();
             }
        }
    }
    
    public Status getStatus() {
        return status;
    }
    
    public boolean isCleared(int x, int y) {
        return grid.getZone(x, y).isShown();
    }
    
    public boolean isFlagged(int x, int y) {
        return grid.getZone(x, y).isFlagged();
    }
    
    public void toggleFlag(int x, int y) {
        Zone zone = grid.getZone(x, y);
        if (!zone.isShown()) {
            if (zone.isFlagged()) {
                zone.setFlag(false);
                flaggedZones.add(zone);
                flaggedZonesCount--;
            }
            else {
                zone.setFlag(true);
                flaggedZones.remove(zone);
                flaggedZonesCount++;
                debugInfo();
                if (victoryConditions()) {
                    victory();
                }
            }
        }
    }
    
    public Grid getGrid() {
        return grid;
    }
    
    public ArrayList<Zone> getAllExplosiveZones() {
        return grid.getExplosiveZones();
    }
    
    public ArrayList<Zone> getAndServeRecentlyRevealedZones() {
        ArrayList<Zone> tmp = new ArrayList<>();
        for (Zone z: recentlyRevealedZones) {
            tmp.add(z);
        }
        recentlyRevealedZones.clear();
        return tmp;
    }
   
    public int getElapsedSeconds() {
        return clock.getElapsedSeconds();
    }
    
    public int getNearbyBombs(int x, int y) {
        return grid.getZone(x, y).getNearbyBombs();
    }  
}
