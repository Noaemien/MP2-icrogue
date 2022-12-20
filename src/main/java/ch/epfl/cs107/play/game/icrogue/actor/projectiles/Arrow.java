package ch.epfl.cs107.play.game.icrogue.actor.projectiles;

import ch.epfl.cs107.play.game.actor.SoundAcoustics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.AreaGame;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;

import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.game.icrogue.ICRogueBehavior;
import ch.epfl.cs107.play.game.icrogue.actor.Connector;
import ch.epfl.cs107.play.game.icrogue.actor.ICRoguePlayer;
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class Arrow extends Projectile{


    Sprite sprite;
    final int ARROW_DAMAGE;


    private class ICRogueArrowInteractionHandler implements ICRogueInteractionHandler {
        @Override
        public void interactWith(ICRogueBehavior.ICRogueCell cell, boolean isCellInteraction) {
            if (!isCellInteraction) {
                if (cell.getType().equals(ICRogueBehavior.CellType.WALL) || cell.getType() == ICRogueBehavior.CellType.HOLE) {
                    consume();
                }
            }
        }

        @Override
        public void interactWith(ICRoguePlayer player, boolean isCellInteraction) {
            if (isCellInteraction) {
                player.inflictDamage(ARROW_DAMAGE);
                consume();
            }
        }
    }

    ICRogueArrowInteractionHandler handler;


    public Arrow(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position, 1, 3);
        ARROW_DAMAGE = 1;
        sprite = new Sprite("zelda/arrow", 1f, 1f, this ,
                new RegionOfInterest(32* orientation.ordinal(), 0, 32, 32),
                new Vector(0, 0));
        setSprite(sprite);
        handler = new ICRogueArrowInteractionHandler();
    }

    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {
        other.acceptInteraction(this.handler , isCellInteraction);
    }

    @Override
    public void update(float deltaTime){
        super.update(deltaTime);
    }

    @Override
    public void draw(Canvas canvas) {
        if (!isConsumed()) sprite.draw(canvas);
    }

    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        if (!isConsumed()) {
            ((ICRogueInteractionHandler) v).interactWith(this, isCellInteraction);
        }
    }
}
