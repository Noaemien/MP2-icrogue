package ch.epfl.cs107.play.game.icrogue.actor.projectiles;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Interactor;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.actor.ICRogueActor;
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

import java.util.Collections;
import java.util.List;

abstract public class Projectile extends ICRogueActor implements Consumable, Interactor {

    final int DEFAULT_DAMAGE = 1;
    final int DEFAULT_MOVE_DURATION = 10;

    private int damage = 0;
    private int framesForMove = 0;

    private Sprite sprite;

    private boolean isConsumed = false;


    /**
     * Default MovableAreaEntity constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity. Not null
     * @param position    (Coordinate): Initial position of the entity. Not null
     */
    public Projectile(Area area, Orientation orientation, DiscreteCoordinates position, int damage, int framesForMove) {
        super(area, orientation, position);
        this.damage = damage;
        this.framesForMove = framesForMove;
    }

    public Projectile(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);
        this.damage = DEFAULT_DAMAGE;
        this.framesForMove = DEFAULT_MOVE_DURATION;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        move(this.framesForMove);
        //if(isConsumed()) leaveArea();
    }

    /**
     * Sets isConsumed to true
     */
    public void consume(){
        if (!isConsumed) {
            isConsumed = true;
            leaveArea();
        }
    }


    @Override
    public boolean isConsumed() {
        return isConsumed;
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates().jump(getOrientation().toVector())); //La cellule a laquelle le joueur fait face
    }

    @Override
    public boolean takeCellSpace() {
        return false;
    }


    @Override
    public boolean wantsCellInteraction() {
        return true;
    }

    @Override
    public boolean wantsViewInteraction() { return true; }

}
