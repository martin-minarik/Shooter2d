package myproject.shooter2d;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class PlayerShip extends Entity implements IPlayer
{
    private final ImageView shipImageView;
    private final ImageView flameImageView;
    private final double THRUST = 300;
    private int currentThrottle = 0;


    public PlayerShip(Group parent, Game game, Point2D position, Point2D velocity)
    {
        super(parent, game, position, velocity);
        shipImageView = new ImageView(Constants.SHIP_IMAGE);
        shipImageView.setFitWidth(66);
        shipImageView.setFitHeight(25);

        flameImageView = new ImageView(Constants.FLAME_IMAGE);
        flameImageView.setFitWidth(40);
        flameImageView.setFitHeight(25);
        flameImageView.setScaleX(-1);

        Pane pane = new Pane();
        pane.setLayoutX(58);
        pane.setMinWidth(flameImageView.getFitWidth());
        flameImageView.setVisible(false);

        pane.getChildren().add(flameImageView);
        this.transform.getChildren().add(pane);
        this.transform.getChildren().add(shipImageView);
    }


    @Override
    public void hitBy(Entity entity)
    {
        if (entity instanceof IProjectile projectile)
        {
            if (projectile.getOwner() instanceof IEnemy)
            {
                game.decreaseLives();
            }
        }

        if (entity instanceof IEnemy)
        {
            entity.destroy();
            game.addScore(10);

            game.decreaseLives();
        }
    }

    public void update(double deltaT)
    {
        if (currentThrottle != 0)
        {

            velocity = velocity.normalize();
            velocity = velocity.multiply(THRUST * currentThrottle);
            flameImageView.setVisible(true);

        } else
        {

            velocity = velocity.multiply((0.7) * (1 - deltaT));

            flameImageView.setVisible(false);
        }


        currentThrottle = 0;
        super.update(deltaT);
    }

    public void move(double horizontalDirection, double verticalDirection)
    {
        this.velocity = new Point2D(horizontalDirection, verticalDirection).normalize();

        this.currentThrottle = (horizontalDirection != 0 || verticalDirection != 0) ? 1 : 0;

        if (horizontalDirection != 0)
            flip_ship(horizontalDirection);

    }

    public void flip_ship(double direction)
    {
        this.root.setScaleX(direction < 0 ? 1 : -1);
    }

    public Bounds getBounds()
    {
        return this.root.getBoundsInParent();
    }

    public int getDirection()
    {
        return this.root.getScaleX() > 0 ? -1 : 1;
    }

    public void fireBullet()
    {
        this.game.addProjectile(
                new Bullet((Group) this.root.getParent(),
                        this.game,
                        new Point2D(this.getBounds().getCenterX(), this.getBounds().getCenterY()),
                        new Point2D(800 * this.getDirection(), 0),
                        Constants.BULLET_PLAYER_IMAGE,
                        this
                )
        );
    }


}
