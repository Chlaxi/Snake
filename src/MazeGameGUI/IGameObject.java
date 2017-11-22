package MazeGameGUI;

import MazeGameGUI.SceneInfo;
import javafx.scene.canvas.GraphicsContext;


/**
 * Created by Chloe Parbst on 16/02/2017.
 */
public interface IGameObject {

    public abstract void update();

    public void draw(GraphicsContext g, SceneInfo scene);
}
