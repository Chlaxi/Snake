package MazeGameGUI;

import java.awt.*;

/**
 * Created by Chloe on 12/04/2017.
 *
 * The Node is a tile, consisting of a position as well as a TileType enum.
 * The enum determines which type the Node is (Wall, Ground or Empty).
 * It also has some variables, which is used for the pathfinding.
 */

public class Node {

    private TileType tileType = TileType.Empty;
    private Node parent = null;
    private Point position;

    private int gcost = 0;  //The current cost from the start node to this node
    private int hcost = 0;  //The approximate cost from the current node to the end node.
    private int fcost = 0;  //The sum of g- and hcost, leaving the ultimate value.

    public Node(Point position){
        this.position = position;
    }

    public boolean isEmpty(){
        if(tileType.equals(TileType.Empty)) return true;
        else return false;
    }

    public boolean isGround(){
        if(tileType.equals(TileType.Ground)) return true;
        else return false;
    }

    public boolean isWall(){
        if(tileType.equals(TileType.Wall)) return true;
        else return false;
    }

    public void setTileType(TileType tileType){
           this.tileType = tileType;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public Point getPosition() {
        return position;
    }

    @Override
    public String toString(){
        if(parent != null)
        return "Node at ("+position.x+","+position.y+") with a parent at ("+parent.position.x+","+parent.position.y+")";
        else
            return "Node at ("+position.x+","+position.y+") with a no parent";
    }

    /**
     * Clears the Node's path-related values: parent, g-, h- and fcost.
     */
    public void clear(){
        setParent(null);
        setGcost(0);
        setHcost(0);
        setFcost(0);
    }

    public int getGcost() {
        return gcost;
    }

    public void setGcost(int gcost) {
        this.gcost = gcost;
    }

    public int getHcost() {
        return hcost;
    }

    public void setHcost(int hcost) {
        this.hcost = hcost;
    }

    /**
     * Returns the fcost
     * @return  Unlike the g- or hcost, fcost is always the sum of these.
     */
    public int getFcost() {
        return gcost + hcost;
    }

    public void setFcost(int fcost) {
        this.fcost = fcost;
    }
}
