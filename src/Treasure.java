import bagel.*;

/**
 * Treasure class that inherits Entity class
 * @author Ayush Tyagi
 */
public class Treasure extends Entity {
    private final Image TREASURE = new Image("res/treasure.png");

    /**
     * Constructor to set entity's position
     */
    public Treasure(double startX, double startY) {
        super(startX, startY);
        this.setCurrImage(TREASURE);
    }

    /**
     * Update method to update treasure state in game
     */
    public void update() {
        getCurrImage().drawFromTopLeft(getPosition().x, getPosition().y);
    }
}
