package MazeGameGUI;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.awt.*;

/**
 * Created by Chloe on 20/04/2017.
 */
public class Item implements IGameObject {

    private boolean isEaten = false;
    private int value = 1;
    private Color colour;
    private Point position;
    private boolean bigCheese = false;

    public Item(Point position) {
        colour = Color.YELLOW;
        this.position = position;
    }

    /**
     *  Additional constructor, allowing a boolean, to check whether it should be a big cheese or not.
     * @param position
     * @param bigCheese
     */
    public Item(Point position, boolean bigCheese) {

        this.position = position;
        if(bigCheese){
            colour = Color.YELLOWGREEN;
            value = 4;
            this.bigCheese = bigCheese;
        }
    }

    public Color getColour() {
        return colour;
    }

    public Point getPosition() {return position; }

    public int getValue(){ return value; }

    public boolean isBigCheese(){ return bigCheese; }

    /**
     * Defines what happens, when the player collides with the cheese.
     * @param player
     */
    public void eat(Player player){
        if(!isEaten){
            player.setScore(value);
            if(bigCheese)
            {
                System.out.println("Big cheese picked up!!");
                player.Empower(value);
            }
            isEaten = true;
        }
    }

    public void update(){

    }

    public void draw(GraphicsContext g, SceneInfo scene){
        if(!isEaten){
            g.setFill(colour);
            Point startPos = new Point((int)(position.x * scene.getFieldWidth() +(scene.getFieldWidth()/2)), (int)(position.y * scene.getFieldHeight() + (scene.getFieldHeight() / 2)));
            g.fillOval(startPos.x -  scene.getFieldWidth()/4, startPos.y -scene.getFieldHeight()/4, scene.getFieldWidth()/2, scene.getFieldHeight()/2);
        }
    }
}
