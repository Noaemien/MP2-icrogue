package ch.epfl.cs107.play.game.icrogue.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;

public class Mew extends ICRogueActor{

    Sprite sprite;
    /**
     * Default MovableAreaEntity constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity. Not null
     * @param position    (Coordinate): Initial position of the entity. Not null
     */
    public Mew(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);
        sprite = new Sprite("mew.fixed", 0.5f, 0.5f, this,
                new RegionOfInterest(0, 0, 16, 32), new Vector(-0.2f, -.15f));
    }


    public void rotateMewSpriteToOrientation(Orientation orientation) {
        if (orientation.equals(Orientation.LEFT)){
            sprite = new Sprite("mew.fixed", .5f, 0.5f, this ,
                    new RegionOfInterest(16, 0, 16, 21), new Vector(-0.2f, -0.15f));
        } else if (orientation.equals(Orientation.RIGHT)){
            sprite = new Sprite("mew.fixed", 0.5f, 0.5f, this ,
                    new RegionOfInterest(48, 0, 16, 21), new Vector(-0.2f, -0.15f));
        } else if (orientation.equals(Orientation.UP)){
            sprite = new Sprite("mew.fixed", .5f, .5f, this ,
                    new RegionOfInterest(32, 0, 16, 21), new Vector(-0.2f, -0.15f));
        } else if (orientation.equals(Orientation.DOWN)) {
            sprite = new Sprite("mew.fixed", .5f, .5f, this,
                    new RegionOfInterest(0, 0, 16, 21), new Vector(-0.2f, -0.15f));
        }
    }


    public void moove(){

    }
}
