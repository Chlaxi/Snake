package NPCs;

import MazeGameGUI.Controller;
import MazeGameGUI.Node;

import java.awt.*;
import java.util.*;

/**
 * Created by Chloe on 12/04/2017.
 */

public class AStar extends IPathfinding{

    MazeGameGUI.Maze maze = Controller.maze;

    /**
     * The A* Pathfinding algorithm uses different costs, to find the shortest path.
     * The gcost is the cost of the path to the current point.
     * hcost is the approximate distance to the end, while fcost is the sum of the two.
     * For each of the neighbours, if they aren't in the closed set, they will be evaluated, get their costs set, and then added to the openset.
     * The algorithm then takes the Node with the lowest fcost, and checks the neighbours of that. If they already have a gcost set, it will be reevaluated,
     * to check if it has a shorter g cost based on the current Node. This will eventually find the optimal path, since each Node can be reevaluated.
     * @param startPos  Start position
     * @param endPos   End position
     * @return  Returns a Node, which has a traceable path of parents from the end point to the start point.
     */
    public Node returnWaypoint(Point startPos, Point endPos){
        int counter = 0;
        ArrayList<Node> openSet = new ArrayList<>();
        HashSet<Node> closedSet = new HashSet<>();

        openSet.add(maze.getNodeAtLocation(startPos));

        if(!maze.isGround(endPos)){
            System.out.println("Pink end position is a wall!");
            return null;
        }

        while(!openSet.isEmpty()){
            counter++;
            Node current = getLowestOpen(openSet);
            closedSet.add(current);
            openSet.remove(current);
            // System.out.println(current.getPosition()+" | "+endPos);
            if(current.getPosition().equals(endPos)){
                 System.out.println("AStar path found in "+counter+" iterations");
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
                        // The gcost of the neighbour, based on the current node.
                        int neighbourCost = current.getGcost() + getDistance(current.getPosition(),neighbour.getPosition());
                        if(maze.isGround(neighbour.getPosition()) && !closedSet.contains(neighbour)){

                            //If the neighbour isn't in the openset, it hasn't been evaluated, and thus it will need the costs.
                            //Or if it already has a cost, which is larger than the new cost from the current node, it needs to be updated too.
                            if(!openSet.contains(neighbour) || neighbourCost < neighbour.getGcost()){
                                neighbour.setGcost(neighbourCost);
                                neighbour.setHcost(getDistance(neighbour.getPosition(), endPos));
                                neighbour.setParent(current);
                               //Adds the neighbour to the openset, if it isn't in it already.
                                if(!openSet.contains(neighbour)){
                                    openSet.add(neighbour);
                                }
                            }
                        }
                    }
                }
            }
        }
        System.out.println("Pink Returns Null!!");
        return null;
    }


    /**
     * Returns the Node with the lowest fcost, by evaluating all nodes from the open set.
     * @param openSet   The set of Nodes, which should be evaluated.
     * @return      The Node with the lowest fcost, and thus the shortest path.
     */
    private Node getLowestOpen(ArrayList<Node> openSet){
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
