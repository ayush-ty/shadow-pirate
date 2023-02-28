import bagel.*;
import bagel.util.Point;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * SWEN20003 Project 2, Semester 1, 2022
 * Game - ShadowPirate
 *
 * @author Ayush Tyagi
 */
public class ShadowPirate extends AbstractGame {
    private final static int WINDOW_WIDTH = 1024;
    private final static int WINDOW_HEIGHT = 768;
    private final static String GAME_TITLE = "ShadowPirate";
    private final static String LEVEL_0 = "res/level0.csv";
    private final static String LEVEL_1 = "res/level1.csv";
    private final Image LEVEL_0_BACKGROUND_IMAGE = new Image("res/background0.png");
    private final Image LEVEL_1_BACKGROUND_IMAGE = new Image("res/background1.png");

    private final static int MESSAGE_Y = 402;
    private final static int FONT_SIZE = 55;
    private final static int INSTRUCTION_OFFSET = 70;
    private final static int LEVEL_END_DELAY = 3000;
    private final Font FONT = new Font("res/wheaton.otf", FONT_SIZE);
    private final static String START_MESSAGE = "PRESS SPACE TO START";
    private final static String ATTACK_MESSAGE = "PRESS S TO ATTACK";
    private final static String INSTRUCTION_MESSAGE_LEVEL_0 = "USE ARROW KEYS TO FIND LADDER";
    private final static String INSTRUCTION_MESSAGE_LEVEL_1 = "FIND THE TREASURE";
    private final static String WIN_MESSAGE = "CONGRATULATIONS";
    private final static String GAME_END_MESSAGE = "GAME OVER";
    private final static String NEXT_LVL_MESSAGE = "LEVEL COMPLETE!";

    private Sailor sailor;
    private Treasure treasure;
    private ArrayList<Entity> obstacles = new ArrayList<>();
    private ArrayList<Pirate> pirates = new ArrayList<>();
    private ArrayList<Entity> items = new ArrayList<>();
    private static Point topLeft;
    private static Point bottomRight;

    private boolean levelStart0;
    private boolean levelStart1;
    private boolean gameEnd;
    private boolean levelWin0;
    private boolean levelWin1;
    private boolean level1Loaded;
    private boolean playLevel1;
    private Timer timer;

    /**
     * Constructor to initialize the game through reading level csv files provided
     */
    public ShadowPirate() {
        super(WINDOW_WIDTH, WINDOW_HEIGHT, GAME_TITLE);
        readCSV(LEVEL_0);
        levelStart0 = levelStart1 = gameEnd = levelWin0 = levelWin1 = level1Loaded = playLevel1 = false;
        timer = new Timer();
    }

    /**
     * The entry point for the program.
     */
    public static void main(String[] args) {
        ShadowPirate game = new ShadowPirate();
        game.run();
    }

