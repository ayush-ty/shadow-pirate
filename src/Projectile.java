import bagel.*;
import bagel.util.*;

/**
 * Projectile class that inherits Entity class
 * @author Ayush Tyagi
 */
public class Projectile extends Entity {
    private double rotation;
    private double speed;
    private double sailorCentreX;
    private double sailorCentreY;
    private final Point INITIAL_POSITION;

    protected final DrawOptions PROJECTILE = new DrawOptions();

    /**
     * Constructor to initialise projectile in game

     */
    public Projectile (double startX, double startY, double speed, Image image, Sailor sailor) {
        super(startX, startY);
        this.INITIAL_POSITION = new Point(startX, startY);
        this.speed = speed;
        this.setCurrImage(image);

        // setting target point
        setCentrePoints(sailor);
    }

    /**
     * Update method to update state of projectile in game
     */
    public void update(Sailor sailor) {
        calculateRotation();
        projectileMovement();
    }

    /**
     * Method that checks if projectile shot by pirate has hit sailor
     */
    public boolean hitSailor(Sailor sailor, Pirate pirate) {
        if (sailor.getBoundingBox().intersects(getPosition())) {
            // sailor has been hit by projectile
            sailor.setHealthPoints(sailor.getHealthPoints() - pirate.getDamagePoints());

            // logging the damage
            if (pirate instanceof Blackbeard) {
                System.out.println("Blackbeard inflicts " + pirate.getDamagePoints() + " damage points on Sailor. " +
                        "Sailor's current health: " + sailor.getHealthPoints() + "/" + sailor.getMaxHealth());
            } else {
                System.out.println("Pirate inflicts " + pirate.getDamagePoints() + " damage points on Sailor. " +
                        "Sailor's current health: " + sailor.getHealthPoints() + "/" + sailor.getMaxHealth());
            }
            return true;
        }

        return false;
    }

    private void calculateRotation() {
        // calculating appropriate rotation for projectile
        double oppositeSideFromAngle = INITIAL_POSITION.y - sailorCentreY;
        double adjacentSideFromAngle = INITIAL_POSITION.x - sailorCentreX;

        rotation = Math.atan(oppositeSideFromAngle/adjacentSideFromAngle);
        PROJECTILE.setRotation(rotation);
    }

    private void setCentrePoints(Sailor sailor) {
        sailorCentreX = sailor.getPosition().x + sailor.getCurrImage().getWidth()/2;
        sailorCentreY = sailor.getPosition().y + sailor.getCurrImage().getHeight()/2;
    }

    private void projectileMovement() {
        // getting unit vectors to get direction to target point
        double directionX = (sailorCentreX - INITIAL_POSITION.x)/INITIAL_POSITION.distanceTo(new Point(sailorCentreX, sailorCentreY));
        double directionY = (sailorCentreY - INITIAL_POSITION.y)/INITIAL_POSITION.distanceTo(new Point(sailorCentreX, sailorCentreY));

        getCurrImage().draw(getPosition().x, getPosition().y, PROJECTILE);

        // moving projectile with correct speed
        setPosition(new Point(getPosition().x + (speed * directionX), getPosition().y + (speed * directionY)));
    }

    /**
     * Method to check if projectile has reached level boundary
     */
    public boolean hitWall() {
        Point bottomRight = ShadowPirate.getBottomRight();
        Point topLeft = ShadowPirate.getTopLeft();

        return getPosition().x <= topLeft.x || getPosition().x >= bottomRight.x ||
               getPosition().y <= topLeft.y || getPosition().y >= bottomRight.y;
    }
}
