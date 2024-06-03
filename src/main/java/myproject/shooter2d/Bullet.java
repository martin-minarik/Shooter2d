package myproject.shooter2d;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Bullet extends Entity implements IProjectile
{
    private ImageView imageView;
    private Entity owner;

    public Bullet(Group parent, Game game, Point2D position, Point2D velocity, Image image, Entity owner)
    {
        super(parent, game, position, velocity);
        imageView = new ImageView(image);
        imageView.setScaleX(Math.signum(velocity.getX()));
        this.owner = owner;
        this.transform.getChildren().add(imageView);
    }

    @Override
    Bounds getBounds()
    {
        return this.root.getBoundsInParent();
    }

    @Override
    public void hitBy(Entity entity)
    {
        if(!(entity instanceof IProjectile))
            if(this.owner != entity)
                this.destroy();
    }

    @Override
    public Entity getOwner()
    {
        return owner;
    }




}
