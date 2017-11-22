package NPCs;

import MazeGameGUI.*;

import java.awt.*;
import java.util.*;

/**
 * Created by Chloe on 12/04/2017.
 */
public class BreadthFirst extends IPathfinding {

    Maze maze = Controller.maze;

    /**
     * Requires a start and end position as points. It then searches for a path between the two points.
     * Using the Breadth First algorithm, it checks each neighbouring node, and adds them to a queue, if they haven't been evaluted yet.
     * The queue simply stores Nodes, which should be analyzed, while the closedSet stores already evaluted Nodes.
     * Each neighbour will also get a parent assigned, so a pth is traceable between the start and end points.
     * @param startPos The start position
     * @param endPos   The end position
     * @return         Returns a Node, which has a traceable path of parents from the end point to the start point.
     */
    public Node returnWaypoint(Point startPos, Point endPos){
        ArrayList<Node> closedSet = new ArrayList<>();
        Queue<Node> queue = new LinkedList<>();
        int counter = 0;
        closedSet.add(maze.getNodeAtLocation(startPos));
        queue.add(closedSet.get(0));

        if(!maze.isGround(endPos)){
            System.out.println("Pink end position is a wall!");
            return null;
        }

        while(!queue.isEmpty()){
            counter++;
           Node current = queue.remove();

           // System.out.println(current.getPosition()+" | "+endPos);
            if(current.getPosition().equals(endPos)){
                System.out.println("Breadth First path found in "+counter+" iterations");
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
                            closedSet.add(neighbour);
                            neighbour.setParent(current);
                            queue.add(neighbour);
                        }
                    }
                }
            }
        }
        System.out.println("Pink Returns Null!!");
        return null;
    }

}
