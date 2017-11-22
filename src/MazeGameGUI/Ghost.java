package MazeGameGUI;

import NPCs.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.awt.*;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;

/**
 * Created by Chloe on 12/04/2017.
 */

/**
 * The Type of the ghost, defined by their colour. Each ghost has some different variables, such as their range,
 * pathfinding algorithm, a small difference in their timers.
 */
enum GhostType{
 Blue, Red, Orange, Pink
}

/**
 * The state which ghosts can be in.
 * Random: The ghost gets a random target position, once its patience has run out
 * Scared: The ghost is vulnerable until the time runs out. When vulnerable, it will move to a random fixed location, and can be killed.
 * Chasing: The ghost is chasing the player, constantly updating the target position. Once a ghost has run out of patience, it will return to the Random state
 * Dead: The ghost is dead. This prevents the ghost from moving, untill it respawns. (see the Die function)
 */
enum GhostState{
    Random, Chasing, Scared, Dead
}

public class Ghost implements INPC {

    private Point position;
    private Point spawn;
    private Point[] fleePoints = {new Point(12,7), new Point(3,3), new Point(21,3), new Point(3,11), new Point(21,11)};
    private Color colour;
    private int range;
    GhostState behaviour = GhostState.Dead ;
    private boolean canGetNewPath = true;
    boolean canMove = false;
    boolean vulnerable = false;
    Point target = new Point(12,5);
    int recoveryTime;   //Time spent dead
    int time = 0;
    int chaseTime = 20;
    boolean mayChase = false;

    IPathfinding pathfinding = null;
    ArrayList<Node> path = new ArrayList<>();

    /**
     *  Based on the behaviour, a ghost is created, with different pathfinding and variables, giving them each a unique feel.
     * @param spawnPosition The position at which the ghost is spawned.
     * @param ghostColour   The colour of the ghost (Blue, Red, Green, Pink)
     */
    public Ghost(Point spawnPosition, GhostType ghostColour){
        spawn = spawnPosition;
        position = spawnPosition;
        switch (ghostColour){
            case Blue:;
                colour = Color.BLUE;
                setupGhost(0,new BiDirectional(),5,4);
                break;
            case Red:
                colour = Color.RED;
                setupGhost(2,new AStar(),4,10);
                break;
            case Pink:
                colour = Color.PINK;
                setupGhost(4,new BreadthFirst(), 5, 6);
                break;
            case Orange:
                colour = Color.ORANGE;
               setupGhost(6,new BestFirst(),6,8);
                break;
        }

    }

    /**
     * Sets up the ghost.
     * @param spawnTime The time before the ghost moves out of the spawn area.
     * @param pathfinding   Determines which pathfiding the ghost uses
     * @param recoveryTime  The recoverytime for the ghost. This is used to determine how long time it spends dead, and also a modifer to the ghost's "patience"
     * @param range The range in which the ghost can see the player.
     */
    private void setupGhost(int spawnTime, IPathfinding pathfinding, int recoveryTime, int range){
        time = spawnTime;
        this.pathfinding = pathfinding;
        this.recoveryTime = recoveryTime;
        this.range = range;
        chaseTime = 15+recoveryTime;

    }

    public Point getPosition(){ return getPosition(); }

    public void setPos(Point position){
        this.position = position;
    }

    public void Update() {
    //As long as a pathfinding type exists.
        if(pathfinding != null)
        {
            //If it doesn't have a path and it can move
            if(path == null || path.isEmpty() && canMove){
                canGetNewPath = true;
            }
            Behaviour();

            //If a pathfinding exists, a new path will be found
            if(canGetNewPath){
                System.out.println("Finding new path");
                path = getPath();
                canGetNewPath = false;
            }
            //If canMoce and a path is available
            if(canMove && path != null){
             followPath();
            }
        }
    }

