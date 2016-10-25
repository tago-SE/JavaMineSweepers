package view;

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

public class Tile extends StackPane{
    private Rectangle tile; //= new Rectangle(100,100);
    private double height;
    private double width;
    private boolean bringText;
    private Text text;
    private String color;
    public Tile(String string){
      tile = new Rectangle(30,30);
      tile.setStroke(Color.ORANGE);
      this.getChildren().add(tile);
      this.color  = string;
  
    }
    public Tile(double x, double y){
        tile = new Rectangle(x,y);
        tile.setFill(Color.ORANGE);
        this.getChildren().add(tile);
    }
    public void setHeight(double height){
        this.height = height;
    }
    public double getheight(){
        return height;
    }
    public void setWidth(double width){
        this.width = width;
    }
    public double width(){
        return width;
    }
    public void validateText(){
        this.bringText = true;
    }
    public void setColor(Color color){
        tile.setFill(color);
        //tile.(Color.)
    }

    public void viewText(String string){
        this.text = new Text(string);
        text.setTextAlignment(TextAlignment.CENTER);
        text.setFill(Color.BLACK);
        text.setTextOrigin(VPos.CENTER);
        text.setFont(Font.font("Comic Sans MS", 10));
        text.setFontSmoothingType(FontSmoothingType.LCD);
        this.getChildren().add(text);
    }
}