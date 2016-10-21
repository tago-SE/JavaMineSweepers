package model;

import java.util.ArrayList;

public class Grid {
    
    private Zone[][] grid;
    private final int xMax;
    private final int yMax;
    private int gridCount = 0;
            
    public Grid(int width, int height, int bombs) {
        xMax = width;
        yMax = height;
        grid = new Zone[width][height];
        
        // Create Grid
        for (int y = 0; y < yMax; y++) {
            for (int x = 0; x < xMax; x++) {
                grid[x][y] = new Zone(x, y);
                gridCount++;
            }
        }
        
        for (int y = 0; y < yMax; y++) {
            for (int x = 0; x < xMax; x++) {
                if (x - 1 >= 0 && y - 1 >= 0) 
                    grid[x][y].addAdjacentZone(grid[x - 1][y - 1]);
                if (y - 1 >= 0) 
                    grid[x][y].addAdjacentZone(grid[x][y - 1]);
                if (x + 1 < xMax && y - 1 >= 0) 
                    grid[x][y].addAdjacentZone(grid[x + 1][y - 1]);
                if (x + 1 < xMax) 
                    grid[x][y].addAdjacentZone(grid[x + 1][y]);
                if (x + 1 < xMax && y + 1 < yMax) 
                    grid[x][y].addAdjacentZone(grid[x + 1][y + 1]);  
                if ( y + 1 < yMax) 
                    grid[x][y].addAdjacentZone(grid[x][y + 1]);  
                if (x - 1 >= 0 && y + 1 < yMax) 
                    grid[x][y].addAdjacentZone(grid[x - 1][y + 1]);  
                if (x - 1 >= 0) 
                    grid[x][y].addAdjacentZone(grid[x - 1][y]);    
            }
        }
        
        // Add bombs
        for (int i = 0; i < bombs; i++) {
            
            if (i >= gridCount) // Stops if full
                break;

            Zone z = null;
            while (z == null) {
                int rdmX = (int) Math.floor(Math.random() * xMax);
                int rdmY = (int) Math.floor(Math.random() * yMax); 
                if (!grid[rdmX][rdmY].hasBomb())
                    z = grid[rdmX][rdmY];
            }
            z.plantBomb();
        }
        
        for (int y = 0; y < yMax; y++) {
            for (int x = 0; x < xMax; x++) {
               if (grid[x][y].getNearbyBombs() != 0 || grid[x][y].hasBomb()) {
                   grid[x][y].clearAdjacentZones();
               }
            }
        }
    }
    
    public ArrayList<Zone> getExplosiveZones() {
        ArrayList<Zone> zones = new ArrayList<>();
        for (int y = 0; y < yMax; y++) {
            for (int x = 0; x < xMax; x++) {
                if (grid[x][y].hasBomb()) 
                    zones.add(grid[x][y]);
            }
        }
        return zones;
    }
    
    public ArrayList<Zone> getZones() {
        ArrayList<Zone> zones = new ArrayList<>();
        for (int y = 0; y < yMax; y++) {
            for (int x = 0; x < xMax; x++) {
                zones.add(grid[x][y]);
            }
        }
        return zones;
    }
    
    public int getZoneCount() {
        return gridCount;
    }
    
    public int getNumColumns() {
        return this.xMax;
    }
    
    public int getNumRows() {
        return this.yMax;
    }
    
    public Zone getZone(int x, int y) {
        return this.grid[x][y];
    }
  
    @Override
    public String toString() {
        String info = "";
        for (int y = 0; y < this.yMax; y++) {
            for (int x = 0; x < this.xMax; x++) {
                if (grid[x][y].hasBomb()) 
                    info += " B ";
                else
                    info += " " + grid[x][y].getNearbyBombs() + " ";
            }
            info += "\n";
        }
        return info;
    }
}
