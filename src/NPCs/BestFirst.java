package NPCs;

import MazeGameGUI.*;

import java.awt.*;
import java.util.*;

/**
 *  Created by Chloe on 24/04/2017.
 */
public class BestFirst extends IPathfinding {

    Maze maze = Controller.maze;

    /**
     * The Best First algorithm evaluates the Node with the shortest approximate distance to the goal.
     * This would in theory give the shortest path, but since walls can be in the way, this might not always be the case.
     * @param startPos The start position
     * @param endPos    The end position
     * @return  Returns a Node, with a traceable path through its parents.
     */
    public Node returnWaypoint(Point startPos, Point endPos){
        int counter = 0;
        ArrayList<Node> closedSet = new ArrayList<>();
        Stack<Node> openSet = new Stack<>();
        openSet.add(maze.getNodeAtLocation(startPos));
        //Sets the hcost, since it's the approximate distance from the point to the goal.
        openSet.get(0).setHcost(getDistance(startPos, endPos));

        if(!maze.isGround(endPos)){
            System.out.println("end position is a wall!");
            return null;
        }

        while(!openSet.isEmpty()){
         counter++;
        Node current = getLowestOpen(openSet);
        closedSet.add(current);
        openSet.remove(current);
        if(current.getPosition().equals(endPos)){
            System.out.println("Best First path found in "+counter+" iterations.");
            return current;
        }
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if(i == 0 && j == 0) continue;  //Skips the current node.
                //The diagonal exceptions
                if((i == -1 && j == -1) ||(i == -1 && j == 1) || (i == 1 && j == -1) || (i == 1 && j == 1)){
                    continue;
                }
                else{
                    Node neighbour = maze.getNodeAtLocation(new Point(current.getPosition().x+i,current.getPosition().y+j));
                    if(maze.isGround(neighbour.getPosition()) && !closedSet.contains(neighbour)){
                        neighbour.setHcost(getDistance(neighbour.getPosition(), endPos));
                        neighbour.setParent(current);
                        openSet.add(neighbour);
                    }
                }
            }
        }
        }

        return null;
    }

    /**
     * Returns the Node with the lowest fcost, by evaluating all nodes from the open set.
     * @param openSet   The set of Nodes, which should be evaluated.
     * @return      The Node with the lowest fcost, and thus the shortest path.
     */
    private Node getLowestOpen(Stack<Node> openSet){
        Node optimal = null;
        for(Node n : openSet){
            if(optimal == null){
                optimal = n;
            }
            else if(n.getFcost() < optimal.getFcost()){
                optimal = n;
            }
        }
        return optimal;
    }

    /**
     * Gets a distance between two points.
     * @param a Startpoint
     * @param b Endpoint
     * @return  Returns the distance between the two points.
     */
    private int getDistance(Point a, Point b){
        return Math.abs(b.x - a.x) + Math.abs(b.y - a.y);
    }
}

