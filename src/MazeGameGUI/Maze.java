package MazeGameGUI;

import javafx.scene.canvas.GraphicsContext;

import javafx.scene.paint.Color;

import java.awt.*;
import java.util.ArrayList;

/**
 * An Enum used to show whether a Node is an impassable wall or a walkable ground tile.
 */
enum TileType{
    Empty, Ground, Wall,
}
public class Maze {

    /**
     * The class, which determines where the ghosts spawns.
     */
    public class GhostSpawner{
        private Point position;

        public GhostSpawner(Point position){
            this.position = position;
        }

        public Point getPosition(){ return position; }
    }


    public Node[][] tiles;
    ArrayList<Node> wallList = new ArrayList<>();
    ArrayList<Node> groundList = new ArrayList<>();

    private Point bigCheesePositions[] ={new Point(7,7), new Point(15,7), new Point(12,1), new Point(12,13), new Point(3,3), new Point(3,11), new Point(21,3), new Point(21,11), new Point(23,1), new Point(23,13), new Point(1,1), new Point(1,13)};
    GhostSpawner[] ghostSpawners = new GhostSpawner[2];

    /**
     * Gets a Node from at Point.
     * @param point
     * @return  Returns a Node, which has a position equal to the point.
     */
    public Node getNodeAtLocation(Point point){
        return tiles[point.x][point.y];
    }

    /**
     * Used to check whether the Node at a position is a ground type.
     * @param position
     * @return  Returns a boolean, based on the Node's TileType.
     */
    public boolean isGround(Point position){
        if(tiles[position.x][position.y].isGround()) return true;
        else{ return false; }
    }

