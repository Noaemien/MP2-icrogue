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

    Foreground winDisplay = new Foreground("icrogue/WinScreen", 10, 10, new RegionOfInterest(0, 0, 640, 640));



    public HUD(String name, float width, float height, RegionOfInterest roi, Vector anchor) {
        super(name, width, height, roi, anchor);
        initHeartTab(heartDisplay);
    }


    public void initHeartTab(Foreground[][] heartDisplay) {
        for (float i = 0; i < 3; ++i) {
            for (float y = 0; y < 3; ++y) {
                /*heartDisplay[(int) i][y] = new Foreground("zelda/heartDisplay", 1, 1, new RegionOfInterest(32, 0, 16, 16),
                        new Vector(i, 0.f)); //full
                heartDisplay[(int) i][y] = new Foreground("zelda/heartDisplay", 1, 1, new RegionOfInterest(16, 0, 16, 16),
                        new Vector(i, 0.f)); //mid*/
                heartDisplay[(int) i][(int) y] = new Foreground("zelda/heartDisplay", 1, 1, new RegionOfInterest((int) y * 16, 0, 16, 16),
                        new Vector(i, 0f)); //empty
            }
        }
    }

    @Override
    public void update(float deltaTime) {
    }

    @Override
    public void draw(Canvas canvas) {
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
        if(ICRogue.hasWin()) winDisplay.draw(canvas);

    }
}
