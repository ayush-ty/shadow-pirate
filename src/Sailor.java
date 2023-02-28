import bagel.*;
import bagel.util.*;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Sailor class that inherits Character
 * @author Ayush Tyagi
 */
public class Sailor extends Character {
    private final Image LEFT_IMAGE = new Image("res/sailor/sailorLeft.png");
    private final Image LEFT_HIT_IMAGE = new Image("res/sailor/sailorHitLeft.png");
    private final Image RIGHT_IMAGE = new Image("res/sailor/sailorRight.png");
    private final Image RIGHT_HIT_IMAGE = new Image("res/sailor/sailorHitRight.png");

    private final static int FONT_SIZE = 30;
    private final static int HEALTH_X = 10;
    private final static int HEALTH_Y = 25;
    private final static int LEVEL_0_WIN_X = 990;
    private final static int LEVEL_0_WIN_Y= 630;
    private final static int LEVEL_0_WIN_OFFSET = 10;
    private final static int MAX_HEALTH = 100;
    private final static int MOVE_SIZE = 1;
    private final static int INITIAL_DAMAGE_POINTS = 15;
    private final static int MAX_ATTACK_STATE_TIME = 1000;
    private final static int ATTACK_COOLDOWN = 2000;
    private final static int PIRATE_INVINCIBILITY_TIME = 1500;
    private final static int INVENTORY_SPACING = 10;

    private boolean isIdle;
    private boolean cooldownOver;
    private Point oldPoint;
    private Timer timer;
    private Point startInventory;

    /**
     * Constructor to initialize sailor object
     */
    public Sailor(double startX, double startY) {
        super(startX, startY, INITIAL_DAMAGE_POINTS, FONT_SIZE, MAX_HEALTH);
        this.setCurrImage(RIGHT_IMAGE);
        this.setHealthX(HEALTH_X);
        this.setHealthY(HEALTH_Y);
        cooldownOver = isIdle = true;
        timer = new Timer();
        startInventory = new Point(HEALTH_X, HEALTH_Y);
    }

    /**
     * Method that performs state update
     */
    public void update(Input input, ArrayList<Entity> obstacles, ArrayList<Pirate> pirates, ArrayList<Entity> items) {
        sailorMovement(input);
        checkInFrame();
        checkCollisions(items);
        checkCollisions(obstacles);
        renderHealthPoints();
        potentialAttack(input, pirates);
        drawInventory(items);
    }

    /**
     * Method that checks for collisions between sailor and entities
     */
    public void checkCollisions(ArrayList<Entity> entities) {
        for (Entity entity : entities) {
            if (collide(this, entity)) {

                // if entity is an item and isn't picked up already, then use the item and add to inventory
                if (entity instanceof Item) {
                    if (!((Item) entity).isPickedUp()) {
                        useItem((Item) entity);
                        break;
                    }
                }

                // if entity is not an item, then prevent sailor from moving through
                if (!(entity instanceof Item))
                    moveBack();

                // if entity is a bomb and hasn't exploded yet, explode and deal damage to sailor
                if (entity instanceof Bomb) {
                    if (!((Bomb) entity).getExplode()) {
                        setHealthPoints(getHealthPoints() - Bomb.getDamagePoints());
                        System.out.println("Bomb inflicts " + Bomb.getDamagePoints() + " damage points on Sailor. " +
                                "Sailor's current health: " + getHealthPoints() + "/" + getMaxHealth());
                    }
                    ((Bomb) entity).setExplode(true);

                    // timer to stop displaying bomb since explosion done
                    TimerTask exploded = new TimerTask() {
                        @Override
                        public void run() {
                            ((Bomb) entity).setExploded(true);
                        }
                    };
                    timer.schedule(exploded, Bomb.getExplosionTime());
                }
             }
        }
    }

    /**
     * Method that checks if sailor has won
     */
    public boolean hasWon(boolean level1, Treasure treasure) {
        Rectangle sailorBox = getBoundingBox();

        if (!level1) {
            Point topLeftWinningRectangle = new Point(LEVEL_0_WIN_X, LEVEL_0_WIN_Y);
            Rectangle winBox = new Rectangle(topLeftWinningRectangle, LEVEL_0_WIN_OFFSET, LEVEL_0_WIN_OFFSET);

            return sailorBox.intersects(winBox);
        }

        return sailorBox.intersects(treasure.getBoundingBox());
    }

