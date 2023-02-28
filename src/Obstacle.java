/**
 * Abstract Obstacle class that inherits Entity class
 * @author Ayush Tyagi
 */
public abstract class Obstacle extends Entity {

    /**
     * Constructor to set obstacle's position in game
     */
    public Obstacle (double startX, double startY) {
        super(startX, startY);
    }

    /**
     * Abstract update method to update the state of an object in the game
     */
    public abstract void update();
}
