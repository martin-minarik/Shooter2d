package myproject.shooter2d;

import javafx.geometry.Bounds;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.List;

public class Minimap implements IMinimap
{
    @Override
    public void draw(Canvas canvas, Bounds bounds, List<Entity> entities)
    {
        clear(canvas);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.save();


        double widthRatio = canvas.getWidth() / bounds.getWidth();
        double heightRatio = canvas.getHeight() / bounds.getHeight();

        double x;
        double y;

        for (Entity entity : entities)
        {
            x = (entity.position.getX() + entity.getBounds().getWidth() / 2) * widthRatio;
            y = (entity.position.getY() + entity.getBounds().getHeight() / 2) * heightRatio;

            if (!(entity instanceof IProjectile projectile))
            {
                if (entity instanceof IPlayer)
                    gc.setFill(Color.GREEN);
                if (entity instanceof IEnemy)
                    gc.setFill(Color.RED);
                gc.fillRect(x, y, 10, 10);

            } else {

                if (projectile.getOwner() instanceof IPlayer)
                    gc.setFill(Color.YELLOW);
                else
                    gc.setFill(Color.RED);

                gc.fillRect(x, y, 3, 3);
            }
        }

        gc.restore();
    }

    @Override
    public void clear(Canvas canvas)
    {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.save();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.restore();
    }
}