    private void potentialAttack(Input input, ArrayList<Pirate> pirates) {
        // no cooldown so try to attack
        if (input.wasPressed(Keys.S) && cooldownOver) {
            isIdle = false;

            // set attack mode images
            if (getCurrImage() == LEFT_IMAGE)
                setCurrImage(LEFT_HIT_IMAGE);
            else
                setCurrImage(RIGHT_HIT_IMAGE);

            // end attack mode after 1000 milliseconds
            TimerTask endAttackState = new TimerTask() {
                @Override
                public void run() {
                    isIdle = true;

                    // set images back to normal
                    if (getCurrImage() == LEFT_HIT_IMAGE)
                        setCurrImage(LEFT_IMAGE);
                    else
                        setCurrImage(RIGHT_IMAGE);
                }
            };
            timer.schedule(endAttackState, MAX_ATTACK_STATE_TIME);

            CooldownStart();  // set cooldown before being able to attack again
        }

        if (!isIdle) {
            // sailor has the potential to attack
            for (Pirate pirate: pirates) {
                // if pirate isn't invincible and not dead, then sailor attacks pirate if they overlap
                if (collide(this, pirate) && !pirate.isInvincible() && !pirate.isDead()) {
                    pirate.setHealthPoints(pirate.getHealthPoints() - getDamagePoints());

                    // log damage
                    System.out.println("Sailor inflicts " + getDamagePoints() + " damage points on " +
                            ((pirate instanceof Blackbeard) ? "Blackbeard. Blackbeard's " : "Pirate. Pirate's ") +
                            "current health: " + pirate.getHealthPoints() + "/" + pirate.getMaxHealth());

                    pirate.setInvincible(true);

                    // time to end pirate invincibility after 1500 milliseconds
                    TimerTask endInvincibility = new TimerTask() {
                        @Override
                        public void run() {
                            pirate.setInvincible(false);
                        }
                    };
                    timer.schedule(endInvincibility, PIRATE_INVINCIBILITY_TIME);
                }
            }
        }
    }

    private void useItem(Item item) {
        if (item instanceof Sword) {
            setDamagePoints(getDamagePoints() + item.getBuffAmount());
            System.out.println("Sailor finds Sword. Sailor's damage points increased to " + getDamagePoints());
        } else if (item instanceof Elixir) {
            setMaxHealth(getMaxHealth() + item.getBuffAmount());
            setHealthPoints(getMaxHealth());
            System.out.println("Sailor finds Elixir. Sailor's current health: " + getHealthPoints() + "/" + getMaxHealth());
            renderHealthPoints();
        } else {
            setHealthPoints(Math.min((getHealthPoints() + item.getBuffAmount()), getMaxHealth()));
            System.out.println("Sailor finds Potion. Sailor's current health: " + getHealthPoints() + "/" + getMaxHealth());
            renderHealthPoints();
        }
        setItemInInventory(item);
        item.setPickedUp(true);
    }

    private void CooldownStart() {
        cooldownOver = false;
        TimerTask endCooldown = new TimerTask() {
            @Override
            public void run() {
                cooldownOver = true;
            }
        };
        timer.schedule(endCooldown, ATTACK_COOLDOWN);
    }

    private void setItemInInventory(Item item) {
        // setting appropriate coordinates for items to be put in inventory
        Point newStartInventory = new Point(startInventory.x, startInventory.y + INVENTORY_SPACING);
        item.setICON_POSITION(newStartInventory);
        startInventory = new Point(newStartInventory.x, newStartInventory.y + item.getICON_IMAGE().getHeight());
    }

    private void drawInventory(ArrayList<Entity> items) {
        for (Entity entity : items) {
            Item item = (Item)entity;
            if (item.isPickedUp()) {
                item.getICON_IMAGE().drawFromTopLeft(item.getICON_POSITION().x, item.getICON_POSITION().y);
            }
        }
    }

    private void checkInFrame() {
        if (hitWall()) {
            moveBack();
        }
    }

    private void sailorMovement(Input input) {
        // store old coordinates everytime sailor moves
        if (input.isDown(Keys.UP)){
            setOldPoints();
            move(0, -MOVE_SIZE);
        } else if (input.isDown(Keys.DOWN)){
            setOldPoints();
            move(0, MOVE_SIZE);
        } else if (input.isDown(Keys.LEFT)){
            setOldPoints();
            move(-MOVE_SIZE,0);
            // checking if sailor in attack mode to render the suitable image
            if (isIdle)
                setCurrImage(LEFT_IMAGE);
            else
                setCurrImage(LEFT_HIT_IMAGE);

        } else if (input.isDown(Keys.RIGHT)){
            setOldPoints();
            move(MOVE_SIZE,0);
            // checking if sailor in attack mode to render the suitable image
            if (isIdle)
                setCurrImage(RIGHT_IMAGE);
            else
                setCurrImage(RIGHT_HIT_IMAGE);
        }
        getCurrImage().drawFromTopLeft(getPosition().x, getPosition().y);
    }

    private void setOldPoints() {
        oldPoint = getPosition();
    }

    private void moveBack() {
        setPosition(oldPoint);
    }
}