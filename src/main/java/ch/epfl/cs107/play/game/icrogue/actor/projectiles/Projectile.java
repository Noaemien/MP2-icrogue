package ch.epfl.cs107.play.game.icrogue.actor.projectiles;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.icrogue.actor.ICRogueActor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

import java.util.Collections;
import java.util.List;

abstract public class Projectile extends ICRogueActor implements Consumable {

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

    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        move(this.framesForMove);
    }

    /**
     * Sets isConsumed to true
     */
    public void consume(){
        isConsumed = true;
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
    public boolean takeCellSpace() {
        return false;
    }
}
