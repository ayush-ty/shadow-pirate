import bagel.*;
import bagel.DrawOptions;
import bagel.util.Colour;
import bagel.util.Point;
import bagel.util.Rectangle;

import java.util.ArrayList;

/**
 * Abstract Character class that inherits from Entity class
 * @author Ayush Tyagi
 */
public abstract class Character extends Entity {
    protected final static int ORANGE_BOUNDARY = 65;
    protected final static int RED_BOUNDARY = 35;
    protected final DrawOptions COLOUR = new DrawOptions();
    protected final static Colour GREEN = new Colour(0, 0.8, 0.2);
    protected final static Colour ORANGE = new Colour(0.9, 0.6, 0);
    protected final static Colour RED = new Colour(1, 0, 0);

    private int maxHealth;
    private double speed;
    private int healthPoints;
    private Font font;
    private int damagePoints;
    private int healthX;
    private int healthY;

    /**
     * Constructor to set character's position
     */
    public Character(double startX, double startY, int damagePoints, int fontSize, int health) {
        super(startX, startY);
        COLOUR.setBlendColour(GREEN);
        this.damagePoints = damagePoints;
        this.font = new Font("res/wheaton.otf", fontSize);
        this.maxHealth = health;
        this.healthPoints = maxHealth;

    }

    /**
     * Method used to move a character
     */
    public void move(double xMove, double yMove) {
        this.setPosition(new Point(getPosition().x + xMove , getPosition().y + yMove));
    }

    /**
     * Method used to display health points of a character
     */
    public void renderHealthPoints() {
        // credit for this code from project 1 solution
        double percentageHP = ((double) getHealthPoints()/getMaxHealth()) * 100;
        if (percentageHP <= RED_BOUNDARY){
            COLOUR.setBlendColour(RED);
        } else if (percentageHP <= ORANGE_BOUNDARY){
            COLOUR.setBlendColour(ORANGE);
        } else {
            COLOUR.setBlendColour(GREEN);
        }
        this.getFont().drawString(Math.round(percentageHP) + "%", healthX, healthY, COLOUR);
    }

    /**
     * Method to check if character is hitting a level boundary
     */
    public boolean hitWall() {
        Point bottomRight = ShadowPirate.getBottomRight();
        Point topLeft = ShadowPirate.getTopLeft();

        return getPosition().x <= topLeft.x || getPosition().x >= bottomRight.x ||
               getPosition().y <= topLeft.y || getPosition().y >= bottomRight.y;
    }

    /**
     * Method to check if character and entity overlap (collide)
     */
    public boolean collide(Character character, Entity entity) {
        Rectangle boundingBox = character.getBoundingBox();
        Rectangle obstacleBox = entity.getBoundingBox();

        return boundingBox.intersects(obstacleBox);
    }

    /**
     * Abstract method to check overlap between entities to execute some behaviour
     */
    public abstract void checkCollisions(ArrayList<Entity> entities);

    /**
     * Setter to set the x-coordinate for displaying health
     */
    public void setHealthX(int healthX) {
        this.healthX = healthX;
    }

    /**
     * * Setter to set the y-coordinate for displaying health
     */
    public void setHealthY(int healthY) {
        this.healthY = healthY;
    }

    /**
     * Method to check if character has no health points and is therefore dead
     */
    public boolean isDead() {
        return healthPoints <= 0;
    }

    /**
     * Getter to get character's max health
     */
    public int getMaxHealth() {
        return maxHealth;
    }

    /**
     * Setter to set character's max health
     */
    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    /**
     * Getter to get character move speed
     */
    public double getSpeed() {
        return speed;
    }

    /**
     * Setter to set character move speed
     */
    public void setSpeed(double speed) {
        this.speed = speed;
    }

    /**
     * Getter to get character's health
     */
    public int getHealthPoints() {
        return healthPoints;
    }

    /**
     * Setter to set character's health
     */
    public void setHealthPoints(int healthPoints) {
        this.healthPoints = healthPoints;
    }

    /**
     * Getter to get font
     */
    public Font getFont() {
        return font;
    }

    /**
     * Getter to get character's damage points
     */
    public int getDamagePoints() {
        return damagePoints;
    }

    /**
     * Setter to set character's damage points
     */
    public void setDamagePoints(int damagePoints) {
        this.damagePoints = damagePoints;
    }
}
