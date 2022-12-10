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

public class Stick extends Item{

    Sprite[] spriTabo = {new Sprite("zelda/staff", 1f, 1f, this ,
            new RegionOfInterest(0,0 , 32, 32), new
            Vector(0, 0)), new Sprite("zelda/staff", 1f, 1f, this ,
            new RegionOfInterest(32, 0, 32,32 ), new
            Vector(0, 0)), new Sprite("zelda/staff", 1f, 1f, this ,
            new RegionOfInterest(64, 0,32, 32), new
            Vector(0, 0)), new Sprite("zelda/staff", 1f, 1f, this ,
            new RegionOfInterest(96, 0, 32, 32), new
            Vector(0, 0)), new Sprite("zelda/staff", 1f, 1f, this ,
            new RegionOfInterest(128, 0, 32,32), new
            Vector(0, 0)), new Sprite("zelda/staff", 1f, 1f, this ,
            new RegionOfInterest(160, 0, 32, 32), new
            Vector(0, 0)),new Sprite("zelda/staff", 1f, 1f, this ,
            new RegionOfInterest(192, 0, 32, 32), new Vector(0, 0))};

    Animation animation = new Animation(2, spriTabo);

    public Stick(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);
    }

    @Override
    public void update(float deltaTime){
        animation.update(deltaTime);
    }

    @Override
    public boolean isViewInteractable() {
        return true;
    }

    @Override
    public void draw(Canvas canvas){
        if(!isCollected()) animation.draw(canvas);
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICRogueInteractionHandler) v).interactWith(this , isCellInteraction);
    }

    @Override
    public boolean takeCellSpace() {
        return (!isCollected());
    }
}



