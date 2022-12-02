package ch.epfl.cs107.play.game.icrogue.actor.items;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class Cherry extends Item{
    final public int ID;
    public Cherry(Area area, Orientation orientation, DiscreteCoordinates position, int id) {
        super(area, orientation, position);
        setSprite(new Sprite("icrogue/cherry", 0.6f, 0.6f, this));
        this.ID = id;
    }

    @Override
    public boolean isViewInteractable() {
        return false;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ICRogueInteractionHandler v_casted = (ICRogueInteractionHandler) v;
        v_casted.interactWith(this , isCellInteraction);
    }
}
