package model;

/**
 *
 * @author T. Redaelli A.Nordlund 
 */

import java.io.Serializable;
import java.util.ArrayList;

public class Model implements Serializable{
    
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
    
    /**
     *  Model constructor initalizes clock and 
     *  grid
     */
    public Model() {
        clock = new Clock();
        grid = new Grid(0, 0, 0); 
        difficulty = Difficulty.BEGINNER;
   }
    /**
     * initalizes a new round based
     * on chosen difficulty
     * initializes flags
     */
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
   /**
    * Pauses the clock
    */
    public void pause() {
        if (clock.isRunning() && firstClicked) {
            clock.pause();
        }
    }
    /**
     * Unpauses the clock
     */
    public void resume() {
        if (!clock.isRunning() && firstClicked) {
            clock.resume();
        }
    }
    /**
     * 
     * @param d 
     * uses Difficulty enum to decide 
     * upon the size of the grid
     */
    
    public void setDifficulty(Difficulty d) {
        difficulty = d;
    }
    /**
     * 
     * @param zone 
     */
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
  /**
   * 
   * @return boolean values that
   * corresponds on victory terms.
   * player can lose if zone
   * clicked has a bomb
   */
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
    /**
     * 
     * @param x 
     * @param y 
     * which row & column was clicked.
     */
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
    /**
     * 
     * @return Enum value that corresponds
     * with status of the game
     */
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
        if (!firstClicked) {
            firstClicked = true;
            clock.start();
        }
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
    /**
     * 
     * @return grid 
     */
    public Grid getGrid() {
        return grid;
    }
    /**
     * 
     * @return  list of explosives
     */
    public ArrayList<Zone> getAllExplosiveZones() {
        return grid.getExplosiveZones();
    }
    /**
     * 
     * @return 
     */
    public ArrayList<Zone> getAndServeRecentlyRevealedZones() {
        ArrayList<Zone> tmp = new ArrayList<>();
        for (Zone z: recentlyRevealedZones) {
            tmp.add(z);
        }
        recentlyRevealedZones.clear();
        return tmp;
    }
   /**
    * 
    * @return elapsed time in seconds 
    */
    public int getElapsedSeconds() {
        return clock.getElapsedSeconds();
    }
    /**
     * 
     * @param x
     * @param y
     * @return nearby bombs 
     */
    public int getNearbyBombs(int x, int y) {
        return grid.getZone(x, y).getNearbyBombs();
    }  
}

