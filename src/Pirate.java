import bagel.*;
import bagel.util.*;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Pirate class that inherits Character class
 * @author Ayush Tyagi
 */
public class Pirate extends Character {
    private Image leftImage = new Image("res/pirate/pirateLeft.png");
    private Image rightImage = new Image("res/pirate/pirateRight.png");
    private Image leftInvincibleImage = new Image("res/pirate/pirateHitLeft.png");
    private Image rightInvincibleImage = new Image("res/pirate/pirateHitRight.png");
    private Image projImage = new Image("res/pirate/pirateProjectile.png");

    private final static int FONT_SIZE = 15;
    private final static int HEALTH_OFFSET = 6;

    private static final double MAX_SPEED = 0.7;
    private static final double MIN_SPEED = 0.2;
    protected static final int INITIAL_DAMAGE_POINTS = 10;
    protected final static int MAX_HEALTH = 45;

    private boolean coolingDown;
    private boolean invincible;
    private Timer timer;
    private double pirateCentreX;
    private double pirateCentreY;
    private String movingDirection;
    private final int PIRATE_RANGE;
    private final int PIRATE_HALF_RANGE;
    private final int COOLDOWN;
    private final double PROJ_SPEED;

    protected ArrayList<Projectile> projectiles = new ArrayList<>();

    /**
     * Constructor for initialising pirate in game
     */
    public Pirate(double startX, double startY) {
        super(startX, startY, INITIAL_DAMAGE_POINTS, FONT_SIZE, MAX_HEALTH);
        coolingDown = invincible = false;

        // setting random direction for pirate movement
        Random random = new Random();
        int randomInt = random.nextInt(4);
        if (randomInt == 0)
            movingDirection = "left";
        else if (randomInt == 1)
            movingDirection = "right";
        else if (randomInt == 2)
            movingDirection = "up";
        else
            movingDirection = "down";

        // setting random speed between 0.2 and 0.7 for pirates
        double speed = MIN_SPEED + (MAX_SPEED - MIN_SPEED) * random.nextDouble();
        this.setSpeed(speed);
        this.setCurrImage(rightImage);
        this.PIRATE_RANGE = 200;
        this.PIRATE_HALF_RANGE = PIRATE_RANGE / 2;
        this.COOLDOWN = 3000;
        this.PROJ_SPEED = 0.4;
        timer = new Timer();
    }

    /**
     * Overloaded constructor for Blackbeard initialization
     */
    public Pirate(double startX, double startY, int dmgPoints, int maxHealth, int pirateRange, int cooldown, double projSpeed) {
        super(startX, startY, dmgPoints, FONT_SIZE, maxHealth);
        coolingDown = invincible = false;

        Random random = new Random();
        int randomInt = random.nextInt(4);
        if (randomInt == 0)
            movingDirection = "left";
        else if (randomInt == 1)
            movingDirection = "right";
        else if (randomInt == 2)
            movingDirection = "up";
        else
            movingDirection = "down";

        double speed = MIN_SPEED + (MAX_SPEED - MIN_SPEED) * random.nextDouble();
        this.setSpeed(speed);
        timer = new Timer();
        this.PIRATE_RANGE = pirateRange;
        this.PIRATE_HALF_RANGE = pirateRange / 2;
        this.COOLDOWN = cooldown;
        this.PROJ_SPEED = projSpeed;
    }

    /**
     * Method that performs state update
     */
    public void update(Sailor sailor, ArrayList<Entity> entities) {
        if (!isDead()) {
            pirateMovement();
            checkCollisions(entities);
            checkInFrame();
            setHealthCoords();
            renderHealthPoints();
            if (sailorInRange(sailor) && !coolingDown) {
                shoot(sailor);
                cooldown();
            }
        }

        // render projectiles if they are shot by pirate
        for (int i =0; i< projectiles.size(); i++) {
            // stop rendering projectile if hitting sailor or reached level boundary
            if (projectiles.get(i).hitSailor(sailor, this) || projectiles.get(i).hitWall())
                projectiles.remove(i);
            else
                projectiles.get(i).update(sailor);
        }
    }