    /**
     * The Behaviour of a ghost, is based on which state it is in.
     * Random: The ghost gets a random target position, once its patience has run out
     * Scared: The ghost is vulnerable until the time runs out. When vulnerable, it will move to a random fixed location, and can be killed.
     * Chasing: The ghost is chasing the player, constantly updating the target position. Once a ghost has run out of patience, it will return to the Random state
     * Dead: The ghost is dead. This prevents the ghost from moving, untill it respawns. (see the Die function)
     */
    public void Behaviour(){
        Random random = new Random();

        if(Controller.player.isEmpowered() && behaviour != GhostState.Dead){
            System.out.println("Ghosts are now scared!");
            canGetNewPath = true;
            behaviour = GhostState.Scared;
        }

        if(mayChase && behaviour == GhostState.Random){
            System.out.println(colour+" is chasing!");
            canGetNewPath = true;
            behaviour = GhostState.Chasing;
            time = chaseTime;
        }

        switch(behaviour){

            case Random:
                getDistToPlayer();  //Checks the distance to the player, so it might begin the chase.
            if(time < 0 || path.isEmpty()){
                canGetNewPath = true;
            }
            if(canGetNewPath){
                if(!canMove) canMove = true;
                ArrayList<Node> groundList = Controller.maze.groundList;
                Node targetNode = groundList.get(random.nextInt(groundList.size()));
                target = targetNode.getPosition();
                time = random.nextInt(recoveryTime)+5;
            }
                break;

            case Scared:
                //Move to a random (or predetermined) position, while also being vulnerable
                if(!vulnerable)
                {
                    System.out.println("is now vulnerable");
                    time = 20;
                    vulnerable = true;
                    target = fleePoints[random.nextInt(fleePoints.length)];
                }
                else if(time < 0){
                    vulnerable = false;
                    behaviour = GhostState.Random;
                }
                break;

            case Chasing:
                //Sets the path position to the player position
                target = Controller.player.getPosition();//Controller.player.getPosition();
                if(time < 0){
                    behaviour = GhostState.Random;
                    mayChase = false;
                }
                break;

            case Dead:
                //Is dead. Return to spawn
                if(time < 0){
                    System.out.println(colour+" has been revived");
                    canMove = true;
                    behaviour = GhostState.Random;
                }
                break;
        }
        time--;
       // System.out.println("Time: "+time+" Behaviour: "+behaviour+" canMove: "+canMove+" NewPath"+canGetNewPath);
    }

    /**
     * Finds a path for the ghost, based on its pathding.
     * @return An arraylist of Nodes will be returned.
     */
    public ArrayList<Node> getPath(){
        return pathfinding.getWaypoint(position,target);

    }

    /**
     * The ghost will follow the given path.
     */
    public void followPath() {
        if(!path.isEmpty()){
            setPos(path.get(0).getPosition());
            path.remove(0);
        }
    }

    /**
     * Gets an approximate distance to the player.
     */
    void getDistToPlayer(){
        Point playerPos = Controller.player.getPosition();
        int dist = Math.abs(playerPos.x - position.x) + Math.abs(playerPos.y - position.y);
        if(dist < range && behaviour == GhostState.Random){
            mayChase = true;
        }
    }

    /**
     * Checks if the ghost is colliding with the player
     * @param p The player.
     * @return
     */
    public boolean collisionWithPlayer(Player p){
        if(position.equals(p.getPosition())){
            return true;
        }
        return false;
    }

    /**
     * Kills the ghost, resetting it back to a state, where it can't do anything, untill the deathtimer has run out.
     */
    public void die(){
        behaviour = GhostState.Dead;
        vulnerable=false;
        canGetNewPath = false;
        canMove = false;
        mayChase = false;
        path.clear();
        time = recoveryTime;
        position = spawn;
    }

    public void Draw(GraphicsContext g, SceneInfo scene){

        if(behaviour == GhostState.Scared){ g.setFill(Color.CORAL); }
        else{
            g.setFill(colour);
        }
        g.fillRoundRect(position.x * scene.getFieldWidth(), position.y * scene.getFieldHeight(), scene.getFieldWidth(), scene.getFieldHeight(), 5, 5);
        if(Controller.DebugMode && path != null){
         for (Node n : path){
             Point p = n.getPosition();
             g.fillOval(p.x * scene.getFieldWidth(), p.y * scene.getFieldHeight(), scene.getFieldWidth()/2, scene.getFieldHeight()/2);
         }
        }
    }

}


