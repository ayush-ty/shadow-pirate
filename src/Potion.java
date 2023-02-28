import bagel.*;

/**
 * Potion class that inherits Item class
 * @author Ayush Tyagi
 */
public class Potion extends Item {
    private final Image POTION = new Image("res/items/potion.png");
    private final Image POTION_ICON = new Image("res/items/potionIcon.png");

    /**
     * Public static attribute storing potion's health buff value
     */
    public static final int HEALTH_BUFF = 25;

    /**
     * Constructor to set entity's position
     */
    public Potion(double startX, double startY) {
        super(startX, startY, HEALTH_BUFF);
        this.setCurrImage(POTION);
        this.setICON_IMAGE(POTION_ICON);
    }

    public void update() {
        getCurrImage().drawFromTopLeft(getPosition().x, getPosition().y);
    }
}
