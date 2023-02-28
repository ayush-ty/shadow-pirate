import bagel.*;
import bagel.util.*;

/**
 * Abstract Entity class
 * @author Ayush Tyagi
 */
public abstract class Entity {
    private Point position;
    private Image currImage;

    /**
     * Constructor to set entity's position
     */
    public Entity(double startX, double startY) {
        this.position = new Point(startX, startY);
    }

    /**
     * Getter to get position of entity
     */
    public Point getPosition() {
        return position;
    }

    /**
     * Getter to get a rectangle of the bounding box of entity
     */
    public Rectangle getBoundingBox() {
        return currImage.getBoundingBoxAt(new Point(position.x + currImage.getWidth()/2,
                position.y + currImage.getHeight()/2));
    }

    /**
     * Setter to set position of entity
     */
    public void setPosition(Point position) {
        this.position = position;
    }

    /**
     * Getter to get the current image of entity
     */
    public Image getCurrImage() {
        return currImage;
    }

    /**
     * Setter to set current image of entity
     */
    public void setCurrImage(Image currImage) {
        this.currImage = currImage;
    }
}
