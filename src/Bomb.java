import bagel.*;

/**
 * Bomb class that inherits from Obstacle class
 * @author Ayush Tyagi
 */
public class Bomb extends Obstacle {
    private final Image BOMB = new Image("res/bomb.png");
    private final Image EXPLOSION = new Image("res/explosion.png");
    private final static int DAMAGE_POINTS = 15;
    private final static int EXPLOSION_TIME = 500;

    private boolean explode;
    private boolean exploded;

    /**
     * Constructor to initialise bomb
     */
    public Bomb (double startX, double startY) {
        super(startX, startY);
        this.setCurrImage(BOMB);
    }

    @Override
    public void update() {
        if (explode) {
            setCurrImage(EXPLOSION);
        }
        getCurrImage().drawFromTopLeft(getPosition().x, getPosition().y);
    }

    /**
     * Getter to check if bomb has started to explode
     */
    public boolean getExplode() {
        return explode;
    }

    /**
     * Setter to set bomb to start to explode
     */
    public void setExplode(boolean explode) {
        this.explode = explode;
    }

    /**
     * Getter to get bomb damage value
     */
    public static int getDamagePoints() {
        return DAMAGE_POINTS;
    }

    /**
     * Getter to check if bomb has exploded
     */
    public boolean isExploded() {
        return exploded;
    }

    /**
     * Setter to set bomb to having exploded already
     */
    public void setExploded(boolean exploded) {
        this.exploded = exploded;
    }

    /**
     * Getter to get the explosion time for bomb
     */
    public static int getExplosionTime() {
        return EXPLOSION_TIME;
    }
}

