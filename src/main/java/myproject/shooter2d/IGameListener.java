package myproject.shooter2d;

public interface IGameListener
{
	
	void stateChanged(int lives, int bombs, int score);
	public void gameOver();
}