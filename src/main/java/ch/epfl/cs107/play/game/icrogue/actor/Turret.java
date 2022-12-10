package ch.epfl.cs107.play.game.icrogue.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;

import ch.epfl.cs107.play.game.icrogue.actor.projectiles.Arrow;
import ch.epfl.cs107.play.game.icrogue.actor.projectiles.Projectile;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.util.ArrayList;

public class Turret  extends Enemies {

    private Sprite sprite;

    private final float COOLDOWN = 2.f;

    private float timer;

    private ArrayList<Projectile> projectiles = new ArrayList<Projectile>();

    /**
     * Default MovableAreaEntity constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity. Not null
     * @param position    (Coordinate): Initial position of the entity. Not null
     */
    public Turret(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);
        new Sprite("icrogue/static_npc", 1.5f, 1.5f,
                this, null, new Vector(-0.25f, 0));

    }

    @Override
    public void draw(Canvas canvas) {
        sprite.draw(canvas);
    }

    public void update(float deltaTime) {
        if (timer < COOLDOWN) timer += deltaTime;
        else timer = 0.f;
        throwArrow(Orientation.UP);
        throwArrow(Orientation.RIGHT);
        throwArrow(Orientation.LEFT);
        throwArrow(Orientation.DOWN);

    }

    public void throwArrow(Orientation orientation) {
        if (timer == 0.f) {
            Projectile arrow = new Arrow(this.getOwnerArea(), orientation, getCurrentMainCellCoordinates());
            arrow.enterArea(this.getOwnerArea(), getCurrentMainCellCoordinates());
            projectiles.add(arrow);
        }
    }
}
