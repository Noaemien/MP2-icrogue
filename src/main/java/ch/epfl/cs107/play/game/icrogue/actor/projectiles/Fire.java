package ch.epfl.cs107.play.game.icrogue.actor.projectiles;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.AreaBehavior;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.ICRogueBehavior;
import ch.epfl.cs107.play.game.icrogue.actor.ICRoguePlayer;
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class Fire extends Projectile{


    Sprite sprite = new Sprite("zelda/fire", 1f, 1f, this ,
            new RegionOfInterest(0, 0, 16, 16), new
            Vector(0, 0));

    Sprite[] spriTabo = {new Sprite("zelda/fire", 1f, 1f, this ,
            new RegionOfInterest(0,0 , 16, 16), new
            Vector(0, 0)), new Sprite("zelda/fire", 1f, 1f, this ,
            new RegionOfInterest(16, 0, 16,16 ), new
            Vector(0, 0)), new Sprite("zelda/fire", 1f, 1f, this ,
            new RegionOfInterest(32, 0,16, 16), new
            Vector(0, 0)), new Sprite("zelda/fire", 1f, 1f, this ,
            new RegionOfInterest(48, 0, 16, 16), new
            Vector(0, 0)), new Sprite("zelda/fire", 1f, 1f, this ,
            new RegionOfInterest(64, 0, 16,16), new
            Vector(0, 0)), new Sprite("zelda/fire", 1f, 1f, this ,
            new RegionOfInterest(80, 0, 16, 16), new
            Vector(0, 0)),new Sprite("zelda/fire", 1f, 1f, this ,
            new RegionOfInterest(96, 0, 16, 16), new Vector(0, 0))};

    Sprite[][] spriTab = new Sprite[4][7];

    /*private void initTab(Sprite[][] spriTab){
        for(int i = 0; i < 7; ++i){
                spriTab[0][i] = new Sprite("zelda/fire", 1f, 1f, this,
                        new RegionOfInterest(16 * i, 0, 16, 16), new
                        Vector(0, 0));
                spriTab[0][i] = new Sprite("zelda/fire", 1f, 1f, this,
                        new RegionOfInterest(16 * i, 16, 16, -16), new
                        Vector(0, 0),);)

        }
    }*/
Animation animation = new Animation(2, spriTabo);

    private class ICRogueFireInteractionHandler implements ICRogueInteractionHandler {
        @Override
        public void interactWith(ICRogueBehavior.ICRogueCell cell, boolean isCellInteraction) {
            if (!isCellInteraction) {
                if (cell.getType().equals(ICRogueBehavior.CellType.WALL) || cell.getType() == ICRogueBehavior.CellType.HOLE) {
                    consume();
                }
            }
        }

    }

    ICRogueFireInteractionHandler handler;


    public Fire(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position, 1, 4);
        //initTab(spriTab);
        //setSprite(sprite);
        handler = new ICRogueFireInteractionHandler();
    }

    @Override
    public void update(float deltaTime) {
        animation.update(deltaTime);
        super.update(deltaTime);
    }

    @Override
    public void draw(Canvas canvas) {
        animation.draw(canvas);
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        if (!isConsumed())
            ((ICRogueInteractionHandler) v).interactWith(this , isCellInteraction);
    }

    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {
        other.acceptInteraction(this.handler , isCellInteraction);
    }
}
