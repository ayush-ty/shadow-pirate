import bagel.*;

/**
 * Elixir class that inherits from Item class
 * @author Ayush Tyagi
 */
public class Elixir extends Item {
    private final Image ELIXIR = new Image("res/items/elixir.png");
    private final Image ELIXIR_ICON = new Image("res/items/elixirIcon.png");

    /**
     * Public static attribute storing elixir's health buff value
     */
    public static final int HEALTH_BUFF = 35;

    /**
     * Constructor to set entity's position
     */
    public Elixir(double startX, double startY) {
        super(startX, startY, HEALTH_BUFF);
        this.setCurrImage(ELIXIR);
        this.setICON_IMAGE(ELIXIR_ICON);
    }

    public void update() {
        getCurrImage().drawFromTopLeft(getPosition().x, getPosition().y);
    }
}
