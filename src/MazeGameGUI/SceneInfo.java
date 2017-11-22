package MazeGameGUI;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import java.awt.*;
import java.util.Random;

/**
 * Created by Chloe on 23/02/2017.
 */
public class SceneInfo {
    private Random random = new Random();
    private double fieldHeight;
    private double fieldWidth;
    private int width = 25;
    private int height = 15;

    /**
     * Calculate height and width of each field
     * @param canvas
     */
    public SceneInfo(Canvas canvas) {
        fieldHeight = canvas.getHeight() / height;
        fieldWidth = canvas.getWidth() / width;
    }

    public Point getRandomPoint() {
        return new Point(random.nextInt(width), random.nextInt(height));
    }

    public void draw(GraphicsContext g){
        g.clearRect(0,0,width*fieldWidth ,height*fieldHeight);
    }

    public double getFieldHeight() {
        return fieldHeight;
    }

    public void setFieldHeight(double fieldHeight) {
        this.fieldHeight = fieldHeight;
    }

    public double getFieldWidth() {
        return fieldWidth;
    }

    public void setFieldWidth(double fieldWidth) {
        this.fieldWidth = fieldWidth;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
