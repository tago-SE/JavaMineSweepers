package model;

import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;

public final class Clock extends Observable {
        
    private Timer timer;
    private int elapsedSeconds;
    private final TimerTask task;
    private boolean running = false;
    
    private class Task extends TimerTask {
        
        @Override
	public void run() {
            tick();
	}
    }
    
    public Clock() {
        timer = new Timer();
        task  = new Task();
        timer.schedule(task, 0, 1000);
        elapsedSeconds = 0;
    }
    
    public void tick() {
        if (running) {
            elapsedSeconds++;
            setChanged();
            notifyObservers();
            System.out.println("Time: " + toString());
        }
    }
    
    public void reset() {
        elapsedSeconds = 0;
    }
    
    public void pause() {
        running = false;
    }
    
    public void start() {
        running = true;
    }
    
    public boolean isRunning() {
        return running;
    }
    
    public void resume() {
        running = true;
    }
    
    public int getElapsedSeconds() {
        return elapsedSeconds;
    }
    
    public int getSeconds() {
        return elapsedSeconds%60;
    }
    
    public int getMinutes() {
        return elapsedSeconds/60;
    }
    
    @Override
    public String toString() {
        String info = "";
        int seconds = getSeconds();
        int minutes = getMinutes();
        
        if (minutes < 10) 
            info += "0" + minutes + ":";
        else 
            info += minutes + ":";  
        if (seconds < 10) 
            info += "0" + seconds; 
        else
            info += seconds;
        return info;
    }
}


