import bagel.Image;

/**
 * Block class that inherits from Obstacle class
 * @author Ayush Tyagi
 */
public class Block extends Obstacle {
    private final Image BLOCK = new Image("res/block.png");

    /**
     * Constructor to set position and image of block
     */
    public Block(int startX, int startY){
        super(startX, startY);
        this.setCurrImage(BLOCK);
    }

    @Override
    public void update() {
        BLOCK.drawFromTopLeft(getPosition().x, getPosition().y);
    }

}