    /**
     * Method used to read file and create objects
     */
    private void readCSV(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String row;

            while ((row=br.readLine()) != null) {
                String[] cells = row.split(",");

                String entity = cells[0];

                if (entity.equals("Sailor")) {
                    sailor = new Sailor(Integer.parseInt(cells[1]), Integer.parseInt(cells[2]));
                } else if (entity.equals("Block") && !levelStart1) {
                    obstacles.add(new Block(Integer.parseInt(cells[1]), Integer.parseInt(cells[2])));
                } else if (entity.equals("Block")) {
                    obstacles.add(new Bomb(Integer.parseInt(cells[1]), Integer.parseInt(cells[2])));
                } else if (entity.equals("Pirate")) {
                    pirates.add(new Pirate(Integer.parseInt(cells[1]), Integer.parseInt(cells[2])));
                } else if (entity.equals("TopLeft")) {
                    topLeft = new Point(Integer.parseInt(cells[1]), Integer.parseInt(cells[2]));
                } else if (entity.equals("BottomRight")) {
                    bottomRight = new Point(Integer.parseInt(cells[1]), Integer.parseInt(cells[2]));
                } else if (entity.equals("Blackbeard")) {
                    pirates.add(new Blackbeard(Integer.parseInt(cells[1]), Integer.parseInt(cells[2])));
                } else if (entity.equals("Treasure")) {
                    treasure = new Treasure(Integer.parseInt(cells[1]), Integer.parseInt(cells[2]));
                } else if (entity.equals("Sword")) {
                    items.add(new Sword(Integer.parseInt(cells[1]), Integer.parseInt(cells[2])));
                } else if (entity.equals("Elixir")) {
                    items.add(new Elixir(Integer.parseInt(cells[1]), Integer.parseInt(cells[2])));
                } else if (entity.equals("Potion")) {
                    items.add(new Potion(Integer.parseInt(cells[1]), Integer.parseInt(cells[2])));
                }
                if (fileName.equals(LEVEL_1))
                    level1Loaded = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }


    /**
     * Performs a state update.
     * allows the game to exit when the escape key is pressed.
     */
    @Override
    public void update(Input input) {
        if (input.wasPressed(Keys.ESCAPE)){
            Window.close();
            System.exit(1);
        }

        // skip to level 1
        if (input.wasPressed(Keys.W)) {
            levelStart1 = true;
        }

        if (!levelStart0 && !levelStart1)
            drawStartScreenLevel0(input);

        if (gameEnd)
            drawEndScreen(GAME_END_MESSAGE);

        // completed level 0 so start level 1
        if (levelWin0 && !levelStart1) {
            drawEndScreen(NEXT_LVL_MESSAGE);

            // start level 1 after 3000 milliseconds
            TimerTask startLevel1 = new TimerTask() {
                @Override
                public void run() {
                    levelStart1 = true;
                }
            };
            timer.schedule(startLevel1, LEVEL_END_DELAY);
        }

        // level 0 running
        if (levelStart0 && !gameEnd && !levelWin0 && !levelStart1) {
            LEVEL_0_BACKGROUND_IMAGE.draw(Window.getWidth() / 2.0, Window.getHeight() / 2.0);
            drawBlocks();
            drawPirates();
            sailor.update(input, obstacles, pirates, items);

            if (sailor.isDead()) {
                gameEnd = true;
            }

            if (sailor.hasWon(levelStart1, treasure)) {
                levelWin0 = true;
            }
        }

        // level 1 start so read new world file and clear entities from level 0
        if (levelStart1 && !level1Loaded) {
            obstacles.clear();
            pirates.clear();
            readCSV(LEVEL_1);
        }

        if (!playLevel1 && levelStart1)
            drawStartScreenLevel1(input);

        // level 1 running
        if (playLevel1 && !gameEnd && !levelWin1) {
            LEVEL_1_BACKGROUND_IMAGE.draw(Window.getWidth() / 2.0, Window.getHeight() / 2.0);
            drawBombs();
            drawPirates();
            drawItems();
            drawTreasure();
            sailor.update(input, obstacles, pirates, items);

            if (sailor.isDead()) {
                gameEnd = true;
            }

            if (sailor.hasWon(level1Loaded, treasure)) {
                levelWin1 = true;
            }
        }

        // game completed!
        if (levelWin1) {
            drawEndScreen(WIN_MESSAGE);
        }
    }

    /**
     * Method used to draw the start screen instructions
     */
    private void drawStartScreenLevel0(Input input) {
        FONT.drawString(START_MESSAGE, (Window.getWidth()/2.0 - (FONT.getWidth(START_MESSAGE)/2.0)),
                MESSAGE_Y);
        FONT.drawString(ATTACK_MESSAGE, (Window.getWidth()/2.0 - (FONT.getWidth(ATTACK_MESSAGE)/2.0)),
                (MESSAGE_Y + INSTRUCTION_OFFSET));
        FONT.drawString(INSTRUCTION_MESSAGE_LEVEL_0, (Window.getWidth()/2.0 - (FONT.getWidth(INSTRUCTION_MESSAGE_LEVEL_0)/2.0)),
                (MESSAGE_Y + 2*INSTRUCTION_OFFSET));

        if (input.wasPressed(Keys.SPACE)){
            levelStart0 = true;
        }
    }

    /**
     * Method used to draw the start screen instructions
     */
    private void drawStartScreenLevel1(Input input) {
        FONT.drawString(START_MESSAGE, (Window.getWidth()/2.0 - (FONT.getWidth(START_MESSAGE)/2.0)),
    MESSAGE_Y);
        FONT.drawString(ATTACK_MESSAGE, (Window.getWidth()/2.0 - (FONT.getWidth(ATTACK_MESSAGE)/2.0)),
            (MESSAGE_Y + INSTRUCTION_OFFSET));
        FONT.drawString(INSTRUCTION_MESSAGE_LEVEL_1, (Window.getWidth()/2.0 - (FONT.getWidth(INSTRUCTION_MESSAGE_LEVEL_1)/2.0)),
            (MESSAGE_Y + 2*INSTRUCTION_OFFSET));

        if (input.wasPressed(Keys.SPACE)) {
            playLevel1 = true;
        }
    }

    /**
     * Method used to draw end screen messages
     * */
    private void drawEndScreen(String message) {
        FONT.drawString(message, (Window.getWidth()/2.0 - (FONT.getWidth(message)/2.0)), MESSAGE_Y);
    }

    private void drawBlocks() {
        for (Entity obstacle : obstacles) {
            ((Obstacle)obstacle).update();
        }
    }

    private void drawBombs() {
        for (int i=0; i<obstacles.size(); i++) {
            ((Obstacle)obstacles.get(i)).update();

            if (((Bomb) obstacles.get(i)).isExploded()) {
                // bomb done exploding so stop rendering the bomb
                obstacles.remove(i);
                obstacles.trimToSize();
            }
        }
    }

    private void drawPirates() {
        for (Pirate pirate: pirates) {
            pirate.update(sailor, obstacles);
        }
    }

    private void drawItems() {
        for (Entity item : items) {
            if (!((Item) item).isPickedUp())
                ((Item) item).update();
        }
    }

    private void drawTreasure() {
        treasure.update();
    }

    /**
     * Getter to get the top left coordinate of the level provided in csv fle
     */
    public static Point getTopLeft() {
        return topLeft;
    }

    /**
     * Getter to get the bottom right coordinate of the level provided in csv fle
     */
    public static Point getBottomRight() {
        return bottomRight;
    }
}
