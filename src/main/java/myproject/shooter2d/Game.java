package myproject.shooter2d;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.media.AudioClip;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Stream;

public class Game
{
    private final double width;
    private final double height;
    private double enemySpawnTimer = 2;
    private double totalTime = 0;

    private final Group group;
    private final IPlayer player;
    private final List<Entity> entities = new LinkedList<>();
    private final IMinimap minimap;

    IGameListener gameListener;

    private int score;
    private int lives;
    private int bombs;

    AudioClip clipPlayerFire;
    AudioClip clipPlayerHit;
    AudioClip clipExplosion;

    public Game(Group group, double width, double height)
    {
        this.width = width;
        this.height = height;
        this.group = group;

        this.score = 0;
        this.lives = 3;
        this.bombs = 3;

        this.minimap = new Minimap();
        player = new PlayerShip(group, this, new Point2D(width / 2, height / 2), new Point2D(0, 0));
        entities.add((Entity) player);

        clipPlayerFire = new AudioClip(Objects.requireNonNull(getClass().getResource("sound/SciFiGun3.wav")).toString());
        clipPlayerHit = new AudioClip(Objects.requireNonNull(getClass().getResource("sound/SciFiGun12.wav")).toString());
        clipExplosion = new AudioClip(Objects.requireNonNull(getClass().getResource("sound/Explosion4.wav")).toString());
    }


    public double getWidth()
    {
        return width;
    }

    public double getHeight()
    {
        return height;
    }

    public void simulate(double deltaT)
    {
        for (int i = 0; i < entities.size(); i++)
        {
            Entity entity = entities.get(i);

            entity.update(deltaT);


            if (entity instanceof IEnemy enemy)
            {
                if (enemy.isReadyTofire() && entity.alive)
                    enemy.fireAtTarget((Entity) player);
            }


            if (entity.leavingBounds(this.group.getParent().getLayoutBounds()))
            {
                if (entity instanceof IProjectile projectile)
                {
                    projectile.destroy();
                } else
                {
                    if (entity instanceof IEnemy)
                        entity.invertVelocity();

                    else
                        entity.clampPositionToBounds(this.group.getParent().getLayoutBounds());
                }
            }
        }

        entities.removeIf(e -> !e.isAlive());


        for (Entity entity_a : entities)
        {
            for (Entity entity_b : entities)
            {
                if ((entity_a != entity_b) && entity_a.intersects(entity_b))
                {
                    entity_a.hitBy(entity_b);
                }
            }
        }


        if (enemySpawnTimer <= 0)
        {
            spawnRandomEnemies(1 + (int) (totalTime / 12));
            this.enemySpawnTimer = 4;
        }

        this.totalTime += deltaT;
        this.enemySpawnTimer -= deltaT;
    }


    public void drawMinimap(Canvas canvas)
    {
        this.minimap.draw(canvas, this.group.getParent().getLayoutBounds(), entities);
    }

    public void playerMove(double horizontalDirection, double verticalDirection)
    {
        player.move(horizontalDirection, verticalDirection);
    }

    public void playerFire()
    {
        clipPlayerFire.play();
        player.fireBullet();
    }

    public void addProjectile(IProjectile projectile)
    {
        if (projectile instanceof Entity entity)
            entities.add(entity);
    }

    public void decreaseLives()
    {
        --this.lives;
        this.gameListener.stateChanged(this.score, this.lives, this.bombs);
        clipPlayerHit.play();
        if (this.lives <= 0)
            this.gameListener.gameOver();
    }

    public void useBomb()
    {
        if(bombs > 0)
        {
            --this.bombs;

            for (Entity entity : entities)
                if (entity instanceof IEnemy || entity instanceof IProjectile)
                    entity.destroy();

            entities.removeIf(e -> !e.isAlive());

            this.gameListener.stateChanged(this.score, this.lives, this.bombs);

            clipExplosion.play();
        }
    }

    public void addScore(int score)
    {
        this.score += score;
        this.gameListener.stateChanged(this.score, this.lives, this.bombs);
    }

    public Point2D generateRandomEnemyPosition()
    {
        Random random = new Random();

        double x;
        double y;

        Point2D playerPosition = ((Entity) player).getPosition();

        do
        {
            x = random.nextBoolean() ?
                    random.nextDouble(Math.max(playerPosition.getX() - 1, 1)) + 1
                    : random.nextDouble(Math.max(this.width - playerPosition.getX() - 40, 1))
                    + playerPosition.getX() + 1;


            y = random.nextBoolean() ?
                    random.nextDouble(Math.max(playerPosition.getY() - 1, 1)) + 1
                    : random.nextDouble(Math.max(this.height - playerPosition.getY() - 40, 1))
                    + playerPosition.getY() + 1;

        } while ((x >= playerPosition.getX() - 120 && x <= playerPosition.getX())
                || (x >= playerPosition.getX() && x <= playerPosition.getX() + 120)
                || (y >= playerPosition.getY() - 120 && y <= playerPosition.getY())
                || (y >= playerPosition.getY() && y <= playerPosition.getY() + 120));

        return new Point2D(x, y);
    }

    public void spawnRandomEnemies(int count)
    {
        Stream.generate(this::generateRandomEnemyPosition)
                .distinct()
                .limit(count).forEach(point2D
                        -> entities.add(new Enemy(this.group, this, point2D, new Point2D(0, 0))));
    }

    public void setController(IGameListener gameListener)
    {
        this.gameListener = gameListener;
    }


}