    /**
     * Sets up the maze.
     * Starts by filling the area with empty Nodes, which will later be filled.
     * @param w The number of tiles on the width
     * @param h The number of tiles on the height
     */
    public void SetupMaze(int w, int h){
        tiles = new Node[w][h];
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                tiles[i][j]= new Node(new Point(i,j));
            }
        }
        SetupBoundary(w,h);
        SetupGhostRoom(w,h);
        GenerateMaze(w,h);
        fillArrays(w, h);
    }

    /**
     *  This function creates the boundary of the map.
     * @param w the width of the gameboard (tiles)
     * @param h the height of the gameboard (tiles)
     */
    public void SetupBoundary(int w, int h){
        for (int i = 0; i < w; i++) {
            tiles[i][0].setTileType(TileType.Wall);
            tiles[i][h-1].setTileType(TileType.Wall);
        }
            for (int i = 0; i < h; i++) {
                tiles[0][i].setTileType(TileType.Wall);
                tiles[w-1][i].setTileType(TileType.Wall);
        }
    }

    /**
     *  This function creates the center room, in which the ghosts spawns.
     * @param w the width of the gameboard (tiles)
     * @param h the height of the gameboard (tiles)
     *          A small nested for-loop, which starts 3 tiles behind the center (width) and one tile above? the center (height)
     *          The loop goes through seven horizontal and three vertical tiles, filling them with walls.
     *          There are however some arguments, where a ground tile should be placed instead, like the middle horizontal row
     */
    public void SetupGhostRoom(int w, int h){
        int startX = (w/2)-2; int startY = (h/2)-1;
        System.out.println(startX+" | "+startY);
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 3; j++) {
                //Creates a ground tile in the top center tile. This makes sure, that the ghosts can get out.
                if(i == 2 && j == 0){
                    tiles[startX+i][startY+j].setTileType(TileType.Ground);
                }
                //Unless it's the first or last horizontal tile in the middle row, the middle row is filled with ground
                if(j == 1 && (i != 0 &&  i != 4)){
                    tiles[startX+i][startY+j].setTileType(TileType.Ground);
                    if(i == 1) ghostSpawners[0] = new GhostSpawner(new Point(startX+i,startY+j));
                    //if(i == 2) ghostSpawners[1] = new GhostSpawner(startX+i,startY+j);
                    if(i == 3) ghostSpawners[1] = new GhostSpawner(new Point(startX+i,startY+j));


                }
                //Any tiles, which are still empty, are filled with walls, creating a boundary for the ghost room.
                if(tiles[startX+i][startY+j].isEmpty()){
                    tiles[startX+i][startY+j].setTileType(TileType.Wall);
                }
            }
        }
    }

    /**
     * Generates the maze.
     * @param w the width of the gameboard (tiles)
     * @param h the height of the gameboard (tiles)
     * Works by adding ground or wall tiles on empty tiles
     */
    private void GenerateMaze(int w, int h){
        //The horizontal tiles at near the edges
        for (int i = 0; i < 5; i++) {
            //Horizontals near the edges
            tiles[2+i][2].setTileType(TileType.Wall);
            tiles[w-3-i][2].setTileType(TileType.Wall);
            tiles[2+i][h-3].setTileType(TileType.Wall);
            tiles[w-3-i][h-3].setTileType(TileType.Wall);

            //The horizontals inside them
            tiles[4+i][4].setTileType(TileType.Wall);
            tiles[4+i][h-5].setTileType(TileType.Wall);
            tiles[w-5-i][4].setTileType(TileType.Wall);
            tiles[w-5-i][h-5].setTileType(TileType.Wall);

            //The horizontals above and below the ghostpit
           // tiles[w/2-2+i][4].setTileType(TileType.Wall);
           // tiles[w/2-2+i][h-5].setTileType(TileType.Wall);

           // tiles[w/2-2+i][2].setTileType(TileType.Wall);
           // tiles[w/2-2+i][h-3].setTileType(TileType.Wall);
        }

        tiles[4][5].setTileType(TileType.Wall);
        tiles[4][h-6].setTileType(TileType.Wall);

        tiles[w-5][5].setTileType(TileType.Wall);
        tiles[w-5][h-6].setTileType(TileType.Wall);

        for (int i = 0; i < 4; i++) {
            //Horizontals in the middle(vert)
            tiles[2+i][h/2].setTileType(TileType.Wall);
            tiles[w-3-i][h/2].setTileType(TileType.Wall);
        }

        //The vertical walls near the edges
        for (int i = 0; i < 3; i++) {
            //Vertical near the edges
            tiles[2][3+i].setTileType(TileType.Wall);
            tiles[w-3][3+i].setTileType(TileType.Wall);
            tiles[2][h-4-i].setTileType(TileType.Wall);
            tiles[w-3][h-4-i].setTileType(TileType.Wall);

            //Verticals closest to the ghost spawner
            tiles[8][h/2-1+i].setTileType(TileType.Wall);
            tiles[w-9][h/2-1+i].setTileType(TileType.Wall);

            //Verticals joining the end of the horizontal in the middle(vert)
            tiles[6][h/2-1+i].setTileType(TileType.Wall);
            tiles[w-7][h/2-1+i].setTileType(TileType.Wall);
        }

        for (int i = 0; i < 2; i++) {
            tiles[8][1+i].setTileType(TileType.Wall);
            tiles[8][h-2-i].setTileType(TileType.Wall);
            tiles[w-9][1+i].setTileType(TileType.Wall);
            tiles[w-9][h-2-i].setTileType(TileType.Wall);
        }
    }

    /**
     *  This function runs through the tiles array, and fills the groundList and wallList, with the corresponding tiles.
     * @param w the width of the gameboard (tiles)
     * @param h the height of the gameboard (tiles)
     */
    private void fillArrays(int w, int h){
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                if(tiles[i][j].isEmpty()){
                   tiles[i][j].setTileType(TileType.Ground);
                }
                if(tiles[i][j].isWall()){
                    wallList.add(tiles[i][j]);

                }

                if(tiles[i][j].isGround())
                {
                    //Makes sure that all empty tiles are set to ground tile
                         groundList.add(tiles[i][j]);
                }
            }
        }
    }

    /**
     * @return Returns and generates a list of cheese, which the player can pickup
     */
    public ArrayList<Item> AddCheese(){
        ArrayList<Item> list = new ArrayList<>();
        for(Node n : groundList){
            list.add(new Item(n.getPosition()));
        }
        for (int i = 0; i < bigCheesePositions.length; i++) {
                list.add(new Item(bigCheesePositions[i], true));
        }
        return list;
    }

    public void Draw(GraphicsContext g, SceneInfo scene){
        for (int i = 0; i < scene.getWidth(); i++) {
            for (int j = 0; j < scene.getHeight(); j++) {

                if(tiles[i][j].isWall()) g.setFill(Color.BLACK);
                else if(tiles[i][j].isGround()) g.setFill(Color.AZURE);
                else if(tiles[i][j].isEmpty()) g.setFill(Color.CORAL);
                g.fillRoundRect(tiles[i][j].getPosition().x * scene.getFieldWidth(), tiles[i][j].getPosition().y * scene.getFieldHeight(), scene.getFieldWidth(), scene.getFieldHeight(), 5, 5);
            }
        }
    }
}
