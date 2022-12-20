package ch.epfl.cs107.play.game.icrogue;

import ch.epfl.cs107.play.game.Updatable;
import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.areagame.actor.Foreground;
import ch.epfl.cs107.play.game.icrogue.actor.ICRoguePlayer;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class HUD extends ImageGraphics implements Updatable {

    private Foreground[][] heartDisplay = new Foreground[3][3];

    Foreground winDisplay = new Foreground("icrogue/WinScreen", 10, 10,
            new RegionOfInterest(0, 0, 640, 640));
    Foreground loseDisplay = new Foreground("icrogue/LoseScreen", 10, 10,
            new RegionOfInterest(0,0,640,640));


    public HUD(String name, float width, float height, RegionOfInterest roi, Vector anchor) {
        super(name, width, height, roi, anchor);
        initHeartTab(heartDisplay);
    }

    /**
     * Initialises the array of heart sprites
     * @param heartDisplay (Foreground[][]): array of heart sprites
     */
    public void initHeartTab(Foreground[][] heartDisplay) {
        for (int i = 0; i < 3; ++i) {
            for (int y = 0; y < 3; ++y) {
                heartDisplay[i][y] = new Foreground("zelda/heartDisplay", 1, 1, new RegionOfInterest(y * 16, 0, 16, 16),
                        new Vector(i, 0f)); //empty
            }
        }
    }

    @Override
    public void update(float deltaTime) {

    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        //Different heart display cases depending on hp
        switch (ICRoguePlayer.getHp()) {
            case 6 -> {
                for (int i = 0; i < 3; ++i) {
                    heartDisplay[i][2].draw(canvas);
                }
            }
            case 5 -> {
                heartDisplay[0][2].draw(canvas);
                heartDisplay[1][2].draw(canvas);
                heartDisplay[2][1].draw(canvas);

            }
            case 4 -> {
                heartDisplay[0][2].draw(canvas);
                heartDisplay[1][2].draw(canvas);
                heartDisplay[2][0].draw(canvas);
            }
            case 3 -> {
                heartDisplay[0][2].draw(canvas);
                heartDisplay[1][1].draw(canvas);
                heartDisplay[2][0].draw(canvas);
            }
            case 2 -> {
                heartDisplay[0][2].draw(canvas);
                heartDisplay[1][0].draw(canvas);
                heartDisplay[2][0].draw(canvas);
            }
            case 1 -> {
                heartDisplay[0][1].draw(canvas);
                heartDisplay[1][0].draw(canvas);
                heartDisplay[2][0].draw(canvas);
            }

        }
        /*
        //Display win screen on win
        if(ICRogue.hasWon()) {
            winDisplay.draw(canvas);
        }else //Display lose screen on lose
        if(ICRogue.hasLost()) {
            loseDisplay.draw(canvas);

        }
        */
    }
}