    private void pirateMovement() {
        // use invincible image if pirate is in invincible state
        if (movingDirection.equals("left")) {
            move(-getSpeed(), 0);
            checkInvincibility(leftImage, leftInvincibleImage);
        } else if (movingDirection.equals("right")) {
            move(getSpeed(), 0);
            checkInvincibility(rightImage, rightInvincibleImage);
        } else if (movingDirection.equals("up")) {
            move(0, -getSpeed());
            checkInvincibility(rightImage, rightInvincibleImage);
        } else {
            move(0, getSpeed());
            checkInvincibility(rightImage, rightInvincibleImage);
        }
        getCurrImage().drawFromTopLeft(getPosition().x, getPosition().y);
    }

    @Override
    public void checkCollisions(ArrayList<Entity> entities) {
        // if colliding with an obstacle, switch direction
        for (Entity entity: entities) {
            if (collide(this, entity)) {
                switchDirection();
                break;
            }
        }
    }

    private void checkInFrame() {
        if (hitWall()) {
            switchDirection();
        }
    }

    private void switchDirection() {
        if (movingDirection.equals("right"))
            movingDirection = "left";
        else if (movingDirection.equals("left"))
            movingDirection = "right";
        else if (movingDirection.equals("up"))
            movingDirection = "down";
        else
            movingDirection = "up";
    }

    private void checkInvincibility(Image image, Image imageInvincible) {
        if (!invincible)
            this.setCurrImage(image);
        else
            this.setCurrImage(imageInvincible);
    }

    private void setHealthCoords() {
        setHealthX((int) getPosition().x);
        setHealthY((int) (getPosition().y - HEALTH_OFFSET));
    }

    protected boolean sailorInRange(Sailor sailor) {
        pirateCentreX = getPosition().x + getCurrImage().getWidth()/2;
        pirateCentreY = getPosition().y + getCurrImage().getHeight()/2;

        // compute top left point of attack range square
        Point topLeftAttackRange = new Point(pirateCentreX - PIRATE_HALF_RANGE, pirateCentreY - PIRATE_HALF_RANGE);

        // compute pirate attack range
        Rectangle attackRange = new Rectangle(topLeftAttackRange, PIRATE_RANGE ,PIRATE_RANGE);

        return sailor.getBoundingBox().intersects(attackRange);
    }

    protected void shoot(Sailor sailor) {
        projectiles.add(new Projectile(pirateCentreX, pirateCentreY, PROJ_SPEED, projImage, sailor));
        coolingDown = true;
    }

    protected void cooldown() {
        TimerTask startCooldown = new TimerTask() {
            @Override
            public void run() {
                coolingDown= false;
            }
        };
        timer.schedule(startCooldown, COOLDOWN);
    }

    /**
     * Getter to check if pirate is in invincible state
     */
    public boolean isInvincible() {
        return invincible;
    }

    /**
     * Setter to set the pirate's state to invincible
     */
    public void setInvincible(boolean invincible) {
        this.invincible = invincible;
    }

    /**
     * Setter to set pirate's moving left image
     */
    public void setLeftImage(Image leftImage) {
        this.leftImage = leftImage;
    }

    /**
     * Setter to set pirate's moving right image
     */
    public void setRightImage(Image rightImage) {
        this.rightImage = rightImage;
    }

    /**
     * Setter to set pirate's moving left image when invincible
     */
    public void setLeftInvincibleImage(Image leftInvincibleImage) {
        this.leftInvincibleImage = leftInvincibleImage;
    }

    /**
     * Setter to set pirate's moving right image when invincible
     */
    public void setRightInvincibleImage(Image rightInvincibleImage) {
        this.rightInvincibleImage = rightInvincibleImage;
    }

    /**
     * Setter to set pirate's projectile image
     */
    public void setProjImage(Image projImage) {
        this.projImage = projImage;
    }
}
