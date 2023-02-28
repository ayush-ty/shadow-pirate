import bagel.*;

/**
 * Sword class that inherits Item class
 * @author Ayush Tyagi
 */
public class Sword extends Item {
    private final Image SWORD = new Image("res/items/sword.png");
    private final Image SWORD_ICON = new Image("res/items/swordIcon.png");

    /**
     * Public static attribute storing sword's damage buff value
     */
    public static final int DAMAGE_BUFF = 15;

    /**
     * Constructor to set entity's position
     */
    public Sword(double startX, double startY) {
        super(startX, startY, DAMAGE_BUFF);
        this.setCurrImage(SWORD);
        this.setICON_IMAGE(SWORD_ICON);
    }

    public void update() {
        getCurrImage().drawFromTopLeft(getPosition().x, getPosition().y);
    }
}
