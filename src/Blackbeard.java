import bagel.*;

/**
 * Blackbeard class which inherits from the Pirate class
 * @author Ayush Tyagi
 */
public class Blackbeard extends Pirate {
    private final Image LEFT_IMAGE = new Image("res/blackbeard/blackbeardLeft.png");
    private final Image RIGHT_IMAGE = new Image("res/blackbeard/blackbeardRight.png");
    private final Image LEFT_INVINCIBLE_IMAGE = new Image("res/blackbeard/blackbeardHitLeft.png");
    private final Image RIGHT_INVINCIBLE_IMAGE = new Image("res/blackbeard/blackbeardHitRight.png");
    private final Image PROJ_IMAGE = new Image("res/blackbeard/blackbeardProjectile.png");

    private final static int RANGE = 400;
    private final static int COOLDOWN = 1500;
    private final static double PROJ_SPEED = 0.8;

    /**
     * Constructor to initialise blackbeard
     */
    public Blackbeard(double startX, double startY) {
        super(startX, startY, Pirate.INITIAL_DAMAGE_POINTS * 2, Pirate.MAX_HEALTH * 2, RANGE, COOLDOWN, PROJ_SPEED);
        this.setCurrImage(RIGHT_IMAGE);
        this.setLeftImage(LEFT_IMAGE);
        this.setRightImage(RIGHT_IMAGE);
        this.setLeftInvincibleImage(LEFT_INVINCIBLE_IMAGE);
        this.setRightInvincibleImage(RIGHT_INVINCIBLE_IMAGE);
        this.setProjImage(PROJ_IMAGE);
    }

}
