package NPCs;

import MazeGameGUI.*;

import java.awt.*;
import java.util.*;

/**
 * Created by Chloe on 24/04/2017.
 *
 *
 Searches from initial state to last state and also from the last state to initial state, stopping when the two searches meet at a node in between (or when found the goal state).
 Advantage: Time and memory requirements good, comparatively
 Disadvantage: Not always feasible, or possible, to search backward through possible states.

 */
public class BiDirectional extends IPathfinding{

    Maze maze = Controller.maze;

    /**
     * Using the same principles from the Breadth First algorithm. However, it searches from both the start- and endpoint.
     * Once the current Node finds a neighbour in the opposing set, two paths have been found, which combined will give the entire path.
     * The path going from the end node will however have an inverted parent hierachy, which needs to be resorted.
     * There is a bug, where the end node will never be reached, but the node before that will be the end node instead
     * @param startPos The start position
     * @param endPos The end position
     * @return  Returns a Node, which has a traceable path of parents from the end point to the start point.
     */
    public Node returnWaypoint(Point startPos, Point endPos){
        int counter = 0;
        ArrayList<Node> closedStartSet = new ArrayList<>();
        ArrayList<Node> closedEndSet = new ArrayList<>();
        Queue<Node> startQueue = new LinkedList<>();
        Queue<Node> endQueue = new LinkedList<>();
        boolean isStart = true;
        closedStartSet.add(maze.getNodeAtLocation(startPos));
        closedEndSet.add(maze.getNodeAtLocation(endPos));
        startQueue.add(closedStartSet.get(0));
        endQueue.add(closedEndSet.get(0));

        if(!maze.isGround(endPos)){
            System.out.println("Blue end position is a wall!");
            return null;
        }

        while(!startQueue.isEmpty() && !endQueue.isEmpty()){
            counter++;
            isStart = !isStart;
            Node current;
            if(isStart)
            current = startQueue.remove();
            else current = endQueue.remove();

            for (int i = -1; i < 2; i++) {
                for (int j = -1; j < 2; j++) {
                    if(i == 0 && j == 0) continue;  //Skips the current node.
                    //The diagonal exceptions
                    if((i == -1 && j == -1) ||(i == -1 && j == 1) || (i == 1 && j == -1) || (i == 1 && j == 1)){
                        continue;
                    }
                    else{
                        Node neighbour = maze.getNodeAtLocation(new Point(current.getPosition().x + i, current.getPosition().y + j));
                        if(isStart)
                        {
                            if(closedEndSet.contains(neighbour)){
                                System.out.println("Bidirectional path found in "+counter+" iterations");
                                return gatherPath(current,neighbour);
                            }
                            if(maze.isGround(neighbour.getPosition()) && !closedStartSet.contains(neighbour)){
                                closedStartSet.add(neighbour);
                                neighbour.setParent(current);
                                startQueue.add(neighbour);
                            }
                        }
                        else{
                            if(closedStartSet.contains(neighbour)){
                                System.out.println("Bidirectional path found in "+counter+" iterations");
                                return gatherPath(neighbour,current);
                            }
                            if(maze.isGround(neighbour.getPosition()) && !closedEndSet.contains(neighbour)){
                                closedEndSet.add(neighbour);
                                neighbour.setParent(current);
                                endQueue.add(neighbour);
                            }
                        }
                    }
                }
            }
        }
        System.out.println("Blue Returns Null!!");
        return null;
    }

    //TODO Refactor this, so current is returned, rather than prev.
    /**
     *  This function gathers the two lists of waypoints into one, which will make a traceable path from end to start, via the Nodes' parents
     * @param start   The endpoint from the end set, which is next to the endpoint from the start set.
     * @param end The endpoint from the start set, which is next to the endpoint from the end set.
     * @return Returns a Node, which has a traceable path of parents from the end point to the start point.
     */
    private Node gatherPath(Node start, Node end){
     //   System.out.println(start+" | "+end);
        ArrayList<Node> list = new ArrayList<>();
        Node prev = start;
        System.out.println(prev.getParent());
        list.add(prev);
        Node current = end;
        Node next = null;

        while(current.getParent() != null){
            next = current.getParent();
            current.setParent(prev);
            list.add(current);
            prev = current;
          //  System.out.println("current: "+current);
            current = next;
        }

      //  System.out.println("Returns the path, starting at "+list.toString());
       // System.out.println("path found");
        return prev;

    }

}
