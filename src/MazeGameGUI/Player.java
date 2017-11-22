package MazeGameGUI;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

import java.awt.*;

/**
 * Created by Chloe on 16/02/2017.
 */
public class Player implements IGameObject {

    private Color colour;
    private int score = 0;
    private int deaths = 0;
    private Point position;
    private Point spawnPosition;
    private boolean empowered;
    private KeyCode keyPressed  = KeyCode.BACK_SPACE;
    private int time = 0;


    public Player(Color colour, Point position){
        this.colour = colour;
        this.position = position;
        spawnPosition = position;
    }

    /**
     * The actionlistener
     * @param keyCode   The key which has been pressed.
     */
    public void keyPressed(KeyCode keyCode)
    {
        this.keyPressed = keyCode;
    }

    public Color getColour(){
        return colour;
    }

    public Point getPosition() { return position; }

    public void setX(int x) {
        if(CheckWallCollision(position.x+x,position.y)) position.x += x;
    }

    public void setY(int y) {
        if(CheckWallCollision(position.x,position.y+y)) position.y += y;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score += score;
    }

    public boolean isEmpowered(){ return empowered; }

    public void update(){
        KeyPressed(keyPressed);
        if(empowered){
            time--;
            if(time < 0)
            {
                empowered = false;
            }
        }
    }

    /**
     * Based on the input from the actionlistener, an action will be performed.
     * @param keyPressed    The pressed key
     */
    public void KeyPressed(KeyCode keyPressed){
        switch (keyPressed)
        {
            case DOWN:
            case S:
                setY(1);
                break;
            case LEFT:
            case A:
                setX(-1);
                break;
            case RIGHT:
            case D:
                setX(1);
                break;
            case UP:
            case W:
                setY(-1);
                break;
        }
        // keyPressed = KeyCode.BACK_SPACE; //Makes sure, that the player doesn't move continously
    }

    /**
     * Resets the player.
     */
    public void Restart() {
        position.x = spawnPosition.x;
        position.y = spawnPosition.y;
        keyPressed = KeyCode.BACK_SPACE;
        score = 0;
        deaths = 0;
    }

    /**
     * Empowers the ghost by eating a big cheese.
     * @param time  The time (in steps), which the player is empowered.
     */
    public void Empower(int time){
        this.time = time;
        empowered = true;
    }

    /**
     * Checks for wall collisions.
     * @param x
     * @param y
     * @return true means no walls, while false means there is a collision
     */
    public boolean CheckWallCollision(int x, int y){
        if(Controller.maze.getNodeAtLocation(new Point(x, y)).isGround())
            return true;
        else
            return false;

    }

    /**
     * When the player dies, it should be moved back to the spawn, and the deathcounter increased.
     */
    public void die(){
        deaths++;
        position = new Point(5,5);
        System.out.println("You died!");
    }

    public void draw(GraphicsContext g, SceneInfo scene){

        if(empowered) g.setFill(Color.GREEN);
        else g.setFill(colour);
        g.fillRoundRect(position.x * scene.getFieldWidth(), position.y * scene.getFieldHeight(), scene.getFieldWidth(), scene.getFieldHeight(), 5, 5);;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }
}

