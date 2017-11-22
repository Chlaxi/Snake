package NPCs;

import MazeGameGUI.Node;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

/**
 *  Created by Chloe on 09/04/2017.
 *  Used as an extension on the pathfinding classes, so they can be used as generics by the ghosts.
 */

public abstract class IPathfinding {

    /**
     * This is the function which should be called. It calls the sort funciton as well as the pathfinding function.
     * @param startPos  Current position
     * @param endPos    End position
     * @return  A list of Nodes will be returned, allowing the user to get a path fra start to end.
     */
    public ArrayList<Node> getWaypoint(Point startPos, Point endPos){
        return sort(returnWaypoint(startPos,endPos),startPos);
    }

    /**
     *
     * @param startPos Start position
     * @param endPos    End position
     * @return  Oughth to return a Node, which will have a path through parents. Returns null in this case, since it's an abstract.
     */
    public Node returnWaypoint(Point startPos, Point endPos){
        return null;
    }

    /**
     * Sorts the List, so the waypoints will be in the correct order.
     * This is done by going through all the nodes, via their parents, and adding them at the start of a List.
     * @param n The end node.
     * @param startPosition The start position, so we know when we reached the startpoint.
     * @return  Returns a listed, which will be sorted in the correct order.
     */
    public ArrayList<Node> sort(Node n,Point startPosition){
        if(n != null)
        {
            ArrayList<Node> sortedList = new ArrayList<>();

            sortedList.add(n);
            while(n.getPosition() != startPosition){
                sortedList.add(0,n);
                if(n.getParent() == null) break;
                n = n.getParent();
            }
            return sortedList;
        }
        else{
            return null;
        }
    }
}
