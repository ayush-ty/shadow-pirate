import bagel.util.*;
import bagel.*;

/**
 * Abstract Item class that inherits from Entity
 * @author Ayush Tyagi
 */
public abstract class Item extends Entity {
    private final int buffAmount;
    private boolean pickedUp;
    private Image ICON_IMAGE;
    private Point ICON_POSITION;

    /**
     * Constructor to set item's position and buff value for sailor
     */
    public Item(double startX, double startY, int buffAmount) {
        super(startX, startY);
        this.buffAmount = buffAmount;
        pickedUp = false;
    }

    /**
     * Abstract update method to update the state of an object in game
     */
    public abstract void update();

    /**
     * Getter to check if item has been picked up
     */
    public boolean isPickedUp() {
        return pickedUp;
    }

    /**
     * Setter to set item as having been picked up
     */
    public void setPickedUp(boolean pickedUp) {
        this.pickedUp = pickedUp;
    }

    /**
     * Getter to get item's buff value
     */
    public int getBuffAmount() {
        return buffAmount;
    }

    /**
     * Getter to get item's icon image for display in sailor's inventory
     */
    public Image getICON_IMAGE() {
        return ICON_IMAGE;
    }

    /**
     * Setter to set item's icon image for sailor's inventory
     */
    public void setICON_IMAGE(Image ICON_IMAGE) {
        this.ICON_IMAGE = ICON_IMAGE;
    }

    /**
     * Getter to get the position of item's icon when displayed in inventory
     */
    public Point getICON_POSITION() {
        return ICON_POSITION;
    }

    /**
     * Setter to set the position of item's icon to be displayed in inventory
     */
    public void setICON_POSITION(Point ICON_POSITION) {
        this.ICON_POSITION = ICON_POSITION;
    }
}
