package model;

import java.util.ArrayList;

public class Zone {
    private ArrayList<Zone> adjacentZone = new ArrayList<>();
    private int nearbyBombs;
    private boolean hasBomb;
    private boolean flagged;
    private boolean shown;
    private final int x;
    private final int y;

    public Zone(int x_, int y_) {
        this.x = x_;
        this.y = y_;
        this.hasBomb = false;
        this.flagged = false;
        this.shown = false;
        this.nearbyBombs = 0;
    }
    
    public void plantBomb() {
        this.hasBomb = true;
        for (Zone z: this.adjacentZone) {
            z.increaseNearbyBombs();
        }
    }
    
    public void addAdjacentZone(Zone z) {
        if (!(z == null)) 
            this.adjacentZone.add(z);
    }
    
    public ArrayList<Zone> getAdjacentZones() {
        return adjacentZone;
    }
    
    public void removeAdjacentZone(Zone z) {
        if (z != null) {
            adjacentZone.remove(z);
        }
    }
    
    public void clearAdjacentZones() {
        adjacentZone.clear();
    }
    
    public int getX() {
        return this.x;
    }
    
    public int getY() {
        return this.y;
    }
    
    public int getNearbyBombs() {
        return this.nearbyBombs;
    }

    public boolean hasBomb() {
        return this.hasBomb;
    }
    
    public void increaseNearbyBombs() {
        this.nearbyBombs++;
    }
    
    public boolean isFlagged() {
        return this.flagged;
    }
    
    public void setFlag(boolean flag) {
        this.flagged = flag;
    }
    
    public boolean isShown() {
        return this.shown;
    }
    
    
    public void show() {
        this.shown = true;
    }
}
