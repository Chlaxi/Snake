package MazeGameGUI;

import javafx.scene.canvas.GraphicsContext;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Chloe on 09/04/2017.
 */
public interface INPC {

    public ArrayList<Node> getPath();

    void followPath();

    public void Behaviour();

    public void Update();

    public void Draw(GraphicsContext g, SceneInfo scene);

}
