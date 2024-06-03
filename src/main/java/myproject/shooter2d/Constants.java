package myproject.shooter2d;

import javafx.scene.image.Image;

import java.util.Objects;

public final class Constants
{
    public static final Image SHIP_IMAGE;
    public static final Image FLAME_IMAGE;
    public static final Image BULLET_PLAYER_IMAGE;
    public static final Image BULLET_ENEMY_IMAGE;
    public static final Image ENEMY_IMAGE;

    static{
        SHIP_IMAGE = new Image(Objects.requireNonNull(Constants.class.getResourceAsStream("image/ship.png")));
        BULLET_PLAYER_IMAGE = new Image(Objects.requireNonNull(Constants.class.getResourceAsStream("image/bullet_player.png")));
        BULLET_ENEMY_IMAGE = new Image(Objects.requireNonNull(Constants.class.getResourceAsStream("image/bullet_enemy.png")));
        ENEMY_IMAGE = new Image(Objects.requireNonNull(Constants.class.getResourceAsStream("image/enemy.png")));
        FLAME_IMAGE = new Image(Objects.requireNonNull(Constants.class.getResourceAsStream("image/flame.png")));
    }
}
