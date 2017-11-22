package MazeGameGUI;

import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;


import java.awt.*;
import java.util.*;

public class Controller {

    /*
    Known bugs:
    - The bi-directional pathfinding stops at the second last point, rather than going all the way to its destination.
    - The ghosts and player can pass through each other, if they move in opposite directions
     */
    @FXML
    Canvas canvas;
    @FXML
    Label labelScore;

    SceneInfo sceneInfo;
    private int gameLoopDelay = 500;
    private float refreshRate =300;

    ArrayList<Item> items = new ArrayList<Item>();
    public static Player player;
    Ghost[] ghosts = new Ghost[4];
    public static Maze maze;
    public static Boolean DebugMode = true;

    public void btnStartAction(ActionEvent event)
    {
        drawCanvas();
    }

    /**
     * Executed when JavaFX is initialized. Used to setup the Snake game
     */
    public void initialize()
    {
        sceneInfo = new SceneInfo(canvas);
        maze = new Maze();
        maze.SetupMaze(sceneInfo.getWidth(),sceneInfo.getHeight());
        //items = maze.itemList;
        player = new Player(Color.LIMEGREEN, new Point(5,5));
        player.Restart();
        ghosts[0] = new Ghost(maze.ghostSpawners[0].getPosition(),GhostType.Blue);
        ghosts[1] = new Ghost(maze.ghostSpawners[1].getPosition(),GhostType.Pink);
        ghosts[2] = new Ghost(maze.ghostSpawners[0].getPosition(),GhostType.Orange);
        ghosts[3] = new Ghost(maze.ghostSpawners[1].getPosition(),GhostType.Red);
        items = maze.AddCheese();
        // Start and control game loop
        new AnimationTimer(){
            long lastUpdate;
            public void handle (long now)
            {
                if (now > lastUpdate + refreshRate * 1000000)
                {
                    lastUpdate = now;
                    update(now);
                }             }
        }.start();
    }

    /**
     * Game loop - executed continously during the game
     * @param now game time in nano seconds
     */
    private void update(long now)
    {
        player.update();
        for(Ghost g : ghosts){
            g.Update();
            for(Node n: maze.groundList){
                n.clear();
            }
            if(g.collisionWithPlayer(player)){
                //If it collides with the player..
                if(g.behaviour == GhostState.Scared){
                    g.die();
                    player.setScore(20);
                }
                else{
                    restart();
                }

            }
        }
        for (Item item : items){
            if(player.getPosition().equals(item.getPosition())){
                item.eat(player);
                //items.remove(item);
            }
        }
        drawCanvas();
        labelText();
    }

    public void restart(){
        for (Ghost ghost : ghosts) {
            ghost.die();
        }
        player.die();
    }

    public void labelText(){
        labelScore.setText("Score: "+player.getScore()+"  Deaths: "+player.getDeaths());
    }

    /**
     * Draw the canvas - used in the gameloop
     * Also calls the other objects draw functions.
     */
    private void drawCanvas() {

        GraphicsContext g = canvas.getGraphicsContext2D();
        sceneInfo.draw(g);
        //Draws the maze
        maze.Draw(g,sceneInfo);

        // draw items
        for (Item item : items)
        {
            item.draw(g,sceneInfo);
        }
        for(Ghost ghost : ghosts){
            ghost.Draw(g, sceneInfo);
        }
        // draw 'player'
            player.draw(g, sceneInfo);


    }
}
