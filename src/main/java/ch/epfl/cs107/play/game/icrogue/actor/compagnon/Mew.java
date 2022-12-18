package ch.epfl.cs107.play.game.icrogue.actor.compagnon;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.actor.ICRogueActor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;

import java.util.List;

public class Mew extends ICRogueActor implements Interactable {

    private Sprite sprite;

    public Mew(Area owner, Orientation orientation, DiscreteCoordinates coordinates, String spriteName) {
        super(owner, orientation, coordinates);

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
    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return null;
    }

    @Override
    public boolean takeCellSpace() {
        return false;
    }

    @Override
    public boolean isCellInteractable() {
        return false;
    }

    @Override
    public boolean isViewInteractable() {
        return false;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {

    }

    @Override
    public void onLeaving(List<DiscreteCoordinates> coordinates) {

    }

    @Override
    public void onEntering(List<DiscreteCoordinates> coordinates) {

    }
}
