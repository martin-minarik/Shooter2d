package myproject.shooter2d;

public interface IEnemy
{
    public boolean isReadyTofire();

    public void moveRandomly();

    public void fireAtTarget(Entity target);
}
