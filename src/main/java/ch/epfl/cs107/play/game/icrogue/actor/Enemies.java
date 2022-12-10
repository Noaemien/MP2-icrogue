package ch.epfl.cs107.play.game.icrogue.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class Enemies  extends ICRogueActor{

    private boolean isDead;

    /**
     * Default MovableAreaEntity constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity. Not null
     * @param position    (Coordinate): Initial position of the entity. Not null
     */
    public Enemies(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);
    }


    public boolean hasBeenKilled() {
        return isDead;
    }


    public void dying(Area area, Orientation orientation, DiscreteCoordinates coords) {
        isDead = true;
    }
}
