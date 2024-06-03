package myproject.shooter2d;

import javafx.geometry.Bounds;
import javafx.scene.canvas.Canvas;

import java.util.List;

public interface IMinimap
{


    public void draw(Canvas canvas, Bounds bounds, List<Entity> entities);
    public void clear(Canvas canvas);
}
