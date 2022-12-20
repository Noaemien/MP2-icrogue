package ch.epfl.cs107.play.game.icrogue.actor.items;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class Heart extends Item{
    public Heart(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);
        setSprite(new Sprite("zelda/heart", 0.6f, 0.6f, this, new RegionOfInterest(0, 0, 16, 16), new
                Vector(0, 0)));
    }

    Sprite[] spriTabo = {new Sprite("zelda/heart", 1f, 1f, this,
            new RegionOfInterest(0, 0, 16, 16), new Vector(0, 0)),
                        new Sprite("zelda/heart", 1f, 1f, this,
            new RegionOfInterest(16, 0, 16, 16), new Vector(0, 0.05f)),
            new Sprite("zelda/heart", 1f, 1f, this,
                    new RegionOfInterest(32, 0, 16, 16), new Vector(0, 0.1f)),
            new Sprite("zelda/heart", 1f, 1f, this,
                    new RegionOfInterest(48, 0, 16, 16), new Vector(0, 0.05f))};

    Animation animation = new Animation(3, spriTabo);

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if(!isCollected()) animation.update(deltaTime);

    }

    @Override
    public void draw(Canvas canvas) {
        if(!isCollected()) animation.draw(canvas);

    }

    @Override
    public boolean isViewInteractable() {
        return false;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICRogueInteractionHandler) v).interactWith(this , isCellInteraction);
    }
}
