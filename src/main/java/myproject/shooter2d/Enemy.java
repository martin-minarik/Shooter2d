package myproject.shooter2d;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.image.ImageView;

import java.util.Random;

public class Enemy extends Entity implements IEnemy
{
    private ImageView imageView;

    double fireTimer = 0;
    double moveTimer = 0;


    public Enemy(Group parent, Game game, Point2D position, Point2D velocity)
    {
        super(parent, game, position, velocity);
        imageView = new ImageView(Constants.ENEMY_IMAGE);

        this.transform.getChildren().add(imageView);
    }


    @Override
    public void hitBy(Entity entity)
    {
        if (entity instanceof IProjectile projectile)
            if (projectile.getOwner() instanceof IPlayer)
            {
                this.destroy();
            }
    }

    @Override
    public void destroy()
    {
        game.addScore(10);
        super.destroy();
    }

    public void update(double deltaT)
    {
        velocity = velocity.multiply(1 - 0.2 * deltaT);

        if (moveTimer <= 0)
        {
            moveRandomly();
            moveTimer = 4;
        }

        fireTimer -= deltaT;
        moveTimer -= deltaT;


        super.update(deltaT);
    }


    public boolean isReadyTofire()
    {
        return fireTimer <= 0;
    }

    public void moveRandomly()
    {
        Random random = new Random();

        velocity = new Point2D((random.nextInt(50 - 20) + 20) * (random.nextBoolean() ? 1 : -1),
                (random.nextInt(50 - 20) + 20) * (random.nextBoolean() ? 1 : -1));
    }

    public Bounds getBounds()
    {
        return this.root.getBoundsInParent();
    }

    public void fireAtTarget(Entity target)
    {
        this.game.addProjectile(
                new Bullet((Group) this.root.getParent(),
                        this.game,
                        this.position,
                        new Point2D(target.position.getX() - this.position.getX(),
                                target.position.getY() - this.position.getY()).normalize().multiply(100),
                        Constants.BULLET_ENEMY_IMAGE,
                        this
                )
        );
        fireTimer = 3;
    }
}
