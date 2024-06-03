package myproject.shooter2d;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.transform.Translate;

public abstract class Entity
{
    protected final Game game;
    protected Point2D position;
    protected Point2D velocity;

    protected Group root;
    protected Group transform;
    protected boolean alive;


    public Entity(Group parent, Game game, Point2D position, Point2D velocity)
    {
        this.game = game;
        this.position = position;
        this.velocity = velocity;


        root = new Group();
        transform = new Group();

        root.getChildren().add(transform);
        parent.getChildren().add(root);

        transform.getTransforms().addAll(
                new Translate(position.getX(), position.getY())
        );

        alive = true;
    }

    public void update(double deltaT)
    {
        position = position.add(velocity.multiply(deltaT));
        transform.getTransforms().clear();
        transform.getTransforms().addAll(
                new Translate(position.getX(), position.getY())
        );
    }

    public void destroy()
    {
        alive = false;
        if(this.root.getParent() != null)
             ((Group) this.root.getParent()).getChildren().remove(root);
    }

    public boolean isAlive()
    {
        return alive;
    }

    abstract Bounds getBounds();

    public boolean intersects(Entity other)
    {
        return this.intersects(other.getBounds());
    }

    public boolean intersects(Bounds otherBounds)
    {
        return this.getBounds().intersects(otherBounds);
    }

    public boolean isOutside(Bounds otherBounds)
    {
        return !intersects(otherBounds);
    }

    public boolean leavingBounds(Bounds otherBounds)
    {
        Bounds bounds = this.getBounds();

        return bounds.getMinX() < otherBounds.getMinX() ||
                bounds.getMaxX() > otherBounds.getMaxX() ||
                bounds.getMinY() < otherBounds.getMinY() ||
                bounds.getMaxY() > otherBounds.getMaxY();
    }

    public void clampPositionToBounds(Bounds bounds)
    {
        this.position = new Point2D(
                clamp(this.position.getX(), bounds.getMinX(), bounds.getMaxX() - this.getBounds().getWidth()),
                clamp(this.position.getY(), bounds.getMinY(), bounds.getMaxY() - this.getBounds().getHeight())
        );
    }

    private double clamp(double value, double min, double max)
    {
        if (value < min) return min;
        else if (value > max) return max;
        return value;
    }

    public Point2D getPosition()
    {
        return position;
    }

    public void setPosition(Point2D position)
    {
        this.position = position;
    }

    public Point2D getVelocity()
    {
        return velocity;
    }

    public void setVelocity(Point2D velocity)
    {
        this.velocity = velocity;
    }

    public void stopMoving()
    {
        this.velocity = new Point2D(0, 0);
    }

    public void invertVelocity()
    {
        this.velocity = this.velocity.multiply(-1);
    }

    public abstract void hitBy(Entity entity);

}
