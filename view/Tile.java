package view;

/**
 *
 * @author T.Redaelli A.Nordlund
 */

import java.io.Serializable;
import javafx.animation.FillTransition;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
/**
 * 
 * @author Redaelli Nordlund
 */
public class Tile extends StackPane implements Serializable {
    private Rectangle tile; //= new Rectangle(100,100);
    private double height;
    private double width;
    private boolean bringText;
    private Text text;
    private String color;
    private Color tileColor;
    /**
     * Constructor
     * @param string 
     */
    public Tile(String string){
      tile = new Rectangle(30,30);
      tile.setStroke(Color.ORANGE);
      this.getChildren().add(tile);
      this.color  = string;
  
    }/**
     * Constructor
     * @param x
     * @param y 
     */
    public Tile(double x, double y){
        tile = new Rectangle(x,y);
        tile.setFill(Color.ORANGE);
        this.getChildren().add(tile);
    }/**
     * mutator method for height
     * @param height 
     */
    public void setHeight(double height){
        this.height = height;
    }/**
     * accessor for height
     * @return 
     */
    public double getheight(){
        return height;
    }/**
     * mutator method for width
     * @param width 
     */
    public void setWidth(double width){
        this.width = width;
    }
    /**
     * return width
     * @return 
     */
    public double width(){
        return width;
    }
    /**
     * sets a boolean trigger
     */
    public void validateText(){
        this.bringText = true;
    }/**
     * Color a tile method
     * @param color 
     */
    public void setColor(Color color){
        tile.setFill(color);
        //tile.(Color.)
    }/**
     * returns color of a tile
     * @return 
     */
    public Color getColor(){
        return tileColor;
    }
/**
 * fades a tile upon a click and shows flag if there is one
 * @param string
 * @param gc 
 */
    public void viewText(String string, Color gc){
         
     //Code to show text in the center of the tile is 
     // code by programmer 'jewelsea' can be found
     //http://stackoverflow.com/questions/10628410/how-to-center-wrap-truncate-text-to-fit-within-rectangle-in-javafx-2-1
        this.text = new Text(string);
        text.setTextAlignment(TextAlignment.CENTER);
        text.setFill(Color.BLACK);
        text.setTextOrigin(VPos.CENTER);
        text.setFont(Font.font("Comic Sans MS", 10));
        text.setFontSmoothingType(FontSmoothingType.LCD);
        this.getChildren().add(text);
        
       
      this.text = new Text(string);
      text.setFill(Color.BLACK);   
      text.setFont(Font.font("Times New Roman", 15));
      text.setFontSmoothingType(FontSmoothingType.LCD);
      this.getChildren().add(text);
      System.out.println(string);
      
     StackPane temp = new StackPane();
     FillTransition ft = new FillTransition(Duration.millis(500),tile , getColor(), gc);
     ft.setCycleCount(1);
     ft.setAutoReverse(false);
 
     ft.play();
        
    }
} 
